const UserObject = (sequelize, DataTypes) => {
    const User = sequelize.define("User", {
        id: {
            type: DataTypes.UUID,
            defaultValue: DataTypes.UUIDV4,
            allowNull: false,
            primaryKey: true
        },
        currentLocation: {
            type: DataTypes.STRING,
            allowNull: false
        },
        pickUpLocation: {
            type: DataTypes.STRING,
            allowNull: false
        }
    });
  
    return User;
  };

module.exports = UserObject;
   