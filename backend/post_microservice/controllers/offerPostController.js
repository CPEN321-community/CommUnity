 /**
  * Post Controller includes all of the endpoints relating to post management in our app
  */
  const { Op } = require("sequelize");
  const { User } = require("../models/userModel");
  const { OfferPost } = require("../models/offerPostModel");
  const {OfferPostTags} = require("../models/offerPostTagsModel");

 const getOffer = async (req, res) => {
     try {
         const offerId = req.postId;
         const response = await OfferPost.findByPk(offerId, {include: ["offerTags"]});
         res.json(response);
         res.sendStatus(200);
     } catch (error) {
         console.log("Error finding an offer post: " + e);
         res.sendStatus(500);
     }
  };
  
  const searchOffers = async (req, res) => {
      try {
          const title = req.title;
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
        console.log("Error with searching for offer posts: " + e);
        res.sendStatus(500);
      }
  }

  const searchOffersWithTags = async (req, res) => {
    try {
        const tagList = req.tagList;

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
        })

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
      console.log("Error with searching for offer posts: " + e);
      res.sendStatus(500);
    }
}

  const createOffer = async (req, res) => {
      try {
          const newOffer = OfferPost.create({
              title: req.postDTO.title,
              description: req.postDTO.description,
              quantity: req.postDTO.quantity,
              pickUpLocation: req.postDTO.pickUpLocation,
              image: req.postDTO.image,
              bestBeforeDate: req.postDTO.bestBeforeDate
            });
          req.postDTO.tagList.foreach(tag => {
              OfferPostTags.create({
                  postId: newOffer.offerId,
                  name: tag
                });
            });
            res.sendMessage("New post has been created.");
            res.sendStatus(200);
      } catch (error) {
        console.log("Error creating a new post: " + e);
        res.sendStatus(500);
      }
  }
  
  const updateOffer = async (req, res) => {
      const newValues = {
          title: req.postDTO.title,
          description: req.postDTO.description,
          quantity: req.postDTO.quantity,
          pickUpLocation: req.postDTO.pickUpLocation,
          image: req.postDTO.image,
          bestBeforeDate: req.postDTO.bestBeforeDate
      }
      try {
          await OfferPost.update({newValues}, {
              where: {
                  offerId: req.postDTO.offerId
              }
          });
          req.postDTO.tagList.foreach(tag => {
              await OfferPostTags.update({name: tag}, {
                  where: {
                      postId: req.postDTO.offerId
                  }
              });
          });
          const message = req.postDTO.title + " post has been updated.";
          res.sendMessage(message);
          res.sendStatus(200);
      } catch (error) {
        console.log("Error updating post: " + e);
        res.sendStatus(500);
      }

  }

  //Delete post
  const deleteOffer = async (req, res) => {
      const { id } = req.body;
      const response = null;
      res.json(response);
  }

  module.exports = {
    getOffer,
    searchOffers,
    searchOffersWithTags,
    createOffer,
    updateOffer,
    deleteOffer
  };