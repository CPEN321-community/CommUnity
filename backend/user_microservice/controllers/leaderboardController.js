const { Op } = require("sequelize");
const { Leaderboard } = require("../models/leaderboardModel");

const getTopNUsers = async (req, res) => {
   try {
        const N = req.query.top;
        const response = await Leaderboard.findAll({
            order: sequelize.fn('max', sequelize.col('score')),
            limit: N,
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

const upsertUserRank = async (req, res) => {
    try {
        const { userId, offerPosts, requestPosts } = req.body;
        await Leaderboard.upsert({
            userId,
            offerPosts,
            requestPosts,
        });
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