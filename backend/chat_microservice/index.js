const express = require('express');
const http = require('http');
const socket = require('socket.io');
const bodyParser = require('body-parser');
const routes = require('./routes');
const socketHandler = require('./socket/socketHandler');
const db = require('./models');

const app = express();
const server = http.Server(app);
const io = socket(server);

io.on('connection', (socket) => socketHandler(socket, io));
app.use(bodyParser.json());
app.use(routes);

db.sequelize.sync().then((req) => {
}).catch(e => console.log(e));

const PORT = process.env.PORT || 3000;

server.listen(PORT, () => {
  console.log(`Node.js Server running on port ${PORT}`);
});
