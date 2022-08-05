const supertest = require("supertest");
const { afterAll, beforeAll, expect } = require("@jest/globals");
const axios = require("axios");
const { RequestPost, RequestPostTags } = require("../../models");
const app = require("../../index");
const s2sToken = require('./../../../config_post.json')["s2sToken"];
const { OK, CREATED, INTERNAL_SERVER_ERROR, UNAUTHORIZED, NOT_FOUND, BAD_REQUEST } = require("../../httpCodes");

jest.mock("axios");

const requestPost = {
    userId: "user1",
    requestId: "123",
    title: "title1",
    description: "des1",
    currentLocation: "location1",
    status: "active",
    tagList: ["beep", "boop"]
};

const requestPostDataVals = {
    dataValues: {
        userId: "user1",
        requestId: "123",
        title: "title1",
        description: "des1",
        currentLocation: "location1",
        status: "active",
        tagList: ["beep", "boop"]
    }
};

const similarPosts = {
    userId: "user1",
    requestId: "123",
    title: "title1",
    description: "des1",
    currentLocation: "location1",
    status: "active",
    tagList: ["beep", "boop"]
}

const createdRequestWithId = {
    userId: "user1",
    requestId: "request1",
    title: "Brocolli",
    description: "Green Crunchy Fresh",
    currentLocation: "My house",
    status: "Active",
    tagList: ["green"]
}

const createdRequest = {
    userId: "user1",
    title: "Brocolli",
    description: "Green Crunchy Fresh",
    currentLocation: "My house",
    status: "Active",
    tagList: ["green"]
}

describe("POST communitypost/requests", () => {
    let request = null;
    beforeAll(async () => {
      request = supertest(app);
    })
  
    test("Request post is successfully created", async () => {
      RequestPost.create = jest.fn().mockReturnValueOnce(createdRequestWithId);
      RequestPostTags.create = jest.fn();
      axios.put = jest.fn().mockReturnValueOnce([createdRequestWithId]);
      const response = await request.post("/communitypost/requests").set('token', s2sToken).send(createdRequest);
      expect(response.statusCode).toEqual(CREATED);
    });

    test("Missing a field", async () => {
        const missingFieldRequest = {
        requestId: "request2",
        userId: "user1",
        title: "Juice",
        description: "Juicy",
        currentLocation: "Juice Bar",
        tagList: ["orange"]
        }
        RequestPost.create = jest.fn().mockReturnValueOnce(missingFieldRequest);
        RequestPostTags.create = jest.fn();
        axios.put = jest.fn().mockReturnValueOnce([missingFieldRequest]);
        const response = await request.post("/communitypost/requests").set('token', s2sToken).send(missingFieldRequest);
        expect(response.statusCode).toEqual(BAD_REQUEST);
      });
    
});

describe("POST communitypost/requestTags", () => {
    let request = null;
    beforeAll(async () => {
      request = supertest(app);
    })

    test("Specified request post tags are successfully added", async () => {
        const newTags = {
            requestId: "request3",
            tagList: ["fruit"]
        }
        RequestPostTags.create = jest.fn().mockReturnValueOnce(newTags);
        const response = await request.post("/communitypost/requestTags").set('token', s2sToken).send(newTags);
        expect(response.statusCode).toEqual(CREATED);
    });

    test("Missing at least 1 field", async () => {
        const missingFieldTags = {
            requestId: "request4"
        }
        RequestPostTags.create = jest.fn();
        const response = await request.post("/communitypost/requestTags").set('token', s2sToken).send(missingFieldTags);
        expect(response.statusCode).toEqual(BAD_REQUEST);
    });

    test("Invalid tag (not part of our preset tags)", async () => {
        const invalidTags = {
            requestId: "request4",
            tagList: ["human"]
        }
        RequestPostTags.create = jest.fn();
        const response = await request.post("/communitypost/requestTags").set('token', s2sToken).send(invalidTags);
        expect(response.statusCode).toEqual(BAD_REQUEST);
    });
    
    test("No tags are provided", async () => {
        const invalidTags = {
            requestId: "request4",
            tagList: []
        }
        RequestPostTags.create = jest.fn();
        const response = await request.post("/communitypost/requestTags").set('token', s2sToken).send(invalidTags);
        expect(response.statusCode).toEqual(BAD_REQUEST);
    });
});

