package etl

import java.io.File

import cats.effect.ContextShift
import cats.implicits._
import com.github.tototoshi.csv.{CSVReader, CSVWriter}
import doobie.implicits._

object MLInputGenerator extends App {
  case class RoiContext(cityName: String, year: String, renovationType: String, roi: String)

  val outputDirectory = new File("/tmp/out")
  if (!outputDirectory.exists)
    outputDirectory.mkdirs

  import cats.effect.IO
  import doobie._

  import scala.concurrent.ExecutionContext
  implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  private val host = System.getenv("HOST")
  private val port = System.getenv("PORT")
  private val database = System.getenv("DATABASE")
  private val user = System.getenv("USER")
  private val password = System.getenv("PASSWORD")
  val aux = Transactor.fromDriverManager[IO]("org.postgresql.Driver", s"jdbc:postgresql://$host:$port/$database", user, password)

  private val years =
    (2008 to 2019)
      .filterNot(2012.equals)
      .map(_.toString)

  private val renovationTypeOverrides = Map(
    "Backyard Patio" -> "Backyard Patio | Midrange",
    "Bath Remodel | Midrange" -> "Bathroom Remodel | Midrange",
    "Bath Remodel | Upscale" -> "Bathroom Remodel | Upscale",
    "Bath Remodel | Universal Design" -> "Bathroom Remodel | Universal Design",
    "Deck Addition | Composite" -> "Deck Addition (composite) | Upscale",
    "Deck Addition | Wood" -> "Deck Addition (wood) | Midrange",
    "Entry Door Replacement (steel) | Midrange*" -> "Entry Door Replacement (steel) | Midrange",
    "Entry Door Replacement | Steel" -> "Entry Door Replacement (steel) | Midrange",
    "Garage Door Replacement" -> "Garage Door Replacement | Upscale",
    "Grand Entrance (fiberglass) | Upscale" -> "Grand Entrance | Fiberglass",
    "Grand Entrance | Upscale" -> "Grand Entrance | Fiberglass",
    "Manufactured Stone Veneer | Midrange" -> "Manufactured Stone Veneer",
    "Roofing Replacement | Asphalt Shingles" -> "Roofing Replacement | Midrange",
    "Roofing Replacement | Metal" -> "Roofing Replacement | Upscale",
    "Siding Replacement" -> "Siding Replacement (fiber-cement) | Upscale",
    "Window Replacement | Vinyl" -> "Window Replacement (vinyl) | Upscale",
    "Window Replacement | Wood" -> "Window Replacement (wood) | Upscale"
  )

  private val roiContexts = years
    .flatMap(year =>
      new File(s"/tmp/etl/$year")
        .listFiles
        .filter(_.toString.endsWith(".csv"))
        .map(file =>
          CSVReader
            .open(file)
            .all
            .map(lineElements => {
              val denormalizedRenovationType = lineElements(2)
              // Normalize the renovation types since the names change over the years
              val renovationType = renovationTypeOverrides.getOrElse(denormalizedRenovationType, denormalizedRenovationType)
              val roi = lineElements(5)
              val cityName = file.getName.takeWhile(_ != '.')
              RoiContext(cityName, year, renovationType, roi)
            })
        )
    )
    .flatten
    .toList

  // Write roiContexts to Postgres
  Update[(Int, String, String, Double)]("INSERT INTO costs_recouped VALUES (?, ?, ?, ?)")
    .updateMany(roiContexts.flatMap(roiContext => roiContext.roi.toDoubleOption.map((roiContext.year.toInt, roiContext.cityName, roiContext.renovationType, _))))
    .transact(aux)
    .unsafeRunSync

  import com.google.maps.GeoApiContext
  import com.google.maps.GeocodingApi

  // Write cities to Postgres
  val context = new GeoApiContext.Builder().apiKey(System.getenv("API_KEY")).build
  def getCity(cityName: String): (String, String, Double, Double) = cityName match {
    case "providenceri" => (cityName, "Providence, RI", 41.825226, -71.418884)
    case "quadcitiesil" => (cityName, "Quad Cities, IL", 41.5236, 90.5776)
    case "tucsonaz" => (cityName, "Tucson, AZ", 32.0575, 111.6661)
    case _ =>
      val results = GeocodingApi.geocode(context, cityName).await
      val result = results(0)
      (cityName, result.formattedAddress.dropRight(5), result.geometry.location.lat, result.geometry.location.lng)
  }
  Update[(String, String, Double, Double)]("INSERT INTO cities VALUES (?, ?, ?, ?)")
    .updateMany(roiContexts.distinctBy(_.cityName).map(_.cityName).map(getCity))
    .transact(aux)
    .unsafeRunSync
  context.shutdown()

  // Produce ML Input files
  roiContexts
    .groupBy(_.cityName)
    .foreach { case (cityName, roiContexts) =>
      val renovationTypes = roiContexts.map(_.renovationType).distinct.sorted.toList
      val headerCsvElementRow = "year" :: renovationTypes
      val bodyCsvElementRows = years.map(year =>
        year :: renovationTypes.map(renovationType =>
          roiContexts
            .find(roiContext => roiContext.year == year && roiContext.renovationType == renovationType)
            .map(_.roi)
            .getOrElse("")
        )).toList
      val csvElementRows = headerCsvElementRow :: bodyCsvElementRows
      CSVWriter
        .open(s"$outputDirectory/$cityName.csv")
        .writeAll(csvElementRows)
    }
}