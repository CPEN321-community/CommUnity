const tf = require('@tensorflow/tfjs-node');
// const tf = require('@tensorflow/tfjs')
const use = require('@tensorflow-models/universal-sentence-encoder');
var Singleton = require('../singleton');

const getSuggestedPosts = async (req, res) => {


    var model = new Singleton().getInstance();
    model.trainModel();
}


const getCosineDistance = async (req, res) => {
    
}

module.exports = { getSuggestedPosts }