'use strict';

const fs = require('fs');
const path = require('path');
const Sequelize = require('sequelize');
const basename = path.basename(__filename);
const env = process.env.NODE_ENV || 'development';
const config = require(__dirname + '/../config/config.json')[env];
const db = {};

function applyRelationships(sequelize) {
    const { OfferPost, OfferPostTags, RequestPost, RequestPostTags, User} = sequelize.models;

    OfferPost.hasMany(OfferPostTags, {as: "offerTags"});
    OfferPostTags.belongsTo(OfferPost, {
      foreignKey: "offerId",
      as: "offerPost",
    });

    RequestPost.hasMany(RequestPostTags, {as: "requestTags"});
    RequestPostTags.belongsTo(RequestPost, {
      foreignKey: "requestId",
      as: "requestPost",
    });

    // User.hasMany(RequestPost, {as: "requestPosts"});
    // RequestPost.belongsTo(User, {
    //   foreignKey: "userId",
    //   as: "user",
    // });

    // User.hasMany(OfferPost, {as: "offerPosts"});
    // OfferPost.belongsTo(User, {
    //   foreignKey: "userId",
    //   as: "user",
    // })
}

let sequelize;
if (config.use_env_variable) {
  sequelize = new Sequelize(process.env[config.use_env_variable], config);
} else {
  sequelize = new Sequelize(config.database, config.username, config.password, config);
}

fs
  .readdirSync(__dirname)
  .filter(file => {
    return (file.indexOf('.') !== 0) && (file !== basename) && (file.slice(-3) === '.js');
  })
  .forEach(file => {
    const model = require(path.join(__dirname, file))(sequelize, Sequelize.DataTypes);
    db[model.name] = model;
  });

Object.keys(db).forEach(modelName => {
  if (db[modelName].associate) {
    db[modelName].associate(db);
  }
});

applyRelationships(sequelize);

db.sequelize = sequelize;
db.Sequelize = Sequelize;

module.exports = db;