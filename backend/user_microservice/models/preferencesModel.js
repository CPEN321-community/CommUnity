const PreferencesObject = (sequelize, DataTypes) => {
    const Preferences = sequelize.define("Preferences", {
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
        timestamp: false,
    });
    return Preferences;
  };

module.exports = PreferencesObject;