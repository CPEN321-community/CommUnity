'use strict';
const {jest: requiredJest} = require('@jest/globals');

const getSuggestedRequests = jest.fn(item => {
  return [{postId: 'post1', score: 100}, { postId: 'post2', score: 50}];
});
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