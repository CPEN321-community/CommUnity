/**
 * Schema Model for all donation post objects stored in the database
 * @param {*} sequelize 
 * @param {*} DataTypes 
 * @returns 
 */

 "use strict";
 const { User } = require("./userModel");
 const Moment = require("moment");

 const RequestPostObject = (sequelize, DataTypes) => {
    const RequestPost = sequelize.define("RequestPost", {
        requestId: {
            type: DataTypes.STRING,
            defaultValue: DataTypes.UUIDV4,
            allowNull: false,
            primaryKey: true
        },
        title: {
            type: DataTypes.STRING, 
            allowNull: false
        },
        description: {
            type: DataTypes.STRING,
            allowNull: false
        },
        currentLocation: {
            type: DataTypes.STRING,
            allowNull: false,
            get: function() {
                return User.currentLocation;
            }
        }
    });

    return RequestPost;
}

module.exports = RequestPostObject;