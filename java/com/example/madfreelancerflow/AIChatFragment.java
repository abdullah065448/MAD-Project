package com.example.madfreelancerflow;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AIChatFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private EditText edtMessage;
    private ImageButton btnSend;
    private AIChatAdapter adapter;
    private List<AIChatMessage> messageList;
    private FirebaseFirestore db;

    // Replace with your actual Google Cloud API Key
    private static final String API_KEY = "AIzaSyCVMz0vVjwCdbnXFpVugVWy5Rh7fS6ox2g";
    private static final String GEMINI_URL ="https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key="+ API_KEY;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ai_chat, container, false);

        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        edtMessage = view.findViewById(R.id.edt_message);
        btnSend = view.findViewById(R.id.btn_send);

        messageList = new ArrayList<>();
        adapter = new AIChatAdapter(getContext(), messageList);

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRecyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        btnSend.setOnClickListener(v -> {
            String message = edtMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                edtMessage.setText("");
            }
        });

        return view;
    }

    private void sendMessage(String userText) {
        long timestamp = System.currentTimeMillis();
        AIChatMessage userMessage = new AIChatMessage(userText, true, timestamp);

        // Add user message to UI
        messageList.add(userMessage);
        adapter.notifyItemInserted(messageList.size() - 1);
        chatRecyclerView.scrollToPosition(messageList.size() - 1);

        // Save user message to Firebase
        db.collection("chatAI").add(userMessage);

        try {
            // Build Gemini request body
            JSONObject requestBody = new JSONObject();
            JSONObject prompt = new JSONObject();
            JSONArray messages = new JSONArray();
            JSONObject userMsgObj = new JSONObject();

            userMsgObj.put("author", "user");

            JSONArray contentArray = new JSONArray();
            JSONObject textContent = new JSONObject();
            textContent.put("type", "text");
            textContent.put("text", userText);
            contentArray.put(textContent);

            userMsgObj.put("content", contentArray);
            messages.put(userMsgObj);
            prompt.put("messages", messages);
            requestBody.put("prompt", prompt);
            requestBody.put("temperature", 0.7); // Optional
            requestBody.put("candidate_count", 1); // Optional

            // Create Volley request
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, GEMINI_URL, requestBody,
                    response -> {
                        Log.d("GEMINI_RESPONSE", response.toString());
                        try {
                            JSONArray candidates = response.optJSONArray("candidates");
                            if (candidates != null && candidates.length() > 0) {
                                JSONObject firstCandidate = candidates.getJSONObject(0);
                                JSONArray parts = firstCandidate.getJSONObject("content")
                                        .optJSONArray("parts");
                                if (parts != null && parts.length() > 0) {
                                    String botReply = parts.getJSONObject(0).optString("text", "No response");

                                    AIChatMessage botMessage = new AIChatMessage(botReply, false, System.currentTimeMillis());
                                    messageList.add(botMessage);
                                    adapter.notifyItemInserted(messageList.size() - 1);
                                    chatRecyclerView.scrollToPosition(messageList.size() - 1);

                                    // Save bot message to Firebase
                                    db.collection("chatAI").add(botMessage);
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("GEMINI_PARSE", "Error parsing response: " + e.getMessage());
                            Toast.makeText(getContext(), "Parsing error", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        String errMsg = "Connection failed";
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            errMsg = new String(error.networkResponse.data);
                        }
                        Log.e("GEMINI_API_ERROR", errMsg);
                        Toast.makeText(getContext(), "AI Connection failed. Check Logcat.", Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + API_KEY);
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            Volley.newRequestQueue(requireContext()).add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
