/**
 * Application Server Main.js
 */
var express = require("express");
var app = express();
var httpServer = require('http').createServer(app);
const search = require('./search')

app.use((req, res, next) => {
  req.data = {};
  next();
})

app.get("/search", search.getGames, search.formatGames, search.sendResponse);

const port = process.env.PORT || 8080;
httpServer.listen(port, function () {
    console.log("Listening on port " + port);
});