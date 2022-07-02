const { getAssociatedRooms, createRoom, sendMessage, getMessageFromSocket, addMessageToSocket } = require('../controllers/chatController');

const socketHandler = (socket) => {
    console.log("socket connection made with id: " + socket.id);
    
    socket.on('join_rooms', async userId => {
        const postIds = await getAssociatedRooms(userId);
        console.log('joining rooms: ', postIds);
        
        if (postIds && postIds.length > 0) {
            socket.join(postIds);
            socket.emit('join_rooms', 'join_rooms_success');
        } else {
            socket.emit('join_rooms', 'join_rooms_failed');
        }
    });

    socket.on('create_room', async roomDto => {
        const isCreated = await createRoom(roomDto);

        if (isCreated) {
            socket.join(roomDto.postId);
            socket.emit('create_room', 'create_room_success')
        } else {
            socket.emit('create_room', 'create_room_failure')
        }
    });

    socket.on('send_message', async ({ message, userId, postId }) => {
        const { msgObj, isSent } = await sendMessage(message, userId, postId);

        if (isSent) {
            socket.to(postId).emit('send_message', msgObj);
        } else {
            socket.to(postId).emit('send_message', 'send_message_failure')
        }
    });
}

module.exports = socketHandler;

