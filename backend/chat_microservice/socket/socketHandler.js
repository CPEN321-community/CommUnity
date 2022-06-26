const socketHandler = (socket) => {
    console.log("socket connection made with id: " + socket.id);
    
    socket.on('join_room', (userId) => {
        // get all postIds userId is in
        
        const postIds = [];
        socket.join(postIds);
        
        
        console.log("Room successfully joined!");
    });

    socket.on('send_message', (msgDto) => {
        // example msgDto
        // {
        //     message: string(“CPEN 321 rocks!”),
        //     sender: string(“userID1”),
        //     receiver: string(“userID2”),
        //     timestamp: long(123456789),
        //     postId: string("123"),
        // }
        
        // save to mysql

    });
}

module.exports = socketHandler;
