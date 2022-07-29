var Singleton = require('../singleton');

const getSuggestedPosts = async (req, res) => {
    var model = new Singleton().getInstance();
    let result;
    if (req.url.includes("/offer")) {
        result = await model.getTopTen(req.params.item, 'offer');
    } else {
        result = await model.getTopTen(req.params.item, 'request');
    }
    res.status(200).json(result);
}

const deletePost = async (req, res) => {
    var model = new Singleton().getInstance();
    if (req.url.includes("/offer")) {
        await model.removePostId(req.params.postId, 'offer');
    } else {
        await model.removePostId(req.params.postId, 'request');
    }
    res.sendStatus(200);
}

module.exports = { getSuggestedPosts, deletePost }