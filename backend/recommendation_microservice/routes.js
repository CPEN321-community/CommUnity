const Router = require('express-promise-router');
const { getSuggestedPosts, trainModel } = require('./controllers/recommendationController');

const router = Router();

router.get('/suggestedPosts/:item', getSuggestedPosts);
router.get('/suggestedPosts/train', trainModel);

module.exports = router;