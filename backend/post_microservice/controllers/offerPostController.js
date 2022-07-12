const { Op } = require("sequelize");
const { OfferPost, OfferPostTags, RequestPostTags } = require("../models");
const axios = require("axios")

const getOffer = async (req, res) => {
    try {
        const offerId = req.params.offerId;
        const response = await OfferPost.findOne({where: {offerId: offerId}});
        res.json(response);
    } catch (error) {
        console.log("Error finding an offer post: " + error);
    }
}

const getAllOffers = async (req, res) => {
    try {
        const response = await OfferPost.findAll();
        res.json(response);
    } catch (error) {
        console.log("Error getting all of the offer posts: " + error);
    }
}

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
    const title = req.params.title;
    try {
        let response = [];
        const similarPosts = await OfferPost.findAll({
            where: {
                title: {[Op.like]: "%" + title + "%"}, 
                status: "Active"
            }
        });

        if (similarPosts != null){
            for (let i = 0; i < similarPosts.length; i = i + 1){
                response.push({
                    userId: similarPosts[i].dataValues.userId,
                    offerId: similarPosts[i].dataValues.offerId,
                    title: similarPosts[i].dataValues.title,
                    description: similarPosts[i].dataValues.description,
                    quantity: similarPosts[i].dataValues.quantity,
                    pickUpLocation: similarPosts[i].dataValues.pickUpLocation,
                    image: similarPosts[i].dataValues.image,
                    status: similarPosts[i].dataValues.status,
                    bestBeforeDate: similarPosts[i].dataValues.bestBeforeDate
                });
            }
        } else {
            const res = await axios.get(`${process.env.RECOMMENDATION_URL}/offer/${req.params.title}`);
            if (res.length) {
                res.forEach(r => {
                    const item = await OfferPost.findOne({ where: { offerId: res.postId }});
                    const { userId, offerId, title, description, quantity, pickUpLocation, image, status, bestBeforeDate } = item.dataValues;
                    result.push({
                        userId, offerId, title, description, quantity, pickUpLocation, image, status, bestBeforeDate
                    })
                })
            }
        }

        res.json(response);

    } catch (error) {
        console.log("Error with searching for offer posts: " + error);
        res.sendStatus(500);
    }
}

const searchOffersWithTags = async (req, res) => {
    try {
        const tagList = req.body.tagList;
        console.log("hello world!");
        console.log(tagList);
        const postTags = await OfferPostTags.findAll({
            where: {name: tagList}
        });
        
        let uniquePostIds = [];
        let duplicateOfferTagIds = [];
        for (let i = 0; i < postTags.length; i = i + 1){
            if (uniquePostIds.includes(postTags[i].dataValues.postId)){
                duplicateOfferTagIds.push(postTags[i].dataValues.offerId);
            } else {
                uniquePostIds.push(postTags[i].dataValues.postId);
            }
        }
        
        const postList = await OfferPost.findAll({
            where: {offerId: uniquePostIds}
        });

        const result = postList.map(post => {
            return post.dataValues;
        })
        console.log(result);

        res.status(200).json({results: result});
    } catch (error) {
      console.log("Error with searching for offer posts: " + error);
      res.sendStatus(500);
    }
}

const createOffer = async (req, res) => {
    try {
        const createdOffer = await OfferPost.create({
            userId: req.body.userId,
            title: req.body.title,
            description: req.body.description,
            quantity: req.body.quantity,
            pickUpLocation: req.body.pickUpLocation,
            image: req.body.image,
            status: req.body.status,
            bestBeforeDate: req.body.bestBeforeDate
        });

        let tagList = req.body.tagList;
        if(tagList != null){
            for(let item of tagList) {
                OfferPostTags.create({
                    postId: createdOffer.offerId,
                    name: item
                });
            }
            }

        const updateUserBody = {
            userId: req.body.userId,
            offerPosts: 1,
            requestPosts: 0,
        };
        console.log(updateUserBody);

        await axios.put(`${process.env.USER_URL}/rank`, updateUserBody);
        res.sendStatus(200);

    } catch (error) {
        console.log("Error creating a new post: " + error);
        res.sendStatus(500);
    }
}

const removeOfferTags = async (req, res) => {
    try {
        const currentTags = await OfferPostTags.findAll({where: {postId: req.body.offerId}});
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

const updateOffer = async (req, res) => {
    try {
        const updateOffer = OfferPost.findOne({where: {offerId: req.body.offerId}});
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
                await axios.delete(`${process.env.RECOMMENDATION_URL}/suggestedPosts/offer/${req.body.offerId}`);
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
        await axios.delete(`${process.env.RECOMMENDATION_URL}/suggestedPosts/offer/${req.body.offerId}`);
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