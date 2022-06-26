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
  getChatData, 
  getChatSocket, 
  getMessageFromSocket, 
  addMessageToSocket
};
