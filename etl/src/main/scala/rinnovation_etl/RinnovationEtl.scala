package rinnovation_etl

import com.github.tototoshi.csv.{CSVParser, CSVWriter}

import scala.io.Source

object RinnovationEtl extends App {
  CSVWriter
    .open("HomeownerImprovementExpendituresByYear.csv")
    .writeAll(Source
      .fromResource("Table W-1-Table 1.csv")
      .getLines
      .drop(47)
      .filterNot(_.endsWith(",,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"))
      .map(
        CSVParser
          .parse(_, '\\', ',', '"')
          .getOrElse(List[String]())
          .tail
          .map(
            _
              .replace(",", "")
              .replace("\"", ""))
          .map {
            case "NA" => null
            case string => string.toInt
          }
          .grouped(3)
          .toList)
      .toList
      .transpose
      .map(_.flatten))
}
