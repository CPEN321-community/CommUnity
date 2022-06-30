const LeaderboardObject = (sequelize, DataTypes) => {
    const Leaderboard = sequelize.define("Leaderboard", {
        id: {
            type: DataTypes.UUIDV4,
            defaultValue: DataTypes.UUIDV4,
            allowNull: false,
            primaryKey: true
        },
        offerPosts: {
            type: DataTypes.INTEGER, 
            allowNull: false,
        },
        requestPosts: {
            type: DataTypes.INTEGER, 
            allowNull: false,
        },
        score: {
            type: DataTypes.INTEGER,
            allowNull: false,
        }
    });
    return Leaderboard;
  };

module.exports = LeaderboardObject;