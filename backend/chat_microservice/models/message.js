const MessageObject = (sequelize, DataTypes) => {
    const Message = sequelize.define("Message", {
        id: {
            type: DataTypes.UUIDV4,
            allowNull: false,
            primaryKey: true,
            defaultValue: DataTypes.UUIDV4,
        },
        postId: {
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