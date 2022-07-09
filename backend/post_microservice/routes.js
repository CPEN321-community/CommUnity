const Router = require('express-promise-router');

const { getOffer, getAllOffers, searchOffers, searchOffersWithTags, createOffer, updateOffer, deleteOffer, removeOfferTags} = require('./controllers/offerPostController');
const { getRequest, getAllRequests, getAllUserRequests, searchRequests, searchRequestsWithTags, createRequest, updateRequest, deleteRequest, getAllUserRequests} = require('./controllers/requestPostController');
const router = Router();

// Requests
router.get('/communitypost/requests/:requestId', getRequest);
router.get('/communitypost/requests', getAllRequests);
router.get('/communitypost/requests/:userId', getAllUserRequests);
router.get('/communitypost/requests/:title', searchRequests);
router.get('/communitypost/requests/:tagsList', searchRequestsWithTags);
router.post('/communitypost/requests', createRequest);
router.put('/communitypost/requests', updateRequest);
router.delete('/communitypost/requests/:requestId', deleteRequest);

/**

 */
// Offers
router.get('/communitypost/offers/:offerId', getOffer);
router.get('/communitypost/offers', getAllOffers);
router.get('/communitypost/offers/:title', searchOffers);
router.get('/communitypost/offers/:tagsList', searchOffersWithTags);
router.post('/communitypost/offers', createOffer);
router.put('/communitypost/offers', updateOffer);
router.put('/communitypost/offers/:tagList', addOfferTags);
router.delete('/communitypost/offers/:tagList', removeOfferTags);
router.delete('/communitypost/offers/:offerId', deleteOffer);

module.exports = router;
