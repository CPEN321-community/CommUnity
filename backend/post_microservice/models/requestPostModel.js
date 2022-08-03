/**
 * Schema Model for all donation post objects stored in the database
 * @param {*} sequelize 
 * @param {*} DataTypes 
 * @returns 
 */

 "use strict";

 const RequestPostObject = (sequelize, DataTypes) => {
    const RequestPost = sequelize.define("RequestPost", {
        requestId: {
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
        currentLocation: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        status: {
            type: DataTypes.STRING,
            allowNull: false
        }
    });

    return RequestPost;
}

module.exports = RequestPostObject;