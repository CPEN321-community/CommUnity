const supertest = require("supertest");
const { afterAll, beforeAll } = require("@jest/globals");
const axios = require("axios");
const { RequestPost, RequestPostTags } = require("../../models");
const app = require("../../index");
const s2sToken = require('./../../../config_post.json')["s2sToken"];
const { OK, CREATED, INTERNAL_SERVER_ERROR, UNAUTHORIZED, NOT_FOUND, BAD_REQUEST } = require("../../httpCodes");

jest.mock("axios");

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
    requestTags: ["beep", "boop"]
};

const requestPostDataVals = {
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
        requestTags: ["beep", "boop"],
    }
};

const similarPosts = {
    userId: "user1",
    title: "title1",
    description: "des1",
    status: "active",
}

describe("GET communitypost/requests/:requestID", () => {
    let request = null;
    beforeAll(async () => {
      request = supertest(app);
    });
    
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
    });
        
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

describe("GET communitypost/requests/users/:userId", () => {
    let request = null;
    beforeAll(async () => {
        request = supertest(app);
    });
        
    test("Pass", async () => {
        RequestPost.findAll = jest.fn().mockReturnValueOnce([requestPost]);
        const response = await request.get("/communitypost/requests/users/user1").set('token', s2sToken);
        expect(JSON.parse(response.text)).toEqual([requestPost]);
        expect(response.statusCode).toEqual(OK);
    });

    test("no request post found", async () => {
        RequestPost.findAll = jest.fn().mockReturnValueOnce(null);
        const response = await request.get("/communitypost/requests/users/user2").set('token', s2sToken);
        expect(response.statusCode).toEqual(NOT_FOUND);
    });

    test("user id not found", async () => {
        RequestPost.findAll = jest.fn().mockReturnValueOnce(null);
        const response = await request.get("/communitypost/requests/users/fakeid").set('token', s2sToken);
        expect(response.statusCode).toEqual(NOT_FOUND);
    });
});

describe("GET communitypost/requests/search/:title", () => {
    let request = null;
    beforeAll(async () => {
        request = supertest(app);
    });
        
    test("Similar posts found", async () => {
        RequestPost.findAll = jest.fn().mockReturnValueOnce([requestPostDataVals]);
        const response = await request.get("/communitypost/requests/search/similarPostsExist").set('token', s2sToken);
        expect(JSON.parse(response.text)).toEqual([similarPosts]);
        expect(response.statusCode).toEqual(OK);
    });

    test("No similar posts", async () => {
        RequestPost.findAll = jest.fn().mockReturnValueOnce(null);
        axios.get = jest.fn().mockReturnValueOnce([requestPost]);
        RequestPost.findOne = jest.fn().mockReturnValueOnce(requestPostDataVals)
        const response = await request.get("/communitypost/requests/search/noSimilarPosts").set('token', s2sToken);
        expect(JSON.parse(response.text)).toEqual([requestPost]);
        expect(response.statusCode).toEqual(OK);
    });
});

describe("GET communitypost/requestTags", () => {
    let request = null;
    beforeAll(async () => {
        request = supertest(app);
    });
        
    test("Pass", async () => {
        RequestPostTags.findAll = jest.fn().mockReturnValueOnce([requestPostDataVals]);
        RequestPost.findAll = jest.fn().mockReturnValueOnce([requestPostDataVals])
        const response = await request
            .put("/communitypost/requestTags")
            .set('token', s2sToken)
            .send({ tagList: ["dairy"] });
        expect(JSON.parse(response.text).results).toEqual([requestPost]);
        expect(response.statusCode).toEqual(OK);
    });

    test("No tags provided", async () => {
        RequestPostTags.findAll = jest.fn().mockReturnValueOnce([requestPostDataVals]);
        RequestPost.findAll = jest.fn().mockReturnValueOnce([requestPostDataVals])
        const response = await request
            .put("/communitypost/requestTags")
            .set('token', s2sToken)
            .send({ tagList: [] });
        expect(JSON.parse(response.text).results).toEqual([requestPost]);
        expect(response.statusCode).toEqual(OK);
    });

    test("Invalid tags", async () => {
        const response = await request
            .put("/communitypost/requestTags")
            .set('token', s2sToken)
            .send({ tagList: null });
        expect(response.statusCode).toEqual(BAD_REQUEST);
    });
});


// const { jest } = require("@jest/globals");

// const requestPost = {
//     userId: "user1",
//     offerId: "123",
//     title: "title1",
//     description: "des1",
//     quantity: 1,
//     pickUpLocation: "location1",
//     image: "img1",
//     status: "active",
//     bestBeforeDate: "date1",
//     offerTags: ["beep", "boop"]
// };

// module.exports = {
//     defaults: {
//         header: jest.fn(),
//     },
//     get: jest.fn((url) => {
//         if (url === `${process.env.RECOMMENDATION_URL}/request/noSimilarPosts`) {
//             return Promise.resolve({
//                 data: [requestPost]
//             });
//         }
//     }),
//     post: jest.fn((url) => {
//         if (url === '/something') {
//             return Promise.resolve({
//                 data: 'data'
//             });
//         }
//         if (url === '/something2') {
//             return Promise.resolve({
//                 data: 'data2'
//             });
//         }
//     }),
//     create: jest.fn(function () {
//         return this;
//     })
// };
