class ISessionStore {
    findSession(id) {}
    saveSession(id, session) {}
    findAllSessions() {}
}

class SessionStore extends ISessionStore {
    constructor() {
      super();
      this.rooms = new Map(); // { posterId: [userId1, userId2] }
    }
  
    findSession(posterId) {
      return this.rooms.get(posterId);
    }
  
    saveSession(userId, session) {
      this.rooms.set(userId, session);
    }
  
    findAllSessions() {
      return [...this.rooms.values()];
    }
}
  
module.exports = SessionStore;