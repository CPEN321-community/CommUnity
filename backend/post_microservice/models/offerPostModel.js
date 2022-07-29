/**
 * Schema Model for all donation post objects stored in the database
 * @param {*} sequelize 
 * @param {*} DataTypes 
 * @returns 
 */

 "use strict";

 const OfferPostObject = (sequelize, DataTypes) => {
    const OfferPost = sequelize.define("OfferPost", {
        offerId: {
            type: DataTypes.STRING,
            defaultValue: DataTypes.UUIDV4,
            allowNull: false,
            primaryKey: true
        },
        userId: {
            type: DataTypes.STRING,
            allowNull: false
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
            allowNull: false
        },
        image: {
            type: DataTypes.STRING,
            allowNull: false
        },
        status: {
            type: DataTypes.STRING,
            allowNull: false
        },
        bestBeforeDate: {
            type: DataTypes.DATE,
            allowNull: false
        }
    });

    return OfferPost;
}

module.exports = OfferPostObject;