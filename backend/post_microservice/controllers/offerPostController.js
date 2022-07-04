  const { Op } = require("sequelize");
  const { OfferPost, OfferPostTags } = require("../models");
 
 const getOffer = async (req, res) => {
     try {
         const offerId = req.params.postId;
         const response = await OfferPost.findByPk(offerId, {include: ["offerTags"]});
         res.json(response);
         res.sendStatus(200);
     } catch (error) {
         console.log("Error finding an offer post: " + error);
         res.sendStatus(500);
     }
  };
  
  const searchOffers = async (req, res) => {
      try {
          const title = req.params.title;
          const query = "%" + title + "%";

          //find all posts which have a title containing the query
          const response = await OfferPost.findAll({
              where: {
                  title: {[Op.like]: query}, 
                }
          });
          if (response = null) {
              const message = "Sorry, there are no offer posts for " + title + "."
              res.json({
                  message: message
              });
              res.sendStatus(200);
          } else {
              res.json(response);
              res.sendStatus(200);
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
            res.json({
                message: message
            });
            res.sendStatus(200);
        } else {
            res.json(postList);
            res.sendStatus(200);
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
            title: req.body.title,
            description: req.body.description,
            quantity: req.body.quantity,
            pickUpLocation: req.body.pickUpLocation,
            image: req.body.image,
            bestBeforeDate: req.body.bestBeforeDate
        });

        const newOffer = await OfferPost.findOne(
            {
                where: {
                    title: req.body.title,
                    description: req.body.description,
                    quantity: req.body.quantity,
                    pickUpLocation: req.body.pickUpLocation,
                    image: req.body.image,
                    bestBeforeDate: req.body.bestBeforeDate
                }
            }
        );

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
  

  //i am great success
  const updateOffer = async (req, res) => {
      try {
        const updateOffer = OfferPost.findOne({
            where: {offerId: req.body.offerId}
        })
        const offerAlreadyExists = updateOffer != null;
        if(offerAlreadyExists){
            await OfferPost.update({
                title: req.body.title,
                description: req.body.description,
                quantity: req.body.quantity,
                pickUpLocation: req.body.pickUpLocation,
                image: req.body.image,
                bestBeforeDate: req.body.bestBeforeDate
            }, {where: {offerId: req.body.offerId}});
            res.json("Post updated");
            res.sendStatus(200);
        }else{
            res.json("You cannot update a post that does not exist");
            res.sendStatus(200);
        }
      } catch (error) {
        console.log("Error updating post: " + error);
        res.sendStatus(500);
      }
  }

  //Delete post
  const deleteOffer = async (req, res) => {
    try {
        await OfferPost.destroy({
            where: {
                offerId: req.body.offerId
            }
        });
        res.sendStatus(200);
    } catch (error) {
        console.log("Error deleting post: " + error);
        res.sendStatus(500);
    }
  }

  module.exports = {
    getOffer,
    searchOffers,
    searchOffersWithTags,
    createOffer,
    updateOffer,
    deleteOffer
  };