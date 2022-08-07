const { Op } = require("sequelize");
const axios = require("axios").default;
const { RequestPost, RequestPostTags } = require("../models");
const { OK, INTERNAL_SERVER_ERROR, NOT_FOUND, BAD_REQUEST, CREATED } = require("../httpCodes");

const getRequest = async (req, res) => {
   if(req.params.requestId){
       const response = await RequestPost.findOne({where: {requestId: req.params.requestId}});
       if (response) {
            res.status(OK).json(JSON.parse(JSON.stringify(response)));
       } else {
           res.sendStatus(NOT_FOUND);
       }
   } else {
       res.sendStatus(NOT_FOUND);
   }
}

const getAllRequests = async (req, res) => {
    const response = await RequestPost.findAll();
    if (response) {
        res.status(OK).json(JSON.parse(JSON.stringify(response)));
    } else {
        res.sendStatus(NOT_FOUND);
    }
}

const getAllUserRequests = async (req, res) => {
    if (req.params.userId) {
        const response = await RequestPost.findAll({where: {userId: req.params.userId}});
        if (response) {
            res.status(OK).json(JSON.parse(JSON.stringify(response)));
        } else {
            res.sendStatus(NOT_FOUND)
        }
    } else {
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

        if (similarPosts != null){
            for (let i = 0; i < similarPosts.length; i = i + 1){
                response.push({
                    userId: similarPosts[i].dataValues.userId,
                    requestId: similarPosts[i].dataValues.requestId,
                    title: similarPosts[i].dataValues.title,
                    description: similarPosts[i].dataValues.description,
                    currentLocation: similarPosts[i].dataValues.currentLocation,
                    status: similarPosts[i].dataValues.status,
                    tagList: similarPosts[i].dataValues.tagList,
                });
            }
        } else {
            const res = await axios.get(`${process.env.RECOMMENDATION_URL}/request/${req.params.title}`);
            if (res.length) {
                const resolved = await Promise.all(res.map(async r => {
                    const item = await RequestPost.findOne({ where: { requestId: r.postId }});

                    const { userId, requestId, title, description, currentLocation, status, tagList } = item.dataValues;
                    return {
                        userId,
                        requestId,
                        title,
                        description,
                        currentLocation,
                        status,
                        tagList
                    };
                }));
                
                response = response.concat(resolved);
            }
        }

        res.status(OK).json(JSON.parse(JSON.stringify(response)));

    } catch (error) {
        res.sendStatus(INTERNAL_SERVER_ERROR);
    }
}

const searchRequestsWithTags = async (req, res) => {
    if (req.body.tagList) {
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
        console.log(uniquePostIds);

        const postList = await RequestPost.findAll({
            where: {requestId: uniquePostIds}
        });

        const result = postList.map(post => {
            return post.dataValues;
        })

        res.status(OK).json(JSON.parse(JSON.stringify({results: result})));
    } else {
      res.sendStatus(BAD_REQUEST);
    }
}

const createRequest = async (req, res) => {
    const hasAllFields = req.body.userId && req.body.title && req.body.description && req.body.status && req.body.tagList;
    if(hasAllFields) {
        const createdRequest = await RequestPost.create({
            userId: req.body.userId,
            title: req.body.title,
            description: req.body.description,
            status: req.body.status
          });
          console.log(createdRequest);
        let tagList = req.body.tagList;
        if (tagList != null) {
            for(let item of tagList) {
                RequestPostTags.create({
                    postId: createdRequest.requestId,
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

        res.status(CREATED).json({message:"Created!"});
    } else {
      res.sendStatus(BAD_REQUEST);
    }
}

const updateRequest = async (req, res) => {
    const hasAllFields = req.body.requestId && req.body.userId && req.body.title && req.body.description && req.body.currentLocation && req.body.status;
    if(hasAllFields) {
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
            res.status(OK).json(JSON.parse(JSON.stringify("Post updated")));
        }else{
            res.status(NOT_FOUND).json(JSON.parse(JSON.stringify("You cannot update a post that does not exist")));
        }
    } else {
      res.sendStatus(BAD_REQUEST);
    }
}

const removeRequestTags = async (req, res) => {
    const requestId = req.body.requestId;
    const tagList = req.body.tagList;
    const hasAllFields = requestId && tagList;
    const foundRequestTags = hasAllFields ? await RequestPostTags.findAll({where: {postId: requestId}}): null;
    if(foundRequestTags && hasAllFields) {
        for(let i = 0; i < tagList.length; i = i + 1){
            RequestPostTags.destroy({
                where: {
                    postId: requestId,
                    name: tagList[i]
                }
            });
        }
        res.sendStatus(OK);
    }
    else {
        res.sendStatus(BAD_REQUEST);
    }
}

const addRequestTags = async (req, res) => {
    const requestId = req.body.requestId;
    const updatedTags = req.body.tagList;
    const hasAllFields = requestId && updatedTags;
    if(hasAllFields){
        const isPresetTags = updatedTags.includes("fruit") || updatedTags.includes("vegetable") || updatedTags.includes("meat");
        if(isPresetTags){
            updatedTags.forEach(tag => {
                RequestPostTags.create({
                    postId: requestId,
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

const deleteRequest = async (req, res) => {
    const requestId = req.params.requestId;
    const foundRequestTags = await RequestPostTags.findAll({where: {postId: requestId}});
    const foundRequest = await RequestPost.findOne({where: {requestId}});
    if(foundRequestTags && foundRequest) {
        await RequestPostTags.destroy({
            where: {
                postId: requestId
            }
        })
        await RequestPost.destroy({
            where: {
                requestId
            }
        });
        await axios.delete(`${process.env.RECOMMENDATION_URL}/suggestedPosts/request/${requestId}`);
        res.sendStatus(OK);
    } else {
        res.sendStatus(NOT_FOUND);
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
  