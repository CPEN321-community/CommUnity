
const { OfferPost, OfferPostTags } = require("../../models");
const supertest = require("supertest");
const axios = require("axios");
const app = require("../../index");
const { beforeAll } = require("@jest/globals");
const s2sToken = require('./../../../config_post.json')["s2sToken"];
const { OK, CREATED, NOT_FOUND, BAD_REQUEST, INTERNAL_SERVER_ERROR } = require("../../httpCodes");

jest.mock("axios");

const offerPost = {
  userId: "user1",
  offerId: "123",
  title: "title1",
  description: "des1",
  quantity: 1,
  pickUpLocation: "location1",
  image: "img1.com",
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
      image: "img1.com",
      status: "active",
      bestBeforeDate: "date1",
      offerTags: ["beep", "boop"]
  }
};

  const createdOffer = {
    userId: "user2",
    title: "Juice",
    description: "Juicy",
    quantity: 2,
    pickUpLocation: "Juice Bar",
    image: "juicyPic.com",
    status: "Active",
    bestBeforeDate: "04/20/2024",
    tagList: ["juice"]
  }

  const createdOfferWithId = {
    offerId: "offer1",
    userId: "user2",
    title: "Juice",
    description: "Juicy",
    quantity: 2,
    pickUpLocation: "Juice Bar",
    image: "juicyPic.com",
    status: "Active",
    bestBeforeDate: "04/20/2024",
    tagList: ["juice"]
  }

describe("POST communitypost/offers", () => {
  let request = null;
  beforeAll(async () => {
    request = supertest(app);
  })

  test("Offer post is successfully created", async () => {
    OfferPost.create = jest.fn().mockReturnValueOnce(createdOfferWithId);
    OfferPostTags.create = jest.fn();
    axios.put = jest.fn().mockReturnValueOnce([createdOfferWithId]);
    const response = await request.post("/communitypost/offers").set('token', s2sToken).send(createdOffer);
    expect(response.statusCode).toEqual(CREATED);
  });

  test("Missing a field", async () => {
    const missingFieldOffer = {
    offerId: "offer1",
    userId: "user1",
    title: "Juice",
    description: "Juicy",
    quantity: 2,
    pickUpLocation: "Juice Bar",
    image: "juicyPic.com",
    status: "Active",
    bestBeforeDate: "04/20/2024"
    }
    OfferPost.create = jest.fn().mockReturnValueOnce(missingFieldOffer);
    OfferPostTags.create = jest.fn();
    axios.put = jest.fn().mockReturnValueOnce([missingFieldOffer]);
    const response = await request.post("/communitypost/offers").set('token', s2sToken).send(missingFieldOffer);
    expect(response.statusCode).toEqual(BAD_REQUEST);
  });

  test("Invalid URL for image", async () => {
    const invalidUrl = {
      offerId: "offer1",
      userId: "user1",
      title: "Juice",
      description: "Juicy",
      quantity: 2,
      pickUpLocation: "Juice Bar",
      image: "juicyPic",
      status: "Active",
      bestBeforeDate: "04/20/2024",
      tagList: []
    }
    OfferPost.create = jest.fn().mockReturnValueOnce(invalidUrl);
    OfferPostTags.create = jest.fn();
    axios.put = jest.fn().mockReturnValueOnce([invalidUrl]);
    const response = await request.post("/communitypost/offers").set('token', s2sToken).send(invalidUrl);
    expect(response.statusCode).toEqual(CREATED);
  });

  test("bestBeforeDate entry is invalid (wrong format)", async () => {
    const invalidDate = {
      offerId: "offer1",
      userId: "user1",
      title: "Juice",
      description: "Juicy",
      quantity: 2,
      pickUpLocation: "Juice Bar",
      image: "juicyPic.com",
      status: "Active",
      bestBeforeDate: "04/20/202",
      tagList: []
    }
    OfferPost.create = jest.fn().mockReturnValueOnce(invalidDate);
    OfferPostTags.create = jest.fn();
    axios.put = jest.fn().mockReturnValueOnce([invalidDate]);
    const response = await request.post("/communitypost/offers").set('token', s2sToken).send(invalidDate);
    expect(response.statusCode).toEqual(BAD_REQUEST);
  });
  
});

describe("POST communitypost/offerTags", () => {
  let request = null;
  beforeAll(async () => {
    request = supertest(app);
  })

  test("Specified offer post tags are successfully added", async () => {
    const newTags = {
      offerId: "offer4",
      tagList: ["fruit"]
    }

    OfferPostTags.create = jest.fn().mockReturnValueOnce(newTags);
    const response = await request.post("/communitypost/offerTags").set('token', s2sToken).send(newTags);
    expect(response.statusCode).toEqual(CREATED);
  });

  test("Missing at least 1 field", async () => {
      const missingFieldTags = {
        offerId: "offer5"
      }
      OfferPostTags.create = jest.fn();
      const response = await request.post("/communitypost/offerTags").set('token', s2sToken).send(missingFieldTags);
      expect(response.statusCode).toEqual(BAD_REQUEST);
  });

  test("Invalid tag (not part of our preset tags)", async () => {
      const invalidTags = {
        offerId: "offer5",
        tagList: ["human"]
      }
      OfferPostTags.create = jest.fn();
      const response = await request.post("/communitypost/offerTags").set('token', s2sToken).send(invalidTags);
      expect(response.statusCode).toEqual(BAD_REQUEST);
  });
  
  test("No tags are provided", async () => {
      const invalidTags = {
        offerId: "offer5",
        tagList: []
      }
      OfferPostTags.create = jest.fn();
      const response = await request.post("/communitypost/offerTags").set('token', s2sToken).send(invalidTags);
      expect(response.statusCode).toEqual(BAD_REQUEST);
  });
});

