package rinnovation_etl

import java.io.File

import com.github.tototoshi.csv.{CSVReader, CSVWriter}

object MLInputGenerator extends App {
  case class RoiContext(cityName: String, year: String, renovationType: String, roi: String)
  private val years =
    (2008 to 2019)
      .filterNot(2012.equals)
      .map(_.toString)
  years
    .flatMap(year =>
      new File(s"/Users/aselvia/Downloads/etl/$year")
        .listFiles
        .map(file =>
          CSVReader
            .open(file)
            .all
            .map(lineElements => {
              val renovationType = lineElements(2)
              val roi = lineElements(5)
              val cityName = file.getName
              RoiContext(cityName, year, renovationType, roi)
            })
        ))
    .flatten
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
        .open(s"/Users/aselvia/Downloads/out/$cityName")
        .writeAll(csvElementRows)
    }
}