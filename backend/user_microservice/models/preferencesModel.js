const PreferenceObject = (sequelize, DataTypes) => {
    const Preference = sequelize.define("Preference", {
        id: {
            type: DataTypes.STRING,
            defaultValue: DataTypes.UUIDV4,
            allowNull: false,
            primaryKey: true
        },
        type: {
            type: DataTypes.STRING, 
            allowNull: false,
        },
        value: {
            type: DataTypes.STRING, 
            allowNull: false,
        }
    }, {
        timestamps: false,
    });
    return Preference;
  };

module.exports = PreferenceObject;