const express = require('express');
const routes = require('./routes');
const db = require('./models');
const dotenv = require("dotenv")

const OK = 200;
const CREATED = 201
const INTERNAL_SERVER_ERROR = 500;
const UNAUTHORIZED = 401
const NOT_FOUND = 400

dotenv.config({path: "../ports.env"});
dotenv.config();

const app = express();

app.use(express.json());
app.use(async (req, res, next) => {
  let token = req.headers['token'];
  let user = await User.findOne({ where: {token} });
  if (user) {
    next();
  } else {
    res.status(UNAUTHORIZED).send("Unsuccessfull");
  }
});
app.use(routes);

db.sequelize.sync().then((req) => {
}).catch(e => console.log(e));

const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}.`);
});

module.exports = { OK, CREATED, INTERNAL_SERVER_ERROR, UNAUTHORIZED, NOT_FOUND};