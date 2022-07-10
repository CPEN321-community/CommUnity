const Router = require('express-promise-router');
const { getChats, changeUserInfo, deleteRoom } = require('./controllers/chatController');
const { createUserToken, sendNotifToUser } = require('./controllers/userTokenController');

const router = Router();

router.get('/chat/:userId', getChats);
router.delete('/chat/:postId', deleteRoom);
router.post('/chat/changeUserInfo', changeUserInfo);
router.get('/chat/test1/html', (req, res) => res.sendFile(__dirname + '/socket/chat1.html'));
router.get('/chat/test2/html', (req, res) => res.sendFile(__dirname + '/socket/chat2.html'));

// Tokens
router.post('/token', createUserToken);
router.post('/notification', sendNotifToUser);


module.exports = router;
