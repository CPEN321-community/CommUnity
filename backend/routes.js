const Router = require('express-promise-router');
const { getChatData, getChatSocket, getMessageFromSocket, addMessageToSocket } = require('./controllers/chatController');
const { getPostData, deletePost, updatePost, createNewPost, findPostList } = require('./controllers/postController');

const router = Router();

//Post Module
router.get('/post/:postId', getPostData);
router.get('/post?query', findPostList);
router.post('/post', createNewPost);
router.put('/post/:postId', updatePost);
router.delete('/post/:postId', deletePost);


// Chat Module
router.get('/chat?userId', getChatData);
router.get('/socket/:chatId', getChatSocket);
router.get('/socket/message/:chatId', getMessageFromSocket);
router.post('/socket/message/:chatId', addMessageToSocket)

module.exports = router;
