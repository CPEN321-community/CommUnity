var Model = require('./model');

class Singleton {

    constructor() {
        if (!Singleton.instance) {
            Singleton.instance = new Model();
        }
    }
  
    getInstance() {
        return Singleton.instance;
    }
  
}
  
module.exports = Singleton;