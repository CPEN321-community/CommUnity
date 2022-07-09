var ActiveUsers = require('./activeUsers');

class Singleton {

    constructor() {
        if (!Singleton.instance) {
            Singleton.instance = new ActiveUsers();
        }
    }
  
    getInstance() {
        return Singleton.instance;
    }
  
}
  
module.exports = Singleton;