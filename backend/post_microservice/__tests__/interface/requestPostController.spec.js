const axios = require("axios");
const s2sToken = require('./../../../config_post.json')["s2sToken"];
const { OK, CREATED, INTERNAL_SERVER_ERROR, UNAUTHORIZED, NOT_FOUND, BAD_REQUEST } = require("../../httpCodes");

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

axios.defaults.headers = { token: s2sToken }
axios.defaults.baseURL = process.env.CLOUD_POST_URL;

describe("GET communitypost/requests/:requestID", () => {
    test("Pass", async () => {
        const response = await request.get("/communitypost/requests/request1");
        expect(JSON.parse(response.text)).toEqual(requestPost);
        expect(response.statusCode).toEqual(OK);
    });

    test("request post not found", async () => {
        const response = await request.get("/communitypost/requests/request2");
        expect(response.statusCode).toEqual(NOT_FOUND);
    });
});

describe("GET communitypost/requests", () => {    
    test("Pass", async () => {
        const response = await request.get("/communitypost/requests");
        expect(JSON.parse(response.text)).toEqual([requestPost]);
        expect(response.statusCode).toEqual(OK);
    });

    test("request post not found", async () => {
        const response = await request.get("/communitypost/requests");
        expect(response.statusCode).toEqual(NOT_FOUND);
    });
});

describe("GET communitypost/requests/users/:userId", () => {    
    test("Pass", async () => {
        const response = await request.get("/communitypost/requests/users/user1");
        expect(JSON.parse(response.text)).toEqual([requestPost]);
        expect(response.statusCode).toEqual(OK);
    });

    test("no request post found", async () => {
        const response = await request.get("/communitypost/requests/users/user2");
        expect(response.statusCode).toEqual(NOT_FOUND);
    });

    test("user id not found", async () => {
        const response = await request.get("/communitypost/requests/users/fakeid");
        expect(response.statusCode).toEqual(NOT_FOUND);
    });
});

describe("GET communitypost/requests/search/:title", () => {    
    test("Similar posts found", async () => {
        const response = await request.get("/communitypost/requests/search/similarPostsExist");
        expect(JSON.parse(response.text)).toEqual([similarPosts]);
        expect(response.statusCode).toEqual(OK);
    });

    test("No similar posts", async () => {
        const response = await request.get("/communitypost/requests/search/noSimilarPosts");
        expect(JSON.parse(response.text)).toEqual([requestPost]);
        expect(response.statusCode).toEqual(OK);
    });
});

describe("PUT communitypost/requestTags", () => {    
    test("Pass", async () => {
        const response = await request.put("/communitypost/requestTags").send({ tagList: ["dairy"] });
        expect(JSON.parse(response.text).results).toEqual([requestPost]);
        expect(response.statusCode).toEqual(OK);
    });

    test("No tags provided", async () => {
        const response = await request.put("/communitypost/requestTags").send({ tagList: [] });
        expect(JSON.parse(response.text).results).toEqual([requestPost]);
        expect(response.statusCode).toEqual(OK);
    });

    test("Invalid tags", async () => {
        const response = await request.put("/communitypost/requestTags").send({ tagList: null });
        expect(response.statusCode).toEqual(BAD_REQUEST);
    });
});

