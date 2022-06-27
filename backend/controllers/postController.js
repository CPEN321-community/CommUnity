 /**
  * Post Controller includes all of the endpoints relating to post management in our app
  */
 
 //Retrieve the post identified by post ID
 const getPostData = async (req, res) => {
    const { id } = req.body;
    const response = null;
    res.json(response);
  };
  
  //Find all posts for a specific user query
  const findPostList = async (req, res) => {
      const { id } = req.body;
      const response = null;
      res.json(response);
  }

  //Create a new post
  const createNewPost = async (req, res) => {
      const { id } = req.body;
      const response = null;
      res.json(response);
  }

  //Update post
  const updatePost = async (req, res) => {
      const { id } = req.body;
      const response = null;
      res.json(response);
  }

  //Delete post
  const deletePost = async (req, res) => {
      const { id } = req.body;
      const response = null;
      res.json(response);
  }

  module.exports = {
    getPostData,
    findPostList,
    createNewPost,
    updatePost,
    deletePost
  };
  