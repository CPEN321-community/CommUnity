const UserObject = require('./user');

const MessageObject = (sequelize, DataTypes) => {
    const Message = sequelize.define("Message", {
        id: {
            type: DataTypes.STRING,
            allowNull: false,
            primaryKey: true,
        },
        postId: {
            type: DataTypes.STRING, 
            allowNull: false,
        },
        userId: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        message: {
            type: DataTypes.STRING,
            allowNull: false,
        },
    });

    return Message;
}

module.exports = MessageObject;