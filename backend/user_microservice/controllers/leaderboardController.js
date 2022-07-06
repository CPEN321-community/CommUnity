const sequelize = require('sequelize');
const { Leaderboard } = require("../models");

const getTopNUsers = async (req, res) => {
   try {
        const N = req.params.top;
        const response = await Leaderboard.findAll({
            order: [["score","DESC"]],
            limit: parseInt(N),
        });
       res.json(response);
       res.sendStatus(200);
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
        res.json(response);
        res.sendStatus(200);
    } catch (error) {
        console.log("Error getting user rank: " + error);
        res.sendStatus(500);
    }
};

const upsertUserMethod = async ({ userId, offerPosts, requestPosts }) => {
    try {
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
        return true;
    } catch (error) {
        console.log("Error upserting user rank: " + error);
        return false;
    }
}

const upsertUserRank = async (req, res) => {
    const result = upsertUserMethod(req.body)
    if (result) {
        res.sendStatus(200);
    } else {
        res.sendStatus(500);
    }
};

module.exports = {
    getTopNUsers,
    getUserRank,
    upsertUserMethod,
    upsertUserRank,
};