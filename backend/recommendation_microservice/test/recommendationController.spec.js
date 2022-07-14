const {getSuggestedRequests, getSuggestedOffers, deleteSuggestedRequest, deleteSuggestedOffer} = require("../__mocks__/mocks.js");

describe("Recommendation Controller", () => {
  it("triggers the mock for getting suggested request posts", () => {
    let result = getSuggestedRequests('peepeepoopoo');
    expect(result).toEqual([{postId: 'post1', score: 100}, { postId: 'post2', score: 50}]);
  });

  it("triggers the mock for getting suggested offer posts", () => {
    let result = getSuggestedOffers('peepeepoopoo');
    expect(result).toEqual([{postId: 'post1', score: 100}, { postId: 'post2', score: 50}]);
  });

  it("triggers the mock for deleting request posts", () => {
    let result = deleteSuggestedRequest();
    expect(result).toBe(true);
  });

  it("triggers the mock for deleting offer posts", () => {
    let result = deleteSuggestedOffer();
    expect(result).toBe(true);
  });
});

// router.get('/suggestedPosts/request/:item', getSuggestedPosts);
// router.get('/suggestedPosts/offer/:item', getSuggestedPosts);

// router.delete('/suggestedPosts/request/:postId', deletePost);
// router.delete('/suggestedPosts/offer/:postId', deletePost);

