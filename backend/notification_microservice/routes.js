const Router = require('express-promise-router');
const router = Router();
const {createUser, sendNotifToUser} = require("./controllers/userController")
// Create a user with token
router.post('/user', createUser);
router.post('/push', sendNotifToUser);

module.exports = router;
