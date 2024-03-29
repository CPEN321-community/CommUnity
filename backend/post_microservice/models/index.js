'use strict';

const Sequelize = require('sequelize');
const env = process.env.NODE_ENV || 'development';
const config = require("./../../config_post.json")[env];
const db = {};

function applyRelationships(sequelize) {
    const { OfferPost, OfferPostTags, RequestPost, RequestPostTags } = sequelize.models;

    OfferPost.hasMany(OfferPostTags, { foreignKey: 'offerId' });
    OfferPostTags.belongsTo(OfferPost, { foreignKey: 'offerId' });

    RequestPost.hasMany(RequestPostTags, { foreignKey: 'requestId' });
    RequestPostTags.belongsTo(RequestPost, { foreignKey: 'requestId' })
}

let sequelize;
if (config.use_env_variable) {
  sequelize = new Sequelize(process.env[config.use_env_variable], config);
} else {
  sequelize = new Sequelize(config.database, config.user, config.password, {
    host: process.env.HOST || config.host,
    port: 3306,
    logging: false,
    maxConcurrentQueries: 100,
    dialect: 'mysql',
    dialectOptions: env === "production" ? 
    {
      ssl: 'Amazon RDS'
    }
  : {
      ssl: { 
        require: true,
        rejectUnauthorized: false 
      }
    },
    pool: { maxConnections: 5, maxIdleTime: 30},
    language: 'en'
  });
}


const offerPostModel = require('./offerPostModel.js')(sequelize, Sequelize.DataTypes);
db[offerPostModel.name] = offerPostModel;
const offerPostTagsModel = require('./offerPostTagsModel.js')(sequelize, Sequelize.DataTypes);
db[offerPostTagsModel.name] = offerPostTagsModel;
const requestPostModel = require('./requestPostModel.js')(sequelize, Sequelize.DataTypes);
db[requestPostModel.name] = requestPostModel;
const requestPostTagsModel = require('./requestPostTagsModel.js')(sequelize, Sequelize.DataTypes);
db[requestPostTagsModel.name] = requestPostTagsModel;

Object.keys(db).forEach(modelName => {
  if (db[modelName].associate) {
    db[modelName].associate(db);
  }
});

applyRelationships(sequelize);

db.sequelize = sequelize;
db.Sequelize = Sequelize;

module.exports = db;