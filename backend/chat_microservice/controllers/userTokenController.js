const {User, UserToken} = require("../models");
const admin = require('firebase-admin/app');
const {getMessaging} = require("firebase-admin/messaging");
var serviceAccount = require("../firebase_service_key.json");
const { OK, CREATED, INTERNAL_SERVER_ERROR } = require("../httpCodes");

admin.initializeApp({
  credential: admin.cert(serviceAccount)
});

const FCM = getMessaging();

const createUserToken = async (req, res) => {
    if (req.body.userId && req.body.token) {
      const created1 = await User.upsert({
        userId: req.body.userId,
        firstName: "",
        lastName: "",
        profilePicture: "",
      });
      const created2 = await UserToken.upsert({
        userId: req.body.userId,
        token: req.body.token,
      });
      // index 0: entity (obj), index 1: created (bool)
      if (created1[1] && created2[1]) {
        res.sendStatus(CREATED);
        return;
      }
      else {
        res.sendStatus(OK);
      }
    } else {
      console.error(e);
      res.status(INTERNAL_SERVER_ERROR).json({
        error: e
      });
    }
}
  
const sendNotifToUser = async (userId, payload) => {
  if (userId) {
    const usertoken = await UserToken.findOne({ where: {userId} });
    const token = usertoken.dataValues.token;
    await FCM.sendToDevice(token, { notification: payload });
  } else {
    console.error("Error: no userId");
  }
}

module.exports = { createUserToken, sendNotifToUser }