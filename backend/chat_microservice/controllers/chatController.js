const { User, Message, Room } = require('../models');

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

const getChatData = async (req, res) => {
  // const { id } = req.body;
  const { userId } = req.query;
  res.json(userId);
};

const getChatSocket = async (req, res) => {
  const { id } = req.body;
  const response = null;
  res.json(response);
};

const getMessageFromSocket = async (req, res) => {
  const { id } = req.body;
  const response = null;
  res.json(response);
};

const addMessageToSocket = async (req, res) => {
  const { id } = req.body;
  const response = null;
  res.json(response);
};

module.exports = {
  getAssociatedRooms,
  getChatData, 
  getChatSocket, 
  getMessageFromSocket, 
  addMessageToSocket
};
