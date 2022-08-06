
const axios = require("axios");
const s2sToken = require('../../../config_post.json')["s2sToken"];
const { OK, CREATED, BAD_REQUEST, INTERNAL_SERVER_ERROR } = require("../../httpCodes");

const user = {
  firstName: "firstName",
  lastName: "lastName",
  email: "email@email.com",
  profilePicture: "profilePicture.com",
}

const userWithId = {
  userId: 'u1',
  firstName: "firstName",
  lastName: "lastName",
  email: "email@email.com",
  profilePicture: "profilePicture.com",
}

axios.defaults.headers = { token: s2sToken, userId: 'u1' }
// axios.defaults.baseURL = process.env.CLOUD_USER_URL;
axios.defaults.baseURL = "http://localhost:8080";

describe("POST /user", () => {
  test("Pass", async () => {
    const response = await axios.post("/user", user);
    console.log(response);
    // expect(response.statusCode).toEqual(CREATED);
  });

  // test("Missing a field", async () => {
  //   const missingFieldUser = {
  //     lastName: "lastName",
  //     email: "email@email.com",
  //     profilePicture: "profilePicture.com",
  //   }    
  //   const response = await axios.post("/user", missingFieldUser);
  //   expect(response.statusCode).toEqual(BAD_REQUEST);
  // });

  // test("Invalid email", async () => {
  //   const badEmail = {
  //     firstName: "firstName",
  //     lastName: "lastName",
  //     email: "whacky",
  //     profilePicture: "profilePicture.com",
  //   }    
  //   const response = await axios.post("/user", badEmail);
  //   expect(response.statusCode).toEqual(BAD_REQUEST);
  // });

  // test("Invalid profile picture", async () => {
  //   const badPic = {
  //     firstName: "firstName",
  //     lastName: "lastName",
  //     email: "email@email.com",
  //     profilePicture: "whacky",
  //   }    
  //   const response = await axios.post("/user", badPic);
  //   expect(response.statusCode).toEqual(BAD_REQUEST);
  // });
});

// describe("PUT /user", () => {
//   test("Pass", async () => {
//     const response = await axios.put("/user", user);
//     expect(JSON.parse(response.text)).toEqual(userWithId);
//     expect(response.statusCode).toEqual(OK);
//   });

//   test("Missing a field", async () => {
//     const missingFieldUser = {
//       lastName: "lastName",
//       email: "email@email.com",
//       profilePicture: "profilePicture.com",
//     }    
//     const response = await axios.put("/user", missingFieldUser);
//     expect(response.statusCode).toEqual(BAD_REQUEST);
//   });

//   test("Invalid email", async () => {
//     const badEmail = {
//       firstName: "firstName",
//       lastName: "lastName",
//       email: "whacky",
//       profilePicture: "profilePicture.com",
//     }    
//     const response = await axios.put("/user", badEmail);
//     expect(response.statusCode).toEqual(BAD_REQUEST);
//   });

//   test("Invalid profile picture", async () => {
//     const badPic = {
//       firstName: "firstName",
//       lastName: "lastName",
//       email: "email@email.com",
//       profilePicture: "whacky",
//     }    
//     const response = await axios.put("/user", badPic);
//     expect(response.statusCode).toEqual(BAD_REQUEST);
//   });

//   test("user not found", async () => {
//     const response = await axios.put("/user", badPic);
//     expect(response.statusCode).toEqual(NOT_FOUND);
//   });
// });

// describe("GET /user", () => {
//   test("Pass", async () => {
//     const response = await axios.get("/user/u1");
//     expect(JSON.parse(response.text).user).toEqual(userWithId);
//     expect(response.statusCode).toEqual(OK);
//   });

//   test("User does not exist", async () => {
//     const response = await axios.get("/user/u1");
//     expect(response.statusCode).toEqual(INTERNAL_SERVER_ERROR);
//   });
// });

