package com.example.chatapp.listeners;

import com.example.chatapp.models.ChatMessage;
import com.example.chatapp.models.User;

public interface ConversationListener {
    public void onConversationClicked(User user);
}
