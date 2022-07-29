const express = require('express');
const routes = require('./routes');
const axios = require('axios');
const db = require('./models');
const dotenv = require("dotenv")
const s2sToken = require('./config/config.json')["s2sToken"];
const {OAuth2Client} = require('google-auth-library');

const {UNAUTHORIZED} = require('./httpCodes');

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

app.use(express.json());

axios.defaults.headers = { token: s2sToken }
app.use(async (req, res, next) => {
  let token = req.headers["token"];
  if (s2sToken && s2sToken === token) {
    req.headers.userId = "testuserid";
    next();
  } else {
    try {
      let userId = await verify(token);
      req.headers.userId = userId;
      next();
    } catch (e) {
      res.status(UNAUTHORIZED).send("Unsuccessfull");
    }
  }
});
app.use(routes);


// app.use(express.urlencoded({ extended: true }));

db.sequelize.sync().then((req) => {}).catch(e => console.log(e));

const PORT = process.env.PORT || 8081;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}.`);
});
