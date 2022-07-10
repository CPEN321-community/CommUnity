const DietaryRestrictionObject = (sequelize, DataTypes) => {
  const DietaryRestriction = sequelize.define("DietaryRestriction", {
      id: {
          type: DataTypes.STRING,
          defaultValue: DataTypes.UUIDV4,
          allowNull: false,
          primaryKey: true
      },
      name: {
          type: DataTypes.STRING, 
          allowNull: false,
      }
  }, {
      timestamps: false,
  });
  return DietaryRestriction;
};

module.exports = DietaryRestrictionObject;