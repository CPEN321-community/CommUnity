'use strict';

const Sequelize = require('sequelize');
const env = process.env.NODE_ENV || 'development';
const config = require("./../config/config.json")[env];
const db = {};

function applyRelationships(sequelize) {
	const { User, Message, Room, UserToken } = sequelize.models;

  User.hasMany(Message, { foreignKey: 'userId' });
  Message.belongsTo(User, { foreignKey: 'userId' });

  User.hasMany(Room, { foreignKey: 'userId' });
  Room.belongsTo(User, { foreignKey: 'userId' });

  User.hasOne(UserToken, { foreignKey: 'userId' });
  UserToken.belongsTo(User, { foreignKey: 'userId' });
}

let sequelize;
if (config.use_env_variable) {
  sequelize = new Sequelize(process.env[config.use_env_variable], config);
} else {
  sequelize = new Sequelize(config.database, config.user, config.password, {
    host: process.env.HOST || config.host,
    port: 3306,
    logging: console.log,
    maxConcurrentQueries: 100,
    dialect: 'mysql',
    dialectOptions: env === "development"
? undefined
: {
        ssl:'Amazon RDS'
    },
    pool: { maxConnections: 5, maxIdleTime: 30},
    language: 'en'
  });
}

const messageModel = require('./message.js')(sequelize, Sequelize.DataTypes);
db[messageModel.name] = messageModel;
const roomModel = require('./room.js')(sequelize, Sequelize.DataTypes);
db[roomModel.name] = roomModel;
const tokenModel = require('./token.js')(sequelize, Sequelize.DataTypes);
db[tokenModel.name] = tokenModel;
const userModel = require('./user.js')(sequelize, Sequelize.DataTypes);
db[userModel.name] = userModel;

Object.keys(db).forEach(modelName => {
  if (db[modelName].associate) {
    db[modelName].associate(db);
  }
});

applyRelationships(sequelize);

db.sequelize = sequelize;
db.Sequelize = Sequelize;

module.exports = db;
