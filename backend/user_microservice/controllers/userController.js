
const { Op } = require("sequelize");
const { User } = require("../models/userModel");
const { Preferences } = require("../models/preferencesModel");

const getUser = async (req, res) => {
   try {
       const userId = req.userId;
       const response = await User.findByPk(userId, {include: ["preferences"]});
       res.json(response);
       res.sendStatus(200);
   } catch (error) {
       console.log("Error finding user: " + error);
       res.sendStatus(500);
   }
};

const upsertUserPreference = async (req, res) => {
    try {
        const response = await Preferences.upsert({
            type: req.body.type,
            value: req.body.value
        });
        res.json(response);
        res.sendStatus(200);
    } catch (error) {
        console.log("Error updating user preferences: " + error);
        res.sendStatus(500);        
    }
}

const upsertUser = async (req, res) => {
    try {
        const response = await User.upsert({
            firstName: req.body.firstName,
            lastName: req.body.lastName,
            email: req.body.email,
            profilePicture: req.body.profilePicture
        });
        
        await fetch('http://localhost:1000/chat/changeUserInfo', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            userId: '',
            firstName: '',
            lastName: '',
            profilePicture: '',
          }),
        })

        res.json(response);
        res.sendStatus(200);

    } catch (error) {
        console.log("Error upserting user: " + error);
        res.sendStatus(500);
    }
}


module.exports = {
    getUser,
    upsertUserPreference,
    upsertUser
  };