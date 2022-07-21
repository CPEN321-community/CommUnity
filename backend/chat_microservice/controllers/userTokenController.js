const {User, UserToken} = require("../models");
const admin = require('firebase-admin/app');
const {getMessaging} = require("firebase-admin/messaging");
var serviceAccount = require("../firebase_service_key.json");
const { OK, CREATED, INTERNAL_SERVER_ERROR } = require("../index");

admin.initializeApp({
  credential: admin.cert(serviceAccount)
});

const FCM = getMessaging();

const createUserToken = async (req, res) => {
    const {userId, token} = req.body;
    if (userId && token) {
      const created1 = await User.upsert({
        userId,
        firstName: "",
        lastName: "",
        profilePicture: "",
      });
      const created2 = await UserToken.upsert({
        userId,
        token,
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
  try {
    const usertoken = await UserToken.findOne({ where: {userId} });
    const token = usertoken.dataValues.token;
    await FCM.sendToDevice(token, { notification: payload });
  }
  catch (e) {
    console.error(e);
  }
}

module.exports = { createUserToken, sendNotifToUser }