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
  new File(getClass.getResource(s"/$regionsDirectoryName").getPath)
    .listFiles
    .toSeq
    .flatMap(file =>
      browser
        .parseString(Source.fromResource(s"$regionsDirectoryName/${file.getName}").mkString)
        >> elementList("#cvv-locations li")
        >> element("a")
        >> attr("href"))
    .map(
      _
        .toString
        .replace("-", "")
        .replace("/", ""))
    .filter(_ == "sandiegoca") // TODO: remove this when you move to production; only for testing
    .foreach(cityName => {
      val byteArrayOutputStream = new ByteArrayOutputStream
      writePdfFileContentsToByteArrayOutputStream(cityName, byteArrayOutputStream)
      writeCsv(cityName, byteArrayOutputStream.toByteArray)
    })

  // TODO: this blocks; when I switch to Akka-HTTP, make it nonblocking; also, it has side-effects; not good
  def writePdfFileContentsToByteArrayOutputStream(cityName: String, byteArrayOutputStream: ByteArrayOutputStream) = new URL(s"https://s3.amazonaws.com/HW_Assets/CVV_Assets/2019/Consumer/$cityName.pdf") #> byteArrayOutputStream !!

  def writeCsv(cityName: String, byteArray: Array[Byte]): Unit = {
    val pageNumber = 11 // 9 for 2018
    val page = getPage(byteArray, pageNumber).getArea(125, 45, 600, 745)
    val bea = new BasicExtractionAlgorithm
    val table = bea.extract(page).get(0)
    new CSVWriter().write(new FileWriter(s"$cityName.csv"), table)
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
