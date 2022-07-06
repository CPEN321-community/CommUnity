const LeaderboardObject = (sequelize, DataTypes) => {
    const Leaderboard = sequelize.define("Leaderboard", {
        id: {
            type: DataTypes.STRING,
            defaultValue: DataTypes.UUIDV4,
            allowNull: false,
            primaryKey: true
        },
        offerPosts: {
            type: DataTypes.INTEGER, 
            allowNull: false,
            defaultValue: 0,
        },
        requestPosts: {
            type: DataTypes.INTEGER, 
            allowNull: false,
            defaultValue: 0,
        },
        score: {
            type: DataTypes.INTEGER,
            allowNull: false,
            defaultValue: 0,
        }
    }, {
        timestamps: false,
    });
    return Leaderboard;
  };

module.exports = LeaderboardObject;