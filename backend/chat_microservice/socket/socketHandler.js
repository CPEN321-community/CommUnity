const { getAssociatedRooms, createRoom, sendMessage } = require('../controllers/chatController');
const Singleton = require('../singleton');

const socketHandler = (socket, io) => {
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

    socket.on('createRoom', async ({postId, isOffer, senderData}) => {
        console.log("Hello world!******");
        console.log(postId, isOffer, senderData);
        const isCreated = await createRoom(postId, isOffer, senderData);

        if (isCreated) {
            socket.join(postId);
            socket.emit('createRoom', 'create_room_success')
        } else {
            socket.emit('createRoom', 'create_room_failure')
        }
    });

    socket.on('sendMessage', async ({ message, userId, postId }) => {
        const msgObj = await sendMessage(message, userId, postId);

        io.to(postId).emit('sendMessage', msgObj);
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

