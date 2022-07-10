const { Op } = require("sequelize");
const { RequestPost, RequestPostTags } = require("../models");

//waaaaaaaaaluigi
const getRequest = async (req, res) => {
   try {
       const requestId = req.body.requestId;
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

  const getAllUserRequests = async (req, res) => {
    try{
        const response = await RequestPost.findAll({where: {userId: userId}});
        res.json(response);
    } catch(error) {
        console.log("Error in retrieving request posts made by user " + error);
    }
  }


 const searchRequests = async (req, res) => {
    try {
        const title = req.body.title;
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
      const tagList = req.body.tagList;

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
        const createdRequest = await RequestPost.create({
            userId: req.body.userId,
            title: req.body.title,
            description: req.body.description,
            currentLocation: req.body.currentLocation,
            status: "Active"
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

const removeRequestTags = async (req, res) => {
    try {
        const currentTags = RequestPostTags.findAll({
            where: {postId: req.body.requestId}
        });
        const updatedTags = req.body.tagList;

        //currentTags.length > updatedTags.length
        for (let i = 0; i < currentTags.length; i = i + 1){
            if (!(updatedTags.contains(currentTags[i]))) {
                RequestPostTags.destroy({
                    where: {
                        postId: req.body.requestId,
                        name: currentTags[i]
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
  
  const addRequestTags = async (req, res) => {
    try {
        const currentTags = RequestPostTags.findAll({
            where: {postId: req.body.requestId}
        });
        const updatedTags = req.body.tagList;

        //check if the post has tags associated with it first...
        if (currentTags == null){
            for(let item of updatedTags) {
                RequestPostTags.create({
                    postId: req.body.requestId,
                    name: item
                });
            }
        } else {
            //updatedTags.length > currentTags.length
            for (let i = 0; i < updatedTags.length; i = i + 1){
                if(!(currentTags.contains(updatedTags[i]))){
                    RequestPostTags.create({
                        postId: req.body.requestId,
                        name: updatedTags[i]
                    });
                }
            }
        }
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
  