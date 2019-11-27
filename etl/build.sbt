name := "etl"
version := "0.1"
scalaVersion := "2.13.1"
libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.3.6"
libraryDependencies += "technology.tabula" % "tabula" % "1.0.3"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.2.0"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.25"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.1.10"
libraryDependencies += "org.tpolecat" %% "doobie-core" % "0.8.6"
libraryDependencies += "org.tpolecat" %% "doobie-postgres" % "0.8.6"
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.8"