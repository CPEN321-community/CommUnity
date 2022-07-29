const axios = require('axios');

class OfferPost {
  static all() {
    return axios.get('communitypost/offers/:offerId').then(resp => resp.data);
  }
}

module.exports = {OfferPost}