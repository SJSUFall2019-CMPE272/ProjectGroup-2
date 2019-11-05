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
import scala.language.postfixOps
import scala.sys.process._

object RemodelingEtlPipeline extends App {
  private val browser = JsoupBrowser()
  private val regionsDirectoryName = "regions"
  private val citiesWithoutPdfsIn2019 = Set(
    "daytonabeachfl",
    "myrtlebeachsc")
  private val year = 2018
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
//    .filterNot(citiesWithoutPdfsIn2019.contains) // Some cities do not have accessible PDFs, they result in errors
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
      2018 -> 9,
      2019 -> 11
    )
    val pageNumber = pageNumbersByYear(year)
    val page = getPage(byteArray, pageNumber).getArea(125, 45, 600, 745)
    val bea = new BasicExtractionAlgorithm
    val table = bea.extract(page).get(0)
    val file = new File(s"/tmp/rinnovation/$year/$cityName.csv")
    file.mkdirs
    new CSVWriter().write(new FileWriter(file), table)
    massageCsv(year, cityName, file)
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
