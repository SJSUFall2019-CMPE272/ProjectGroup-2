const express = require("express");
var bodyParser = require("body-parser");
const app = express();
const port = 3001;

app.all("*", function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE');
  res.header('Access-Control-Allow-Headers', 'Content-Type');
  next();
});

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.use("/api", (req, res) => {
  console.log("======================================from server ===================",res);
  //req.body.json
  res.send("1001");
  res.send(req);
});
app.use("/temp", (req, res) => res.json("resting"));

app.listen(port, () => console.log(`Example app listening on port ${port}!`));