const {User, UserToken} = require("../models");
const admin = require('firebase-admin/app');
const {getMessaging} = require("firebase-admin/messaging");
var serviceAccount = require("../firebase_service_key.json");

admin.initializeApp({
  credential: admin.cert(serviceAccount)
});

const FCM = getMessaging();

const createUserToken = async (req, res) => {
  try {
    const {userId, token} = req.body;

    await User.upsert({
      userId,
      firstName: "",
      lastName: "",
      profilePicture: "",
    });

    const [data, created] = await UserToken.upsert({
      userId,
      token,
    });
    if (created) {
      res.sendStatus(201);
      return;
    }
    else {
      res.sendStatus(200);
    }
  }
  catch (e) {
    console.error(e);
    res.status(500).json({
      error: e
    })
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