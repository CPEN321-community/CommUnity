const Router = require('express-promise-router');
const { getTopNUsers, getUserRank} = require('./controllers/leaderBoardController');
const { getUser, upsertUserPreference, createUser } = require('./controllers/userController');

const router = Router();

// user
router.get('/user/:userId', getUser);
router.put('/user/preference', upsertUserPreference);
router.post('/user', createUser);

// leaderboard
router.get('/rank?top', getTopNUsers);
router.get('/rank/:userId', getUserRank);
router.put('/rank', upsertUserRank);

