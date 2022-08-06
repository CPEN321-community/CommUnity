const axios = require("axios");
const s2sToken = require('./../../../config_post.json')["s2sToken"];
const { OK, CREATED, INTERNAL_SERVER_ERROR, UNAUTHORIZED, NOT_FOUND, BAD_REQUEST } = require("../../httpCodes");

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

const offerPostDataVals = {
  dataValues: {
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
  }
};

axios.defaults.headers = { token: s2sToken }
// axios.defaults.baseURL = process.env.CLOUD_POST_URL;
axios.defaults.baseURL = "http://localhost:8081";

describe("GET communitypost/offers/:offerID", () => {
  test("Pass", async () => {
    const response = await axios.get("/communitypost/offers/offer1");
    expect(response).toEqual(offerPost);
    expect(response.statusCode).toEqual(OK);
  });

  test("request post not found", async () => {
    const response = await axios.get("/communitypost/offers/offer2");
    expect(response.statusCode).toEqual(NOT_FOUND);
  });
});

describe("GET communitypost/offers", () => {
  test("Pass", async () => {
    const response = await axios.get("/communitypost/offers");
    expect(response).toEqual([offerPost]);
    expect(response.statusCode).toEqual(OK);
  });

  test("offers post not found", async () => {
    const response = await axios.get("/communitypost/offers");
    expect(response.statusCode).toEqual(NOT_FOUND);
  });
});


describe("GET communitypost/offers/users/:userId", () => {
  test("Pass", async () => {
      const response = await axios.get("/communitypost/offers/users/user1");
      expect(response).toEqual([offerPost]);
      expect(response.statusCode).toEqual(OK);
  });

  test("no request post found", async () => {
      const response = await axios.get("/communitypost/offers/users/user2");
      expect(response.statusCode).toEqual(NOT_FOUND);
  });

  test("user id not found", async () => {
      const response = await axios.get("/communitypost/offers/users/fakeid");
      expect(response.statusCode).toEqual(NOT_FOUND);
  });
});

describe("GET communitypost/offers/search/:title", () => {
  test("Similar posts found", async () => {
      const response = await axios.get("/communitypost/offers/search/similarPostsExist");
      expect(response).toEqual([offerPost]);
      expect(response.statusCode).toEqual(OK);
  });

  test("No similar posts", async () => {
      const response = await axios.get("/communitypost/offers/search/noSimilarPosts");
      expect(response).toEqual([offerPost]);
      expect(response.statusCode).toEqual(OK);
  });
});

describe("GET communitypost/offerTags", () => {
  test("Pass", async () => {
      const response = await axios.put("/communitypost/offerTags", { tagList: ["dairy"] });
      expect(response.results).toEqual([offerPost]);
      expect(response.statusCode).toEqual(OK);
  });

  test("No tags provided", async () => {
      const response = await axios.put("/communitypost/offerTags", { tagList: [] });
      expect(response.results).toEqual([offerPost]);
      expect(response.statusCode).toEqual(OK);
  });

  test("Invalid tags", async () => {
      const response = await axios.put("/communitypost/offerTags", { tagList: null });
      expect(response.statusCode).toEqual(BAD_REQUEST);
  });
});