describe("DELETE communitypost/offers/:offerId", () => {
  let request = null;
  beforeAll(async () => {
    request = supertest(app);
  })
  
  test("Offer post is successfully deleted", async () => {
    OfferPostTags.findAll = jest.fn().mockReturnValueOnce(createdOfferWithId);
    OfferPost.findOne = jest.fn().mockReturnValueOnce(createdOfferWithId);
    OfferPostTags.destroy = jest.fn().mockReturnValueOnce(createdOfferWithId);
    OfferPost.destroy = jest.fn().mockReturnValueOnce(createdOfferWithId);
    const response = await request.delete("/communitypost/offers/offer1").set('token', s2sToken);
    expect(response.statusCode).toEqual(OK);
  });
  
  test("Offer post with corresponding offerId does not exist", async () => {
    OfferPostTags.findAll = jest.fn().mockReturnValueOnce(false);
    OfferPost.findOne = jest.fn().mockReturnValueOnce(false);
    OfferPostTags.destroy = jest.fn().mockReturnValueOnce(createdOfferWithId);
    OfferPost.destroy = jest.fn().mockReturnValueOnce(createdOfferWithId);
    const response = await request.delete("/communitypost/offers/offer1").set('token', s2sToken);
    expect(response.statusCode).toEqual(NOT_FOUND);
  });
});

describe("DELETE communitypost/offers/tags", () => {
  let request = null;
  beforeAll(async () => {
    request = supertest(app);
  })
  
  test("Specified offer post tags are successfully deleted", async () => {
    const deleteTags = {
      offerId: "offer1",
      tagList: ["fruit", "vegetable"]
    }
    OfferPostTags.findAll = jest.fn().mockReturnValueOnce(true);
    OfferPostTags.destroy = jest.fn().mockReturnValueOnce(deleteTags);
    const response = await request.delete("/communitypost/offers/tags").set('token', s2sToken).send(deleteTags);
    expect(response.statusCode).toEqual(OK);
  });
  
  test("Missing at least 1 field", async () => {
    const deleteTags = {
      tagList: ["fruit", "vegetable"]
    }
    OfferPostTags.findAll = jest.fn().mockReturnValueOnce(true);
    OfferPostTags.destroy = jest.fn().mockReturnValueOnce(deleteTags);
    const response = await request.delete("/communitypost/offers/tags").set('token', s2sToken).send(deleteTags);
    expect(response.statusCode).toEqual(BAD_REQUEST);
  });

  test("Offer post corresponding to the offerId does not have any tags", async () => {
    const deleteTags = {
      offerId: "offer1",
      tagList: []
    }
    OfferPostTags.findAll = jest.fn().mockReturnValueOnce(true);
    OfferPostTags.destroy = jest.fn().mockReturnValueOnce(deleteTags);
    const response = await request.delete("/communitypost/offers/tags").set('token', s2sToken).send(deleteTags);
    expect(response.statusCode).toEqual(OK);
  });

  test("Specified tags do not exist for the offer post associated with the offerId", async () => {
    const deleteTags = {
      offerId: "offer1",
      tagList: ["fruit", "vegetable"]
    }
    OfferPostTags.findAll = jest.fn().mockReturnValueOnce(true);
    OfferPostTags.destroy = jest.fn().mockReturnValueOnce(deleteTags);
    const response = await request.delete("/communitypost/offers/tags").set('token', s2sToken).send(deleteTags);
    expect(response.statusCode).toEqual(OK);
  });

  test("No tags provided", async () => {
    const deleteTags = {
      offerId: "offer1",
      tagList: null
    }
    OfferPostTags.findAll = jest.fn().mockReturnValueOnce(true);
    OfferPostTags.destroy = jest.fn().mockReturnValueOnce(deleteTags);
    const response = await request.delete("/communitypost/offers/tags").set('token', s2sToken).send(deleteTags);
    expect(response.statusCode).toEqual(BAD_REQUEST);
  });
});

describe("PUT communitypost/offers", () => {
  let request = null;
  beforeAll(async () => {
    request = supertest(app);
  });
  
  test("Offer post is successfully updated", async () => {
      const originalOfferPost = {
        offerId: "offer1",
        userId: "kulkarni",
        title: "Juice",
        description: "Juicy",
        quantity: 2,
        pickUpLocation: "Juice Bar",
        image: "juicyPic.com",
        status: "Active",
        bestBeforeDate: "04/20/2024",
        tagList: ["juice"]
      };
      const updatedOfferPost = {
        offerId: "offer1",
        userId: "kulkarni",
        title: "Mango Juice",
        description: "I only have mango juice",
        quantity: 2,
        pickUpLocation: "Juice Bar",
        image: "juicyPic.com",
        status: "Active",
        bestBeforeDate: "04/20/2024",
        tagList: ["juice"]
      };

      OfferPost.findOne = jest.fn().mockReturnValueOnce(originalOfferPost);
      OfferPost.update = jest.fn().mockReturnValueOnce(updatedOfferPost);
      const response = await request.put("/communitypost/offers").set('token', s2sToken).send(updatedOfferPost);
      expect(response.statusCode).toEqual(OK);
  });
});

describe("GET communitypost/offers/:offerID", () => {
    let request = null;
    beforeAll(async () => {
      request = supertest(app);
    });

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

describe("PUT communitypost/offerTags", () => {
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