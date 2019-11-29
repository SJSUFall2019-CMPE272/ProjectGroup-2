package etl

import java.io.{ByteArrayOutputStream, File, FileWriter, IOException}
import java.net.URL
import java.nio.file.Files

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
  private val citiesCompleted = Set[String](
  )
  private val citiesWithoutPdfsByYear = Map(
    2015 -> Set(
      "greensboronc"
    ),
    2016 -> Set(
      "wichitaks"
    ),
    2019 -> Set(
      "daytonabeachfl",
      "myrtlebeachsc"
    )
  )
  private val pdfUrlsByYear = Map(
    2008 -> Map(
      "chicagoil" -> "https://cdnassets.hw.net/16/bb/caa721354a01bc5a1b624cb70888/cvv-2008-2009-professional-chicago.pdf",
      "cincinnatioh" -> "https://cdnassets.hw.net/f8/1a/b705f5f8490fa29a47476a94719e/cvv-2008-2009-professional-cincinnati.pdf",
      "clevelandoh" -> "https://cdnassets.hw.net/b7/40/1eeb926a4e7aa13797068da9e7de/cvv-2008-2009-professional-cleveland.pdf",
      "columbusoh" -> "https://cdnassets.hw.net/e6/d8/d0ff21174ecbb48034622398d98c/cvv-2008-2009-professional-columbus.pdf",
      "daytonoh" -> "https://cdnassets.hw.net/9a/9b/f62cdff2477aa0b4fc7abba9af69/cvv-2008-2009-professional-dayton.pdf",
      "detroitmi" -> "https://cdnassets.hw.net/b3/04/90a764024cbfaf2c4868178a9fdd/cvv-2008-2009-professional-detroit.pdf",
      "grandrapidsmi" -> "https://cdnassets.hw.net/bf/fe/b4ae362245a5b870026346d0f3cc/cvv-2008-2009-professional-grandrapids.pdf",
      "indianapolisin" -> "https://cdnassets.hw.net/94/fa/f55696b94ebbb95d15d44f435cec/cvv-2008-2009-professional-indianapolis.pdf",
      "madisonwi" -> "https://cdnassets.hw.net/57/e3/886ef5fb4351864b31dabf6c735c/cvv-2008-2009-professional-madison.pdf",
      "milwaukeewi" -> "https://cdnassets.hw.net/92/c9/091842ed4111b6f2be0aa079e0e6/cvv-2008-2009-professional-milwaukee.pdf",
      "birminghamal" -> "https://cdnassets.hw.net/b7/55/128c08314a7a99d300e5d468d196/cvv-2008-2009-professional-birmingham.pdf",
      "jacksonms" -> "https://cdnassets.hw.net/72/57/aecefd3443e4999435511df6a17c/cvv-2008-2009-professional-jackson.pdf",
      "knoxvilletn" -> "https://cdnassets.hw.net/da/7a/07b10e0544dcb4c2e2e9a1241d5d/cvv-2008-2009-professional-knoxville.pdf",
      "louisvilleky" -> "https://cdnassets.hw.net/4e/7d/5248ea3c4edd90f7c23a87896261/cvv-2008-2009-professional-louisville.pdf",
      "memphistn" -> "https://cdnassets.hw.net/46/15/56342dd94884a1dd7ef152d5d0f8/cvv-2008-2009-professional-memphis.pdf",
      "albanyny" -> "https://cdnassets.hw.net/2a/26/722dd1804184bda91e45af92a469/cvv-2008-2009-professional-albany.pdf",
      "allentownpa" -> "https://cdnassets.hw.net/ae/8b/d32ca5204467bc78a6786fde19e0/cvv-2008-2009-professional-allentown.pdf",
      "buffalony" -> "https://cdnassets.hw.net/19/d1/0d3712ac42259063e11416fa1c90/cvv-2008-2009-professional-buffalo.pdf",
      "harrisburgpa" -> "https://cdnassets.hw.net/73/4c/7c497d8241a19e3e2e60e1197e8e/cvv-2008-2009-professional-harrisburg.pdf",
      "newyorkny" -> "https://cdnassets.hw.net/f2/dd/390b6d65478f80eca78fb0f60958/cvv-2008-2009-professional-newyork.pdf",
      "philadelphiapa" -> "https://cdnassets.hw.net/f0/c7/de73e923471cb6cd1ba33056061c/cvv-2008-2009-professional-philadelphia.pdf",
      "pittsburghpa" -> "https://cdnassets.hw.net/7d/95/00f0559d4bf8934ad869af688823/cvv-2008-2009-professional-pittsburgh.pdf",
      "rochesterny" -> "https://cdnassets.hw.net/32/89/2f7eac3d4ca582d9e2ba9fc2167c/cvv-2008-2009-professional-rochester.pdf",
      "albuquerquenm" -> "https://cdnassets.hw.net/e5/69/95fd2a0442a1900ba973416a97b3/cvv-2008-2009-professional-albuquerque.pdf",
      "billingsmt" -> "https://cdnassets.hw.net/f5/2f/55fbb2e940c092e9e78c635e2d6e/cvv-2008-2009-professional-billings.pdf",
      "boiseid" -> "https://cdnassets.hw.net/92/a4/018560f44b05a40112b2e690c501/cvv-2008-2009-professional-boise.pdf",
      "denverco" -> "https://cdnassets.hw.net/14/19/cc18cffb4b2f8796e5408cdb8286/cvv-2008-2009-professional-denver.pdf",
      "lasvegasnv" -> "https://cdnassets.hw.net/e3/23/84b7b74e4cc9bfa8d2b85285432c/cvv-2008-2009-professional-lasvegas.pdf",
      "phoenixaz" -> "https://cdnassets.hw.net/f2/de/f524934c4b328e8d22f71fa38093/cvv-2008-2009-professional-phoenix.pdf",
      "saltlakecityut" -> "https://cdnassets.hw.net/ac/99/153ce9474fefaa3cb68191793d1b/cvv-2008-2009-professional-saltlakecity.pdf",
      "bostonma" -> "https://cdnassets.hw.net/e9/4b/d8b033d64a0997015f4a6f9ea004/cvv-2008-2009-professional-boston.pdf",
      "burlingtonvt" -> "https://cdnassets.hw.net/a9/b8/dea627904d468456e0640549e6a5/cvv-2008-2009-professional-burlington.pdf",
      "hartfordct" -> "https://cdnassets.hw.net/46/1e/76f31d7a45f78b126c99eb3aa096/cvv-2008-2009-professional-hartford.pdf",
      "manchesternh" -> "https://cdnassets.hw.net/9d/4f/da31090d42d1bb4268682ced0bc0/cvv-2008-2009-professional-manchester.pdf",
      "newhavenct" -> "https://cdnassets.hw.net/d8/0a/b0958aad431e951cb7e00500630e/cvv-2008-2009-professional-newhaven.pdf",
      "providenceri" -> "https://cdnassets.hw.net/0c/16/029f24c94faeb7bf21ef7ccfa206/cvv-2008-2009-professional-providence.pdf",
      "springfieldma" -> "https://cdnassets.hw.net/14/85/230e5d9345ea9632045799c83137/cvv-2008-2009-professional-springfield.pdf",
      "worcesterma" -> "https://cdnassets.hw.net/80/67/cef893b640bbbff4d900efc1b23f/cvv-2008-2009-professional-worcester.pdf",
      "anchorageak" -> "https://cdnassets.hw.net/73/05/8578877b421bb0eec6f7d3b9c4a5/cvv-2008-2009-professional-anchorage.pdf",
      "honoluluhi" -> "https://cdnassets.hw.net/e2/19/382c27ce47b385ff8a1709e1de98/cvv-2008-2009-professional-honolulu.pdf",
      "losangelesca" -> "https://cdnassets.hw.net/f4/c1/61b9397d4199a09e958121a1000d/cvv-2008-2009-professional-losangeles.pdf",
      "portlandor" -> "https://cdnassets.hw.net/34/35/b1f4b3a64863b693389ca9a7cf6c/cvv-2008-2009-professional-portland.pdf",
      "riversideca" -> "https://cdnassets.hw.net/c6/7e/5c8feff649d4b6728d17dc33cbaf/cvv-2008-2009-professional-riverside.pdf",
      "sacramentoca" -> "https://cdnassets.hw.net/0c/e9/dc4a13b8431fb96b36f5742b9dac/cvv-2008-2009-professional-sacramento.pdf",
      "sandiegoca" -> "https://cdnassets.hw.net/0e/f5/a26d8119484bb7bd7923a7324f9d/cvv-2008-2009-professional-sandiego.pdf",
      "sanfranciscoca" -> "https://cdnassets.hw.net/af/05/31f044184b50a2c751cd640b685a/cvv-2008-2009-professional-sanfrancisco.pdf",
      "seattlewa" -> "https://cdnassets.hw.net/54/e4/aa8f026e41978a32c604eb25a291/cvv-2008-2009-professional-seattle.pdf",
      "venturaca" -> "https://cdnassets.hw.net/62/c5/324a3f244706a70d2e4c609fb999/cvv-2008-2009-professional-ventura.pdf",
      "atlantaga" -> "https://cdnassets.hw.net/3a/46/089694bd44078a4dceccdb5a593d/cvv-2008-2009-professional-atlanta.pdf",
      "baltimoremd" -> "https://cdnassets.hw.net/e5/63/6d62af8f49189d82df0969a12a74/cvv-2008-2009-professional-baltimore.pdf",
      "charlestonwv" -> "https://cdnassets.hw.net/0c/ef/a9f5d4ff486d9babd90550b13c40/cvv-2008-2009-professional-charleston.pdf",
      "charlestonsc" -> "https://cdnassets.hw.net/cd/ce/a82ef1b64a25b978cf83aff87033/cvv-2008-2009-professional-charleston-northcharleston.pdf",
      "charlottenc" -> "https://cdnassets.hw.net/7f/98/3748b6754063bae2f4e5a1e8a73c/cvv-2008-2009-professional-charlotte.pdf",
      "columbiasc" -> "https://cdnassets.hw.net/fc/82/63c0f4de4e1bb79a67b65bf4386c/cvv-2008-2009-professional-columbia.pdf",
      "jacksonvillefl" -> "https://cdnassets.hw.net/73/5d/0ea7ba8d47d9bb25aea55a6164bd/cvv-2008-2009-professional-jacksonville.pdf",
      "miamifl" -> "https://cdnassets.hw.net/ae/fc/cf8513f7494592be6242a7bf13b6/cvv-2008-2009-professional-miami.pdf",
      "orlandofl" -> "https://cdnassets.hw.net/d6/4c/223f5ea143a19c555f8ae2fa4536/cvv-2008-2009-professional-orlando.pdf",
      "raleighnc" -> "https://cdnassets.hw.net/e7/00/778e03b54321b1db0806ae643f80/cvv-2008-2009-professional-raleigh.pdf",
      "richmondva" -> "https://cdnassets.hw.net/db/9e/a59f6c774381bc621cf258ab3b51/cvv-2008-2009-professional-richmond.pdf",
      "tampafl" -> "https://cdnassets.hw.net/27/d9/031277ed4e96b0a353ab38996798/cvv-2008-2009-professional-tampa.pdf",
      "virginiabeachva" -> "https://cdnassets.hw.net/c2/0c/13acbb6240648e2caf6f17181c49/cvv-2008-2009-professional-virginiabeach.pdf",
      "washingtondc" -> "https://cdnassets.hw.net/d4/aa/5eb8e3a14243b9ea7ccd5d945e94/cvv-2008-2009-professional-washington.pdf",
      "desmoinesia" -> "https://cdnassets.hw.net/85/9e/583098d249a9865d44a3dfe7db4d/cvv-2008-2009-professional-desmoines.pdf",
      "fargond" -> "https://cdnassets.hw.net/fa/4d/1c2320ab48c1afe3dbd8819d4063/cvv-2008-2009-professional-fargo.pdf",
      "kansascitymo" -> "https://cdnassets.hw.net/ad/70/59a3197b4160afb55e8fd441bf1e/cvv-2008-2009-professional-kansascity.pdf",
      "minneapolismn" -> "https://cdnassets.hw.net/5d/0e/9779a57b4136b189bc39cbc16dce/cvv-2008-2009-professional-minneapolis.pdf",
      "omahane" -> "https://cdnassets.hw.net/17/af/03552c544d378775a54e59bcc6d4/cvv-2008-2009-professional-omaha.pdf",
      "siouxfallssd" -> "https://cdnassets.hw.net/fc/25/a24198fc4759989f65d2534a8ec9/cvv-2008-2009-professional-siouxfalls.pdf",
      "stlouismo" -> "https://cdnassets.hw.net/16/10/924213654a919f37bc1624cac7d4/cvv-2008-2009-professional-stlouis.pdf",
      "wichitaks" -> "https://cdnassets.hw.net/27/3e/b18233684215b766f9c58f36f878/cvv-2008-2009-professional-wichita.pdf",
      "austintx" -> "https://cdnassets.hw.net/d3/7b/d2a7708b4c0f9bb6ef1574d69eac/cvv-2008-2009-professional-austin.pdf",
      "dallastx" -> "https://cdnassets.hw.net/06/20/00d097634725b18e4abefff29bcd/cvv-2008-2009-professional-dallas.pdf",
      "elpasotx" -> "https://cdnassets.hw.net/a4/a9/4896984443029d026e8e0577763a/cvv-2008-2009-professional-elpaso.pdf",
      "houstontx" -> "https://cdnassets.hw.net/30/01/396115d644cba4a00a1c9df82464/cvv-2008-2009-professional-houston.pdf",
      "littlerockar" -> "https://cdnassets.hw.net/11/0a/2fcd2e064a8ba202598588fdeb27/cvv-2008-2009-professional-littlerock.pdf",
      "neworleansla" -> "https://cdnassets.hw.net/42/15/cf0e639247b0ad7c517dfe4db272/cvv-2008-2009-professional-neworleans.pdf",
      "oklahomacityok" -> "https://cdnassets.hw.net/5e/69/99336dd649ba8bd277e4ffbf10b8/cvv-2008-2009-professional-oklahomacity.pdf",
      "sanantoniotx" -> "https://cdnassets.hw.net/b7/08/8228bfd44a9788387f7eca2437f8/cvv-2008-2009-professional-sanantonio.pdf",
      "tulsaok" -> "https://cdnassets.hw.net/44/d1/167919b1489bb89f06485d66d12d/cvv-2008-2009-professional-tulsa.pdf"
    ),
    2009 -> Map(
      "chicagoil" -> "https://cdnassets.hw.net/aa/91/31c382ae4db0a7edcd54a94040d6/cvv-2009-2010-professional-chicagoil.pdf",
      "cincinnatioh" -> "https://cdnassets.hw.net/c0/70/9a1f19a9404c8c1fe7f1de31db28/cvv-2009-2010-professional-cincinnatioh.pdf",
      "clevelandoh" -> "https://cdnassets.hw.net/93/25/53cfa90a40a0a1b1ac7117d1abf9/cvv-2009-2010-professional-clevelandoh.pdf",
      "columbusoh" -> "https://cdnassets.hw.net/fd/36/a5b4847b4b4fbf85496d05c4ef71/cvv-2009-2010-professional-columbusoh.pdf",
      "daytonoh" -> "https://cdnassets.hw.net/a0/91/abffa6d44788a934f002eb58f92c/cvv-2009-2010-professional-daytonoh.pdf",
      "detroitmi" -> "https://cdnassets.hw.net/af/15/50ad42124f76a721db72a4f78a9c/cvv-2009-2010-professional-detroitmi.pdf",
      "grandrapidsmi" -> "https://cdnassets.hw.net/18/cf/352243b04d9ca044ca5698d4c768/cvv-2009-2010-professional-grandrapidsmi.pdf",
      "indianapolisin" -> "https://cdnassets.hw.net/b9/a1/88824d804d4999ca49de1074e25b/cvv-2009-2010-professional-indianapolisin.pdf",
      "madisonwi" -> "https://cdnassets.hw.net/70/37/b4a63c69453fafc4af8721674e93/cvv-2009-2010-professional-madisonwi.pdf",
      "milwaukeewi" -> "https://cdnassets.hw.net/4c/ea/d99be48d4a179841da8c99758a7a/cvv-2009-2010-professional-milwaukeewi.pdf",
      "birminghamal" -> "https://cdnassets.hw.net/3a/70/94f1e2b64f8684cfec1ff40992c8/cvv-2009-2010-professional-birminghamal.pdf",
      "jacksonms" -> "https://cdnassets.hw.net/4a/97/f4a2c95b4adfa76fba87d6c377c3/cvv-2009-2010-professional-jacksonms.pdf",
      "knoxvilletn" -> "https://cdnassets.hw.net/72/31/69cdfaba471ba0fd22f1edbea0dc/cvv-2009-2010-professional-knoxvilletn.pdf",
      "louisvilleky" -> "https://cdnassets.hw.net/72/0e/83451a194ca2acf40f95aa5f443f/cvv-2009-2010-professional-louisvilleky.pdf",
      "memphistn" -> "https://cdnassets.hw.net/3e/76/8a4a0e0a433cb1db978af6c99715/cvv-2009-2010-professional-memphistn.pdf",
      "albanyny" -> "https://cdnassets.hw.net/ea/30/aabf065d4a9a8479fece86d58c5c/cvv-2009-2010-professional-albanyny.pdf",
      "allentownpa" -> "https://cdnassets.hw.net/32/a2/4426315c45d999e6065cc654e8ef/cvv-2009-2010-professional-allentownpa.pdf",
      "buffalony" -> "https://cdnassets.hw.net/12/2a/6f89468a4ddfa2233522aefff125/cvv-2009-2010-professional-buffalony.pdf",
      "harrisburgpa" -> "https://cdnassets.hw.net/08/bc/857f1dc24b309d8f4404f7ba185d/cvv-2009-2010-professional-harrisburgpa.pdf",
      "newyorkny" -> "https://cdnassets.hw.net/e8/35/daaeb485410ea3ce4145ebdf47b6/cvv-2009-2010-professional-newyorkny.pdf",
      "philadelphiapa" -> "https://cdnassets.hw.net/33/89/6acd2e1c48acaa8c91f5b13adf35/cvv-2009-2010-professional-philadelphiapa.pdf",
      "pittsburghpa" -> "https://cdnassets.hw.net/92/8f/ff7e835c49afadd0ac86439ea7df/cvv-2009-2010-professional-pittsburghpa.pdf",
      "rochesterny" -> "https://cdnassets.hw.net/5c/98/b7b31a4c4c789a6460294aef8487/cvv-2009-2010-professional-rochesterny.pdf",
      "albuquerquenm" -> "https://cdnassets.hw.net/6a/31/28beae5b493db078071f984794e3/cvv-2009-2010-professional-albuquerquenm.pdf",
      "billingsmt" -> "https://cdnassets.hw.net/e2/73/e19a1dcf4b1dab58eeb8f65b6f7d/cvv-2009-2010-professional-billingsmt.pdf",
      "boiseid" -> "https://cdnassets.hw.net/86/e5/5d13c8d44ac286937015c8eb5c96/cvv-2009-2010-professional-boiseid.pdf",
      "denverco" -> "https://cdnassets.hw.net/cc/8e/34c5580a402eb5a8e34718fc9006/cvv-2009-2010-professional-denverco.pdf",
      "lasvegasnv" -> "https://cdnassets.hw.net/33/26/a9182c6441ac8c8137fb347882de/cvv-2009-2010-professional-lasvegasnv.pdf",
      "phoenixaz" -> "https://cdnassets.hw.net/49/65/3b91ed5e478bb93211cc5218a8d8/cvv-2009-2010-professional-phoenixaz.pdf",
      "saltlakecityut" -> "https://cdnassets.hw.net/a2/7d/b48489394480816bd1da5a590b69/cvv-2009-2010-professional-saltlakecityut.pdf",
      "bostonma" -> "https://cdnassets.hw.net/20/a8/04f46b70479da3f527e77b45b07d/cvv-2009-2010-professional-bostonma.pdf",
      "burlingtonvt" -> "https://cdnassets.hw.net/f5/c7/a5d85a9541bfa3a7d0d683323738/cvv-2009-2010-professional-burlingtonvt.pdf",
      "hartfordct" -> "https://cdnassets.hw.net/94/1a/ba1e358649beb20f5e5454475c60/cvv-2009-2010-professional-hartfordct.pdf",
      "manchesternh" -> "https://cdnassets.hw.net/35/5b/f7d9331345a287b5b7a072de0902/cvv-2009-2010-professional-manchesternh.pdf",
      "newhavenct" -> "https://cdnassets.hw.net/cf/6e/0e18262a4ee686fb792a8d75e726/cvv-2009-2010-professional-newhavenct.pdf",
      "portlandme" -> "https://cdnassets.hw.net/62/af/bb728c5046d2a0e540c24efe1864/cvv-2009-2010-professional-portlandme.pdf",
      "providenceri" -> "https://cdnassets.hw.net/74/9d/a1224cdd4052a6e226b46ca083f1/cvv-2009-2010-professional-providenceri.pdf",
      "springfieldma" -> "https://cdnassets.hw.net/5f/97/667858ca4b5b863486c849b6f3dc/cvv-2009-2010-professional-springfieldma.pdf",
      "worcesterma" -> "https://cdnassets.hw.net/cd/57/a72a67294b55be168045b66a220f/cvv-2009-2010-professional-worcesterma.pdf",
      "anchorageak" -> "https://cdnassets.hw.net/16/65/dc860de644d88127736144316684/cvv-2009-2010-professional-anchorageak.pdf",
      "honoluluhi" -> "https://cdnassets.hw.net/45/a6/49e4a08349618fe89f1ad0a50009/cvv-2009-2010-professional-honoluluhi.pdf",
      "losangelesca" -> "https://cdnassets.hw.net/a0/d1/e99040a54698bd2561b3b3d7f2ed/cvv-2009-2010-professional-losangelesca.pdf",
      "portlandor" -> "https://cdnassets.hw.net/a1/cb/de9d04d14999b6a532cd02d95116/cvv-2009-2010-professional-portlandor.pdf",
      "riversideca" -> "https://cdnassets.hw.net/f2/41/59dc46a14d5b99a62681bdc26b7c/cvv-2009-2010-professional-riversideca.pdf",
      "sacramentoca" -> "https://cdnassets.hw.net/bb/16/3121ec444505b1c6445dcf5a5808/cvv-2009-2010-professional-sacramentoca.pdf",
      "sandiegoca" -> "https://cdnassets.hw.net/93/a7/c4cd9c2747c6966b4629d8cda798/cvv-2009-2010-professional-sandiegoca.pdf",
      "sanfranciscoca" -> "https://cdnassets.hw.net/ac/87/fe57144045deb117a91282bac9cd/cvv-2009-2010-professional-sanfranciscoca.pdf",
      "seattlewa" -> "https://cdnassets.hw.net/ac/f8/b37018334a8bb3613be99533a9d1/cvv-2009-2010-professional-seattlewa.pdf",
      "venturaca" -> "https://cdnassets.hw.net/bf/85/e9bd41f648aa9aaa00afd88bbed0/cvv-2009-2010-professional-venturaca.pdf",
      "atlantaga" -> "https://cdnassets.hw.net/a8/49/d44cfc7c4ca0900e692d54cc2b19/cvv-2009-2010-professional-atlantaga.pdf",
      "baltimoremd" -> "https://cdnassets.hw.net/1a/77/6af054a745f5afedc507bd68edf0/cvv-2009-2010-professional-baltimoremd.pdf",
      "charlestonwv" -> "https://cdnassets.hw.net/07/24/a9d51ba6436d88cf58e38dd219eb/cvv-2009-2010-professional-charlestonwv.pdf",
      "charlestonsc" -> "https://cdnassets.hw.net/52/1b/6e1ea4a349a7b2ebc33f0ae71833/cvv-2009-2010-professional-charleston-northcharlestonsc.pdf",
      "charlottenc" -> "https://cdnassets.hw.net/44/ce/ebc9795148b5b6c0b467cf2e66ce/cvv-2009-2010-professional-charlottenc.pdf",
      "columbiasc" -> "https://cdnassets.hw.net/de/7e/a26598c34e88b989589079cce9fb/cvv-2009-2010-professional-columbiasc.pdf",
      "jacksonvillefl" -> "https://cdnassets.hw.net/3c/71/a05e0650474e9d8c7e6a717f1dcf/cvv-2009-2010-professional-jacksonvillefl.pdf",
      "miamifl" -> "https://cdnassets.hw.net/9a/eb/d5cd18124e1097cac6956884583e/cvv-2009-2010-professional-miamifl.pdf",
      "orlandofl" -> "https://cdnassets.hw.net/08/a5/b6bb579f492b807b8ab8bd3fa382/cvv-2009-2010-professional-orlandofl.pdf",
      "raleighnc" -> "https://cdnassets.hw.net/cf/3d/f39534a14b3fa05e39d879a73335/cvv-2009-2010-professional-raleighnc.pdf",
      "richmondva" -> "https://cdnassets.hw.net/88/dc/d3e0de7846b995809de95b6e3fc4/cvv-2009-2010-professional-richmondva.pdf",
      "tampafl" -> "https://cdnassets.hw.net/2e/97/9025e6824d04b5cc59d11c788533/cvv-2009-2010-professional-tampafl.pdf",
      "virginiabeachva" -> "https://cdnassets.hw.net/f0/da/d7d947e84e0fa087531f91ead39a/cvv-2009-2010-professional-virginiabeachva.pdf",
      "washingtondc" -> "https://cdnassets.hw.net/21/d2/81111d5746afb0dc08d4de190bab/cvv-2009-2010-professional-washingtondc.pdf",
      "desmoinesia" -> "https://cdnassets.hw.net/02/1c/b368a5a147a69c8841bea4b45c46/cvv-2009-2010-professional-desmoinesia.pdf",
      "fargond" -> "https://cdnassets.hw.net/92/9d/33918bf14d8a9edd704c4ac2532a/cvv-2009-2010-professional-fargond.pdf",
      "kansascitymo" -> "https://cdnassets.hw.net/17/34/5b6824034740ba30210dda17578b/cvv-2009-2010-professional-kansascitymo.pdf",
      "minneapolismn" -> "https://cdnassets.hw.net/92/8b/d200e43147499698fb4dac3c2fa3/cvv-2009-2010-professional-minneapolismn.pdf",
      "omahane" -> "https://cdnassets.hw.net/0c/18/10d96ef24c098f405d136e097b2e/cvv-2009-2010-professional-omahane.pdf",
      "siouxfallssd" -> "https://cdnassets.hw.net/8a/d4/bc50033b411fb86a7a1131aab356/cvv-2009-2010-professional-siouxfallssd.pdf",
      "stlouismo" -> "https://cdnassets.hw.net/c4/90/3de580304034a8499fe183797432/cvv-2009-2010-professional-stlouismo.pdf",
      "wichitaks" -> "https://cdnassets.hw.net/f5/9b/0c30d49d4b53b8ee5c9c519e1dec/cvv-2009-2010-professional-wichitaks.pdf",
      "austintx" -> "https://cdnassets.hw.net/fa/1e/518e7dc9413bbb3ca2682ce21211/cvv-2009-2010-professional-austintx.pdf",
      "dallastx" -> "https://cdnassets.hw.net/43/23/05fa3ecb46b8acd26378f53f7fa5/cvv-2009-2010-professional-dallastx.pdf",
      "elpasotx" -> "https://cdnassets.hw.net/12/fa/c5e0ce69432caf5b8f3531874d13/cvv-2009-2010-professional-elpasotx.pdf",
      "houstontx" -> "https://cdnassets.hw.net/a5/fb/b45ace184a2ab282feef0d282d95/cvv-2009-2010-professional-houstontx.pdf",
      "littlerockar" -> "https://cdnassets.hw.net/14/d6/f4f72f8a491b91ebb56b029006b7/cvv-2009-2010-professional-littlerockar.pdf",
      "neworleansla" -> "https://cdnassets.hw.net/02/8d/20963f504a23a7f6f954f888d7ba/cvv-2009-2010-professional-neworleansla.pdf",
      "oklahomacityok" -> "https://cdnassets.hw.net/26/88/88c4b3fe480999d03d61e9e5ddfe/cvv-2009-2010-professional-oklahomacityok.pdf",
      "sanantoniotx" -> "https://cdnassets.hw.net/8b/6f/d78f5e354ed589e37750f756a560/cvv-2009-2010-professional-sanantoniotx.pdf",
      "tulsaok" -> "https://cdnassets.hw.net/93/e9/06549c2e45dfa7e87d18888f6084/cvv-2009-2010-professional-tulsaok.pdf"
    ),
    2010 -> Map(
      "chicagoil" -> "https://cdnassets.hw.net/ae/a2/97f4243b4baea66d5a7cc8815971/cvv-2010-2011-professional-chicagoil.pdf",
      "cincinnatioh" -> "https://cdnassets.hw.net/b4/05/225b742a4668ae72f25d15023d0c/cvv-2010-2011-professional-cincinnatioh.pdf",
      "clevelandoh" -> "https://cdnassets.hw.net/69/bd/cd1862e14778a7752c76d5b7fc07/cvv-2010-2011-professional-clevelandoh.pdf",
      "columbusoh" -> "https://cdnassets.hw.net/67/56/3761931f46718939ba0a19a755ed/cvv-2010-2011-professional-columbusoh.pdf",
      "daytonoh" -> "https://cdnassets.hw.net/2c/7b/332a466a498ab74864f620d03b28/cvv-2010-2011-professional-daytonoh.pdf",
      "detroitmi" -> "https://cdnassets.hw.net/8a/de/c9952508413b84838796732d5308/cvv-2010-2011-professional-detroitmi.pdf",
      "grandrapidsmi" -> "https://cdnassets.hw.net/10/36/9a7d1cdb41fd9c1c9b7de1e2ca24/cvv-2010-2011-professional-grandrapidsmi.pdf",
      "indianapolisin" -> "https://cdnassets.hw.net/13/fa/18577998418285bb065184b26a8d/cvv-2010-2011-professional-indianapolisin.pdf",
      "madisonwi" -> "https://cdnassets.hw.net/8a/8b/34a0e53a4394a6d2c0c3a63a998e/cvv-2010-2011-professional-madisonwi.pdf",
      "milwaukeewi" -> "https://cdnassets.hw.net/ad/73/ece227894a31a5b633d2461a71dc/cvv-2010-2011-professional-milwaukeewi.pdf",
      "birminghamal" -> "https://cdnassets.hw.net/9e/a3/e81f98214fedb92971a0ed38d3ab/cvv-2010-2011-professional-birminghamal.pdf",
      "jacksonms" -> "https://cdnassets.hw.net/94/78/0d9aa5a045619e8a500b6164e9af/cvv-2010-2011-professional-jacksonms.pdf",
      "knoxvilletn" -> "https://cdnassets.hw.net/fb/a0/87a66820469a875c8593b23265c2/cvv-2010-2011-professional-knoxvilletn.pdf",
      "louisvilleky" -> "https://cdnassets.hw.net/51/93/e34ad58847cea86ea2ad3dc71b8b/cvv-2010-2011-professional-louisvilleky.pdf",
      "memphistn" -> "https://cdnassets.hw.net/a6/d3/592f2f7c482c94493746e6c01a35/cvv-2010-2011-professional-memphistn.pdf",
      "albanyny" -> "https://cdnassets.hw.net/0e/47/e90c0f39447c9610d1d9be99a8ee/cvv-2010-2011-professional-albanyny.pdf",
      "allentownpa" -> "https://cdnassets.hw.net/b4/bb/d813d9d44e0dbdbdf93606a8dfee/cvv-2010-2011-professional-allentownpa.pdf",
      "buffalony" -> "https://cdnassets.hw.net/d5/7c/3c7023c94f30a051a10c033d0ab6/cvv-2010-2011-professional-buffalony.pdf",
      "harrisburgpa" -> "https://cdnassets.hw.net/44/de/d19048ec42d38515a042d7e6bc08/cvv-2010-2011-professional-harrisburgpa.pdf",
      "newyorkny" -> "https://cdnassets.hw.net/28/e5/0623646d43f4a21fa07cc961ec78/cvv-2010-2011-professional-newyorkny.pdf",
      "philadelphiapa" -> "https://cdnassets.hw.net/e7/75/d3a41f3140e084fb619b73608760/cvv-2010-2011-professional-philadelphiapa.pdf",
      "pittsburghpa" -> "https://cdnassets.hw.net/b2/53/430bd1764d4ebd44cbafd15774a7/cvv-2010-2011-professional-pittsburghpa.pdf",
      "rochesterny" -> "https://cdnassets.hw.net/1e/1d/f436dfb64028b27e224c446bfa64/cvv-2010-2011-professional-rochesterny.pdf",
      "albuquerquenm" -> "https://cdnassets.hw.net/4c/ae/b1f584d44e6e93a3f6f1637b5c0d/cvv-2010-2011-professional-albuquerquenm.pdf",
      "billingsmt" -> "https://cdnassets.hw.net/0f/ff/ba38ae9c4d3aad338f7310c2fc48/cvv-2010-2011-professional-billingsmt.pdf",
      "boiseid" -> "https://cdnassets.hw.net/26/84/40f15aa64fb2b047edb7c36a6e7f/cvv-2010-2011-professional-boiseid.pdf",
      "denverco" -> "https://cdnassets.hw.net/1a/92/71c3ce0c48fbb9f2560f5af26215/cvv-2010-2011-professional-denverco.pdf",
      "lasvegasnv" -> "https://cdnassets.hw.net/c6/a4/b9b5ff9f44c6b24012add55350c3/cvv-2010-2011-professional-lasvegasnv.pdf",
      "phoenixaz" -> "https://cdnassets.hw.net/22/fc/9d025e7d4c5ab1d6ab2b8ee004ef/cvv-2010-2011-professional-phoenixaz.pdf",
      "saltlakecityut" -> "https://cdnassets.hw.net/55/3a/88c5cc474f78a4f606e37f6b4953/cvv-2010-2011-professional-saltlakecityut.pdf",
      "bostonma" -> "https://cdnassets.hw.net/53/a5/2c3208d142488eb10c1f594a38de/cvv-2010-2011-professional-bostonma.pdf",
      "burlingtonvt" -> "https://cdnassets.hw.net/0e/ca/89f74687481e9731902dcf672c16/cvv-2010-2011-professional-burlingtonvt.pdf",
      "hartfordct" -> "https://cdnassets.hw.net/a4/8c/07408ea94283b8cd96409e803928/cvv-2010-2011-professional-hartfordct.pdf",
      "manchesternh" -> "https://cdnassets.hw.net/18/49/bb9702224890a8e68be5f267bf38/cvv-2010-2011-professional-manchesternh.pdf",
      "newhavenct" -> "https://cdnassets.hw.net/f6/e6/1a2e23b649b6bcc4232efbd6f15e/cvv-2010-2011-professional-newhavenct.pdf",
      "portlandme" -> "https://cdnassets.hw.net/21/ac/e4446a704058b5d4974d8e3e8540/cvv-2010-2011-professional-portlandme.pdf",
      "providenceri" -> "https://cdnassets.hw.net/82/04/ee4c052645caacfb2c0c5b351de3/cvv-2010-2011-professional-providenceri.pdf",
      "springfieldma" -> "https://cdnassets.hw.net/03/95/b0782ff34b72b344f8a2230088f7/cvv-2010-2011-professional-springfieldma.pdf",
      "worcesterma" -> "https://cdnassets.hw.net/d6/09/55e970d6435fbe746b1f683f85b2/cvv-2010-2011-professional-worcesterma.pdf",
      "anchorageak" -> "https://cdnassets.hw.net/09/e7/8f7c0c534ba4a1c0a3f85e311124/cvv-2010-2011-professional-anchorageak.pdf",
      "honoluluhi" -> "https://cdnassets.hw.net/52/56/57016b014bf8a832ae20faf06199/cvv-2010-2011-professional-honoluluhi.pdf",
      "losangelesca" -> "https://cdnassets.hw.net/bb/21/112db86d4527a02baa475300dfce/cvv-2010-2011-professional-losangelesca.pdf",
      "portlandor" -> "https://cdnassets.hw.net/2a/58/f898015241f4889eb26b85500a44/cvv-2010-2011-professional-portlandor.pdf",
      "riversideca" -> "https://cdnassets.hw.net/ce/c7/ffafdcfc4fe8a18ccecc13f89bb8/cvv-2010-2011-professional-riversideca.pdf",
      "sacramentoca" -> "https://cdnassets.hw.net/8d/62/4b8ad76447f5a8f498a660acee12/cvv-2010-2011-professional-sacramentoca.pdf",
      "sandiegoca" -> "https://cdnassets.hw.net/bc/b5/8444f28542d8991f3a7591bbc25f/cvv-2010-2011-professional-sandiegoca.pdf",
      "sanfranciscoca" -> "https://cdnassets.hw.net/48/72/8cf79d37428f8ead4dfa938d6658/cvv-2010-2011-professional-sanfranciscoca.pdf",
      "seattlewa" -> "https://cdnassets.hw.net/5c/3b/d3c4cfd74aac9e8c5b2e0beffb89/cvv-2010-2011-professional-seattlewa.pdf",
      "venturaca" -> "https://cdnassets.hw.net/ae/02/58447d9b45999a0679322af28697/cvv-2010-2011-professional-venturaca.pdf",
      "atlantaga" -> "https://cdnassets.hw.net/3b/40/f6a34164486093a79bf107c97161/cvv-2010-2011-professional-atlantaga.pdf",
      "baltimoremd" -> "https://cdnassets.hw.net/0a/0d/94f9f67b4bfc98d55e0b699ee9de/cvv-2010-2011-professional-baltimoremd.pdf",
      "charlestonwv" -> "https://cdnassets.hw.net/c0/14/9d4dd3384c4fa0398976e3784334/cvv-2010-2011-professional-charlestonwv.pdf",
      "charlestonsc" -> "https://cdnassets.hw.net/2c/1c/717578cc409a90ab8e1d07969413/cvv-2010-2011-professional-charlestonsc.pdf",
      "charlottenc" -> "https://cdnassets.hw.net/d8/46/869fe3054cccafd2adbc1d263414/cvv-2010-2011-professional-charlottenc.pdf",
      "columbiasc" -> "https://cdnassets.hw.net/62/ae/20435e0e4333a59fbc886530f26d/cvv-2010-2011-professional-columbiasc.pdf",
      "jacksonvillefl" -> "https://cdnassets.hw.net/7f/28/e37ecbb94bd4a29d3bad48d4259c/cvv-2010-2011-professional-jacksonvillefl.pdf",
      "miamifl" -> "https://cdnassets.hw.net/5b/20/6c0196cb4b079aaf6b8660bd48ef/cvv-2010-2011-professional-miamifl.pdf",
      "orlandofl" -> "https://cdnassets.hw.net/ab/91/81da909849c3b786f92381ca5a5f/cvv-2010-2011-professional-orlandofl.pdf",
      "raleighnc" -> "https://cdnassets.hw.net/17/4c/61a3c7a7440e8fc7079e64fd4029/cvv-2010-2011-professional-raleighnc.pdf",
      "richmondva" -> "https://cdnassets.hw.net/89/6d/f86c45f24966bb54cbb7c25be475/cvv-2010-2011-professional-richmondva.pdf",
      "tampafl" -> "https://cdnassets.hw.net/3e/b6/5b3e07634a369fdb9924ae98d135/cvv-2010-2011-professional-tampafl.pdf",
      "virginiabeachva" -> "https://cdnassets.hw.net/30/41/9059b33b4402ad6175c66ab97692/cvv-2010-2011-professional-virginiabeachva.pdf",
      "washingtondc" -> "https://cdnassets.hw.net/9f/2b/d10d2a034e8bbebc24bd973f0f4b/cvv-2010-2011-professional-washingtondc.pdf",
      "desmoinesia" -> "https://cdnassets.hw.net/b6/3d/047accdd4174a4965051631d7900/cvv-2010-2011-professional-desmoinesia.pdf",
      "fargond" -> "https://cdnassets.hw.net/c6/42/0b291b5c4aeba8997b15ef78056e/cvv-2010-2011-professional-fargond.pdf",
      "kansascitymo" -> "https://cdnassets.hw.net/d9/c3/33ed57af4a12b5a49ed099ecab42/cvv-2010-2011-professional-kansascitymo.pdf",
      "minneapolismn" -> "https://cdnassets.hw.net/f5/4f/752e517b4395a2f99af9e3de8b25/cvv-2010-2011-professional-minneapolismn.pdf",
      "omahane" -> "https://cdnassets.hw.net/04/4b/09d785a8468c89a4578f32825df8/cvv-2010-2011-professional-omahane.pdf",
      "siouxfallssd" -> "https://cdnassets.hw.net/30/79/93db4bb74da985374c41aff98392/cvv-2010-2011-professional-siouxfallssd.pdf",
      "stlouismo" -> "https://cdnassets.hw.net/4c/06/6e7c4c7546e9941ea34ea35323ee/cvv-2010-2011-professional-stlouismo.pdf",
      "wichitaks" -> "https://cdnassets.hw.net/6f/46/2bd3c611443ba6da600a0b3c7bc3/cvv-2010-2011-professional-wichitaks.pdf",
      "austintx" -> "https://cdnassets.hw.net/ae/e8/2279247b448d8e36ee7a659ec455/cvv-2010-2011-professional-austintx.pdf",
      "dallastx" -> "https://cdnassets.hw.net/53/5b/a3caea954959877eecc90fc6c50c/cvv-2010-2011-professional-dallastx.pdf",
      "elpasotx" -> "https://cdnassets.hw.net/d0/dd/a986b81248edb6b1fe8873da215e/cvv-2010-2011-professional-elpasotx.pdf",
      "houstontx" -> "https://cdnassets.hw.net/cb/3e/a9a7d34742ba90f80536aeacb7f9/cvv-2010-2011-professional-houstontx.pdf",
      "littlerockar" -> "https://cdnassets.hw.net/14/f6/b5c08ee94e48b142624d4036609a/cvv-2010-2011-professional-littlerockar.pdf",
      "neworleansla" -> "https://cdnassets.hw.net/43/93/750fcd6f4cb7a5f49a568ace5ce6/cvv-2010-2011-professional-neworleansla.pdf",
      "oklahomacityok" -> "https://cdnassets.hw.net/86/62/3213b5b04d7285fcba22737543bf/cvv-2010-2011-professional-oklahomacityok.pdf",
      "sanantoniotx" -> "https://cdnassets.hw.net/42/7b/bb25dda04fee86bb2e97dd9f8482/cvv-2010-2011-professional-sanantoniotx.pdf",
      "tulsaok" -> "https://cdnassets.hw.net/e7/cc/9ffbf1a84d9083a552ca52811c04/cvv-2010-2011-professional-tulsaok.pdf"
    ),
    2011 -> Map(
      "chicagoil" -> "https://cdnassets.hw.net/d4/f6/48763541469fb2cec04da12449d7/cvv-2011-2012-professional-chicagoil.pdf",
      "cincinnatioh" -> "https://cdnassets.hw.net/11/42/1752bcb6465597f96c409307c533/cvv-2011-2012-professional-cincinnatioh.pdf",
      "columbusoh" -> "https://cdnassets.hw.net/da/1b/30ea3eba447498996b7a34fca166/cvv-2011-2012-professional-columbusoh.pdf",
      "daytonoh" -> "https://cdnassets.hw.net/d4/cf/5dd13858425eabd6e463826da092/cvv-2011-2012-professional-daytonoh.pdf",
      "grandrapidsmi" -> "https://cdnassets.hw.net/48/21/f251c45c4e1da6048b6241940856/cvv-2011-2012-professional-grandrapidsmi.pdf",
      "indianapolisin" -> "https://cdnassets.hw.net/4b/e9/f722f26f4c9d812a003d0c1318d4/cvv-2011-2012-professional-indianapolisin.pdf",
      "madisonwi" -> "https://cdnassets.hw.net/76/6f/2c21607c4318ba41351e624c27bd/cvv-2011-2012-professional-madisonwi.pdf",
      "milwaukeewi" -> "https://cdnassets.hw.net/c6/df/50ac164d4f2fb7c556aa794b33e9/cvv-2011-2012-professional-milwaukeewi.pdf",
      "birminghamal" -> "https://cdnassets.hw.net/ba/4b/2ba6766444f9af08722606b791da/cvv-2011-2012-professional-birminghamal.pdf",
      "jacksonms" -> "https://cdnassets.hw.net/31/bd/179789f645ef806e81e728fe163e/cvv-2011-2012-professional-jacksonms.pdf",
      "knoxvilletn" -> "https://cdnassets.hw.net/ab/b3/dca318e8421cb888dab4543644f6/cvv-2011-2012-professional-knoxvilletn.pdf",
      "louisvilleky" -> "https://cdnassets.hw.net/7c/b4/90eedd424f5294c0bd2809f901c2/cvv-2011-2012-professional-louisvilleky.pdf",
      "memphistn" -> "https://cdnassets.hw.net/40/fc/799bd5b4455186b54bba1e6c61ff/cvv-2011-2012-professional-memphistn.pdf",
      "albanyny" -> "https://cdnassets.hw.net/14/de/3febc0af4639a07e04a6a81373c6/cvv-2011-2012-professional-albanyny.pdf",
      "allentownpa" -> "https://cdnassets.hw.net/c7/72/93b8a10841af9041656f9cc21ce9/cvv-2011-2012-professional-allentownpa.pdf",
      "buffalony" -> "https://cdnassets.hw.net/a8/10/a74a50d349de982a45bbe040f38f/cvv-2011-2012-professional-buffalony.pdf",
      "harrisburgpa" -> "https://cdnassets.hw.net/1e/5a/95311c6d44a28042337fedc2a8ef/cvv-2011-2012-professional-harrisburgpa.pdf",
      "newyorkny" -> "https://cdnassets.hw.net/2e/70/6bb95b804d47b9b9fe72ce7dd129/cvv-2011-2012-professional-newyorkny.pdf",
      "philadelphiapa" -> "https://cdnassets.hw.net/1e/88/32e025ef47fd9055cfed01bc8c3c/cvv-2011-2012-professional-philadelphiapa.pdf",
      "pittsburghpa" -> "https://cdnassets.hw.net/9d/6a/7bfd212f45e19dfd3734ed1e4042/cvv-2011-2012-professional-pittsburghpa.pdf",
      "rochesterny" -> "https://cdnassets.hw.net/f4/9c/d4be81014859a9f26935303f434b/cvv-2011-2012-professional-rochesterny.pdf",
      "albuquerquenm" -> "https://cdnassets.hw.net/21/ff/ba66ed164878a09207b7cc875e39/cvv-2011-2012-professional-albuquerquenm.pdf",
      "billingsmt" -> "https://cdnassets.hw.net/84/1b/479029814cacaa1267e9529080e6/cvv-2011-2012-professional-billingsmt.pdf",
      "boiseid" -> "https://cdnassets.hw.net/c1/cc/b767e9a14cd8bbc91d756071574c/cvv-2011-2012-professional-boiseid.pdf",
      "denverco" -> "https://cdnassets.hw.net/0e/e2/8f53dc3141b891c771622b1bbf9d/cvv-2011-2012-professional-denverco.pdf",
      "lasvegasnv" -> "https://cdnassets.hw.net/ba/92/f55e4dc34f5cbf04cff8605f5f85/cvv-2011-2012-professional-lasvegasnv.pdf",
      "phoenixaz" -> "https://cdnassets.hw.net/ea/c6/09f5f221415ab1b75ef4e2af9841/cvv-2011-2012-professional-phoenixaz.pdf",
      "saltlakecityut" -> "https://cdnassets.hw.net/1d/3d/ebf52d2c4a869a3a09f6a5ac9f90/cvv-2011-2012-professional-saltlakecityut.pdf",
      "bostonma" -> "https://cdnassets.hw.net/20/35/d50e0bda41999e4ac8ea1481fe66/cvv-2011-2012-professional-bostonma.pdf",
      "burlingtonvt" -> "https://cdnassets.hw.net/bb/0c/1a3a95ee4e19ba3f3dc66218be32/cvv-2011-2012-professional-burlingtonvt.pdf",
      "hartfordct" -> "https://cdnassets.hw.net/6e/6d/2189c9074dcfabf0b777fed46406/cvv-2011-2012-professional-hartfordct.pdf",
      "manchesternh" -> "https://cdnassets.hw.net/a0/fa/d6b98c6e42529cef57b8b71cc702/cvv-2011-2012-professional-manchesternh.pdf",
      "newhavenct" -> "https://cdnassets.hw.net/df/51/23f30b09497f9a4a92a10b063f5a/cvv-2011-2012-professional-newhavenct.pdf",
      "portlandme" -> "https://cdnassets.hw.net/26/fb/b6dd695d47029850ea34a6563ef1/cvv-2011-2012-professional-portlandme.pdf",
      "providenceri" -> "https://cdnassets.hw.net/aa/c9/28d9dd364330a4aa4978b6531893/cvv-2011-2012-professional-providenceri.pdf",
      "springfieldma" -> "https://cdnassets.hw.net/2c/39/8be53eca417e81c8105a5a58bb42/cvv-2011-2012-professional-springfieldma.pdf",
      "worcesterma" -> "https://cdnassets.hw.net/96/60/020b2c664781b6b1b065ac7f75d6/cvv-2011-2012-professional-worcesterma.pdf",
      "anchorageak" -> "https://cdnassets.hw.net/33/be/406ce1b6413b8379e7c3c34d4a3b/cvv-2011-2012-professional-anchorageak.pdf",
      "honoluluhi" -> "https://cdnassets.hw.net/3d/2f/6c8c8bcc49bfad8d557e69be33c9/cvv-2011-2012-professional-honoluluhi.pdf",
      "losangelesca" -> "https://cdnassets.hw.net/d4/95/c01b0c4c4c428f116fd48064d082/cvv-2011-2012-professional-losangelesca.pdf",
      "portlandor" -> "https://cdnassets.hw.net/1b/6e/ee827e2e44b79663f591e424208b/cvv-2011-2012-professional-portlandor.pdf",
      "riversideca" -> "https://cdnassets.hw.net/4d/a3/a535ef2944c0aa2def0adb25a23d/cvv-2011-2012-professional-riversideca.pdf",
      "sacramentoca" -> "https://cdnassets.hw.net/03/0b/5b9ae1ad468a951824c06d5a1c0d/cvv-2011-2012-professional-sacramentoca.pdf",
      "sandiegoca" -> "https://cdnassets.hw.net/29/5a/8ac9cf2946c49f043f2341922bf1/cvv-2011-2012-professional-sandiegoca.pdf",
      "sanfranciscoca" -> "https://cdnassets.hw.net/1b/89/bab3e3e64679869ee4d976a37e33/cvv-2011-2012-professional-sanfranciscoca.pdf",
      "seattlewa" -> "https://cdnassets.hw.net/81/c5/59e04505406083ced1da57749dfd/cvv-2011-2012-professional-seattlewa.pdf",
      "venturaca" -> "https://cdnassets.hw.net/82/7d/a1de17ba4509b847ac774378a639/cvv-2011-2012-professional-venturaca.pdf",
      "atlantaga" -> "https://cdnassets.hw.net/bc/83/ca8d8cfb4f93a2cfa5a0c9baa8f9/cvv-2011-2012-professional-atlantaga.pdf",
      "baltimoremd" -> "https://cdnassets.hw.net/86/0f/5758f74947d19c303cbfc4a2da2f/cvv-2011-2012-professional-baltimoremd.pdf",
      "charlestonwv" -> "https://cdnassets.hw.net/d0/ae/dd2ca922420a897dbbf42f7b415d/cvv-2011-2012-professional-charlestonwv.pdf",
      "charlestonsc" -> "https://cdnassets.hw.net/fd/21/6f73afd4409fab344bdbcb386953/cvv-2011-2012-professional-charlestonsc.pdf",
      "charlottenc" -> "https://cdnassets.hw.net/ea/bf/288294574328a12f76114146f1e6/cvv-2011-2012-professional-charlottenc.pdf",
      "columbiasc" -> "https://cdnassets.hw.net/96/4f/f5e2119846bd8fa06ca4b2a0cb18/cvv-2011-2012-professional-columbiasc.pdf",
      "jacksonvillefl" -> "https://cdnassets.hw.net/cd/9e/46ea875147169e4fa9f2a007c5d3/cvv-2011-2012-professional-jacksonvillefl.pdf",
      "miamifl" -> "https://cdnassets.hw.net/79/66/dfc96ae84657a66713262bb45899/cvv-2011-2012-professional-miamifl.pdf",
      "orlandofl" -> "https://cdnassets.hw.net/55/39/fa7ce0024c2c946e787040a636ed/cvv-2011-2012-professional-orlandofl.pdf",
      "raleighnc" -> "https://cdnassets.hw.net/d2/d6/0a73782f4b72916bd381c6619aab/cvv-2011-2012-professional-raleighnc.pdf",
      "richmondva" -> "https://cdnassets.hw.net/36/91/1659c0a44a74be42c1c2fdc0b846/cvv-2011-2012-professional-richmondva.pdf",
      "tampafl" -> "https://cdnassets.hw.net/0d/d9/cbe4191e4e059169e1fabc6468f0/cvv-2011-2012-professional-tampafl.pdf",
      "virginiabeachva" -> "https://cdnassets.hw.net/63/66/22189b3849a697562184b5a373ba/cvv-2011-2012-professional-virginiabeachva.pdf",
      "washingtondc" -> "https://cdnassets.hw.net/f3/16/c0d30e8843b0b4b50494bfa2c4ee/cvv-2011-2012-professional-washingtondc.pdf",
      "desmoinesia" -> "https://cdnassets.hw.net/5e/8a/c0f6c77b4e75986409ba175b0c3e/cvv-2011-2012-professional-desmoinesia.pdf",
      "fargond" -> "https://cdnassets.hw.net/60/3b/686b39bf4d61b119df2001e27149/cvv-2011-2012-professional-fargond.pdf",
      "kansascitymo" -> "https://cdnassets.hw.net/b7/3a/a1c01b86444fb906d0ed9541ead1/cvv-2011-2012-professional-kansascitymo.pdf",
      "minneapolismn" -> "https://cdnassets.hw.net/b2/c9/43a9a10648e49f45fe49d2fe4db0/cvv-2011-2012-professional-minneapolismn.pdf",
      "omahane" -> "https://cdnassets.hw.net/4b/03/81dc5b4e426585f70103fe607aef/cvv-2011-2012-professional-omahane.pdf",
      "siouxfallssd" -> "https://cdnassets.hw.net/73/9b/fc0763964dca804aec8906586c83/cvv-2011-2012-professional-siouxfallssd.pdf",
      "stlouismo" -> "https://cdnassets.hw.net/f3/a8/06acd4704723ae6ac695d7b09754/cvv-2011-2012-professional-stlouismo.pdf",
      "wichitaks" -> "https://cdnassets.hw.net/74/78/ec46a3ba4b1b8979e75791233dce/cvv-2011-2012-professional-wichitaks.pdf",
      "austintx" -> "https://cdnassets.hw.net/02/96/edfbcb7049c4866d50ab387dbc85/cvv-2011-2012-professional-austintx.pdf",
      "elpasotx" -> "https://cdnassets.hw.net/ff/f4/97ea2b0e460aa8907653ef5032f3/cvv-2011-2012-professional-elpasotx.pdf",
      "houstontx" -> "https://cdnassets.hw.net/31/c8/7fe25cfc4af9b8e2e4d2f626620d/cvv-2011-2012-professional-houstontx.pdf",
      "littlerockar" -> "https://cdnassets.hw.net/9b/a7/b36bc4a94067a5f8a0f4c41d5aaf/cvv-2011-2012-professional-littlerockar.pdf",
      "neworleansla" -> "https://cdnassets.hw.net/67/79/a83a32624ad4a85a85fd7242a382/cvv-2011-2012-professional-neworleansla.pdf",
      "oklahomacityok" -> "https://cdnassets.hw.net/65/bb/d08d80f7450e9e5cf88fdc7730f9/cvv-2011-2012-professional-oklahomacityok.pdf",
      "sanantoniotx" -> "https://cdnassets.hw.net/b4/ce/9272617c44f9b0e9c729b0e5fc37/cvv-2011-2012-professional-sanantoniotx.pdf",
      "tulsaok" -> "https://cdnassets.hw.net/e0/78/ab3012b040ee87eabd6e343f1616/cvv-2011-2012-professional-tulsaok.pdf"
    )
  )

  List(2008, 2009, 2010, 2011, 2013, 2014, 2015, 2016, 2017, 2018, 2019).foreach(year => {
    val cityNames = if (year <= 2011) {
      pdfUrlsByYear(year).keys
    } else {
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
    }
    cityNames
      .filterNot(citiesCompleted.contains)
      .filterNot(citiesWithoutPdfsByYear.getOrElse(year, Set()).contains) // Some cities do not have accessible PDFs, they result in errors
      .foreach(cityName => {
        val byteArrayOutputStream = new ByteArrayOutputStream
        writePdfFileContentsToByteArrayOutputStream(year, cityName, byteArrayOutputStream)
        writeCsv(year, cityName, byteArrayOutputStream.toByteArray)
      })
  })

  // TODO: this blocks; when I switch to Akka-HTTP, make it nonblocking; also, it has side-effects; not good
  def writePdfFileContentsToByteArrayOutputStream(year: Int, cityName: String, byteArrayOutputStream: ByteArrayOutputStream) = new URL(if (year <= 2011) pdfUrlsByYear(year)(cityName) else s"https://s3.amazonaws.com/HW_Assets/CVV_Assets/$year/Consumer/$cityName.pdf") #> byteArrayOutputStream !!

  def writeCsv(year: Int, cityName: String, byteArray: Array[Byte]): Unit = {
    val pageNumbersByYear = Map(
      2008 -> 2,
      2009 -> 2,
      2010 -> 2,
      2011 -> 2,
      2013 -> 6,
      2014 -> 6,
      2019 -> 11
    )
    val pageNumber = pageNumbersByYear.getOrElse(year, 9)
    val basicExtractionAlgorithm = new BasicExtractionAlgorithm
    val path = new File(s"/tmp/rinnovation/$year")
    path.mkdirs
    val file = path.toPath.resolve(s"$cityName.csv").toFile
    val csvWriter = new CSVWriter()
    year match {
      case 2008 =>
        val page = getPage(byteArray, pageNumber)
        val table1 = basicExtractionAlgorithm.extract(page.getArea(132, 24, 364, 717)).get(0)
        val table2 = basicExtractionAlgorithm.extract(page.getArea(390, 24, 523, 717)).get(0)
        csvWriter.write(new FileWriter(file), List(table1, table2).asJava)
        massageCsv(year, cityName, file)
      case 2009 =>
        val page = getPage(byteArray, pageNumber)
        val table1 = basicExtractionAlgorithm.extract(page.getArea(110, 24, 365, 717)).get(0)
        val table2 = basicExtractionAlgorithm.extract(page.getArea(395, 24, 540, 717)).get(0)
        csvWriter.write(new FileWriter(file), List(table1, table2).asJava)
        massageCsv(year, cityName, file)
      case 2010 | 2011 | 2013 =>
        val page = getPage(byteArray, pageNumber)
        val table1 = basicExtractionAlgorithm.extract(page.getArea(105, 24, 361, 717)).get(0)
        val table2 = basicExtractionAlgorithm.extract(page.getArea(398, 24, 548, 717)).get(0)
        csvWriter.write(new FileWriter(file), List(table1, table2).asJava)
        massageCsv(year, cityName, file)
      case 2014 =>
        val page = getPage(byteArray, pageNumber)
        val table1 = basicExtractionAlgorithm.extract(page.getArea(75, 24, 362, 717)).get(0)
        val table2 = basicExtractionAlgorithm.extract(page.getArea(398, 24, 548, 717)).get(0)
        csvWriter.write(new FileWriter(file), List(table1, table2).asJava)
        massageCsv(year, cityName, file)
      case 2015 =>
        val page = getPage(byteArray, pageNumber)
        val table1 = basicExtractionAlgorithm.extract(page.getArea(77, 24, 370, 717)).get(0)
        val table2 = basicExtractionAlgorithm.extract(page.getArea(397, 24, 546, 717)).get(0)
        csvWriter.write(new FileWriter(file), List(table1, table2).asJava)
        massageCsv(year, cityName, file)
      case 2016 =>
        val page = getPage(byteArray, pageNumber)
        val table1 = basicExtractionAlgorithm.extract(page.getArea(152, 45, 382, 745)).get(0)
        val table2 = basicExtractionAlgorithm.extract(page.getArea(388, 45, 492, 745)).get(0)
        csvWriter.write(new FileWriter(file), List(table1, table2).asJava)
        massageCsv(year, cityName, file)
      case 2017 =>
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
          .map(line =>
            line
              .replace(",", "")
              .replace("\"", "")
              .replace("%", "")
              .replace("$", ""))
          .flatMap(line => {
            val split = line.split(" ")
            if (year == 2016 && "[0-9]+".r.findFirstIn(line).nonEmpty) {
              List(split.dropRight(1).mkString(" "), split.last)
            } else if (line.contains(" ") && line.toLowerCase == line) {
              split.map(_.trim)
            } else {
              List(line)
            }
          })
          .filter(_.nonEmpty))
      .toSeq
      .map(year :: cityName :: _)

    val csvInput =
      if (year < 2018) {
        values.zipWithIndex.map { case (value, index) =>
          val renovationTypeIndex = 2
          val upscaleRenovationTypeStartIndicesByYear = Map(
            2008 -> 19,
            2009 -> 21,
            2010 -> 22,
            2011 -> 22,
            2013 -> 22,
            2014 -> 22,
            2015 -> 23,
            2016 -> 18,
            2017 -> 19
          )
          value.updated(renovationTypeIndex, s"${value(renovationTypeIndex)} | ${if (index < upscaleRenovationTypeStartIndicesByYear(year)) "Midrange" else "Upscale"}") }
      } else {
        values
        }
        .filter(_(5).toString.toDouble < 700) // Cut out bad data (for instance, many records from 2018 PDFs are incorrect)

    if (csvInput.isEmpty) {
      Files.deleteIfExists(file.toPath)
    } else {
      CSVWriter
        .open(file)
        .writeAll(csvInput)
    }
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
