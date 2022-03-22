package com.example.chatapp.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.databinding.ItemContainerRecentConversationBinding;
import com.example.chatapp.listeners.ConversationListener;
import com.example.chatapp.models.ChatMessage;
import com.example.chatapp.models.User;

import java.util.List;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.RecentConversationHolder>{

    private List<ChatMessage> chatMessages;
    private ConversationListener listener;

    public RecentConversationsAdapter(List<ChatMessage> chatMessages, ConversationListener listener) {
        this.chatMessages = chatMessages;
        this.listener = listener;
    }

    private Bitmap getConservationImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @NonNull
    @Override
    public RecentConversationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemContainerRecentConversationBinding binding = ItemContainerRecentConversationBinding.inflate(
                layoutInflater,
                parent,
                false);
        return new RecentConversationHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentConversationHolder holder, int position) {
        holder.bindingData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public void setConversations(List<ChatMessage> conversations) {
        chatMessages = conversations;
    }

    class RecentConversationHolder extends RecyclerView.ViewHolder {
        ItemContainerRecentConversationBinding binding;
        public RecentConversationHolder(@NonNull ItemContainerRecentConversationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindingData(ChatMessage chatMessage) {
            binding.imageProfile.setImageBitmap(getConservationImage(chatMessage.getConversationImage()));
            binding.textName.setText(chatMessage.getConversationName());
            binding.textRecentMessage.setText(chatMessage.getMessage());
            binding.getRoot().setOnClickListener(view -> {
                User user = new User();
                user.setId(chatMessage.getConversationId());
                user.setName(chatMessage.getConversationName());
                user.setImage(chatMessage.getConversationImage());
                listener.onConversationClicked(user);
            });
        }
    }
}
