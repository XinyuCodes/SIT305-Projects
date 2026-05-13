package com.example.llmchat;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private TextInputEditText messageInput;
    private MessageAdapter adapter;
    private List<Message> messageList;
    private DatabaseManager databaseManager;
    private GeminiClient geminiClient;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Retrieve email from intent
        email = getIntent().getStringExtra("email");

        // Find views
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.newMessageBox);

        // Set up database and gemini client
        databaseManager = new DatabaseManager(this);
        geminiClient = new GeminiClient();

        // Set up RecyclerView with existing messages
        messageList = databaseManager.getMessagesByEmail(email);
        adapter = new MessageAdapter(this, messageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(adapter);

        // Listen for send action on keyboard
        messageInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String userMessage = messageInput.getText().toString().trim();
                    if (!userMessage.isEmpty()) {
                        String currentTime = getCurrentTimestamp();
                        
                        // Add user message to list and database
                        messageList.add(new Message(0, email, userMessage, "user", currentTime));
                        databaseManager.insertMessage(email, userMessage, "user");
                        adapter.notifyItemInserted(messageList.size() - 1);

                        // Clear input and scroll to bottom
                        messageInput.setText("");
                        chatRecyclerView.scrollToPosition(messageList.size() - 1);

                        // Call Gemini
                        geminiClient.sendMessage(userMessage, new GeminiClient.GeminiCallback() {
                            @Override
                            public void onResponse(String response) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String responseTime = getCurrentTimestamp();
                                        messageList.add(new Message(0, email, response, "gemini", responseTime));
                                        databaseManager.insertMessage(email, response, "gemini");
                                        adapter.notifyItemInserted(messageList.size() - 1);
                                        chatRecyclerView.scrollToPosition(messageList.size() - 1);
                                    }
                                });
                            }
                            @Override
                            public void onError(String error) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ChatActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private String getCurrentTimestamp() {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }
}
