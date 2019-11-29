package controllers

import javax.inject._
import play.api.db.Database
import play.api.mvc._

import scala.collection.mutable.ListBuffer

@Singleton
class HomeController @Inject()(db: Database, cc: ControllerComponents) extends AbstractController(cc) {
  def index: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def getCostsRecouped: Action[AnyContent] = Action { implicit request =>
    val whereSubClauses = Set("city", "renovation")
      .flatMap(key =>
        request
          .queryString
          .get(key)
          .flatMap(_.headOption)
          .map(value => s"$key = '$value'"))
    val connection = db.getConnection
    try {
      val statement = connection.createStatement
      val query =
        s"""
           |SELECT cost_recouped
           |FROM costs_recouped
           |${if (whereSubClauses.isEmpty) "" else "WHERE " + whereSubClauses.mkString("\nAND ")}
           |""".stripMargin
      val resultSet = statement.executeQuery(query)
      val costsRecouped = ListBuffer[String]()
      while (resultSet.next()) {
        costsRecouped += resultSet.getDouble("cost_recouped").toString
      }
      Ok(costsRecouped.mkString(", "))
    } finally {
      connection.close()
    }
  }

  def getCities: Action[AnyContent] = Action { implicit request =>
    val str = request
      .queryString
      .get("renovation")
      .flatMap(_.headOption)
      .map(value => s" AND renovation = '$value'")
      .getOrElse("")
    // TODO: DRY up
    val connection = db.getConnection
    try {
      val statement = connection.createStatement
      val query = s"SELECT DISTINCT city FROM costs_recouped WHERE year = '2020'$str"
      val resultSet = statement.executeQuery(query)
      val cities = ListBuffer[String]()
      while (resultSet.next()) {
        cities += resultSet.getString("city")
      }
      Ok(cities.sorted.mkString(", "))
    } finally {
      connection.close()
    }
  }

  def getRenovations: Action[AnyContent] = Action { implicit request =>
    val str = request
      .queryString
      .get("city")
      .flatMap(_.headOption)
      .map(value => s" AND city = '$value'")
      .getOrElse("")
    // TODO: DRY up
    val connection = db.getConnection
    try {
      val statement = connection.createStatement
      val query = s"SELECT DISTINCT renovation FROM costs_recouped WHERE year = '2020'$str"
      val resultSet = statement.executeQuery(query)
      val renovations = ListBuffer[String]()
      while (resultSet.next()) {
        renovations += resultSet.getString("renovation")
      }
      Ok(renovations.sorted.mkString(", "))
    } finally {
      connection.close()
    }
  }
}
