const { Op } = require("sequelize");
const axios = require("axios").default;
const { RequestPost, RequestPostTags } = require("../models");
const { OK, INTERNAL_SERVER_ERROR, NOT_FOUND } = require("../index.js");

const getRequest = async (req, res) => {
    console.log("Get request endpoint hit");
   if(req.params.requestId){
       const response = await RequestPost.findOne({where: {requestId}});
       res.json(response);
   } else {
       res.status(INTERNAL_SERVER_ERROR);
   }
}

const getAllRequests = async (req, res) => {
    if (response) {
        const response = await RequestPost.findAll();
        res.status(OK).json(response);
    } else {
        console.log("Error getting all of the offer posts: " + error);
        res.status(INTERNAL_SERVER_ERROR);
    }
}

const getAllUserRequests = async (req, res) => {
    console.log("get user endpoint hit");
    try{
        const response = await RequestPost.findAll({where: {userId: req.params.userId}});
        res.status(OK).json(response);
    } catch(error) {
        console.log("Error in retrieving request posts made by user " + error);
        res.status(INTERNAL_SERVER_ERROR);
    }
}

const searchRequests = async (req, res) => {
    const title = req.params.title
    try {
        let response = [];

        const similarPosts = await RequestPost.findAll({
            where: {
                [Op.or]: [
                    {title: {[Op.like]: "%"+title+"%"}, 
                    status: "Active"},
                    {description: {[Op.like]: "%"+title+"%"}, 
                    status: "Active"}
                ]
            }
        });

        console.log(similarPosts);
        if (similarPosts != null){
            for (let i = 0; i < similarPosts.length; i = i + 1){
                response.push({
                    userId: similarPosts[i].dataValues.userId,
                    requestId: similarPosts[i].dataValues.requestId,
                    title: similarPosts[i].dataValues.title,
                    description: similarPosts[i].dataValues.description,
                    currentLocation: similarPosts[i].dataValues.currentLocation,
                    status: similarPosts[i].dataValues.status,
                    createdAt: similarPosts[i].dataValues.createdAt,
                });
            }
        } else {
            const res = await axios.get(`${process.env.RECOMMENDATION_URL}/request/${req.params.title}`);
            if (res.length) {
                const resolved = await Promise.all(res.map(async r => {
                    const item = await RequestPost.findOne({ where: { requestId: r.postId }});
                    const { userId, offerId, title, description, quantity, pickUpLocation, image, status, bestBeforeDate } = item.dataValues;
                    return {
                        userId,
                        offerId,
                        title,
                        description,
                        quantity,
                        pickUpLocation,
                        image,
                        status,
                        bestBeforeDate
                    };
                }));

                response = response.concat(resolved);
            }
        }

        res.status(OK).json(response);

    } catch (error) {
        console.log("Error with searching for request posts: " + error);
        res.sendStatus(INTERNAL_SERVER_ERROR);
    }
}

const searchRequestsWithTags = async (req, res) => {
    try {
        const tagList = req.body.tagList;
        const postTags = await RequestPostTags.findAll({
            where: {name: tagList}
        });
        
        let uniquePostIds = [];
        let duplicateRequestTagIds = [];
        for (let i = 0; i < postTags.length; i = i + 1){
            if (uniquePostIds.includes(postTags[i].dataValues.postId)){
                duplicateRequestTagIds.push(postTags[i].dataValues.offerId);
            } else {
                uniquePostIds.push(postTags[i].dataValues.postId);
            }
        }
        
        const postList = await RequestPost.findAll({
            where: {requestId: uniquePostIds}
        });

        const result = postList.map(post => {
            return post.dataValues;
        })

        res.status(OK).json({results: result});
    } catch (error) {
      console.log("Error with searching for request posts: " + error);
      res.sendStatus(INTERNAL_SERVER_ERROR);
    }
}

const createRequest = async (req, res) => {
    try {
        const createdRequest = await RequestPost.create({
            userId: req.body.userId,
            title: req.body.title,
            description: req.body.description,
            status: req.body.status
          });

        let tagList = req.body.tagList;
        if (tagList) {
            for(let item of tagList) {
                RequestPostTags.create({
                    postId: createdRequest.dataValues.requestId,
                    name: item
                  });
            }
        }
        
        const updateUserBody = {
            userId: req.body.userId,
            offerPosts: 0,
            requestPosts: 1,
        };
        await axios.put(`${process.env.USER_URL}/rank`, updateUserBody);
        res.sendStatus(OK);
    } catch (error) {
      console.log("Error creating a new post: " + error);
      res.sendStatus(INTERNAL_SERVER_ERROR);
    }
}

const updateRequest = async (req, res) => {
    try {
        const updateRequest = RequestPost.findOne({
            where: {requestId: req.body.requestId}
        });
        const requestAlreadyExists = updateRequest != null;
        if(requestAlreadyExists){
            await RequestPost.update({
                userId: req.body.userId,
                title: req.body.title,
                description: req.body.description,
                currentLocation: req.body.currentLocation,
                status: req.body.status
            }, {where: {requestId: req.body.requestId}});
            if (req.body.status == "Fulfilled") {
                await axios.delete(`${process.env.RECOMMENDATION_URL}/suggestedPosts/request/${req.body.requestId}`);
            }
            res.status(OK).json("Post updated");
        }else{
            res.status(NOT_FOUND).json("You cannot update a post that does not exist");
        }
    } catch (error) {
      console.log("Error updating post: " + error);
      res.sendStatus(INTERNAL_SERVER_ERROR);
    }

}

const removeRequestTags = async (req, res) => {
    try {
        const currentTags = await RequestPostTags.findAll({
            where: {postId: req.body.requestId}
        });
        const updatedTags = req.body.tagList;

        for (let i = 0; i < currentTags.length; i = i + 1){
            if (!(updatedTags.includes(currentTags[i].dataValues.name))) {
                RequestPostTags.destroy({
                    where: {
                        postId: req.body.requestId,
                        name: currentTags[i].dataValues.name
                    }
                });
            }
        }
        res.sendStatus(OK);
    } catch (error) {
        console.log("Error deleting offer tags: " + error);
        res.sendStatus(INTERNAL_SERVER_ERROR);
    }
}

const addRequestTags = async (req, res) => {
    try {
        const currentTags = await RequestPostTags.findAll({where: {postId: req.body.requestId}});

        const updatedTags = req.body.tagList;
        const currentTagsList = currentTags.map(tag => tag.dataValues.name);

        updatedTags.forEach(tag => {
            if (!currentTagsList.includes(tag)) {
                RequestPostTags.create({
                    postId: req.body.requestId,
                    name: tag
                });
            }
        });
        res.sendStatus(OK);
    } catch (error) {
        console.log("Error with adding new offer tags: " + error);
        res.sendStatus(INTERNAL_SERVER_ERROR);
    }
}

const deleteRequest = async (req, res) => {
    try {
        await RequestPostTags.destroy({where: {postId: req.body.requestId}});
        await RequestPost.destroy({where: {requestId: req.body.requestId}});
        await axios.delete(`${process.env.RECOMMENDATION_URL}/suggestedPosts/request/${req.body.requestId}`);
        res.sendStatus(OK);
    } catch (error) {
        console.log("Error deleting post: " + error);
        res.sendStatus(INTERNAL_SERVER_ERROR);
    }
}

module.exports = {
  getRequest,
  getAllRequests,
  getAllUserRequests,
  searchRequests,
  searchRequestsWithTags,
  createRequest,
  updateRequest,
  removeRequestTags,
  addRequestTags,
  deleteRequest
};
  