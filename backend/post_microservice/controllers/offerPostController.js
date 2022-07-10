  const { Op } = require("sequelize");
  const { OfferPost, OfferPostTags } = require("../models");
 
  //works
 const getOffer = async (req, res) => {
     try {
         const offerId = req.params.offerId;
         const response = await OfferPost.findOne({where: {offerId: offerId}});
         res.json(response);
     } catch (error) {
         console.log("Error finding an offer post: " + error);
     }
  };


  //wahoo
  const getAllOffers = async (req, res) => {
    try {
        const response = await OfferPost.findAll();
        res.json(response);
    } catch (error) {
        console.log("Error getting all of the offer posts: " + error);
    }
  }


  //works!
  const getAllUserOffers = async (req, res) => {
    try{
        console.log(req.params.userId);
        const response = await OfferPost.findAll({where: {userId: req.params.userId}});
        res.json(response);
    } catch(error) {
        console.log("Error in retrieving offer posts made by user " + error);
    }
  }
  
  const searchOffers = async (req, res) => {
    const title = req.params.title
      try {
        let response = [];

        const identicalPosts = await OfferPost.findAll({
            where: {title: title}
        })

        if (identicalPosts != null){
            
        }
        
        // let similarPosts = await OfferPost.findAll({
        //     where: {
        //         title: {[Op.like]: title + "%"}
        //     }
        // });
        
        // similarPosts = await OfferPost.findAll({
        //     where: {
        //         title: {[Op.like]: "%" + title}
        //     }
        // });

        // similarPosts = await OfferPost.findAll({
        //     where: {
        //         title: {[Op.like]: "%" + title + "%"}
        //     }
        // });

          if (response == null) {
              res.sendStatus(200);
          } else {
              res.json(response);
          }
      } catch (error) {
        console.log("Error with searching for offer posts: " + error);
        res.sendStatus(500);
      }
  }

  const searchOffersWithTags = async (req, res) => {
    try {
        const tagList = req.params.tagList;

        //list of postIds that have the tags
        const postIds = await OfferPostTags.findAll({
            attributes: {include: ["postId"]},
            where: {name: tagList}
        });
        
        //check whether there are duplicates in postIds list
        let uniquePostIds = [...new Set(postIds)];
        
        //find list of posts that match postIds
        const postList = await OfferPost.findAll({
            where: {offerId: uniquePostIds}
        });

        if (postList = null) {
            const message = "Sorry, there are no offer posts for " + tagList + "."
            res.status(200).json({
                message: message
            });
        } else {
            res.status(200).json(postList);
        }
    } catch (error) {
      console.log("Error with searching for offer posts: " + error);
      res.sendStatus(500);
    }
}

//woohoo
  const createOffer = async (req, res) => {
    try {
        await OfferPost.create({
            userId: req.body.userId,
            title: req.body.title,
            description: req.body.description,
            quantity: req.body.quantity,
            pickUpLocation: req.body.pickUpLocation,
            image: req.body.image,
            status: req.body.status,
            bestBeforeDate: req.body.bestBeforeDate
        });

        const newOffer = await OfferPost.findOne({where: {userId: req.body.userId}});

        let tagList = req.body.tagList;
        for(let item of tagList) {
            OfferPostTags.create({
                postId: newOffer.offerId,
                name: item
            });
        }
        res.sendStatus(200);

      } catch (error) {
        console.log("Error creating a new post: " + error);
        res.sendStatus(500);
      }
  }


  //maaaaario
  const removeOfferTags = async (req, res) => {
    try {
        const currentTags = await OfferPostTags.findAll({
            where: {postId: req.body.offerId}
        });

        const updatedTags = req.body.tagList;

        for (let i = 0; i < currentTags.length; i = i + 1){
            if (!(updatedTags.includes(currentTags[i].dataValues.name))) {
                OfferPostTags.destroy({
                    where: {
                        postId: req.body.offerId,
                        name: currentTags[i].dataValues.name
                    }
                });
            }
        }
        res.sendStatus(200);
    } catch (error) {
        console.log("Error deleting offer tags: " + error);
        res.sendStatus(500);
    }
  }
  
  //mama mia
  const addOfferTags = async (req, res) => {
    try {
        const currentTags = await OfferPostTags.findAll({where: {postId: req.body.offerId}});

        const updatedTags = req.body.tagList;
        const currentTagsList = currentTags.map(tag => tag.dataValues.name);
        
        updatedTags.forEach(tag => {
            if (!currentTagsList.includes(tag)) {
                OfferPostTags.create({
                    postId: req.body.offerId,
                    name: tag
                });
            }
        });
        res.sendStatus(200);
    } catch (error) {
        console.log("Error with adding new offer tags: " + error);
        res.sendStatus(500);
    }
  }

  //i am great success
  const updateOffer = async (req, res) => {
      try {
        const updateOffer = OfferPost.findOne({
            where: {offerId: req.body.offerId}
        });
        const offerAlreadyExists = updateOffer != null;
        if(offerAlreadyExists){
            await OfferPost.update({
                userId: req.body.userId,
                title: req.body.title,
                description: req.body.description,
                quantity: req.body.quantity,
                pickUpLocation: req.body.pickUpLocation,
                image: req.body.image,
                status: req.body.status,
                bestBeforeDate: req.body.bestBeforeDate
            }, {where: {offerId: req.body.offerId}});
            if (req.body.status == "Fulfilled") {
                await axios.delete(`http://ec2-35-183-145-212.ca-central-1.compute.amazonaws.com:3000/suggestedPosts/offer/${req.body.offerId}`);
            }
            res.sendStatus(200);
        }else{
            res.sendStatus(200);
        }
      } catch (error) {
        console.log("Error updating post: " + error);
        res.sendStatus(500);
      }
  }

  //woop woop that's the sound of the beast
  const deleteOffer = async (req, res) => {
    try {
        await OfferPostTags.destroy({
            where: {
                postId: req.body.offerId
            }
        })
        await OfferPost.destroy({
            where: {
                offerId: req.body.offerId
            }
        });
        await axios.delete(`http://ec2-35-183-145-212.ca-central-1.compute.amazonaws.com:3000/suggestedPosts/offer/${req.body.offerId}`);
        res.sendStatus(200);
    } catch (error) {
        console.log("Error deleting post: " + error);
        res.sendStatus(500);
    }
  }

  module.exports = {
    getOffer,
    getAllOffers,
    getAllUserOffers,
    searchOffers,
    searchOffersWithTags,
    createOffer,
    updateOffer,
    removeOfferTags,
    addOfferTags,
    deleteOffer
  };