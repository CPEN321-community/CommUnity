const {getOffer, getAllOffers,getAllUserOffers,searchOffers,searchOffersWithTags,createOffer,updateOffer,removeOfferTags,addOfferTags,deleteOffer} = require('../../controllers/offerPostController');

describe("Offer Post Controller", () => {
  it("triggers createOffer", () => {
    const response = '';
    const offerPost = {
      userId: "user1",
      title: "Lettuce",
      description: "Lettuce Lettuce",
      quantity: 2,
      pickUpLocation: "Let You, Let Us",
      image: "Lettuce Image",
      status: "Active",
      bestBeforeDate: "07/28/2023",
      tagList: ["Green", "Leafy"]
    }
    let result = createOffer(offerPost, response);
    console.log("THIS IS RESPONSE ", repsonse);
    console.log("THIS IS RESULT ", result);
    //let result = axios.post('http://localhost:8081/communitypost/offers', offerPost);
    expect(result.status).toEqual(200);
    //await axios.put(`${process.env.USER_URL}/rank`, updateUserBody);
    //let result = getSuggestedRequests('peepeepoopoo');
    // expect(result).toEqual([{postId: 'post1', score: 100}, { postId: 'post2', score: 50}]);
    // expect(getSuggestedRequests).toHaveBeenCalledWith('peepeepoopoo');
    // expect(getSuggestedRequests).toHaveBeenCalledTimes(1);
  });
});





// describe("Some test", () => {
//   it("triggers GET /user", () => {
//     const userObj = { id: '123' }
//     const result = axios.put('https://aws.my_backend.com', userObj);
//     const updatedObj = { name: 'bob', lastName: 'smith' }
//     expect(result.body).toEqual(updatedObj);
//   });
// });