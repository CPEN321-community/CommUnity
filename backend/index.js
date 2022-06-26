const express = require('express');
const http = require('http');
const socket = require('socket.io');
const bodyParser = require('body-parser');
const routes = require('./routes');
const socketHandler = require('./socket/socketHandler');
const sessionMapper = require('./socket/sessionMapper');
// const SessionStore = require('./socket/sessionStore');

const app = express();
const server = http.Server(app);
const io = socket(server);
// const sessionStore = new SessionStore();

app.use(bodyParser.json());
app.use(routes);

// io.use((socket, next) => sessionMapper(socket, next, sessionStore))
io.on('connection', (socket) => socketHandler(socket, sessionStore));

const PORT = process.env.PORT || 3000;

server.listen(PORT, () => {
  console.log(`Server running on port http://localhost:${PORT}`);
});
