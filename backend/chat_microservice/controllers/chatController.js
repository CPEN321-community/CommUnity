const { Op } = require("sequelize");
const { User, Message, Room } = require('../models');
const { sendNotifToUser } = require('./userTokenController');
const Singleton = require('../singleton');
const axios = require("axios");
const deleteRoom = async (req, res) => {
  try {
    await Room.destroy({
      where: {
        postId: req.params.postId,
      }
    });
    await Message.destroy({
      where: {
        postId: req.params.postId,
      }
    });

    res.sendStatus(200);
  } catch (e) {
    console.log('deleteRoom Error ' + e);
    res.sendStatus(500);
  }
}

const getChats = async (req, res) => {
  try {
    const { userId } = req.params;
    console.log("***", userId);
  
    const user = await User.findOne({ 
      where: { userId } 
    });
    console.log(user);

    const { firstName, lastName, profilePicture } = user;
    
    console.log("Got here");
    
    const rooms = await Room.findAll({ 
      where: { userId } 
    });
  
    const allChats = await Promise.all(rooms.map(async room => {

      const postId = room.dataValues.postId;      
      const receiver = await Room.findOne({ where: {userId: {[Op.ne]: userId}, postId} });
      const receiverUser = await User.findByPk(receiver.userId);
      const unformattedMessages = await Message.findAll({where: { postId }, order: [["createdAt", "ASC"]]});
      const msg = unformattedMessages.map(m => {
        const { postId, ...rest } = m.dataValues;
        return rest;
      })

      return {
        postId,
        sender: userId,
        senderFirstName: firstName,
        senderLastName: lastName,
        senderProfilePicture: profilePicture,
        receiver: receiver.userId,
        receiverFirstName: receiverUser.firstName,
        receiverLastName: receiverUser.lastName,
        receiverProfilePicture: receiverUser.profilePicture,
        messages: msg,
      }
    }));
  
    res.status(200).json(allChats);
  } catch (e) {
    console.log("getChats Error: " + e);
    res.status(500).json({err: e.toString()})
  }
};

const changeUserInfo = async (req, res) => {
  try {
    const { userId, firstName, lastName, profilePicture } = req.body;
    
    await User.upsert({
      userId,
      firstName,
      lastName,
      profilePicture,
    });

    res.sendStatus(200);
  } catch (e) {
    console.log("changeInfo Error: " + e);
    res.sendStatus(500);
  }
}

const getAssociatedRooms = async (userId) => {
  try {
    const user = await Room.findAll({ where: { userId: userId } });
    if (user) {
      return user.map(room => room.postId); 
    } else {
      console.log('no rooms were found for userId: ' + userId);
    }
  } catch (e) {
    console.log('getAssociatedRooms Error: ' + e);
  }
}

const createRoom = async (postId, isOffer, senderData) => {
  try {
    let typeString = isOffer ? "offers" : "requests";
    let post = await axios.get(`${process.env.POST_URL}/communitypost/${typeString}/${postId}`);
    let postData = post.data;
    console.log("POSTDATA");
    console.log(postData);
    if (!postData){
      console.error("Tried to make chat for non-existent post");
      return false;
    }

    const postOwnerId = postData.userId;
    const receiverReq = await axios.get(`${process.env.USER_URL}/user/${postOwnerId}`);
    console.log("RECIEVERREQ!!");
    console.log(receiverReq);
    const userData = receiverReq.data;
    console.log("userData");
    console.log(userData);
    if (!userData || !userData.user) {
      console.error("Failed to get user data to create chat");
      return false;
    }

    const user = userData.user;

    const receiverId = user.userId;
    const receiverFirstName = user.firstName;
    const receiverLastName =  user.lastName;
    const receiverProfilePicture = user.profilePicture;

    const { 
      senderId,
      senderFirstName,
      senderLastName,
      senderProfilePicture } = senderData;

    await User.upsert({
      userId: receiverId,
      firstName: receiverFirstName,
      lastName: receiverLastName,
      profilePicture: receiverProfilePicture,
    });

    console.log("first one");
    await User.upsert({
      userId: senderId,
      firstName: senderFirstName,
      lastName: senderLastName,
      profilePicture: senderProfilePicture,
    });
    const [r1, created1] = await Room.upsert({
      postId,
      userId: receiverId,
    });
    const [r2, created2] = await Room.upsert({
      postId,
      userId: senderId,
    });

    return created1 && created2;
  } catch (e) {
    console.log('createRoom Error ' + e);
  }
};

const sendMessage = async (message, userId, postId) => {
  try {
    const msg = await Message.create({
      postId,
      userId,
      message,
    });

    const userInfo = await User.findByPk(userId);
    const receiver = await Room.findOne({ where: {userId: {[Op.ne]: userId}, postId} });

    const ActiveUsers = (new Singleton()).getInstance();
    const receieverIsInactive = !ActiveUsers.set.has(receiver.userId);

    if (receieverIsInactive) {
        await sendNotifToUser(receiver.userId, {
          "title": "Message from " + userInfo.dataValues.firstName,
          "body": message,
        });
    }
    return msg.dataValues;
  } catch (e) {
    console.log('sendMessage Error ' + e);
  }
}

module.exports = {
  deleteRoom,
  getChats,
  changeUserInfo,
  getAssociatedRooms,
  createRoom,
  sendMessage,
};
