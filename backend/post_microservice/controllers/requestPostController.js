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

const searchRequestsWithTags = async (req, res) => {
  try {
      const tagList = req.params.tagList;

      //list of postIds that have the tags
      const postIds = await RequestPostTags.findAll({
          attributes: {include: ["postId"]},
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

//success! this works!
const createRequest = async (req, res) => {
    try {
        await RequestPost.create({
            title: req.body.title,
            description: req.body.description,
            currentLocation: req.body.currentLocation
          });
        const newRequest = await RequestPost.findOne(
            {
                where: {
                    title: req.body.title, 
                    description:req.body.description, 
                    currentLocation: req.body.currentLocation
                }
            }
        )

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
                title: req.body.title,
                description: req.body.description,
                currentLocation: req.body.currentLocation
            }, {where: {requestId: req.body.requestId}});
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
  searchRequests,
  searchRequestsWithTags,
  createRequest,
  updateRequest,
  deleteRequest
};
  