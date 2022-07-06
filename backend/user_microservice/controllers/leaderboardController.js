const sequelize = require('sequelize');
const { Leaderboard } = require("../models");

const getTopNUsers = async (req, res) => {
   try {
        const N = req.params.top;
        const response = await Leaderboard.findAll({
            order: [["score","DESC"]],
            limit: parseInt(N),
        });
       res.status(200).json(response);
   } catch (error) {
       console.log("Error getting top N users: " + error);
       res.sendStatus(500);
   }
};

const getUserRank = async (req, res) => {
    try {
        const { userId } = req.params;
        const response = await Leaderboard.findOne({ 
            where: { userId }
        });
        res.status(200).json(response);
    } catch (error) {
        console.log("Error getting user rank: " + error);
        res.sendStatus(500);
    }
 };

const upsertUserRank = async (req, res) => {
    try {
        const { userId, offerPosts, requestPosts } = req.body;
        const currLeaderboard = await Leaderboard.findOne({
            where: { userId }
        });
        const scoreAlreadyExists = currLeaderboard != null;
        if (scoreAlreadyExists) {
            const newOfferPosts = currLeaderboard.offerPosts + offerPosts;
            const newRequestPosts = currLeaderboard.requestPosts + requestPosts;
            const score = (newOfferPosts * 7) + (newRequestPosts * 3);    

            await Leaderboard.update({
                userId,
                offerPosts: newOfferPosts,
                requestPosts: newRequestPosts,
                score,
            }, { where: { userId } });
        } else {
            await Leaderboard.create({
                userId,
                offerPosts,
                requestPosts,
                score: (offerPosts * 7) + (requestPosts * 3),
            });
        }

        res.sendStatus(200);
    } catch (error) {
        console.log("Error upserting user rank: " + error);
        res.sendStatus(500);
    }
};

module.exports = {
    getTopNUsers,
    getUserRank,
    upsertUserRank,
};