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
            type: DataTypes,
            allowNull: false,
            get: function() {
                return User.currentLocation;
            }
        },
        creationDate: {
            type: DataTypes.DATE,
            allowNull: false,
            get: function() {
              return Moment(this.getDataValue("date")).format("DD/MM/YYYY");
            }
        }
    });

    return RequestPost;
}

module.exports = RequestPostObject;