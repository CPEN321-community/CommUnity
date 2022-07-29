"use strict";

const Sequelize = require("sequelize");
const env = process.env.NODE_ENV || "development";
const config = require("./../config.json")[env];
const db = {};

function applyRelationships(sequelize) {
  const { User, Leaderboard, Preference } = sequelize.models;

  Leaderboard.User = Leaderboard.belongsTo(User, {
    foreignKey: {
      field: "userId",
    }
  });
  User.Leaderboard = User.hasOne(Leaderboard, {
   foreignKey: {
      allowNull: false,
      field: "userId",
    },
    as: "leaderboard",
    onDelete: "cascade",
  });

  Preference.User = Preference.belongsTo(User, {
    foreignKey: {
      field: "userId",
      as: "user",
    }
  });
  User.Preferences = User.hasMany(Preference, {
    foreignKey: {
      field: "userId",
      as: "user"
    },
    onDelete: "cascade",
    as: "preferences",
  });
}

console.log(config);

let sequelize;
if (config.use_env_variable) {
  sequelize = new Sequelize(process.env[config.use_env_variable], config);
} else {
  sequelize = new Sequelize(config.database, config.user, config.password, {
    host:  process.env.HOST || config.host,
    port: 3306,
    logging: false,
    maxConcurrentQueries: 100,
    dialect: 'mysql',
   dialectOptions: env === "development" ? 
   undefined
: {
        ssl:'Amazon RDS'
    },
    pool: { maxConnections: 5, maxIdleTime: 30},
    language: 'en'
  });
}

const leaderboardModel = require('./leaderboardModel.js')(sequelize, Sequelize.DataTypes);
db[leaderboardModel.name] = leaderboardModel;
const preferencesModel = require('./preferencesModel.js')(sequelize, Sequelize.DataTypes);
db[preferencesModel.name] = preferencesModel;
const userModel = require('./userModel.js')(sequelize, Sequelize.DataTypes);
db[userModel.name] = userModel;

Object.keys(db).forEach((modelName) => {
  if (db[modelName].associate) {
    db[modelName].associate(db);
  }
});

applyRelationships(sequelize);

db.sequelize = sequelize;
db.Sequelize = Sequelize;

module.exports = db;
