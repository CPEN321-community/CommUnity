const Router = require('express-promise-router');

const { getOffer, getAllOffers, getAllUserOffers, searchOffers, searchOffersWithTags, createOffer, updateOffer, deleteOffer, removeOfferTags, addOfferTags} = require('./controllers/offerPostController');
const { getRequest, getAllRequests, getAllUserRequests, searchRequests, searchRequestsWithTags, createRequest, updateRequest, deleteRequest, removeRequestTags, addRequestTags} = require('./controllers/requestPostController');

const router = Router();

// Requests
// TODO: these endpoints are clashing:
// It's ambiguious when I do GET /communitypost/requests/asdfg, which one am I trying to hit
// Express defaults this to do the first one in the list (getRequest in this case)

router.get('/communitypost/requests/:requestId', getRequest);
router.get('/communitypost/requests', getAllRequests);
router.get('/communitypost/requests/:userId', getAllUserRequests);
router.get('/communitypost/requests/:title', searchRequests);
router.get('/communitypost/requests/:tagList', searchRequestsWithTags);
router.post('/communitypost/requests', createRequest);
router.put('/communitypost/requests', updateRequest);
router.put('/communitypost/requests/:tagList', addRequestTags);
router.delete('/communitypost/requests/:tagList', removeRequestTags);
router.delete('/communitypost/requests/:requestId', deleteRequest);

/**

 */
// Offers
// TODO: these endpoints are clashing
router.get('/communitypost/offers/:offerId', getOffer);
router.get('/communitypost/offers', getAllOffers);
router.get('/communitypost/offers/:userId', getAllUserOffers);
router.get('/communitypost/offers/:title', searchOffers);
router.get('/communitypost/offers/:tagList', searchOffersWithTags);
router.post('/communitypost/offers', createOffer);
router.put('/communitypost/offers', updateOffer);
router.put('/communitypost/offers/:tagList', addOfferTags);
router.delete('/communitypost/offers/:tagList', removeOfferTags);
router.delete('/communitypost/offers/:offerId', deleteOffer);

module.exports = router;
