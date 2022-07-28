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
const {OAuth2Client} = require('google-auth-library');

const OK = 200;
const CREATED = 201
const INTERNAL_SERVER_ERROR = 500;

dotenv.config({path: "../ports.env"});
dotenv.config();

const client = new OAuth2Client(process.env.CLIENT_ID);
async function verify(token) {
  const ticket = await client.verifyIdToken({
      idToken: token,
      audience: [process.env.CLIENT_ID],
  });
  const payload = ticket.getPayload();
  const userid = payload['sub'];
  return userid;
}

const app = express();
const server = http.Server(app);
const io = socket(server);

io.on('connection', (socket) => socketHandler(socket, io));

axios.defaults.headers = { token: s2sToken };
app.use(async (req, res, next) => {
  let token = req.headers['token'];
  if (s2sToken && s2sToken === token) {
    next();
  }
  try {
    let userId = await verify(token)
    req.headers.userId = userId;
    next();
  }
  catch (e) {
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
