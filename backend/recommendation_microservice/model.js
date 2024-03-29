const tf = require('@tensorflow/tfjs');
const use = require('@tensorflow-models/universal-sentence-encoder');
const axios = require('axios').default;

class IModel {
    trainModel() {}
    getTopTen(item, type) {}
    removePostId(postId) {}
}

class Model extends IModel {
    constructor() {
      super();
        this.requestTensors = [];
        this.offerTensors = [];
        this.requestModel = null;
        this.offerModel = null;
    }

    async trainModel() {
        // re-initialize tensors
        this.requestTensors = [];
        this.offerTensors = [];

        // train the offer model
        const offerModel = await use.load();
        this.offerModel = offerModel;

        const offerRes = await axios.get(`${process.env.POST_URL}/communitypost/offers`);
        const allOffers = offerRes.data;

        for (let i=0; i < allOffers.length; i+=1) {
            const offer = allOffers[i];
            const offerTitle = (await offerModel.embed([offer.title])).unstack();
            const offerDesc = (await offerModel.embed([offer.description])).unstack();
            
            this.offerTensors.push({
                postId: offer.offerId,
                titleEmbedding: offerTitle[0],
                descriptionEmbedding: offerDesc[0],
            });
        }

        // train the request model
        const requestModel = await use.load();
        this.requestModel = requestModel;

        const requestRes = await axios.get(`${process.env.POST_URL}/communitypost/requests`);
        const allRequests = requestRes.data;

        for (let i=0; i < allRequests.length; i+=1) {
            const request = allRequests[i];
            const requestTitle = (await requestModel.embed([request.title])).unstack();
            const requestDesc = (await requestModel.embed([request.description])).unstack();
            
            this.requestTensors.push({
                postId: request.requestId,
                titleEmbedding: requestTitle[0],
                descriptionEmbedding: requestDesc[0],
            });
        }
        console.log("train complete");
    }

    async getTopTen(item, type) {
        const model = type == 'offer' ? this.offerModel : this.requestModel;
        const tensors = type == 'offer' ? this.offerTensors : this.requestTensors;
        const itemTensor = (await model.embed([item])).unstack();
        const distances = []

        for (let i=0; i < tensors.length; i += 1) {
            const distanceTitle = await tf.losses.cosineDistance(itemTensor[0], tensors[i].titleEmbedding).array();
            const distanceDesc = await tf.losses.cosineDistance(itemTensor[0], tensors[i].descriptionEmbedding).array();

            const distanceTitlePercentage = (1.0 - distanceTitle) * 100.0;
            const distanceDescPercentage = (1.0 - distanceDesc) * 100.0;

            const distanceAverage = (distanceTitlePercentage + distanceDescPercentage) / 2.0;
            distances.push({ 
                postId: tensors[i].postId, 
                score: distanceAverage
            });
        }
        distances.sort((first, two) => two.score - first.score);
        return distances.length >= 10 ? distances.slice(0,10) : distances;
    }
    
    removePostId(postId, type) {
        const tensors = type == 'offer' ? this.offerTensors : this.requestTensors;
        const newTensors = []
        tensors.forEach(tensor => {
            if (tensor.postId != postId) {
                newTensors.push(tensor);
            }
        });

        if (type == 'offer') {
            this.offerTensors = newTensors;
        } else {
            this.requestTensors = newTensors;
        }
        return 'done';
    }
}

module.exports = Model;
