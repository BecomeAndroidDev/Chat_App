package com.example.chatapp.adapters;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.databinding.ItemContainerReceivedMessageBinding;
import com.example.chatapp.databinding.ItemContainerSentMessageBinding;
import com.example.chatapp.models.ChatMessage;

import java.util.List;

//public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.SentMessageHolder>{
//    private final String TAG = "ChatAdapter";
//    private List<ChatMessage> chatMessages;
//    private Bitmap receiverProfileImage;
//    private String senderId;
//
//    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receiverProfileImage, String senderId) {
//        this.chatMessages = chatMessages;
//        this.receiverProfileImage = receiverProfileImage;
//        this.senderId = senderId;
//    }
//
//    @NonNull
//    @Override
//    public SentMessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        ItemContainerSentMessageBinding binding = ItemContainerSentMessageBinding.inflate(
//                layoutInflater, parent, false);
//        return new SentMessageHolder(binding);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SentMessageHolder holder, int position) {
//        holder.bindingData(chatMessages.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return chatMessages.size();
//    }
//
//
//    public void setChatMessages(List<ChatMessage> chatMessages) {
//        this.chatMessages = chatMessages;
//    }
//
//    static class SentMessageHolder extends RecyclerView.ViewHolder {
//        ItemContainerSentMessageBinding binding;
//        public SentMessageHolder(@NonNull ItemContainerSentMessageBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//
//        public void bindingData(ChatMessage chatMessage) {
//            binding.textMessage.setText(chatMessage.getMessage());
//            binding.textDateTime.setText(chatMessage.getDatetime());
//        }
//    }
//
//    static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
//        ItemContainerReceivedMessageBinding binding;
//        public ReceivedMessageHolder(@NonNull ItemContainerReceivedMessageBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//
//        public void bindingData(ChatMessage chatMessage, Bitmap receiverProfileImage) {
//            binding.textMessage.setText(chatMessage.getMessage());
//            binding.textDateTime.setText(chatMessage.getDatetime());
//            binding.imageProfile.setImageBitmap(receiverProfileImage);
//        }
//    }
//
//}
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final String TAG = "ChatAdapter";
    private List<ChatMessage> chatMessages;
    private Bitmap receiverProfileImage;
    private String senderId;

    public static int VIEW_TYPE_SENT = 1;
    public static int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receiverProfileImage, String senderId) {
        this.chatMessages = chatMessages;
        this.receiverProfileImage = receiverProfileImage;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType == VIEW_TYPE_SENT) {
            ItemContainerSentMessageBinding binding = ItemContainerSentMessageBinding.inflate(
                    layoutInflater, parent, false);
            return new SentMessageHolder(binding);
        }
        else {
            ItemContainerReceivedMessageBinding binding = ItemContainerReceivedMessageBinding.inflate(
                    layoutInflater, parent, false);
            return new ReceivedMessageHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageHolder) holder).bindingData(chatMessages.get(position));
        }
        else {
            ((ReceivedMessageHolder) holder).bindingData(chatMessages.get(position), receiverProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        if(senderId.equals(chatMessage.getSenderId())) {
            return VIEW_TYPE_SENT;
        }

        return VIEW_TYPE_RECEIVED;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    static class SentMessageHolder extends RecyclerView.ViewHolder {
        ItemContainerSentMessageBinding binding;
        public SentMessageHolder(@NonNull ItemContainerSentMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindingData(ChatMessage chatMessage) {
            binding.textMessage.setText(chatMessage.getMessage());
            binding.textDateTime.setText(chatMessage.getDatetime());
        }
    }

    static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        ItemContainerReceivedMessageBinding binding;
        public ReceivedMessageHolder(@NonNull ItemContainerReceivedMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindingData(ChatMessage chatMessage, Bitmap receiverProfileImage) {
            binding.textMessage.setText(chatMessage.getMessage());
            binding.textDateTime.setText(chatMessage.getDatetime());
            binding.imageProfile.setImageBitmap(receiverProfileImage);
        }
    }

}
