
const { OfferPost, OfferPostTags } = require("../../models");
const supertest = require("supertest");
const axios = require("axios");
const app = require("../../index");
const { afterAll, beforeAll } = require("@jest/globals");
const s2sToken = require('./../../../config_post.json')["s2sToken"];
const { OK, CREATED, INTERNAL_SERVER_ERROR, UNAUTHORIZED, NOT_FOUND, BAD_REQUEST } = require("../../httpCodes");

jest.mock("axios");

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

describe("GET communitypost/offers/:offerID", () => {
    let request = null;
    beforeAll(async () => {
      request = supertest(app);
    })
    
  test("Pass", async () => {
    OfferPost.findOne = jest.fn().mockReturnValueOnce(offerPost);
    const response = await request.get("/communitypost/offers/offer1").set('token', s2sToken);
    expect(JSON.parse(response.text)).toEqual(offerPost);
    expect(response.statusCode).toEqual(OK);
  });

  test("request post not found", async () => {
    OfferPost.findOne = jest.fn().mockReturnValueOnce(null);
    const response = await request.get("/communitypost/offers/offer2").set('token', s2sToken);
    expect(response.statusCode).toEqual(NOT_FOUND);
  });
});

describe("GET communitypost/offers", () => {
  let request = null;
  beforeAll(async () => {
    request = supertest(app);
  })
    
  test("Pass", async () => {
    OfferPost.findAll = jest.fn().mockReturnValueOnce([offerPost]);
    const response = await request.get("/communitypost/offers").set('token', s2sToken);
    expect(JSON.parse(response.text)).toEqual([offerPost]);
    expect(response.statusCode).toEqual(OK);
  });

  test("offers post not found", async () => {
    OfferPost.findAll = jest.fn().mockReturnValueOnce(null);
    const response = await request.get("/communitypost/offers").set('token', s2sToken);
    expect(response.statusCode).toEqual(NOT_FOUND);
  });
});


describe("GET communitypost/offers/users/:userId", () => {
  let request = null;
  beforeAll(async () => {
      request = supertest(app);
  });
      
  test("Pass", async () => {
      OfferPost.findAll = jest.fn().mockReturnValueOnce([offerPost]);
      const response = await request.get("/communitypost/offers/users/user1").set('token', s2sToken);
      expect(JSON.parse(response.text)).toEqual([offerPost]);
      expect(response.statusCode).toEqual(OK);
  });

  test("no request post found", async () => {
      OfferPost.findAll = jest.fn().mockReturnValueOnce(null);
      const response = await request.get("/communitypost/offers/users/user2").set('token', s2sToken);
      expect(response.statusCode).toEqual(NOT_FOUND);
  });

  test("user id not found", async () => {
      OfferPost.findAll = jest.fn().mockReturnValueOnce(null);
      const response = await request.get("/communitypost/offers/users/fakeid").set('token', s2sToken);
      expect(response.statusCode).toEqual(NOT_FOUND);
  });
});

describe("GET communitypost/offers/search/:title", () => {
  let request = null;
  beforeAll(async () => {
      request = supertest(app);
  });
      
  test("Similar posts found", async () => {
      OfferPost.findAll = jest.fn().mockReturnValueOnce([offerPostDataVals]);
      const response = await request.get("/communitypost/offers/search/similarPostsExist").set('token', s2sToken);
      expect(JSON.parse(response.text)).toEqual([offerPost]);
      expect(response.statusCode).toEqual(OK);
  });

  test("No similar posts", async () => {
      OfferPost.findAll = jest.fn().mockReturnValueOnce(null);
      axios.get = jest.fn().mockReturnValueOnce([offerPost]);
      OfferPost.findOne = jest.fn().mockReturnValueOnce(offerPostDataVals)
      const response = await request.get("/communitypost/offers/search/noSimilarPosts").set('token', s2sToken);
      expect(JSON.parse(response.text)).toEqual([offerPost]);
      expect(response.statusCode).toEqual(OK);
  });
});

describe("GET communitypost/offerTags", () => {
  let request = null;
  beforeAll(async () => {
      request = supertest(app);
  });
      
  test("Pass", async () => {
      OfferPostTags.findAll = jest.fn().mockReturnValueOnce([offerPostDataVals]);
      OfferPost.findAll = jest.fn().mockReturnValueOnce([offerPostDataVals])
      const response = await request
          .put("/communitypost/offerTags")
          .set('token', s2sToken)
          .send({ tagList: ["dairy"] });
      expect(JSON.parse(response.text).results).toEqual([offerPost]);
      expect(response.statusCode).toEqual(OK);
  });

  test("No tags provided", async () => {
      OfferPostTags.findAll = jest.fn().mockReturnValueOnce([offerPostDataVals]);
      OfferPost.findAll = jest.fn().mockReturnValueOnce([offerPostDataVals])
      const response = await request
          .put("/communitypost/offerTags")
          .set('token', s2sToken)
          .send({ tagList: [] });
      expect(JSON.parse(response.text).results).toEqual([offerPost]);
      expect(response.statusCode).toEqual(OK);
  });

  test("Invalid tags", async () => {
      const response = await request
          .put("/communitypost/offerTags")
          .set('token', s2sToken)
          .send({ tagList: null });
      expect(response.statusCode).toEqual(BAD_REQUEST);
  });
});