
const { Op } = require("sequelize");
const { User, Preference, Leaderboard } = require("../models");
const { upsertUserMethod } = require("./leaderboardController.js");
const axios = require("axios");
const getUser = async (req, res) => {
   try {
       const userId = req.params.userId;
       const response = await User.findByPk(userId, {include: ["preferences", "leaderboard"]});
       res.json({user: response});
   } catch (error) {
       console.log("Error finding user: " + error);
       res.sendStatus(500);
   }
};

const upsertUserPreference = async (req, res) => {
    try {
        const {userId} = req.body;
        const user = await User.findByPk(userId);
        const [preference, isCreated] = await Preference.upsert({
            userId,
            type: req.body.type,
            value: req.body.value
        });
        await preference.setUser(user);
        res.json(preference);
    } catch (error) {
        console.log("Error updating user preferences: " + error);
        res.sendStatus(500);        
    }
}

const deleteUserPreference = async (req, res) => {
    console.log("Delete user pref");
    try {
        const {preferenceId} = req.params;
        const deleted = await Preference.destroy({
            where: {id: preferenceId},
        });
        res.json({deleted});
    } catch (error) {
        console.log("Error deleting user preferences: " + error);
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
        }, {where: {userId: req.body.userId}});

        // TODO: fix this
        // await axios.post('http://ec2-35-183-28-141.ca-central-1.compute.amazonaws.com:3000', {
        //     userId: req.body.userId,
        //     firstName: req.body.firstName,
        //     lastName: req.body.lastName,
        //     profilePicture: req.body.profilePicture
        // });

        res.json(response);
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
            profilePicture: req.body.profilePicture,
            leaderboard: {
                offerPosts: 0,
                requestPosts: 0,
                score: 0,
            }
        },
        {
            include: [{association: User.Leaderboard, as: "leaderboard"}]
        }
        );
        res.status(201).json(response)
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
    deleteUserPreference
  };