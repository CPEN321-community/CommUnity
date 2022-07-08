const tf = require('@tensorflow/tfjs-node');
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
        // train the offer model
        const offerModel = await use.load();
        this.offerModel = offerModel;

        const offerRes = await axios.get('http://localhost:8080/communityPost/offers');
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

        const requestRes = await axios.get('http://localhost:8080/communityPost/requests');
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
    }

    async getTopTen(item, type) {
        const itemTensor = (await this.model.embed([item])).unstack();
        const distances = []
        const tensors = type == 'offer' ? this.offerTensors : this.requestTensors;

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
        let foundId = null;
        for(let i = 0; i < tensors.length; i = i+1) {
            if (tensors[i].postId == postId){
                foundId = i;
            }
        }

        if (foundId != null) {
            tensors = tensors.splice(foundId, 1);
        }
    }
}

module.exports = Model;
