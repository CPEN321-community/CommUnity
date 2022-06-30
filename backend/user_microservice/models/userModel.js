const UserObject = (sequelize, DataTypes) => {
    const User = sequelize.define("User", {
        userId: {
            type: DataTypes.UUIDV4,
            defaultValue: DataTypes.UUIDV4,
            allowNull: false,
            primaryKey: true
        },
        firstName: {
            type: DataTypes.STRING, 
            allowNull: false,
        },
        lastName: {
            type: DataTypes.STRING, 
            allowNull: false,
        },
        email: {
            type: DataTypes.STRING, 
            allowNull: false,
        },
        profilePicture: {
            type: DataTypes.STRING,
            allowNull: false,
        }
    });
    return User;
  };

module.exports = UserObject;