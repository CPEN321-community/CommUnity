const { Op } = require("sequelize");
const db = require("../models");
const { User, Message, Room } = require("../models");
const { sendNotifToUser } = require("./userTokenController");
const Singleton = require("../singleton");
const axios = require("axios");
const { OK, INTERNAL_SERVER_ERROR } = require("../httpCodes");

const deleteRoom = async (req, res) => {
  if (req.params.postId) {
    await Room.destroy({
      where: {
        postId: req.params.postId,
      },
    });
    await Message.destroy({
      where: {
        postId: req.params.postId,
      },
    });
    res.sendStatus(OK);
  } else {
    console.log("deleteRoom Error " + e);
    res.sendStatus(INTERNAL_SERVER_ERROR);
  }
};

const getChats = async (req, res) => {
  if (req.params.userId) {
    const user = await User.findOne({
      where: { userId: req.params.userId },
    });

    const { firstName, lastName, profilePicture } = user;

    const rooms = await Room.findAll({
      where: { userId: req.params.userId },
    });

    const allChats = await Promise.all(
      rooms.map(async (room) => {
        const postId = room.dataValues.postId;
        const receiver = await Room.findOne({
          where: { userId: { [Op.ne]: req.params.userId }, postId },
        });
        const receiverUser = await User.findByPk(receiver.userId);
        const unformattedMessages = await Message.findAll({
          where: { postId },
          order: [["createdAt", "ASC"]],
        });
        const msg = unformattedMessages.map((m) => {
          const { id, message, userId, createdAt, updatedAt } = m.dataValues;
          return { id, message, userId, createdAt, updatedAt };
        });

        const returnObj = {
          postId,
          sender: req.params.userId,
          senderFirstName: firstName,
          senderLastName: lastName,
          senderProfilePicture: profilePicture,
          receiver: receiver.userId,
          receiverFirstName: receiverUser.firstName,
          receiverLastName: receiverUser.lastName,
          receiverProfilePicture: receiverUser.profilePicture,
          messages: msg,
        };

        return returnObj;
      })
    );

    res.status(OK).json(allChats);
  } else {
    console.log("getChats Error: userId does not exist in parameter");
    res.status(INTERNAL_SERVER_ERROR);
  }
};

const changeUserInfo = async (req, res) => {
  const userId = req.body.userId;
  const firstName = req.body.firstName;
  const lastName = req.body.lastName;
  const profilePicture = req.body.profilePicture;
  if (userId && firstName && lastName && profilePicture) {
    await User.upsert({
      userId,
      firstName,
      lastName,
      profilePicture,
    });

    res.sendStatus(OK);
  } else {
    res.sendStatus(INTERNAL_SERVER_ERROR);
  }
};

const getAssociatedRooms = async (userId) => {
  if (userId) {
    const user = await Room.findAll({ where: { userId } });
    if (user) {
      return user.map((room) => room.postId);
    } else {
      console.log("no rooms were found for userId: " + userId);
    }
  } else {
    console.log("getAssociatedRooms Error: " + e);
  }
};

const createRoom = async (postId, isOffer, senderData) => {
  const senderId = senderData.senderId;
  const senderFirstName = senderData.senderFirstName;
  const senderLastName = senderData.senderLastName;
  const senderProfilePicture = senderData.senderProfilePicture;
  if (!senderId) {
    console.error("createRoom Error: senderId does not exist");
    return false;
  }

  const existingChat = await Room.findOne({ where: { postId, userId: senderId } });
  if (existingChat) {
    console.error("User already joined this chat room");
    return false;
  }

  let typeString = isOffer ? "offers" : "requests";
  let post;
  let postData;
  try {
    console.log(process.env.POST_URL);
    post = await axios.get(
      `${process.env.POST_URL}/communitypost/${typeString}/${postId}`
    );
    postData = post.data;
    if (!postData) {
      console.error("Tried to make chat for non-existent post");
      return false;
    }
  } catch (e) {
    console.log("ERROR GETTING POST");
    console.error(e);
    return false;
  }

  const postOwnerId = postData.userId;
  if (postOwnerId === senderId) {
    console.error("createRoom Error: userId is the same as post owner");
    return false;
  }

  let userData;
  try {
    const receiverReq = await axios.get(
      `${process.env.USER_URL}/user/${postOwnerId}`
    );
    userData = receiverReq.data;
  } catch (e) {
    console.error(e.response.status);
  }
  if (!userData || !userData.user) {
    console.error("Failed to get user data to create chat");
    return false;
  }

  const user = userData.user;

  const receiverId = user.userId;
  const receiverFirstName = user.firstName;
  const receiverLastName = user.lastName;
  const receiverProfilePicture = user.profilePicture;
  try {
    const room = await db.sequelize.transaction(async (t) => {
      await User.upsert(
        {
          userId: receiverId,
          firstName: receiverFirstName,
          lastName: receiverLastName,
          profilePicture: receiverProfilePicture,
        },
        { transaction: t }
      );

      await User.upsert(
        {
          userId: senderId,
          firstName: senderFirstName,
          lastName: senderLastName,
          profilePicture: senderProfilePicture,
        },
        { transaction: t }
      );

      const room1 = await Room.upsert(
        {
          postId,
          userId: receiverId,
        },
        { transaction: t }
      );

      const room2 = await Room.upsert(
        {
          postId,
          userId: senderId,
        },
        { transaction: t }
      );
      if (!(room1[1] && room2[1])) {
        console.error("Failed to create chat room");
        return false;
      }
      return { postId, sender: senderId, senderFirstName, senderLastName, senderProfilePicture, reciever: receiverId, receiverFirstName, receiverLastName, receiverProfilePicture,  messages: []  }
    });
    return room;
  } catch (e) {
    console.error(e);
    console.error("Failed to create chat rooms or users");
    return false;
  }
};

const sendMessage = async (message, userId, postId) => {
  if (message && userId && postId) {
    const msg = await Message.create({
      postId,
      userId,
      message,
    });

    const userInfo = await User.findByPk(userId);
    const receiver = await Room.findOne({
      where: { userId: { [Op.ne]: userId }, postId },
    });

    const ActiveUsers = new Singleton().getInstance();
    const receieverIsInactive = !ActiveUsers.set.has(receiver.userId);

    if (receieverIsInactive) {
      await sendNotifToUser(receiver.userId, {
        title: "Message from " + userInfo.dataValues.firstName,
        body: message,
      });
    }
    return msg.dataValues;
  } else {
    console.log("sendMessage Error, no message or userId or postId");
  }
};

module.exports = {
  deleteRoom,
  getChats,
  changeUserInfo,
  getAssociatedRooms,
  createRoom,
  sendMessage,
};
