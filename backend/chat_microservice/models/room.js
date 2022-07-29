const RoomObj = (sequelize, DataTypes) => {
    const Room = sequelize.define("Room", {
        id: {
            type: DataTypes.STRING,
            allowNull: false,
            primaryKey: true,
            defaultValue: DataTypes.UUIDV4,
        },
        postId: {
            type: DataTypes.STRING,
            allowNull: false,
        },
    }, {
        timestamps: false
    });
    
    return Room;
}

module.exports = RoomObj;