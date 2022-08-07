const axios = require("axios");
const s2sToken = require('./../../../config_post.json')["s2sToken"];
const { OK, CREATED, NOT_FOUND, BAD_REQUEST } = require("../../httpCodes");

const createdRequest = {
  userId: "user2",
  requestId: "request4",
  title: "Brocolli",
  description: "Green Crunchy Fresh",
  status: "Active",
  tagList: ["green"]
}

axios.defaults.headers = { token: s2sToken }
axios.defaults.baseURL = "http://ec2-3-99-226-175.ca-central-1.compute.amazonaws.com:3000";

describe("POST communitypost/requests", () => {
    test("Request post is successfully created", async () => {
      const response = await axios.post("/communitypost/requests", createdRequest);
      expect(response.status).toEqual(CREATED);
    });

    test("Missing a field", async () => {
        const missingFieldRequest = {
            userId: "user1",
            title: "Juice",
            description: "Juicy",
            currentLocation: "Juice Bar",
            status: "Active",
        }
        await axios.post("/communitypost/requests", missingFieldRequest).catch(e => {
            expect(e.response.status).toEqual(BAD_REQUEST);
        });
      });
});

describe("POST communitypost/requestTags", () => {
    test("Specified request post tags are successfully added", async () => {
        const newTags = {
            requestId: "request3",
            tagList: ["fruit"]
        }
        const response = await axios.post("/communitypost/requestTags", newTags);
        expect(response.status).toEqual(CREATED);
    });

    test("Missing at least 1 field", async () => {
        const missingFieldTags = {
            requestId: "request4"
        }
        await axios.post("/communitypost/requestTags", missingFieldTags).catch(e => {
            expect(e.response.status).toEqual(BAD_REQUEST);
        });
    });

    test("Invalid tag (not part of our preset tags)", async () => {
        const invalidTags = {
            requestId: "request4",
            tagList: ["human"]
        }
        await axios.post("/communitypost/requestTags", invalidTags).catch(e => {
            expect(e.response.status).toEqual(BAD_REQUEST)
        });
    });

    test("No tags are provided", async () => {
        const invalidTags = {
            requestId: "request4",
            tagList: []
        }
        await axios.post("/communitypost/requestTags", invalidTags).catch(e => {
            expect(e.response.status).toEqual(BAD_REQUEST);
        });
    });
});

describe("GET communitypost/requests/:requestID", () => {
    test("request post not found", async () => {
        await axios.get("/communitypost/requests/request2").catch(e => {
          expect(e.response.status).toEqual(NOT_FOUND);
        });
    });
});

describe("GET communitypost/requests", () => {    
    test("Pass", async () => {
        const response = await axios.get("/communitypost/requests");
        expect(response.status).toEqual(OK);
    });
});

describe("GET communitypost/requests/users/:userId", () => {    
    test("Pass", async () => {
        const response = await axios.get("/communitypost/requests/users/user2");
        expect(response.status).toEqual(OK);
    });

    test("user id not found", async () => {
        await axios.get('/communitypost/requests/users/123123123').catch(e => {
            expect(e.response.status).toEqual(NOT_FOUND);
          });
    });
});

describe("GET communitypost/requests/search/:title", () => {    
    test("Similar posts found", async () => {
        const response = await axios.get("/communitypost/requests/search/Brocolli");
        expect(response.status).toEqual(OK);
    });

    test("No similar posts", async () => {
        const response = await axios.get("/communitypost/requests/search/noSimilarPosts");
        expect(response.status).toEqual(OK);
    });
});

describe("PUT communitypost/requestTags", () => {    
    test("Pass", async () => {
        const response = await axios.put('/communitypost/requestTags', { tagList: ["dairy"] });
        expect(response.status).toEqual(OK);
    });

    test("No tags provided", async () => {
        const response = await axios.put('/communitypost/requestTags', { tagList: [] });
        expect(response.status).toEqual(OK);
    });

    test("Invalid tags", async () => {
        await axios.put('/communitypost/requestTags', { tagList: null }).catch(e => {
            expect(e.response.status).toEqual(BAD_REQUEST);
        });
    });
});

describe("DELETE communitypost/requests/:requestId", () => {
    test("Request post is successfully deleted", async () => {
      const requests = await axios.get("/communitypost/requests");
      const response = await axios.delete(`/communitypost/requests/${requests.data[0].requestId}`);
      expect(response.status).toEqual(OK);
    });

    test("Request post with corresponding requestId does not exist", async () => {
      await axios.delete("/communitypost/requests/aasdfasdfasdfasdf").catch(e => {
        expect(e.response.status).toEqual(NOT_FOUND);
      });
    });
  });

  describe("DELETE communitypost/requests/tags", () => {
    test("Specified request post tags are successfully deleted", async () => {
      const deleteTags = {
        requestId: "request3",
        tagList: ["fruit"]
      }
      const response = await axios.delete("/communitypost/requests/tags",{ data: deleteTags });
      expect(response.status).toEqual(OK);
    });
    
    test("Missing at least 1 field", async () => {
      const deleteTags = {
        tagList: ["fruit"]
      }
      await axios.delete("/communitypost/requests/tags", {data: deleteTags}).catch(e => {
        expect(e.response.status).toEqual(BAD_REQUEST);
      });
    });

    test("Request post corresponding to the requestId does not have any tags", async () => {
      const deleteTags = {
        requestId: "request1",
        tagList: []
      }
      const response = await axios.delete("/communitypost/requests/tags", { data: deleteTags });
      expect(response.status).toEqual(OK);
    });

    test("Specified tags do not exist for the request post associated with the requestId", async () => {
      const deleteTags = {
        requestId: "request1",
        tagList: ["fruit", "vegetable"]
      }
      const response = await axios.delete("/communitypost/requests/tags", { data: deleteTags });
      expect(response.status).toEqual(OK);
    });

    test("No tags provided", async () => {
      const deleteTags = {
        requestId: "request1",
        tagList: null
      }
      await axios.delete("/communitypost/requests/tags", { data: deleteTags }).catch(e => {
        expect(e.response.status).toEqual(BAD_REQUEST);
      });
    });
});
