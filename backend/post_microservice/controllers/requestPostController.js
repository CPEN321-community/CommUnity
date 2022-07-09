const { Op } = require("sequelize");
const { RequestPost, RequestPostTags } = require("../models");

//waaaaaaaaaluigi
const getRequest = async (req, res) => {
   try {
       const requestId = req.params.requestId;
       const response = await RequestPost.findOne({where: {requestId: requestId}});
       res.json(response);
   } catch (error) {
       console.log("Error finding an offer post: " + error);
   }
}

 //yahoo
 const getAllRequests = async (req, res) => {
    try {
        const response = await RequestPost.findAll();
        res.json(response);
    } catch (error) {
        console.log("Error getting all of the offer posts: " + error);
    }
  }

  //works!
  const getAllUserRequests = async (req, res) => {
    try{
        const response = await RequestPost.findAll({where: {userId: req.params.userId}});
        res.json(response);
    } catch(error) {
        console.log("Error in retrieving request posts made by user " + error);
    }
  }


 const searchRequests = async (req, res) => {
    try {
        const title = req.params.title;
        const query = "%" + title + "%";

        //find all posts which have a title containing the query
        const response = await RequestPost.findAll({
            where: {
                title: {[Op.like]: query}, 
              }
        });
        if (response = null) {
            res.sendStatus(200);
        } else {
            res.json(response);
        }
    } catch (error) {
      console.log("Error with searching for offer posts: " + error);
      res.sendStatus(500);
    }
}

const searchRequestsWithTags = async (req, res) => {
  try {
      const tagList = req.params.tagList;

      //list of postIds that have the tags
      const postIds = await RequestPostTags.findAll({
          attributes: {include: ["requestId"]},
          where: {name: tagList}
      });
      
      //check whether there are duplicates in postIds list
      let uniquePostIds = [...new Set(postIds)];
      
      //find list of posts that match postIds
      const postList = await RequestPost.findAll({
          where: {requestId: uniquePostIds}
      });

      if (postList = null) {
          const message = "Sorry, there are no offer posts for " + tagList + "."
          res.json({
              message: message
          });
      } else {
          res.json(postList);
      }
  } catch (error) {
    console.log("Error with searching for offer posts: " + error);
    res.sendStatus(500);
  }
}

//success! this works!
const createRequest = async (req, res) => {
    try {
        await RequestPost.create({
            userId: req.body.userId,
            title: req.body.title,
            description: req.body.description,
            currentLocation: req.body.currentLocation,
            status: req.body.status
          });
        const newRequest = await RequestPost.findOne({where: {userId: req.body.userId}});

        let tagList = req.body.tagList;
        for(let item of tagList) {
            RequestPostTags.create({
                postId: newRequest.requestId,
                name: item
              });
        }
          res.sendStatus(200);
    } catch (error) {
      console.log("Error creating a new post: " + error);
      res.sendStatus(500);
    }
}

//yay success :)
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
            res.json("Post updated");
        }else{
            res.json("You cannot update a post that does not exist");
        }
    } catch (error) {
      console.log("Error updating post: " + error);
      res.sendStatus(500);
    }

}

//wwwweeeeee
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
        res.sendStatus(200);
    } catch (error) {
        console.log("Error deleting offer tags: " + error);
        res.sendStatus(500);
    }
  }

  //ahhhhhhh
  const addRequestTags = async (req, res) => {
    try {
        const currentTags = await RequestPostTags.findAll({where: {postId: req.body.requestId}});

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

//whaaaaaaaa
//woop woop that's the sound of the police
const deleteRequest = async (req, res) => {
  try {
    await RequestPostTags.destroy({
        where: {
            postId: req.body.requestId
        }
    })
      await RequestPost.destroy({
          where: {
              requestId: req.body.requestId
          }
      });
      res.sendStatus(200);
  } catch (error) {
      console.log("Error deleting post: " + error);
      res.sendStatus(500);
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
  