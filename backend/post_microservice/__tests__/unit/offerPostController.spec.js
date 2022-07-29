const {mockResponse, mockRequest} = require("../../controllers/offerPostController.js");
jest.mock("../../controllers/offerPostController.js");

describe("Offer Post Controller", () => {
  it("uses mock offer post", () => {

    expect(offerPost.name).toBe("Offer Post");
    
  });
});

// const { checkAuth } = require('./express-handlers');

// test('checkAuth > should 401 if session data is not set', async (t) => {
//   const req = mockRequest();
//   const res = mockResponse();
//   await checkAuth(req, res);
//   t.true(res.status.calledWith(401));
// });

// test('checkAuth > should 200 with username from session if data is set', async (t) => {
//   const req = mockRequest({ username: 'hugo' });
//   const res = mockResponse();
//   await checkAuth(req, res);
//   t.true(res.status.calledWith(200));
//   t.true(res.json.calledWith({ username: 'hugo' }));
// })