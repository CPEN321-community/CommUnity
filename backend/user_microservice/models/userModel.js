const UserObject = (sequelize, DataTypes) => {
    const User = sequelize.define("User", {
        userId: {
            type: DataTypes.STRING,
            defaultValue: DataTypes.UUIDV4,
            allowNull: false,
            primaryKey: true
        },
        firstName: {
            type: DataTypes.STRING,
            defaultValue: "",
            allowNull: false,
        },
        lastName: {
            type: DataTypes.STRING, 
            defaultValue: "",
            allowNull: false,
        },
        email: {
            type: DataTypes.STRING, 
            defaultValue: "",
            allowNull: false,
        },
        profilePicture: {
            type: DataTypes.STRING,
            defaultValue: "",
            allowNull: false,
        }
    }, {
        timestamps: false,
    });
    return User;
  };

module.exports = UserObject;