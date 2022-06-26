const MessageObject = require('./message');

const UserObject = (sequelize, DataTypes) => {
    const User = sequelize.define("User", {
        id: {
            type: DataTypes.STRING,
            allowNull: false,
            primaryKey: true,
        },
        name: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        postIds: {
            type: DataTypes.ARRAY(DataTypes.STRING),
            allowNull: false,
        },
    });
    
    return User;
}

module.exports = UserObject;