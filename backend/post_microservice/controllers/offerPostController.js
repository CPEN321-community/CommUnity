const { Op } = require("sequelize");
const { OfferPost, OfferPostTags } = require("../models");
const axios = require("axios");
const { INTERNAL_SERVER_ERROR, OK, NOT_FOUND, BAD_REQUEST, CREATED } = require('../httpCodes');

const getOffer = async (req, res) => {
    if(req.params.offerId) {
        const response = await OfferPost.findOne({where: {offerId: req.params.offerId}});
        if (response) {
            res.status(OK).json(response);
        } else {
            res.sendStatus(NOT_FOUND);
        }
    } else {
        res.sendStatus(INTERNAL_SERVER_ERROR);
    }
}

const getAllOffers = async (req, res) => {
    const response = await OfferPost.findAll();
    if (response) {
        res.status(OK).json(response);
    } else {
        res.sendStatus(NOT_FOUND);
    }
}

const getAllUserOffers = async (req, res) => {
    if (req.params.userId) {
        const response = await OfferPost.findAll({where: {userId: req.params.userId}});
        if (response) {
            res.status(OK).json(response);
        } else {
            res.sendStatus(NOT_FOUND);
        }
    } else {
        console.log("Error in retrieving offer posts made by user");
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
                    bestBeforeDate: similarPosts[i].dataValues.bestBeforeDate,
                    offerTags: similarPosts[i].dataValues.offerTags,
                });
            }
        } else {
            const res = await axios.get(`${process.env.RECOMMENDATION_URL}/offer/${req.params.title}`);
            if (res.length) {
                const resolved = await Promise.all(res.map(async r => {
                    const item = await OfferPost.findOne({ where: { offerId: r.postId }});
                    const { userId, offerId, title, description, quantity, pickUpLocation, image, status, bestBeforeDate, offerTags } = item.dataValues;
                    const returnObj = {
                        userId,
                        offerId,
                        title,
                        description,
                        quantity,
                        pickUpLocation,
                        image,
                        status,
                        bestBeforeDate,
                        offerTags
                    };
                    return returnObj;
                }));

                response = response.concat(resolved);
            }
        }

        res.json(response);

    } catch (error) {
        console.log("Error with searching for offer posts: " + error);
        res.sendStatus(INTERNAL_SERVER_ERROR);
    }
}

const searchOffersWithTags = async (req, res) => {
    if(req.body.tagList) {
        const tagList = req.body.tagList;
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

        res.status(OK).json({results: result});
    } else {
      res.sendStatus(BAD_REQUEST);
    }
}

const createOffer = async (req, res) => {
    const hasAllFields = req.body.userId && req.body.title && req.body.description && req.body.quantity && req.body.pickUpLocation && req.body.image && req.body.status && req.body.bestBeforeDate && req.body.tagList;
    const validDate = req.body.bestBeforeDate.length == 10;
    const validImage = req.body.image.includes(".com");
    if(hasAllFields && validImage && validDate) {
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

        await axios.put(`${process.env.USER_URL}/rank`, updateUserBody);
        res.sendStatus(CREATED);

    } else {
        res.sendStatus(BAD_REQUEST);
    }
}

const removeOfferTags = async (req, res) => {
    if(req.body.tagList) {
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
        res.sendStatus(OK);
    } else {
        console.log("Error deleting offer tags: " + error);
        res.sendStatus(INTERNAL_SERVER_ERROR);
    }
}
// const addRequestTags = async (req, res) => {
//     const requestId = req.body.requestId;
//     const updatedTags = req.body.tagList;
//     const hasAllFields = requestId && updatedTags;
//     if(hasAllFields){
//         const isPresetTags = updatedTags.includes("fruit") || updatedTags.includes("vegetable") || updatedTags.includes("meat");
//         if(isPresetTags){
//             updatedTags.forEach(tag => {
//                 RequestPostTags.create({
//                     postId: requestId,
//                     name: tag
//                 });
//             });
//             res.sendStatus(CREATED);
//         } else {
//             res.sendStatus(BAD_REQUEST);
//         }
//     } else {
//         res.sendStatus(BAD_REQUEST);
//     }
// }
  
const addOfferTags = async (req, res) => {
    const offerId = req.body.offerId;
    const updatedTags = req.body.tagList;
    const hasAllFields = offerId && updatedTags;
    if(hasAllFields) {
        const isPresetTags = updatedTags.includes("fruit") || updatedTags.includes("vegetable") || updatedTags.includes("meat");
        if(isPresetTags){
            updatedTags.forEach(tag => {
                OfferPostTags.create({
                    postId: offerId,
                    name: tag
                });
            });
            res.sendStatus(CREATED);
        } else {
            res.sendStatus(BAD_REQUEST);
        }
    } else {
        res.sendStatus(BAD_REQUEST);
    }
}

const updateOffer = async (req, res) => {
    if(req.body.offerId) {
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
            res.sendStatus(OK);
        }else{
            res.sendStatus(OK);
        }
    } else {
        console.log("Error updating post: " + error);
        res.sendStatus(INTERNAL_SERVER_ERROR);
    }
}

const deleteOffer = async (req, res) => {
    if(req.body.offerId) {
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
        res.sendStatus(OK);
    } else {
        console.log("Error deleting post: " + error);
        res.sendStatus(INTERNAL_SERVER_ERROR);
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