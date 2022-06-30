/**
 * Schema Model for all donation post objects stored in the database
 * @param {*} sequelize 
 * @param {*} DataTypes 
 * @returns 
 */

 "use strict";
 const { User } = require("./userModel");
 const Moment = require("moment");

 const OfferPostObject = (sequelize, DataTypes) => {
    const OfferPost = sequelize.define("OfferPost", {
        offerId: {
            type: DataTypes.UUIDV4,
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
        quantity: {
            type: DataTypes.INTEGER,
            allowNull: false
        },
        pickUpLocation: {
            type: DataTypes.STRING,
            allowNull: false,
            get: function() {
                return User.pickUpLocation;
            }
        },
        image: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        creationDate: {
            type: DataTypes.DATE,
            allowNull: false,
            get: function() {
              return Moment(this.getDataValue("date")).format("DD/MM/YYYY");
            }
        },
        bestBeforeDate: {
            type: DataTypes.DATE,
            allowNull: false
        },
    });

    return OfferPost;
}

module.exports = OfferPostObject;