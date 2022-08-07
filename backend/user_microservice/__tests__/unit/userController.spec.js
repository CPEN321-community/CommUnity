
const { Leaderboard, User } = require("../../models");
const supertest = require("supertest");
const app = require("../../index");
const { beforeAll } = require("@jest/globals");
const s2sToken = require('./../../../config_post.json')["s2sToken"];
const { OK, CREATED, BAD_REQUEST, NOT_FOUND } = require("../../httpCodes");

jest.mock("axios");

const user = {
  firstName: "firstName",
  lastName: "lastName",
  email: "email@email.com",
  profilePicture: "profilePicture.com",
}

const userWithId = {
  userId: 'user1',
  firstName: "firstName",
  lastName: "lastName",
  email: "email@email.com",
  profilePicture: "profilePicture.com",
}

describe("POST /user", () => {
    let request = null;
    beforeAll(async () => {
      request = supertest(app);
    })
    
  test("Pass", async () => {
    User.create = jest.fn().mockReturnValueOnce(userWithId);
    const response = await request.post("/user").set('token', s2sToken).set('userId', 'user1').send(user);
    expect(JSON.parse(response.text)).toEqual(userWithId);
    expect(response.statusCode).toEqual(CREATED);
  });

  test("Missing a field", async () => {
    const missingFieldUser = {
      lastName: "lastName",
      email: "email@email.com",
      profilePicture: "profilePicture.com",
    }    
    const response = await request.post("/user").set('token', s2sToken).set('userId', 'user1').send(missingFieldUser);
    expect(response.statusCode).toEqual(BAD_REQUEST);
  });

  test("Invalid email", async () => {
    const badEmail = {
      firstName: "firstName",
      lastName: "lastName",
      email: "whacky",
      profilePicture: "profilePicture.com",
    }    
    const response = await request.post("/user").set('token', s2sToken).set('userId', 'user1').send(badEmail);
    expect(response.statusCode).toEqual(BAD_REQUEST);
  });

  test("Invalid profile picture", async () => {
    const badPic = {
      firstName: "firstName",
      lastName: "lastName",
      email: "email@email.com",
      profilePicture: "whacky",
    }    
    const response = await request.post("/user").set('token', s2sToken).set('userId', 'user1').send(badPic);
    expect(response.statusCode).toEqual(BAD_REQUEST);
  });
});

describe("PUT /user", () => {
  let request = null;
  beforeAll(async () => {
    request = supertest(app);
  })
  
  test("Pass", async () => {
    User.update = jest.fn().mockReturnValueOnce(userWithId);
    const response = await request.put("/user").set('token', s2sToken).set('userId', 'user1').send(user);
    expect(JSON.parse(response.text)).toEqual(userWithId);
    expect(response.statusCode).toEqual(OK);
  });

  test("Missing a field", async () => {
    const missingFieldUser = {
      lastName: "lastName",
      email: "email@email.com",
      profilePicture: "profilePicture.com",
    }    
    const response = await request.put("/user").set('token', s2sToken).set('userId', 'user1').send(missingFieldUser);
    expect(response.statusCode).toEqual(BAD_REQUEST);
  });

  test("Invalid email", async () => {
    const badEmail = {
      firstName: "firstName",
      lastName: "lastName",
      email: "whacky",
      profilePicture: "profilePicture.com",
    }    
    const response = await request.put("/user").set('token', s2sToken).set('userId', 'user1').send(badEmail);
    expect(response.statusCode).toEqual(BAD_REQUEST);
  });

  test("Invalid profile picture", async () => {
    const badPic = {
      firstName: "firstName",
      lastName: "lastName",
      email: "email@email.com",
      profilePicture: "whacky",
    }    
    const response = await request.put("/user").set('token', s2sToken).set('userId', 'user1').send(badPic);
    expect(response.statusCode).toEqual(BAD_REQUEST);
  });

  test("user not found", async () => {
    const badPic = {
      firstName: "firstName",
      lastName: "lastName",
      email: "email@email.com",
      profilePicture: "whacky",
    }    
    const response = await request.put("/user").set('token', s2sToken).set('userId', 'user1').send(badPic);
    expect(response.statusCode).toEqual(BAD_REQUEST);
  });
});

