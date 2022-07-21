const express = require('express');
const http = require('http');
const socket = require('socket.io');
const bodyParser = require('body-parser');
const axios = require('axios');
const routes = require('./routes');
const socketHandler = require('./socket/socketHandler');
const db = require('./models');
const dotenv = require("dotenv");
const s2sToken = require('./config/config')["s2sToken"];

const OK = 200;
const CREATED = 201
const INTERNAL_SERVER_ERROR = 500;

dotenv.config({path: "../ports.env"});
dotenv.config();

const app = express();
const server = http.Server(app);
const io = socket(server);

io.on('connection', (socket) => socketHandler(socket, io));

axios.defaults.headers = { s2sToken }
app.use(async (req, res, next) => {
  const token = req.headers['token'] || req.headers['s2sToken'];
  await axios.post(`${process.env.USER_URL}/token/verify${token}`);
  if (user) {
    next();
  } else {
    res.status(UNAUTHORIZED).send("Unsuccessfull");
  }
});
app.use(bodyParser.json());
app.use(routes);

db.sequelize.sync().then((req) => {
}).catch(e => console.log(e));

const PORT = process.env.PORT || 3000;

server.listen(PORT, () => {
  console.log(`Node.js Server running on port ${PORT}`);
});

module.exports = { OK, CREATED, INTERNAL_SERVER_ERROR };
