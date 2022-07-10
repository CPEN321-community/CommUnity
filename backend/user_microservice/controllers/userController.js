
const { Op } = require("sequelize");
const { User, Preferences, Leaderboard } = require("../models");
const { upsertUserMethod } = require("./leaderboardController.js");

const getUser = async (req, res) => {
   try {
       const userId = req.params.userId;
       const response = await User.findByPk(userId, {include: ["preferences"]});
       console.log("Fetching user");
       console.log(userId);
       console.log(response);
       res.json({user: response});
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

        await axios.post('http://ec2-35-183-28-141.ca-central-1.compute.amazonaws.com:3000', {
            userId: req.body.userId,
            firstName: req.body.firstName,
            lastName: req.body.lastName,
            profilePicture: req.body.profilePicture
        });

        res.json(response);
    } catch (error) {
        console.log("Error upserting user: " + error);
        res.sendStatus(500);
    }
}

const createUser = async (req, res) => {
    try {
        console.log(req.body);
        const response = await User.create({
            userId: req.body.userId,
            firstName: req.body.firstName,
            lastName: req.body.lastName,
            email: req.body.email,
            profilePicture: req.body.profilePicture,
            preferences: {
                type: "test",
                value: "success"
            }
        },
        {
            include: [{association: Preferences.User, as: "preferences"}]
        }
        );
        res.sendStatus(201)

        // const result = await upsertUserMethod({ userId: req.body.userId, offerPosts: 0, requestPosts: 0 });
        // console.log(result);
        // if (result) {
        //     res.json(response);
        // } else {
        //     await User.destroy({
        //         userId: req.body.userId,
        //         firstName: req.body.firstName,
        //         lastName: req.body.lastName,
        //         email: req.body.email,
        //         profilePicture: req.body.profilePicture    
        //     });
        //     console.log("Error creating scoreboard for user: " + error);
        //     res.sendStatus(500);
        // }
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