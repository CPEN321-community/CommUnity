const Router = require('express-promise-router');

const { getOffer, getAllOffers, getAllUserOffers, searchOffers, searchOffersWithTags, createOffer, updateOffer, deleteOffer, removeOfferTags, addOfferTags} = require('./controllers/offerPostController');
const { getRequest, getAllRequests, getAllUserRequests, searchRequests, searchRequestsWithTags, createRequest, updateRequest, deleteRequest, removeRequestTags, addRequestTags} = require('./controllers/requestPostController');

const router = Router();

// Requests
router.get('/communitypost/requests/:requestId', getRequest);
router.get('/communitypost/requests', getAllRequests);
router.get('/communitypost/requests/users/:userId', getAllUserRequests);
router.get('/communitypost/requests/search/:title', searchRequests);
router.get('/communitypost/requests/searchTags', searchRequestsWithTags);
router.post('/communitypost/requests', createRequest);
router.put('/communitypost/requests', updateRequest);
router.put('/communitypost/requestTags', addRequestTags);
router.delete('/communitypost/requests/tags', removeRequestTags);
router.delete('/communitypost/requests/:requestId', deleteRequest);


// Offers
router.get('/communitypost/offers/:offerId', getOffer);
router.get('/communitypost/offers', getAllOffers);
router.get('/communitpost/offers/users/:userId', getAllUserOffers);
router.get('/communitypost/offers/search/:title', searchOffers);
router.get('/communitypost/offers/searchTags', searchOffersWithTags);
router.post('/communitypost/offers', createOffer);
router.put('/communitypost/offers', updateOffer);
router.put('/communitypost/offerTags', addOfferTags);
router.delete('/communitypost/offers/tags', removeOfferTags);
router.delete('/communitypost/offers/:offerId', deleteOffer);

module.exports = router;
