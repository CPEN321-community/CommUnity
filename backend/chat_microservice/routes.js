const Router = require('express-promise-router');
const { getChats, changeInfo, deleteRoom } = require('./controllers/chatController');

const router = Router();

router.get('/chat/:userId', getChats);
router.delete('/chat/:postId', deleteRoom);
router.post('/chat/changeInfo/:userId', changeInfo);
router.get('/chat/test/html', (req, res) => res.sendFile(__dirname + '/socket/chat.html'));

module.exports = router;
