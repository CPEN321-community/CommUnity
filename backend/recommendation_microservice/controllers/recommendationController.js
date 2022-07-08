var Singleton = require('../singleton');

const getSuggestedPosts = async (req, res) => {
    var model = new Singleton().getInstance();

    if (req.url.contains("/offer")) {
        res.status(200).json(model.getTopTen(req.params.item, 'offer'));
    } else {
        res.status(200).json(model.getTopTen(req.params.item, 'request'));
    }
}

const deletePost = async (req, res) => {
    var model = new Singleton().getInstance();
    
    if (req.url.contains("/offer")) {
        await model.removePostId(req.params.postId, 'offer');
    } else {
        await model.removePostId(req.params.postId, 'request');
    }
    res.status(200);
}

module.exports = { getSuggestedPosts, deletePost }