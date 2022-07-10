const { Op } = require("sequelize");
const sequelize = require('sequelize');
const { Leaderboard, User } = require("../models");

const getTopNUsers = async (req, res) => {
   try {
        const N = req.params.N;
        const response = await Leaderboard.findAll({
            order: [["score","DESC"]],
            limit: parseInt(N),
        });
        
        const responseWithNames = await Promise.all(response.map(async userScore => {
            console.log(userScore);         
            const user = await User.findOne({
                where: { userId: userScore.dataValues.UserUserId }
            })

            return { 
                firstName: user.dataValues.firstName,
                lastName: user.dataValues.lastName,
                offerPosts: userScore.dataValues.offerPosts,
                requestPosts: userScore.dataValues.requestPosts,
                score: userScore.dataValues.score,
                profilePicture: user.dataValues.profilePicture
            }
        }));
       res.status(200).json(responseWithNames);
   } catch (error) {
       console.log("Error getting top N users: " + error);
       res.sendStatus(500);
   }
};

/**
 * Only return the user's position on the leaderboard
 */
const getUserRank = async (req, res) => {
    try {
        const { userId } = req.params;
        console.log(userId);
        const user = await Leaderboard.findOne({ 
            where: { userId }
        });

        console.log(user);
        //create a function that finds all of the users with higher scores than you
        const higherScoringUsers = await Leaderboard.findAll({
            where: {score: {[Op.gte]: user.score}}
        })
        const rank = higherScoringUsers.length;
        res.status(200).json({ rank });
    } catch (error) {
        console.log("Error getting user rank: " + error);
        res.sendStatus(500);
    }
};

const getUserLeaderboardStats = async (req, res) => {
    try {
        const { userId } = req.params;
        const user = await Leaderboard.findOne({ 
            where: { userId }
        });

        res.json(user);
    }
    catch (e) {
        console.error(e);
        res.sendStatus(500);
    }
}

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
    getUserLeaderboardStats,
}; 