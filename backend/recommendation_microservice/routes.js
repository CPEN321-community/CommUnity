const Router = require('express-promise-router');
const { getSuggestedPosts } = require('./controllers/recommendationController');

const router = Router();

router.get('suggestedPosts?userId', getSuggestedPosts);
