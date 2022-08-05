package com.example.community.classes;

import com.example.community.ui.chat.ChatAdapter;
import com.example.community.ui.chat.message.MessageAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatMessageHandlerUtil {
    private static ChatAdapter chatAdapter;
    private static final String TAG = "CHAT_MESSAGE_HANDLER_CLASS";
    private static final HashMap<String, ArrayList<Message>> chatMap = new HashMap<>();
    private static final HashMap<String, MessageAdapter> adapterHashMap = new HashMap<>();


    public static void AddRoom(String roomId) {
        chatMap.put(roomId, new ArrayList<>());
    }

//    public static void AddAdapter(String roomId, MessageAdapter adapter) {
//        adapterHashMap.put(roomId, adapter);
//        chatMap.putIfAbsent(roomId, new ArrayList<>());
//        Log.d(TAG, "AddAdapter: " + adapter.getCount());
//        Log.d(TAG, "AddAdapter: " + chatMap.get(roomId));
////        adapter.AddMessages(chatMap.get(roomId));
//        adapter.notifyDataSetChanged();
//    }

//    public static void AddMessage(String roomId, Message message) {
//        if (!chatMap.containsKey(roomId)) return;
//        if (adapterHashMap.containsKey(roomId)) {
//            adapterHashMap.get(roomId).AddMessage(message);
//        } else {
//            chatMap.get(roomId).add(message);
//        }
//        chatAdapter.notifySelf();
//    }
//
//    public static Message GetPreview(String roomId) throws NoMessagesException {
//        try {
//            if (!adapterHashMap.containsKey(roomId)) {
//                ArrayList<Message> messages = chatMap.getOrDefault(roomId, new ArrayList<>());
//                return messages.get(messages.size() - 1);
//            } else {
//                return adapterHashMap.get(roomId).getLastMessage();
//            }
//        } catch (ArrayIndexOutOfBoundsException e) {
//            throw new NoMessagesException();
//        }
//    }
//
//    public static HashMap<String, ArrayList<Message>> getChatMap() {
//        return chatMap;
//    }
//
//    public static void SetChatMap(HashMap<String, ArrayList<Message>> chatMap) {
//        ChatMessageHandlerUtil.chatMap = chatMap;
//    }
//
//    public static HashMap<String, MessageAdapter> GetAdapterHashMap() {
//        return adapterHashMap;
//    }
//
//    public static ChatAdapter getChatAdapter() {
//        return chatAdapter;
//    }
//
//    public static void setChatAdapter(ChatAdapter chatAdapter) {
//        ChatMessageHandlerUtil.chatAdapter = chatAdapter;
//    }
//
//    public static void cleanup() {
//        chatAdapter = null;
//        chatMap = new HashMap<>();
//        adapterHashMap = new HashMap<>();
//    }
}
