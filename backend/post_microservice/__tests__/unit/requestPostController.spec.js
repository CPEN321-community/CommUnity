const { RequestPost, RequestPostTags } = require("../../models");
const supertest = require("supertest");
const app = require("../../index");
const { afterAll, beforeAll } = require("@jest/globals");
const s2sToken = require('./../../../config_post.json')["s2sToken"];
const { OK, CREATED, INTERNAL_SERVER_ERROR, UNAUTHORIZED, NOT_FOUND } = require("../../httpCodes");

const requestPost = {
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

describe("GET communitypost/requests/:requestID", () => {
    let request = null;
    beforeAll(async () => {
      request = supertest(app);
    })
    
  test("Pass", async () => {
    RequestPost.findOne = jest.fn().mockReturnValueOnce(requestPost);
    const response = await request.get("/communitypost/requests/request1").set('token', s2sToken);
    expect(JSON.parse(response.text)).toEqual(requestPost);
    expect(response.statusCode).toEqual(OK);
  });

  test("request post not found", async () => {
    RequestPost.findOne = jest.fn().mockReturnValueOnce(null);
    const response = await request.get("/communitypost/requests/request2").set('token', s2sToken);
    expect(response.statusCode).toEqual(NOT_FOUND);
  });
});


describe("GET communitypost/requests", () => {
    let request = null;
    beforeAll(async () => {
      request = supertest(app);
    })
    
  test("Pass", async () => {
    RequestPost.findAll = jest.fn().mockReturnValueOnce([requestPost]);
    const response = await request.get("/communitypost/requests").set('token', s2sToken);
    expect(JSON.parse(response.text)).toEqual([requestPost]);
    expect(response.statusCode).toEqual(OK);
  });

  test("request post not found", async () => {
    RequestPost.findAll = jest.fn().mockReturnValueOnce(null);
    const response = await request.get("/communitypost/requests").set('token', s2sToken);
    expect(response.statusCode).toEqual(NOT_FOUND);
  });
});
