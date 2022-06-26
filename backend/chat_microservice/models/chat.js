const chatObject = (sequelize, DataTypes) => {
    const Chats = sequelize.define("chats", {
        message: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        sender: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        receiver: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        timestamp: {
            type: DataTypes.INTEGER,
            allowNull: false,
        },
        postId: {
            type: DataTypes.STRING, 
            allowNull: false,
        },
    });

    return Chats;
}

module.exports = chatObject;