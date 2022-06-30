const Router = require('express-promise-router');
const { getTopNUsers, getUserRank, upsertUserRank } = require('./controllers/leaderboardController');
const { getUser, upsertUserPreference, upsertUser } = require('./controllers/userController');

const router = Router();

// user
router.get('/user/:userId', getUser);
router.put('/user/preference', upsertUserPreference);
router.post('/user', upsertUser);

// leaderboard
router.get('/rank/:top', getTopNUsers);
router.get('/rank/:userId', getUserRank);
router.put('/rank', upsertUserRank);

module.exports = router;