"use strict";

 const RequestPostTagsObject = (sequelize, DataTypes) => {
    const RequestPostTags = sequelize.define("RequestPostTags", {
        requestTagId: {
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
    return RequestPostTags;
}


module.exports = RequestPostTagsObject;