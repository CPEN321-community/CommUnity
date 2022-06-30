const Router = require('express-promise-router');
const { getChatData, getChatSocket, getMessageFromSocket, addMessageToSocket } = require('./controllers/chatController');
const { getOffer, searchOffers, searchOffersWithTags, createOffer, updateOffer, deleteOffer} = require('./post_microservice/controllers/offerPostController');
const { getRequest, searchRequests, createRequest, updateRequest, deleteRequest} = require('./post_microservice/controllers/requestPostController');
const router = Router();

//Post Module
// Requests Submodule
router.get('/communitypost/requests/:postId', getRequest);
router.get('/communitypost/requests?query', searchRequests);
router.post('/communitypost/requests/:postDTO', createRequest);
router.put('/communitypost/requests/:postId', updateRequest);
router.delete('/communitypost/requests/:postId', deleteRequest);
// Offers Submodule
router.get('/communitypost/offers/:postId', getOffer);
router.get('/communitypost/offers?title', searchOffers);
router.get('/communitypost/offers?tagsList', searchOffersWithTags)
router.post('/communitypost/offers/:postDTO', createOffer);
router.put('/communitypost/offers/:postDTO', updateOffer);
router.delete('/communitypost/offers/:postId', deleteOffer);


// Chat Module
router.get('/chat?userId', getChatData);
router.get('/socket/:chatId', getChatSocket);
router.get('/socket/message/:chatId', getMessageFromSocket);
router.post('/socket/message/:chatId', addMessageToSocket)

module.exports = router;
