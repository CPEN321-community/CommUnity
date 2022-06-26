const Router = require('express-promise-router');
const { getChatData, getChatSocket, getMessageFromSocket, addMessageToSocket } = require('./controllers/chatController');

const router = Router();

// parthvi's part
// router.get('/cart/get', getCartController);
// router.post('/cart/add', addCartController);

// Chat Module
router.get('/chat', getChatData);
router.get('/socket/:chatId', getChatSocket);
router.get('/socket/message/:chatId', getMessageFromSocket);
router.post('/socket/message/:chatId', addMessageToSocket)

module.exports = router;
