const socketHandler = (socket) => {

    console.log("socket connection made!");
    
    socket.on('joined', function(data) {
        console.log(data);
        socket.emit('acknowledge', 'Acknowledged');
    });
    
    socket.on('chat message', function(msg){
        console.log('message: ' + msg);
        socket.emit('response message', msg + '  from server');
        //socket.broadcast.emit('response message', msg + '  from server');
    });
}

module.exports = socketHandler;
