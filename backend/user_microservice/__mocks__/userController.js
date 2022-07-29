'use strict';
const {jest: requiredJest} = require('@jest/globals');

const getSuggestedRequests = jest.fn(item => {
  return [{postId: 'post1', score: 100}, { postId: 'post2', score: 50}];
});

const createUser = async (req, res) => {
  try {
    const response = await User.create(
      {
        userId: req.headers.userId,
        firstName: req.body.firstName,
        lastName: req.body.lastName,
        email: req.body.email,
        profilePicture: req.body.profilePicture,
        leaderboard: {
          offerPosts: 0,
          requestPosts: 0,
          score: 0,
        },
      },
      {
        include: [{ association: User.Leaderboard, as: "leaderboard" }],
      }
    );
    res.status(CREATED).json(response);
  } catch (error) {
    console.log("Error creating user: " + error);
    res.sendStatus(INTERNAL_SERVER_ERROR);
  }
};

const getSuggestedOffers = jest.fn(item => {
  return [{postId: 'post1', score: 100}, { postId: 'post2', score: 50}];
});
const deleteSuggestedRequest = jest.fn(() => true);
const deleteSuggestedOffer = jest.fn(() => true);

module.exports = {
  getSuggestedRequests,
  getSuggestedOffers,
  deleteSuggestedRequest,
  deleteSuggestedOffer
}