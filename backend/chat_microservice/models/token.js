const UserTokenObj = (sequelize, DataTypes) => {
  const UserToken = sequelize.define("UserToken", {
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
  
  return UserToken;
}

module.exports = UserTokenObj;