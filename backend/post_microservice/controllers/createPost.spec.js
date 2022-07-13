//routes we want to test here: 
//router.post('/communitypost/offers', createOffer);
//router.post('/communitypost/requests', createRequest);

test("Sanity check", () => {
    expect(true).toBe(true);
});

const offerPostController = require("./offerPostController");

test("offerPostController can create an offer post", () => {
    const req = []
    
    req.push({
        userId: "user2",
        title: "Carrots",
        description: "Please take all of my carrots.",
        quantity: 420,
        pickUpLocation: "My house",
        image: "aws.storage.com/carrots",
        bestBeforeDate: "04/20/2040",
        offerTags: ["Orange", "Vegetable"]
    });

    offerPostController.createOffer();
    expect()
});
