"use strict";

 const OfferPostTagsObject = (sequelize, DataTypes) => {
    const OfferPostTags = sequelize.define("OfferPostTags", {
        offerTagId: {
            type: DataTypes.STRING,
            defaultValue: DataTypes.UUIDV4,
            allowNull: false,
            primaryKey: true
        },
        postId: {
            type: DataTypes.STRING,
            allowNull: false
        },
        name: {
            type: DataTypes.STRING,
            allowNull: false,
        }
    });
    return OfferPostTags;
}


module.exports = OfferPostTagsObject;