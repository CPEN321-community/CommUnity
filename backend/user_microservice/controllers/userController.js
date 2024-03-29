const { User, Preference } = require("../models");
const axios = require("axios");
const {
  OK,
  CREATED,
  INTERNAL_SERVER_ERROR,
  UNAUTHORIZED,
  NOT_FOUND,
  BAD_REQUEST
} = require("../httpCodes");
const s2sToken = require('../../config_user.json')["s2sToken"];

const verifyToken = async (req, res) => {
  let response = await axios(
    `https://oauth2.googleapis.com/tokeninfo?id_token=${req.body.token}`
  );
  if (response.status == OK) {
    let foundUser = await userStore.findUserForLogin(response.data.sub);
    if (!foundUser) {
      res.status(NOT_FOUND).json(JSON.parse(JSON.stringify({ token: response.data.sub })));
    }
  } else {
    res.status(UNAUTHORIZED);
  }
};

const getUser = async (req, res) => {
  const userId = req.params.userId;
  const response = await User.findByPk(userId, {
    include: ["preferences", "leaderboard"],
  });
  if (response) {
    res.json(JSON.parse(JSON.stringify({ user: response })));
  } else {
    res.status(NOT_FOUND).json({message:"User not Found!"});
  }
};

const upsertUserPreference = async (req, res) => {
  const userId = req.headers.userId;
  const user = await User.findByPk(userId);
  if (!user) {
    console.error("User not found");
    res.status(NOT_FOUND).json(JSON.parse(JSON.stringify({error: "User not found!"})));
    return;
  }
  const [preference] = await Preference.upsert({
    userId,
    type: req.body.type,
    value: req.body.value,
  });
  await preference.setUser(user);
  res.status(CREATED).json(JSON.parse(JSON.stringify(preference)));
};

const deleteUserPreference = async (req, res) => {

  if (req.params.preferenceId) {
    const preferenceId = req.params.preferenceId;
    const deleted = await Preference.destroy({
      where: { id: preferenceId },
    });
    res.json(JSON.parse(JSON.stringify({ deleted })));
  } else {

    res.sendStatus(INTERNAL_SERVER_ERROR);
  }
};

const updateUser = async (req, res) => {
  const hasAllFields = (req.headers.userid || req.headers.userId) && req.body.firstName && req.body.lastName && req.body.email && req.body.profilePicture;
  const validEmail = req.body.email.includes("@") && req.body.email.includes(".com");
  const validProfilePic = req.body.profilePicture.includes(".com");

  if (hasAllFields && validEmail && validProfilePic) {
    const response = await User.update(
      {
        userId: req.headers.userId || req.headers.userid,
        firstName: req.body.firstName,
        lastName: req.body.lastName,
        email: req.body.email,
        profilePicture: req.body.profilePicture,
      }, { where: { userId: req.headers.userId || req.headers.userid } }
    );

    await axios.post(`${process.env.CHAT_URL}/chat/changeUserInfo`, {
      userId: req.headers.userId || req.headers.userid,
      firstName: req.body.firstName,
      lastName: req.body.lastName,
      profilePicture: req.body.profilePicture,
    }, {
      headers: {
        token: s2sToken,
      }
    });

    res.status(OK).json(JSON.parse(JSON.stringify(response)));
  } else {
    res.sendStatus(BAD_REQUEST);
  }
}

const createUser = async (req, res) => {
  const hasAllFields = (req.headers.userid || req.headers.userId) && req.body.firstName && req.body.lastName && req.body.email && req.body.profilePicture;
  const validEmail = req.body.email.includes("@") && req.body.email.includes(".com");
  const validProfilePic = req.body.profilePicture.includes(".com");
  console.log(req.body, req.headers.userId);
  if (hasAllFields && validEmail && validProfilePic) {
    const response = await User.create(
      {
        userId: req.headers.userId || req.headers.userid,
        firstName: req.body.firstName,
        lastName: req.body.lastName,
        email: req.body.email,
        profilePicture: req.body.profilePicture,
        leaderboard: {
          offerPosts: 0,
          requestPosts: 0,
          score: 0,
        },
      },
      {
        include: [{ association: User.Leaderboard, as: "leaderboard" }],
      }
    );
    res.status(CREATED).json(JSON.parse(JSON.stringify(response)));
  } else {
    res.sendStatus(BAD_REQUEST);
  }
};

module.exports = {
  getUser,
  updateUser,
  createUser,
  verifyToken,
  upsertUserPreference,
  deleteUserPreference
};