describe("GET /user", () => {
  let request = null;
  beforeAll(async () => {
    request = supertest(app);
  })
  
  test("Pass", async () => {
    User.findByPk = jest.fn().mockReturnValueOnce(userWithId);
    const response = await request.get("/user/user1").set('token', s2sToken);
    expect(JSON.parse(response.text).user).toEqual(userWithId);
    expect(response.statusCode).toEqual(OK);
  });

  test("User does not exist", async () => {
    User.findByPk = jest.fn().mockReturnValueOnce(null);
    const response = await request.get("/user/user1").set('token', s2sToken);
    expect(response.statusCode).toEqual(NOT_FOUND);
  });
});


describe("PUT /rank", () => {
    let request = null;
    beforeAll(async () => {
      request = supertest(app);
    })
    
    test("Successfully updates the user's score", async () => {
      const existingUserStats = {
        dataValues: {
        userId: "parthvi", 
        offerPosts: 2,
        requestPosts: 0
        }
      }
      const newUserStats = {
        userId: "parthvi",
        offerPosts: 3,
        requestPosts: 0
      }
      Leaderboard.findOne = jest.fn().mockReturnValueOnce(existingUserStats);
      Leaderboard.update = jest.fn().mockReturnValueOnce(newUserStats);
      const response = await request.put("/rank").set('token', s2sToken).send(newUserStats);
      expect(response.statusCode).toEqual(OK);
    });
    
    test("Successfully creates the user's score when it doesn't already exist", async () => {
      const newUserStats = {
        userId: "parthvi",
        offerPosts: 3,
        requestPosts: 0
      }
      Leaderboard.findOne = jest.fn().mockReturnValueOnce(null);
      Leaderboard.create = jest.fn().mockReturnValueOnce(newUserStats);
      const response = await request.put("/rank").set('token', s2sToken).send(newUserStats);
      expect(response.statusCode).toEqual(OK);
    });
  
    test("Missing at least 1 field", async () => {
      const existingUserStats = {
        dataValues: {
        userId: "parthvi", 
        offerPosts: 2,
        requestPosts: 0
        }
      }
      const newUserStats = {
        userId: "parthvi",
        requestPosts: 0
      }
      Leaderboard.findOne = jest.fn().mockReturnValueOnce(existingUserStats);
      Leaderboard.update = jest.fn().mockReturnValueOnce(newUserStats);
      const response = await request.put("/rank").set('token', s2sToken).send(newUserStats);
      expect(response.statusCode).toEqual(OK);
    });
  });
  
  describe("GET /rank", () => {
    let request = null;
    beforeAll(async () => {
      request = supertest(app);
    })
    
    test("Successfully gets the user's placement on the leaderboard", async () => {
      const existingUser = {
        dataValues: {
          userId: "user1", 
          offerPosts: 0,
          requestPosts: 0,
          score: 0
        }
      }
      const higherScoringUsers = [{
        userId: "user2",
        offerPosts: 1,
        requestPosts: 0,
        score: 7
      }];
  
      Leaderboard.findOne = jest.fn().mockReturnValueOnce(existingUser);
      Leaderboard.findAll = jest.fn().mockReturnValueOnce(higherScoringUsers);
      const response = await request.get("/rank/user1").set('token', s2sToken).set('userId', 'user1');
      expect(response.statusCode).toEqual(OK);
    });
  
    test("User does not exist", async () => {
      User.findByPk = jest.fn().mockReturnValueOnce(null);
      const response = await request.get("/user/user1").set('token', s2sToken);
      expect(response.statusCode).toEqual(NOT_FOUND);
    });
  });
  
  describe("GET /rank/top/:N", () => {
    let request = null;
    beforeAll(async () => {
      request = supertest(app);
    })
    
    test("Pass", async () => {
      const leaderboard = {
        dataValues: {
          userId: "user1", 
          offerPosts: 0,
          requestPosts: 0,
          score: 0
        }
      }
  
      const foundUser = {
        dataValues: {
          userId: 'user1',
          firstName: "firstName",
          lastName: "lastName",
          email: "email@email.com",
          profilePicture: "profilePicture.com",
        }
      }
  
      Leaderboard.findAll = jest.fn().mockReturnValueOnce([leaderboard]);
      User.findOne = jest.fn().mockReturnValueOnce(foundUser);
      const response = await request.get("/rank/top/1").set('token', s2sToken).set('userId', 'user1');
      expect(response.statusCode).toEqual(OK);
    });
  
    test("Missing a field", async () => {
      const missingFieldUser = {
        lastName: "lastName",
        email: "email@email.com",
        profilePicture: "profilePicture.com",
      }    
      const response = await request.put("/user").set('token', s2sToken).set('userId', 'user1').send(missingFieldUser);
      expect(response.statusCode).toEqual(BAD_REQUEST);
    });
  
    test("Invalid email", async () => {
      const badEmail = {
        firstName: "firstName",
        lastName: "lastName",
        email: "whacky",
        profilePicture: "profilePicture.com",
      }    
      const response = await request.put("/user").set('token', s2sToken).set('userId', 'user1').send(badEmail);
      expect(response.statusCode).toEqual(BAD_REQUEST);
    });
  
    test("Invalid profile picture", async () => {
      const badPic = {
        firstName: "firstName",
        lastName: "lastName",
        email: "email@email.com",
        profilePicture: "whacky",
      }    
      const response = await request.put("/user").set('token', s2sToken).set('userId', 'user1').send(badPic);
      expect(response.statusCode).toEqual(BAD_REQUEST);
    });
  
    test("user not found", async () => {
      const badPic = {
        firstName: "firstName",
        lastName: "lastName",
        email: "email@email.com",
        profilePicture: "whacky",
      }    
      const response = await request.put("/user").set('token', s2sToken).set('userId', 'user1').send(badPic);
      expect(response.statusCode).toEqual(BAD_REQUEST);
    });
  });
  
  describe("PUT /rank", () => {
    let request = null;
    beforeAll(async () => {
      request = supertest(app);
    })
    
    test("Successfully updates the user's score", async () => {
      const existingUserStats = {
        dataValues: {
        userId: "parthvi", 
        offerPosts: 2,
        requestPosts: 0
        }
      }
      const newUserStats = {
        userId: "parthvi",
        offerPosts: 3,
        requestPosts: 0
      }
      Leaderboard.findOne = jest.fn().mockReturnValueOnce(existingUserStats);
      Leaderboard.update = jest.fn().mockReturnValueOnce(newUserStats);
      const response = await request.put("/rank").set('token', s2sToken).send(newUserStats);
      expect(response.statusCode).toEqual(OK);
    });
    
    test("Successfully creates the user's score when it doesn't already exist", async () => {
      const newUserStats = {
        userId: "parthvi",
        offerPosts: 3,
        requestPosts: 0
      }
      Leaderboard.findOne = jest.fn().mockReturnValueOnce(null);
      Leaderboard.create = jest.fn().mockReturnValueOnce(newUserStats);
      const response = await request.put("/rank").set('token', s2sToken).send(newUserStats);
      expect(response.statusCode).toEqual(OK);
    });
  
    test("Missing at least 1 field", async () => {
      const existingUserStats = {
        dataValues: {
        userId: "parthvi", 
        offerPosts: 2,
        requestPosts: 0
        }
      }
      const newUserStats = {
        userId: "parthvi",
        requestPosts: 0
      }
      Leaderboard.findOne = jest.fn().mockReturnValueOnce(existingUserStats);
      Leaderboard.update = jest.fn().mockReturnValueOnce(newUserStats);
      const response = await request.put("/rank").set('token', s2sToken).send(newUserStats);
      expect(response.statusCode).toEqual(OK);
    });
  });