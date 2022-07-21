const express = require('express');
const axios = require('axios');
const routes = require('./routes');
const Singleton = require('./singleton');
const model = (new Singleton()).getInstance();
const dotenv = require("dotenv");
const s2sToken = require('./config/config')["s2sToken"];

dotenv.config({path: "../ports.env"});
dotenv.config();

const app = express();

app.use(express.json());
app.use(routes);

axios.defaults.headers = { s2sToken }
app.use(async (req, res, next) => {
  if (req.headers["s2sToken"] && req.headers["s2sToken"] == s2sToken) {
    next();
  } else {
    const token = req.headers['token'];
    await axios.post(`${process.env.USER_URL}/token/verify${token}`);
    if (user) {
      next();
    } else {
      res.status(UNAUTHORIZED).send("Unsuccessfull");
    }
  }
});

setInterval(() => {
  model.trainModel();
}, [36000 * 24]); // 24 hours

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}.`);
});