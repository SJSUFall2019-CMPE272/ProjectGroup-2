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

  private val roiContexts = years
    .flatMap(year =>
      new File(s"/tmp/etl/$year")
        .listFiles
        .map(file =>
          CSVReader
            .open(file)
            .all
            .map(lineElements => {
              val renovationType = lineElements(2)
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