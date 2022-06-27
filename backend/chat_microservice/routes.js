const Router = require('express-promise-router');
// const { getChatData, getChatSocket, getMessageFromSocket, addMessageToSocket } = require('./controllers/chatController');

const router = Router();

// Chat Module
// router.get('/chat', getChatData);
// router.get('/socket/:chatId', getChatSocket);
// router.get('/socket/message/:chatId', getMessageFromSocket);
// router.post('/socket/message/:chatId', addMessageToSocket)
router.get('/testChat', (req, res) => res.sendFile(__dirname + '/socket/chat.html')); // testing socket with simple html file

// Post Module

// Recommendation Module

// User Module

module.exports = router;
