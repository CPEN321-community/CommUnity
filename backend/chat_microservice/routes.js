const Router = require('express-promise-router');
const { getChats, changeUserInfo, deleteRoom } = require('./controllers/chatController');
const { createUserToken } = require('./controllers/userTokenController');

const router = Router();

router.get('/chat/:userId', getChats);
router.delete('/chat/:postId', deleteRoom);
router.post('/chat/changeUserInfo', changeUserInfo);
router.post('/token', createUserToken);

module.exports = router;
