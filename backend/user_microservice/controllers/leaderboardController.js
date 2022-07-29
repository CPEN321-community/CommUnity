const { Op } = require("sequelize");
const { Leaderboard, User } = require("../models");
const { INTERNAL_SERVER_ERROR } = require('../httpCodes');

const getTopNUsers = async (req, res) => {
   if (req.params.N) {
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
            const returnObj = { 
                firstName: user.dataValues.firstName,
                lastName: user.dataValues.lastName,
                offerPosts: userScore.dataValues.offerPosts,
                requestPosts: userScore.dataValues.requestPosts,
                score: userScore.dataValues.score,
                profilePicture: user.dataValues.profilePicture
            };
            return returnObj;
        }));
       res.status(200).json(responseWithNames);
   } else {
       console.log("Error getting top N users");
       res.sendStatus(INTERNAL_SERVER_ERROR);
   }
};

/**
 * Only return the user's position on the leaderboard
 */
const getUserRank = async (req, res) => {
    const userId  = req.params.userId;
    if (userId) {
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
    } else {
        console.log("Error getting user rank");
        res.sendStatus(INTERNAL_SERVER_ERROR);
    }
};

const upsertUserMethod = async ({ userId, offerPosts, requestPosts }) => {
    if (userId) {
        const currLeaderboard = await Leaderboard.findOne({
                    where: { userId }
        });
        const scoreAlreadyExists = currLeaderboard != null;
        console.log(currLeaderboard);
        if (scoreAlreadyExists) {
            const newOfferPosts = currLeaderboard.dataValues.offerPosts + offerPosts;
            const newRequestPosts = currLeaderboard.dataValues.requestPosts + requestPosts;
            const score = (newOfferPosts * 7) + (newRequestPosts * 3);    

            await Leaderboard.update({
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
    } else {
        console.log("Error upserting user rank: " + error);
        return false;
    }
}

const upsertUserRank = async (req, res) => {
    const result = upsertUserMethod(req.body)
    if (result) {
        res.sendStatus(200);
    } else {
        res.sendStatus(INTERNAL_SERVER_ERROR);
    }
};

module.exports = {
    getTopNUsers,
    getUserRank,
    upsertUserMethod,
    upsertUserRank,
}; 