describe("DELETE communitypost/requests/:requestId", () => {
    let request = null;
    beforeAll(async () => {
      request = supertest(app);
    })
    
    test("Offer post is successfully deleted", async () => {
      RequestPostTags.findAll = jest.fn().mockReturnValueOnce(createdRequestWithId);
      RequestPost.findOne = jest.fn().mockReturnValueOnce(createdRequestWithId);
      RequestPostTags.destroy = jest.fn().mockReturnValueOnce(createdRequestWithId);
      RequestPost.destroy = jest.fn().mockReturnValueOnce(createdRequestWithId);
      const response = await request.delete("/communitypost/requests/request1").set('token', s2sToken);
      expect(response.statusCode).toEqual(OK);
    });
    
    test("Offer post with corresponding offerId does not exist", async () => {
      RequestPostTags.findAll = jest.fn().mockReturnValueOnce(false);
      RequestPost.findOne = jest.fn().mockReturnValueOnce(false);
      RequestPostTags.destroy = jest.fn().mockReturnValueOnce(createdRequestWithId);
      RequestPost.destroy = jest.fn().mockReturnValueOnce(createdRequestWithId);
      const response = await request.delete("/communitypost/requests/request1").set('token', s2sToken);
      expect(response.statusCode).toEqual(NOT_FOUND);
    });
  });

  describe("DELETE communitypost/requests/tags", () => {
    let request = null;
    beforeAll(async () => {
      request = supertest(app);
    })
    
    test("Specified request post tags are successfully deleted", async () => {
      const deleteTags = {
        requestId: "request1",
        tagList: ["fruit", "vegetable"]
      }
      RequestPostTags.findAll = jest.fn().mockReturnValueOnce(true);
      RequestPostTags.destroy = jest.fn().mockReturnValueOnce(deleteTags);
      const response = await request.delete("/communitypost/requests/tags").set('token', s2sToken).send(deleteTags);
      expect(response.statusCode).toEqual(OK);
    });
    
    test("Missing at least 1 field", async () => {
      const deleteTags = {
        tagList: ["fruit", "vegetable"]
      }
      RequestPostTags.findAll = jest.fn().mockReturnValueOnce(true);
      RequestPostTags.destroy = jest.fn().mockReturnValueOnce(deleteTags);
      const response = await request.delete("/communitypost/requests/tags").set('token', s2sToken).send(deleteTags);
      expect(response.statusCode).toEqual(BAD_REQUEST);
    });
  
    test("Request post corresponding to the requestId does not have any tags", async () => {
      const deleteTags = {
        requestId: "request1",
        tagList: []
      }
      RequestPostTags.findAll = jest.fn().mockReturnValueOnce(true);
      RequestPostTags.destroy = jest.fn().mockReturnValueOnce(deleteTags);
      const response = await request.delete("/communitypost/requests/tags").set('token', s2sToken).send(deleteTags);
      expect(response.statusCode).toEqual(OK);
    });
  
    test("Specified tags do not exist for the request post associated with the requestId", async () => {
      const deleteTags = {
        requestId: "request1",
        tagList: ["fruit", "vegetable"]
      }
      RequestPostTags.findAll = jest.fn().mockReturnValueOnce(true);
      RequestPostTags.destroy = jest.fn().mockReturnValueOnce(deleteTags);
      const response = await request.delete("/communitypost/requests/tags").set('token', s2sToken).send(deleteTags);
      expect(response.statusCode).toEqual(OK);
    });
  
    test("No tags provided", async () => {
      const deleteTags = {
        requestId: "request1",
        tagList: null
      }
      RequestPostTags.findAll = jest.fn().mockReturnValueOnce(true);
      RequestPostTags.destroy = jest.fn().mockReturnValueOnce(deleteTags);
      const response = await request.delete("/communitypost/requests/tags").set('token', s2sToken).send(deleteTags);
      expect(response.statusCode).toEqual(BAD_REQUEST);
    });
  });

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

describe("PUT communitypost/requests", () => {
    let request = null;
    beforeAll(async () => {
      request = supertest(app);
    });
    
    test("Request post is successfully updated", async () => {
        const originalRequestPost = {
            userId: "parthvi",
            requestId: "R13",
            title: "Mangoes",
            description: "Mangoes are my favourite fruit of all time :)",
            currentLocation: "Mangoless place :(",
            status: "active",
            tagList: ["fruit"]
        };
        const updatedRequestPost = {
            userId: "parthvi",
            requestId: "R13",
            title: "YUMMY Mangoes",
            description: "Mangoes are my favourite fruit of all time :)",
            currentLocation: "Mangoless place :(",
            status: "active",
            tagList: ["fruit"]
        };

        RequestPost.findOne = jest.fn().mockReturnValueOnce(originalRequestPost);
        RequestPost.update = jest.fn().mockReturnValueOnce(updatedRequestPost);
        const response = await request.put("/communitypost/requests").set('token', s2sToken).send(updatedRequestPost);
        expect(response.statusCode).toEqual(OK);
    });

    test("Missing at least 1 field", async () => {
        const originalRequestPost = {
            userId: "parthvi",
            requestId: "R13",
            title: "Mangoes",
            description: "Mangoes are my favourite fruit of all time :)",
            currentLocation: "Mangoless place :(",
            status: "active",
            tagList: ["fruit"]
        };
        const updatedRequestPost = {
            requestId: "R13",
            title: "YUMMY Mangoes",
            description: "Mangoes are my favourite fruit of all time :)",
            currentLocation: "Mangoless place :(",
            status: "active",
            tagList: ["fruit"]
        };

        RequestPost.findOne = jest.fn().mockReturnValueOnce(originalRequestPost);
        RequestPost.update = jest.fn().mockReturnValueOnce(updatedRequestPost);
        const response = await request.put("/communitypost/requests").set('token', s2sToken).send(updatedRequestPost);
        expect(response.statusCode).toEqual(BAD_REQUEST);
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
        RequestPost.findOne = jest.fn().mockReturnValueOnce(requestPostDataVals);
        const response = await request.get("/communitypost/requests/search/noSimilarPosts").set('token', s2sToken);
        expect(JSON.parse(response.text)).toEqual([requestPost]);
        expect(response.statusCode).toEqual(OK);
    });
});

describe("PUT communitypost/requestTags", () => {
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

