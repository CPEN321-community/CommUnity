const UserTokenObj = (sequelize, DataTypes) => {
  const UserToken = sequelize.define("UserToken", {
    token: {
      type: DataTypes.STRING,
      allowNull: false
    }
  });
  
  return UserToken;
}

module.exports = UserTokenObj;