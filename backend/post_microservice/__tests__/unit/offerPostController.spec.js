const { expect } = require('@jest/globals');
const { getOffer } = require("../../controllers/offerPostController.js");
const httpMocks = require('node-mocks-http');
const db = require('../../models/index.js');

const offerPost = {
  userId: "user1",
  offerId: "123",
  title: "title1",
  description: "des1",
  quantity: 1,
  pickUpLocation: "location1",
  image: "img1",
  status: "active",
  bestBeforeDate: "date1",
  offerTags: ["beep", "boop"]
};

jest.mock("../../models/offerPostModel");

beforeAll(async () => {
  await db.sync();
});

afterAll(async () => {
  await db.close();
});

describe("Offer Post Controller", () => {
  it("gets an offer post that corresponds with offerId", async () => {
    const req = httpMocks.createRequest({ params: { offerId } });
    const res = httpMocks.createResponse({ locals: "marijuana" });

    return getOffer(req, res).then((response) => {
      expect(response).toEqual("marijuana");
    });
  });
});

// // Offers
// router.get('/communitypost/offers/:offerId', getOffer);
// router.get('/communitypost/offers', getAllOffers);
// router.get('/communitpost/offers/users/:userId', getAllUserOffers);
// router.get('/communitypost/offers/search/:title', searchOffers);
// router.put('/communitypost/offerTags', searchOffersWithTags);
// router.post('/communitypost/offers', createOffer);
// router.put('/communitypost/offers', updateOffer);
// router.post('/communitypost/offerTags', addOfferTags);
// router.delete('/communitypost/offers/tags', removeOfferTags);
// router.delete('/communitypost/offers/:offerId', deleteOffer);

// // Requests
// router.get('/communitypost/requests/:requestId', getRequest);
// router.get('/communitypost/requests', getAllRequests);
// router.get('/communitypost/requests/users/:userId', getAllUserRequests);
// router.get('/communitypost/requests/search/:title', searchRequests);
// router.put('/communitypost/requestTags', searchRequestsWithTags);
// router.post('/communitypost/requests', createRequest);
// router.put('/communitypost/requests', updateRequest);
// router.post('/communitypost/requestTags', addRequestTags);
// router.delete('/communitypost/requests/tags', removeRequestTags);
// router.delete('/communitypost/requests/:requestId', deleteRequest);

// jest.mock("./../models/OfferPostModel");
// jest.mock("./../controllers/offerPostController"); //automatically creates mocks of all methods in the class

// afterEach(() => {
//     OfferPost.findOne.mockReset();
//     // User.find.mockReset();
// });


// apptest.get(`/communitypost/offers/${offerId}`)
//.expect(200)
//.expect(res.test).toEqual('OK');
// console.log(res);
// expect(res.statusCode).toBe(200);
// expect(res.text).toEqual('OK');
// expect(res).toEqual(offerPost);
// expect(res.offerId).toEqual(offerId);
      
// or you could use the following depending on your use case:
// axios.get.mockImplementation(() => Promise.resolve(resp))
// const offerId = '123'
// const res = await request(apptest).get(`/communitypost/offers/${offerId}`);
// const offerPosts = [offerPost];
// const resp = {data: offerPosts};
// axios.get.mockResolvedValue(resp);
// return OfferPost.all().then(data => expect(data).toEqual(offerPosts));
