
const { Op } = require("sequelize");
const { User, Preferences } = require("../models");
const { upsertUserMethod } = require("./leaderboardController.js");

const getUser = async (req, res) => {
   try {
       const userId = req.params.userId;
       const response = await User.findByPk(userId, {include: ["preferences"]});
       console.log("Fetching user");
       console.log(userId);
       console.log(response);
       res.json({user: response});
    //    res.sendStatus(200);
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

const updateUser = async (req, res) => {
    try {
        const response = await User.update({
            userId: req.body.userId,
            firstName: req.body.firstName,
            lastName: req.body.lastName,
            email: req.body.email,
            profilePicture: req.body.profilePicture
        });

        // await fetch('http://localhost:1000/chat/changeUserInfo', {
        //   method: 'POST',
        //   headers: {
        //     'Content-Type': 'application/json',
        //   },
        //   body: JSON.stringify({
        //     userId: '',
        //     firstName: '',
        //     lastName: '',
        //     profilePicture: '',
        //   }),
        // })

        res.json(response);
        res.sendStatus(200);
    } catch (error) {
        console.log("Error upserting user: " + error);
        res.sendStatus(500);
    }
}

const createUser = async (req, res) => {
    try {
        const response = await User.create({
            userId: req.body.userId,
            firstName: req.body.firstName,
            lastName: req.body.lastName,
            email: req.body.email,
            profilePicture: req.body.profilePicture
        });

        const result = await upsertUserMethod({ userId: req.body.userId, offerPosts: 0, requestPosts: 0 });
        if (result) {
            res.json(response);
            res.sendStatus(200);
        } else {
            await User.destroy({
                userId: req.body.userId,
                firstName: req.body.firstName,
                lastName: req.body.lastName,
                email: req.body.email,
                profilePicture: req.body.profilePicture    
            });
            console.log("Error creating scoreboard for user: " + error);
            res.sendStatus(500);
        }
    } catch (error) {
        console.log("Error upserting user: " + error);
        res.sendStatus(500);
    }
}


module.exports = {
    getUser,
    upsertUserPreference,
    updateUser,
    createUser,
  };