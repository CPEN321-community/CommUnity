class IActiveUsers {
    findSession(id) {}
    saveSession(id, session) {}
    findAllSessions() {}
}

class ActiveUsers extends IActiveUsers {
    constructor() {
      super();
      this.set = new Set();
    }
}
  
module.exports = ActiveUsers;