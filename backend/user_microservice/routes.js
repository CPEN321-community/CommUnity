const Router = require('express-promise-router');
const { getTopNUsers, getUserRank, upsertUserRank } = require('./controllers/leaderboardController');
const { getUser, upsertUserPreference, createUser, updateUser, deleteUserPreference } = require('./controllers/userController');

const router = Router();

// user
router.get('/user/:userId', getUser);
router.put('/user/preference', upsertUserPreference);
router.post('/user', createUser);
router.put('/user', updateUser);
router.delete('/user/preference/:preferenceId', deleteUserPreference);

// leaderboard
router.get('/rank/top/:N', getTopNUsers);
router.get('/rank/:userId', getUserRank);
router.put('/rank', upsertUserRank);

module.exports = router;