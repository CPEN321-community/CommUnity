"use strict";
 
const { RequestPost } = require("./requestPostModel");

 const RequestPostTagsObject = (sequelize, DataTypes) => {
    const RequestPostTags = sequelize.define("RequestPostTags", {
        id: {
            type: DataTypes.STRING,
            defaultValue: DataTypes.UUIDV4,
            allowNull: false,
            primaryKey: true
        },
        postId: {
            type: DataTypes.STRING,
            get: function() {
                return RequestPost.requestId;
            }
        },
        name: {
            type: DataTypes.STRING,
            allowNull: false,
        }
    });
    return RequestPostTags;
}


module.exports = RequestPostTagsObject;