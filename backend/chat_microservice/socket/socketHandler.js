const Singleton = require('./singleton');
const ActiveUsers = (new Singleton()).getInstance();
const { getAssociatedRooms, createRoom, sendMessage, getMessageFromSocket, addMessageToSocket } = require('../controllers/chatController');

const socketHandler = (socket, io) => {
    console.log("socket connection made with id: " + socket.id);
    
    socket.on('joinRooms', async ({ userId }) => {
        const postIds = await getAssociatedRooms(userId);
        console.log('joining rooms: ', postIds);
        
        if (postIds && postIds.length > 0) {
            socket.join(postIds);
            socket.emit('joinRooms', 'join_rooms_success');
            ActiveUsers.set.add(userId);
        } else {
            socket.emit('joinRooms', 'join_rooms_failed');
        }
    });

    socket.on('createRoom', async roomDto => {
        const isCreated = await createRoom(roomDto);

        if (isCreated) {
            socket.join(roomDto.postId);
            socket.emit('createRoom', 'create_room_success')
        } else {
            socket.emit('createRoom', 'create_room_failure')
        }
    });

    socket.on('sendMessage', async ({ message, userId, postId }) => {
        const { msgObj, isSent } = await sendMessage(message, userId, postId);
        console.log(msgObj);

        if (isSent) {
            io.to(postId).emit('sendMessage', msgObj);
            // socket.in(postId).emit('sendMessage', msgObj);
        } else {
            socket.emit('sendMessage', 'send_message_failure')
        }
    });
}

module.exports = socketHandler;

