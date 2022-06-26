// const sessionMapper = (socket, next, sessionStore) => {
//     const sessionID = socket.id;

//     if (sessionID) {      
//       const session = sessionStore.findSession(sessionID);
//       if (session) {
//         socket.sessionID = sessionID;
//         socket.userID = session.userID;
//         socket.username = session.username;
//         return next();
//       } else {
          
//       }
//     }

//     const username = socket.handshake.auth.username;
//     if (!username) {
//       return next(new Error("invalid username"));
//     }

//     // create new session
//     socket.sessionID = randomId();
//     socket.userID = randomId();
//     socket.username = username;
//     next();
// }

// module.exports = sessionMapper;
