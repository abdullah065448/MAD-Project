package com.example.madfreelancerflow;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private RecyclerView recyclerView;
    private EditText edtMessage;
    private Button btnSend;

    private ArrayList<MessageModel> messageList;
    private ChatAdapter adapter;

    private String currentUserId;
    private String currentUserName;

    public ChatFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind views
        recyclerView = view.findViewById(R.id.recyclerMessages);
        edtMessage = view.findViewById(R.id.edtMessage);
        btnSend = view.findViewById(R.id.btnSend);

        // Setup RecyclerView
        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Load user + messages
        getCurrentUser();
        listenMessages();

        // Send button
        btnSend.setOnClickListener(v -> sendMessage());

        return view;
    }

    // ✅ Get current user data
    private void getCurrentUser() {

        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;

        currentUserId = user.getUid();

        db.collection("users")
                .document(currentUserId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        currentUserName = snapshot.getString("email");
                    }
                });
    }

    // ✅ Send message
    private void sendMessage() {

        String text = edtMessage.getText().toString().trim();

        if (TextUtils.isEmpty(text)) return;

        MessageModel message = new MessageModel(
                currentUserId,
                currentUserName,
                text,
                System.currentTimeMillis()
        );

        db.collection("chatMessages")
                .add(message)
                .addOnSuccessListener(documentReference -> {
                    edtMessage.setText("");
                })
                .addOnFailureListener(Throwable::printStackTrace);
    }

    // ✅ Listen messages in real-time
    private void listenMessages() {

        db.collection("chatMessages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {

                    if (error != null || value == null) return;

                    messageList.clear();

                    for (DocumentSnapshot doc : value.getDocuments()) {
                        MessageModel msg = doc.toObject(MessageModel.class);
                        messageList.add(msg);
                    }

                    adapter.notifyDataSetChanged();

                    if (messageList.size() > 0) {
                        recyclerView.scrollToPosition(messageList.size() - 1);
                    }
                });
    }
}
