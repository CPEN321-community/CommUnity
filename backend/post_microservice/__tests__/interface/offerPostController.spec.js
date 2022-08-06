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
const createdOffer = {
  userId: "user2",
  offerId: "offer4",
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
axios.defaults.headers = { token: s2sToken }
// axios.defaults.baseURL = process.env.CLOUD_POST_URL;
axios.defaults.baseURL = "http://localhost:8081";
describe("POST communitypost/offers", () => {

  test("Offer post is successfully created", async () => {
    const response = await axios.post("/communitypost/offers", createdOffer);
    expect(response.status).toEqual(CREATED);
  });

  test("Missing a field", async () => {
    const missingFieldOffer = {
      userId: "user1",
      title: "Juice",
      description: "Juicy",
      quantity: 2,
      pickUpLocation: "Juice Bar",
      image: "juicyPic.com",
      status: "Active",
      bestBeforeDate: "04/20/2024"
    }
    await axios.post("/communitypost/offers", missingFieldOffer).catch(e => {
      expect(e.response.status).toEqual(BAD_REQUEST);
    });
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
    const response = await axios.post("/communitypost/offers", invalidUrl).catch(e => {
      expect(e.response.status).toEqual(BAD_REQUEST);
    });
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
    const response = await axios.post("/communitypost/offers", invalidDate).catch(e => {
      expect(e.response.status).toEqual(BAD_REQUEST);
    });
  });
});

 describe("POST communitypost/offerTags", () => {
   test("Specified offer post tags are successfully added", async () => {
     const newTags = {
       offerId: "offer4",
       tagList: ["fruit"]
     }

     const response = await axios.post("/communitypost/offerTags", newTags);
     expect(response.status).toEqual(CREATED);
   });

   test("Missing at least 1 field", async () => {
       const missingFieldTags = {
         offerId: "offer5"
       }
       const response = await axios.post("/communitypost/offerTags", missingFieldTags).catch(e => {
        expect(e.response.status).toEqual(BAD_REQUEST);
      });
   });

   test("Invalid tag (not part of our preset tags)", async () => {
       const invalidTags = {
         offerId: "offer5",
         tagList: ["human"]
       }
       const response = await axios.post("/communitypost/offerTags", invalidTags).catch(e => {
        expect(e.response.status).toEqual(BAD_REQUEST);
      });
   });
  
   test("No tags are provided", async () => {
       const invalidTags = {
         offerId: "offer5",
         tagList: []
       }
       const response = await axios.post("/communitypost/offerTags", invalidTags).catch(e => {
        expect(e.response.status).toEqual(BAD_REQUEST);
      });
   });
 });

 describe("DELETE communitypost/offers/:offerId", () => {
   test("Offer post is successfully deleted", async () => {
     const offers = await axios.get("/communitypost/offers");
     const response = await axios.delete(`/communitypost/offers/${offers.data[0].offerId}`);
     expect(response.status).toEqual(OK);
   });
  
   test("Offer post with corresponding offerId does not exist", async () => {
     await axios.delete('/communitypost/offers/aasdfasdfasdfasdf').catch(e => {
      expect(e.response.status).toEqual(NOT_FOUND);
    });
   });
 });

 describe("DELETE communitypost/offers/tags", () => {
   test("Specified offer post tags are successfully deleted", async () => {
     const deleteTags = {
       offerId: "offer4",
       tagList: ["fruit"]
     }
     const response = await axios.delete("/communitypost/offers/tags", { data: deleteTags });
     expect(response.status).toEqual(OK);
   });
  
   test("Missing at least 1 field", async () => {
     const deleteTags = {
       tagList: ["fruit", "vegetable"]
     }
     const response = await axios.delete("/communitypost/offers/tags", { data: deleteTags }).catch(e => {
      expect(e.response.status).toEqual(BAD_REQUEST);
    });
   });

   test("Offer post corresponding to the offerId does not have any tags", async () => {
     const deleteTags = {
       offerId: "offer1",
       tagList: []
     }
     const response = await axios.delete("/communitypost/offers/tags", { data: deleteTags });
     expect(response.status).toEqual(OK);
   });

   test("Specified tags do not exist for the offer post associated with the offerId", async () => {
     const deleteTags = {
       offerId: "offer1",
       tagList: ["fruit", "vegetable"]
     }
     const response = await axios.delete("/communitypost/offers/tags", { data: deleteTags });
     expect(response.status).toEqual(OK);
   });

   test("No tags provided", async () => {
     const deleteTags = {
       offerId: "offer1",
       tagList: null
     }
     const response = await axios.delete("/communitypost/offers/tags", { data: deleteTags }).catch(e => {
      expect(e.response.status).toEqual(BAD_REQUEST);
    });
   });
 });

 describe("PUT communitypost/offers", () => {
   test("Offer post is successfully updated", async () => {
       const updatedOfferPost = {
         offerId: "offer4",
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

       const response = await axios.put("/communitypost/offers", updatedOfferPost);
       expect(response.status).toEqual(OK);
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

       await axios.put("/communitypost/requests", updatedRequestPost).catch(e => {
        expect(e.response.status).toEqual(BAD_REQUEST);
      });
   });
 });

 describe("GET communitypost/offers/:offerID", () => {
  test("Pass", async () => {
    const offers = await axios.get("/communitypost/offers");
    const response = await axios.get(`/communitypost/offers/${offers.data[0].offerId}`);
    expect(response.status).toEqual(OK);
  });

  test("request post not found", async () => {
    await axios.get("/communitypost/offers/asdfasdfasdf").catch(e => {
      expect(e.response.status).toEqual(NOT_FOUND);
    });
  });
});

describe("GET communitypost/offers/users/:userId", () => {
  test("Pass", async () => {
      const response = await axios.get('/communitypost/offers/users/user2');
      expect(response.status).toEqual(OK);
  });

  test("user id not found", async () => {
    await axios.get('/communitypost/offers/users/123123123').catch(e => {
      expect(e.response.status).toEqual(NOT_FOUND);
    });
  });
});

describe("GET communitypost/offers/search/:title", () => {
  test("Similar posts found", async () => {
      const response = await axios.get('/communitypost/offers/search/a');
      expect(response.status).toEqual(OK);
  });

  test("No similar posts", async () => {
      const response = await axios.get('/communitypost/offers/search/z');  
      expect(response.status).toEqual(OK);
  });
});

describe("PUT communitypost/offerTags", () => {
  test("Pass", async () => {
      const response = await axios.put('/communitypost/offerTags', {tagList: ['juice']});
      expect(response.status).toEqual(OK);
  });

  test("No tags provided", async () => {
    const response = await axios.put('/communitypost/offerTags', {tagList: []});
    expect(response.status).toEqual(OK);
  });

  test("Invalid tags", async () => {
    await axios.put('/communitypost/offerTags', {tagList: null}).catch(e => {
      expect(e.response.status).toEqual(BAD_REQUEST);
    });
  });
});