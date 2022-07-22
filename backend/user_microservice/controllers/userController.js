const { User, Preference } = require("../models");
const axios = require("axios");
const { OK, CREATED, INTERNAL_SERVER_ERROR, UNAUTHORIZED, NOT_FOUND } = require("../index.js");

const verifyToken = async (req, res) => {
    let response = await axios(`https://oauth2.googleapis.com/tokeninfo?id_token=${req.body.token}`);
    if (response.status == OK) {
        let foundUser = await userStore.findUserForLogin(response.data.sub);
        if (!foundUser) {
            res.status(NOT_FOUND).send({ token: response.data.sub });
        }
    } else {
        res.status(UNAUTHORIZED).send(e);
    } 
}

const getUser = async (req, res) => {
   if (req.params.userId) {
       const userId = req.params.userId;
       const response = await User.findByPk(userId, {include: ["preferences", "leaderboard"]});
       res.json({user: response});
   } else {
       console.log("Error finding user");
       res.sendStatus(INTERNAL_SERVER_ERROR);
   }
};

const upsertUserPreference = async (req, res) => {
    if(req.body.userId) {
        const userId = req.body.userId;
        const user = await User.findByPk(userId);
        const [preference] = await Preference.upsert({
            userId,
            type: req.body.type,
            value: req.body.value
        });
        await preference.setUser(user);
        res.json(preference);
    } else {
        console.log("Error updating user preferences: " + error);
        res.sendStatus(INTERNAL_SERVER_ERROR);        
    }
}

const deleteUserPreference = async (req, res) => {
    console.log("Delete user pref");
    if(req.params.preferenceId) {
        const preferenceId = req.params.preferenceId;
        const deleted = await Preference.destroy({
            where: {id: preferenceId},
        });
        res.json({deleted});
    } else {
        console.log("Error deleting user preferences: " + error);
        res.sendStatus(INTERNAL_SERVER_ERROR);        
    }
}

const updateUser = async (req, res) => {
    if(req.body.userId) {
        const response = await User.update({
            userId: req.body.userId,
            firstName: req.body.firstName,
            lastName: req.body.lastName,
            email: req.body.email,
            profilePicture: req.body.profilePicture
        }, {where: {userId: req.body.userId}});

        await axios.post(`${process.env.CHAT_URL}/chat/changeUserInfo`, {
            userId: req.body.userId,
            firstName: req.body.firstName,
            lastName: req.body.lastName,
            profilePicture: req.body.profilePicture
        });

        res.json(response);
    } else {
        console.log("Error upserting user: " + error);
        res.sendStatus(INTERNAL_SERVER_ERROR);
    }
}

const createUser = async (req, res) => {
    if(req.body.token) {
        const response = await User.create({
            token: req.body.token,
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
        res.status(CREATED).json(response)
    } else {
        console.log("Error upserting user: " + error);
        res.sendStatus(INTERNAL_SERVER_ERROR);
    }
}


module.exports = {
    getUser,
    upsertUserPreference,
    updateUser,
    createUser,
    deleteUserPreference,
    verifyToken
  };