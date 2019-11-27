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
    val whereSubClauses = Set("year", "city", "renovation")
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
}
