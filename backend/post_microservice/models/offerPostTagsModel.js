"use strict";
 
const { OfferPost } = require("./offerPostModel");

 const OfferPostTagsObject = (sequelize, DataTypes) => {
    const OfferPostTags = sequelize.define("OfferPostTags", {
        id: {
            type: DataTypes.STRING,
            defaultValue: DataTypes.UUIDV4,
            allowNull: false,
            primaryKey: true
        },
        postId: {
            type: DataTypes.STRING,
            get: function() {
                return OfferPost.offerId;
            }
        },
        name: {
            type: DataTypes.STRING,
            allowNull: false,
        }
    });
    return OfferPostTags;
}


module.exports = OfferPostTagsObject;