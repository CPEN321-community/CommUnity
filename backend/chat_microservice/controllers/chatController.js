const { Op } = require("sequelize");
const { User, Message, Room } = require('../models');
const { sendNotifToUser } = require('./userTokenController');

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

const createRoom = async roomDto => {
  try {
    const { 
      postId,
      receiverId,
      receiverFirstName,
      receiverLastName,
      receiverProfilePicture,
      senderId,
      senderFirstName,
      senderLastName,
      senderProfilePicture } = roomDto;
      console.log(roomDto);

      console.log(receiverFirstName);
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

    // TODO : check if user is online

    const userInfo = await User.findByPk(userId);
    const receiver = await Room.findOne({ where: {userId: {[Op.ne]: userId}, postId} });
  
    await sendNotifToUser(receiver.userId, {
      "title": "Message from " + userInfo.dataValues.firstName,
      "body": message,
    });

    return { msgObj: msg.dataValues, isSent: created };
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
