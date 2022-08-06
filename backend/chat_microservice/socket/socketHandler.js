const {
  getAssociatedRooms,
  createRoom,
  sendMessage,
} = require("../controllers/chatController");
const Singleton = require("../singleton");

const Sockets = {};

const socketHandler = async (socket, io) => {
  var handshakeData = socket.request;
  var myUserId = handshakeData._query["auth_token"];
  Sockets[myUserId] = socket;
  const ActiveUsers = new Singleton().getInstance();
  ActiveUsers.set.add(myUserId);
  const postIds = await getAssociatedRooms(myUserId);
  console.log("joining rooms: ", postIds);
  if (postIds && postIds.length >= 0) {
    socket.join(postIds);
    ActiveUsers.set.add(myUserId);
  }

  console.log("socket connection made with id: " + socket.id);

  socket.on("createRoom", async ({ postId, isOffer, senderData }) => {
    console.log(postId, isOffer, senderData);
    let room;
    try {
      room = await createRoom(postId, isOffer, senderData);
    } catch (e) {
      console.log(e.data);
      room = false;
    }

    if (room) {
      socket.join(postId);
      const reciever = room.reciever;
      if (Sockets[reciever]) {
        let other = Sockets[reciever];
        other.join(postId);
        const {
          senderFirstName,
          senderLastName,
          senderProfilePicture,
          receiverFirstName,
          receiverLastName,
          recieverProfilePicture,
        } = room;
        const inverse = {
          postId,
          senderFirstName: receiverFirstName || "",
          senderLastName: receiverLastName || "",
          senderProfilePicture: recieverProfilePicture || "",
          receiverFirstName: senderFirstName || "",
          receiverLastName: senderLastName || "",
          receiverProfilePicture: senderProfilePicture || "",
          messages: [],
        };
        other.emit("createRoom", inverse);
      }
      socket.emit("createRoom", room);
    } else {
      socket.emit("createRoom", null);
    }
  });

  socket.on("sendMessage", async ({ message, userId, postId }) => {
    const msgObj = await sendMessage(message, userId, postId);

    io.to(postId).emit("sendMessage", msgObj);
  });

  socket.on("disconnect", async () => {
    console.log("socket disconnected with id: " + socket.id);
    ActiveUsers.set.delete(myUserId);
    Sockets[myUserId] = null;
  });
};

module.exports = socketHandler;
