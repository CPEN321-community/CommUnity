const UserObject = (sequelize, DataTypes) => {
  const User = sequelize.define("User", {
      userId: {
          type: DataTypes.STRING,
          defaultValue: DataTypes.UUIDV4,
          allowNull: false,
          primaryKey: true
      },
      token: {
        type: DataTypes.STRING,
        allowNull: false
      }
  });

  return User;
};

module.exports = UserObject;
 