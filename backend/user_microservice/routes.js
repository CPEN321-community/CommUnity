const Router = require('express-promise-router');
const { getTopNUsers, getUserRank, upsertUserRank, getUserLeaderboardStats } = require('./controllers/leaderboardController');
const { getUser, upsertUserPreference, createUser, updateUser } = require('./controllers/userController');

const router = Router();

// user
router.get('/user/:userId', getUser);
router.put('/user/preference', upsertUserPreference);
router.post('/user', createUser);
router.put('/user', updateUser);

// leaderboard
router.get('/rank/top/:N', getTopNUsers);
router.get('/rank/:userId', getUserRank);
router.put('/rank', upsertUserRank);
router.get('/leaderboard/:userId', getUserLeaderboardStats);

module.exports = router;