const { getAssociatedRooms, createRoom, sendMessage } = require('../controllers/chatController');
const Singleton = require('./singleton');

const socketHandler = (socket) => {
    const ActiveUsers = (new Singleton()).getInstance();
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

        if (isSent) {
            socket.to(postId).emit('sendMessage', msgObj);
        } else {
            socket.to(postId).emit('sendMessage', 'send_message_failure')
        }
    });

    socket.on('leave-all', async ({ userId }) => {
        const postIds = await getAssociatedRooms(userId);
        console.log('leaving rooms: ', postIds);
        
        if (postIds && postIds.length > 0) {
            socket.leave(postIds);
            ActiveUsers.set.delete(userId);
        }
    });
}

module.exports = socketHandler;

