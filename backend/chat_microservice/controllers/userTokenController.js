const {UserToken} = require("../models")
const admin = require('firebase-admin/app');
const {getMessaging} = require("firebase-admin/messaging")
var serviceAccount = require("../firebase_service_key.json");

admin.initializeApp({
  credential: admin.cert(serviceAccount)
});

const FCM = getMessaging();

const createUserToken = async (req, res) => {
  try {
    const {userId, token} = req.body;
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
  
const sendNotifToUser = async (req, res) => {
  const {userId, payload} = req.body;
  try {
    const user = await User.findOne({where: {userId}});
    console.log(user);
    const token = user.dataValues.token;
    await sendNotif(token, payload);

    res.sendStatus(201);
  }
  catch (e) {
    console.error(e);
    res.status(500).json({
      error: e
    });
  }

}

const sendNotif = async (token, payload) => {
  const notif = {
    notification: payload
  }
  return await FCM.sendToDevice(token, notif)
}

module.exports = {createUserToken, sendNotifToUser, sendNotif}