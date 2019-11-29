package etl

import java.io.File

import cats.effect.{ContextShift, IO}
import cats.implicits._
import com.github.tototoshi.csv.CSVReader
import doobie.{Update, _}
import doobie.implicits._
import etl.MLInputGenerator.RoiContext

import scala.Double.NaN
import scala.concurrent.ExecutionContext

object PredictionLoader extends App {
  val predictions = List("")

  implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  private val host = System.getenv("HOST")
  private val port = System.getenv("PORT")
  private val database = System.getenv("DATABASE")
  private val user = System.getenv("USER")
  private val password = System.getenv("PASSWORD")
  val aux = Transactor.fromDriverManager[IO]("org.postgresql.Driver", s"jdbc:postgresql://$host:$port/$database", user, password)


  private val roiContexts =
    new File(s"/tmp/predictions")
      .listFiles
      .flatMap(file =>
        CSVReader
          .open(file)
          .all
          .map(lineElements => {
            val renovationType = lineElements(0)
            val roi = lineElements(1)
            val cityName = file.getName.takeWhile(_ != '.')
            RoiContext(cityName, "2020", renovationType, roi)
          }))
      .toList

  // Write roiContexts to Postgres
  Update[(Int, String, String, Double)]("INSERT INTO costs_recouped VALUES (?, ?, ?, ?)")
    .updateMany(roiContexts.flatMap(roiContext => roiContext.roi.toDoubleOption.filterNot(NaN.equals).map((roiContext.year.toInt, roiContext.cityName, roiContext.renovationType, _))))
    .transact(aux)
    .unsafeRunSync
}
