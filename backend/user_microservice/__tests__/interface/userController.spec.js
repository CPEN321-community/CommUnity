
const axios = require("axios");
const s2sToken = require('../../../config_post.json')["s2sToken"];
const { OK, CREATED, BAD_REQUEST, INTERNAL_SERVER_ERROR, NOT_FOUND} = require("../../httpCodes");
const { v4 } = require('uuid');

const user = {
  firstName: "firstName",
  lastName: "lastName",
  email: "email@email.com",
  profilePicture: "profilePicture.com",
}

const uuid1 = v4();
const uuid2 = v4();

axios.defaults.headers = { token: s2sToken, userId: uuid1 }
axios.defaults.baseURL = "http://ec2-3-98-122-163.ca-central-1.compute.amazonaws.com:3000";

describe("POST /user", () => {
  test("Pass", async () => {
    const response = await axios.post("/user", user);
    expect(response.status).toEqual(CREATED);
  });

  test("Missing a field", async () => {
    const missingFieldUser = {
      lastName: "lastName",
      email: "email@email.com",
      profilePicture: "profilePicture.com",
    }
    await axios.post("/user", missingFieldUser).catch(e => {
      expect(e.response.status).toEqual(BAD_REQUEST);
    })
  });

  test("Invalid email", async () => {
    const badEmail = {
      firstName: "firstName",
      lastName: "lastName",
      email: "whacky",
      profilePicture: "profilePicture.com",
    }    
    await axios.post("/user", badEmail).catch(e => {
      expect(e.response.status).toEqual(BAD_REQUEST);
    })
  });

  test("Invalid profile picture", async () => {
    const badPic = {
      firstName: "firstName",
      lastName: "lastName",
      email: "email@email.com",
      profilePicture: "whacky",
    }    
    await axios.post("/user", badPic).catch(e => {
      expect(e.response.status).toEqual(BAD_REQUEST);
    })
  });
});

describe("PUT /user", () => {
  // test("Pass", async () => {
  //   const response = await axios.put("/user", user);
  //   expect(response.status).toEqual(OK);
  // });

  test("Missing a field", async () => {
    const missingFieldUser = {
      lastName: "lastName",
      email: "email@email.com",
      profilePicture: "profilePicture.com",
    }    
    await axios.put("/user", missingFieldUser).catch(e => {
      expect(e.response.status).toEqual(BAD_REQUEST);
    })
  });

  test("Invalid email", async () => {
    const badEmail = {
      firstName: "firstName",
      lastName: "lastName",
      email: "whacky",
      profilePicture: "profilePicture.com",
    }    
    await axios.put("/user", badEmail).catch(e => {
      expect(e.response.status).toEqual(BAD_REQUEST);
    })
  });

  test("Invalid profile picture", async () => {
    const badPic = {
      firstName: "firstName",
      lastName: "lastName",
      email: "email@email.com",
      profilePicture: "whacky",
    }    
    await axios.put("/user", badPic).catch(e => {
      expect(e.response.status).toEqual(BAD_REQUEST);
    })
  });

  test("user not found", async () => {
    await axios.put("/user", user, {header: {userId: uuid2}}).catch(e => {
      expect(e.response.status).toEqual(INTERNAL_SERVER_ERROR);
    })
  });
});

describe("GET /user", () => {
  test("Pass", async () => {
    const response = await axios.get(`/user/${uuid1}`);
    expect(response.status).toEqual(OK);
  });

  test("User does not exist", async () => {
    await axios.get("/user/u1").catch(e => {
      expect(e.response.status).toEqual(NOT_FOUND);
    })
  });
});

describe("PUT /rank", () => {  
  test("Successfully updates the user's score", async () => {
    const newUserStats = {
      userId: "parthvi",
      offerPosts: 3,
      requestPosts: 0
    }
    const response = await axios.put("/rank", newUserStats);
    expect(response.status).toEqual(OK);
  });
  
  test("Successfully creates the user's score when it doesn't already exist", async () => {
    const newUserStats = {
      userId: "parthvi",
      offerPosts: 3,
      requestPosts: 0
    }
    const response = await axios.put("/rank", newUserStats);
    expect(response.status).toEqual(OK);
  });

  test("Missing at least 1 field", async () => {
    const newUserStats = {
      userId: "parthvi",
      requestPosts: 0
    }
    const response = await axios.put("/rank", newUserStats);
    expect(response.status).toEqual(OK);
  });
});

describe("GET /rank", () => {  
  test("Successfully gets the user's placement on the leaderboard", async () => {
    const response = await axios.get(`/rank/${uuid1}`);
    expect(response.status).toEqual(OK);
  });

  test("User does not exist", async () => {
    await axios.get(`/user/nulllll`).catch(e => {
      expect(e.response.status).toEqual(NOT_FOUND);
    })
  });
});

describe("GET /rank/top/:N", () => {  
  test("Pass", async () => {
    const response = await axios.get("/rank/top/1");
    expect(response.status).toEqual(OK);
  });

  test("Missing a field", async () => {
    const missingFieldUser = {
      lastName: "lastName",
      email: "email@email.com",
      profilePicture: "profilePicture.com",
    }    
    await axios.put("/user", missingFieldUser).catch(e => {
      expect(e.response.status).toEqual(BAD_REQUEST);
    })
  });

  test("Invalid email", async () => {
    const badEmail = {
      firstName: "firstName",
      lastName: "lastName",
      email: "whacky",
      profilePicture: "profilePicture.com",
    }    
    await axios.put("/user", badEmail).catch(e => {
      expect(e.response.status).toEqual(BAD_REQUEST);
    })
  });

  test("Invalid profile picture", async () => {
    const badPic = {
      firstName: "firstName",
      lastName: "lastName",
      email: "email@email.com",
      profilePicture: "whacky",
    }    
    await axios.put("/user", badPic).catch(e => {
      expect(e.response.status).toEqual(BAD_REQUEST);
    })
  });

  test("user not found", async () => {
    const badPic = {
      firstName: "firstName",
      lastName: "lastName",
      email: "email@email.com",
      profilePicture: "whacky",
    }    
    await axios.put("/user", badPic).catch(e => {
      expect(e.response.status).toEqual(BAD_REQUEST);
    })
  });
});

describe("PUT /rank", () => {  
  test("Successfully updates the user's score", async () => {
    const newUserStats = {
      userId: "parthvi",
      offerPosts: 3,
      requestPosts: 0
    }
    const response = await axios.put("/rank", newUserStats);
    expect(response.status).toEqual(OK);
  });
  
  test("Successfully creates the user's score when it doesn't already exist", async () => {
    const newUserStats = {
      userId: "parthvi",
      offerPosts: 3,
      requestPosts: 0
    }
    const response = await axios.put("/rank", newUserStats);
    expect(response.status).toEqual(OK);
  });

  test("Missing at least 1 field", async () => {
    const newUserStats = {
      userId: "parthvi",
      requestPosts: 0
    }
    const response = await axios.put("/rank", newUserStats);
    expect(response.status).toEqual(OK);
  });
});