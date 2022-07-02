const { Op } = require("sequelize");
const { User, Message, Room } = require('../models');

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
  
    const { name, profilePicture } = await User.findOne({ 
      where: { userId } 
    });
    
    const rooms = await Room.findAll({ 
      where: { userId } 
    });
  
    const allChats = await Promise.all(rooms.map(async r => {

      const postId = r.dataValues.postId;      
      const receiever = await Room.findOne({ where: {userId: {[Op.ne]: userId}, postId} });
      const receieverUser = await User.findByPk(receiever.id);
      const msg = await Message.findAll({ postId });

      return {
        sender: userId,
        senderName: name,
        senderProfilePicture: profilePicture,
        receiever: receiever.userId,
        receieverName: receieverUser.name,
        receieverProfilePicture: receieverUser.profilePicture,
        messages: msg,
      }
    }));
  
    res.json(allChats);
    res.sendStatus(200);
  } catch (e) {
    console.log("getChats Error: " + e);
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
      associatedRooms = user.map(room => room.postId);
      console.log(associatedRooms);
      return associatedRooms;
    } else {
      console.log('no rooms were found for userId: ' + userId);
    }
  } catch (e) {
    console.log('getAssociatedRooms Error: ' + e);
  }
}

const createRoom = async (postId, receieverId, senderId) => {
  try {
    const [r1, created1] = await Room.upsert({
      postId,
      userId: receieverId,
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
    const [_, created] = await Message.upsert({
      postId,
      userId,
      message,
    });
    return created;
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
