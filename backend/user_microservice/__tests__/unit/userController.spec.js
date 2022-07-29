
const { Leaderboard, Preference, User } = require("../../models");
const supertest = require("supertest");
const axios = require("axios");
const app = require("../../index");
const { beforeAll } = require("@jest/globals");
const s2sToken = require('./../../../config_post.json')["s2sToken"];
const { OK, CREATED, BAD_REQUEST, INTERNAL_SERVER_ERROR } = require("../../httpCodes");

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
    User.create = jest.fn().mockReturnValueOnce(userWithId);
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
    expect(response.statusCode).toEqual(INTERNAL_SERVER_ERROR);
  });
});

