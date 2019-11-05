package rinnovation_etl

import java.io.{ByteArrayOutputStream, File, FileWriter, IOException}
import java.net.URL

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import org.apache.pdfbox.pdmodel.PDDocument
import technology.tabula.ObjectExtractor
import technology.tabula.extractors.BasicExtractionAlgorithm
import technology.tabula.writers.CSVWriter

import scala.io.Source
import scala.jdk.CollectionConverters._
import scala.language.postfixOps
import scala.sys.process._

object RemodelingEtlPipeline extends App {
  private val browser = JsoupBrowser()
  private val regionsDirectoryName = "regions"
  private val citiesWithoutPdfsByYear = Map(
    2016 -> Set(
      "wichitaks"
    ),
    2019 -> Set(
      "daytonabeachfl",
      "myrtlebeachsc"
    )
  )
  private val year = 2016
  new File(getClass.getResource(s"/$regionsDirectoryName/$year").getPath)
    .listFiles
    .toSeq
    .flatMap(file =>
      browser
        .parseString(Source.fromResource(s"$regionsDirectoryName/$year/${file.getName}").mkString)
        >> elementList("#cvv-locations li")
        >> element("a")
        >> attr("href"))
    .map(
      _
        .toString
        .replace("-", "")
        .replace("/", ""))
    .filterNot(citiesWithoutPdfsByYear(year).contains) // Some cities do not have accessible PDFs, they result in errors
//    .filter(_ == "sandiegoca") // TODO: remove this when you move to production; only for testing
    .foreach(cityName => {
      val byteArrayOutputStream = new ByteArrayOutputStream
      writePdfFileContentsToByteArrayOutputStream(year, cityName, byteArrayOutputStream)
      writeCsv(year, cityName, byteArrayOutputStream.toByteArray)
    })

  // TODO: this blocks; when I switch to Akka-HTTP, make it nonblocking; also, it has side-effects; not good
  def writePdfFileContentsToByteArrayOutputStream(year: Int, cityName: String, byteArrayOutputStream: ByteArrayOutputStream) = new URL(s"https://s3.amazonaws.com/HW_Assets/CVV_Assets/$year/Consumer/$cityName.pdf") #> byteArrayOutputStream !!

  def writeCsv(year: Int, cityName: String, byteArray: Array[Byte]): Unit = {
    val pageNumbersByYear = Map(
      2016 -> 9,
      2017 -> 9,
      2018 -> 9,
      2019 -> 11
    )
    val pageNumber = pageNumbersByYear(year)
    val basicExtractionAlgorithm = new BasicExtractionAlgorithm
    val path = new File(s"/tmp/rinnovation/$year")
    path.mkdirs
    val file = path.toPath.resolve(s"$cityName.csv").toFile
    val csvWriter = new CSVWriter()
    year match {
      case 2016 | 2017 =>
        val page = getPage(byteArray, pageNumber)
        val table1 = basicExtractionAlgorithm.extract(page.getArea(152, 45, 382, 745)).get(0)
        val table2 = basicExtractionAlgorithm.extract(page.getArea(406, 45, 519, 745)).get(0)
        csvWriter.write(new FileWriter(file), List(table1, table2).asJava)
        massageCsv(year, cityName, file)
      case 2018 | 2019 =>
        val page = getPage(byteArray, pageNumber).getArea(125, 45, 600, 745)
        val table = basicExtractionAlgorithm.extract(page).get(0)
        csvWriter.write(new FileWriter(file), table)
        massageCsv(year, cityName, file)
    }
  }

  def massageCsv(year: Int, cityName: String, file: File): Unit = {
    import com.github.tototoshi.csv.{CSVParser, CSVWriter}
    val source = Source.fromFile(file)
    val values = source
      .getLines
      .filter(_.endsWith("%"))
      .map(
        CSVParser
          .parse(_, '\\', ',', '"')
          .getOrElse(List[String]())
          .map(
            _
              .replace(",", "")
              .replace("\"", "")
              .replace("%", "")
              .replace("$", ""))
          .flatMap(line =>
            if (line.contains(" ") && line.toLowerCase == line) {
              line.split(" ").map(_.trim)
            } else {
              List(line)
            }
          )
          .filter(_.nonEmpty))
      .toSeq
      .map(year :: cityName :: _)
    CSVWriter
      .open(file)
      .writeAll(values)
  }

  @throws[IOException]
  def getPage(byteArray: Array[Byte], pageNumber: Int) = {
    var oe: ObjectExtractor = null
    try {
      oe = new ObjectExtractor(PDDocument.load(byteArray))
      oe.extract(pageNumber)
    } finally if (oe != null) oe.close()
  }
}
