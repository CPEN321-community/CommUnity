const express = require('express');
const http = require('http');
const socket = require('socket.io');
const bodyParser = require('body-parser');
const routes = require('./routes');
const socketHandler = require('./socket');

const app = express();
const server = http.Server(app);
const io = socket(server);

app.use(bodyParser.json());
app.use(routes);

app.get('/', (req, res) => res.sendFile(__dirname + '/chat.html'));

io.on('connection', (socket) => socketHandler(socket));

server.listen(8080, function() {
  console.log('Server running on port %d', server.address().port);
});
