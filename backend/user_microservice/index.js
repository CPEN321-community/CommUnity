const express = require("express");
const routes = require("./routes");
const db = require("./models");
const dotenv = require("dotenv");
const { OAuth2Client } = require("google-auth-library");
const { UNAUTHORIZED } = require("./httpCodes");
const s2sToken = require("./../config_user.json")["s2sToken"];

dotenv.config({ path: "../ports.env" });
dotenv.config();


const client = new OAuth2Client(process.env.CLIENT_ID);

async function verify(token) {
  const ticket = await client.verifyIdToken({
    idToken: token,
    audience: [process.env.CLIENT_ID],
  });
  const payload = ticket.getPayload();
  const userid = payload["sub"];
  return userid;
}

const app = express();

app.use(express.json());
app.use(async (req, res, next) => {
  let token = req.headers["token"];
  if (s2sToken && s2sToken === token) {
    next();
  } else {
    try {
      let userId = await verify(token);
      req.headers.userId = userId;
      next();
    } catch (e) {
      console.error(e);
      res.status(UNAUTHORIZED).send("Unsuccessfull");
    }
  }
});
app.use(routes);


if (process.env.NODE_ENV != "test") {
  db.sequelize.sync();

  const PORT = process.env.PORT || 8080;
  app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}.`);
  });
}

module.exports = app;