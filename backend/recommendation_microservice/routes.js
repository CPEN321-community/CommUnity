const Router = require('express-promise-router');
const { getSuggestedPosts, deletePost } = require('./controllers/recommendationController');

const router = Router();

router.get('/suggestedPosts/request/:item', getSuggestedPosts);
router.get('/suggestedPosts/offer/:item', getSuggestedPosts);

router.delete('/suggestedPosts/request/:postId', deletePost);
router.delete('/suggestedPosts/offer/:postId', deletePost);

module.exports = router;