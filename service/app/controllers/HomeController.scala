package controllers

import javax.inject._
import play.api.db.Database
import play.api.libs.json.{JsArray, JsNumber, JsObject, JsString}
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
           |SELECT cost_recouped, year
           |FROM costs_recouped
           |${if (whereSubClauses.isEmpty) "" else "WHERE " + whereSubClauses.mkString("\nAND ")}
           |""".stripMargin
      val resultSet = statement.executeQuery(query)
      val costsRecouped = ListBuffer[JsObject]()
      while (resultSet.next()) {
        costsRecouped += JsObject(Map("costRecouped" -> JsNumber(resultSet.getDouble("cost_recouped")), "year" -> JsNumber(resultSet.getInt("year"))))
      }
      Ok(JsArray(costsRecouped))
    } finally {
      connection.close()
    }
  }

  def getCities: Action[AnyContent] = Action { implicit request =>
    // TODO: DRY up
    val connection = db.getConnection
    try {
      val statement = connection.createStatement
      val renovationOption =
        request
          .queryString
          .get("renovation")
          .flatMap(_.headOption)
      val query =
        renovationOption
          .map(value => s"SELECT id, english_name, lat, lng, cost_recouped FROM costs_recouped JOIN cities ON city=id WHERE year = 2020 AND renovation = '$value'")
          .getOrElse(s"SELECT DISTINCT id, english_name, lat, lng FROM costs_recouped JOIN cities ON city=id")
      val resultSet = statement.executeQuery(query)
      val cities = ListBuffer[JsObject]()
      while (resultSet.next()) {
        var jsObject = JsObject(Map(
          "id" -> JsString(resultSet.getString("id")),
          "englishName" -> JsString(resultSet.getString("english_name")),
          "lat" -> JsNumber(resultSet.getDouble("lat")),
          "lng" -> JsNumber(resultSet.getDouble("lng"))
        ))
        if (renovationOption.isDefined) {
          jsObject += ("costRecouped", JsNumber(resultSet.getDouble("cost_recouped")))
        }
        cities += jsObject
      }
      Ok(JsArray(cities.sortBy(x => x.fields.filter(_._1 == "englishName").head._2.toString)))
    } finally {
      connection.close()
    }
  }

  def getRenovations: Action[AnyContent] = Action { implicit request =>
    val str = request
      .queryString
      .get("city")
      .flatMap(_.headOption)
      .map(value => s" WHERE city = '$value'")
      .getOrElse("")
    // TODO: DRY up
    val connection = db.getConnection
    try {
      val statement = connection.createStatement
      val query = s"SELECT DISTINCT renovation FROM costs_recouped$str"
      val resultSet = statement.executeQuery(query)
      val renovations = ListBuffer[String]()
      while (resultSet.next()) {
        renovations += resultSet.getString("renovation")
      }
      Ok(JsArray(renovations.sorted.map(JsString.apply)))
    } finally {
      connection.close()
    }
  }
}
