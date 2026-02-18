package com.example.madfreelancerflow;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.*;

public class ChatHelper {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Send Message
    public void sendMessage(String chatId,
                            MessageModel message,
                            OnCompleteListener<DocumentReference> listener) {

        db.collection("chatMessages")
                .document(chatId)
                .collection("Messages")
                .add(message)
                .addOnCompleteListener(listener);
    }

    // Listen for Messages
    public void listenForMessages(String chatId,
                                  EventListener<QuerySnapshot> listener) {

        db.collection("Chats")
                .document(chatId)
                .collection("Messages")
                .orderBy("timestamp")
                .addSnapshotListener(listener);
    }
}
