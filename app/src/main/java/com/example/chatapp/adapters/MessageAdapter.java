package com.example.chatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;
import com.example.chatapp.model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView civProfileImage;
        public TextView tvMessage;
        public TextView tvSeenMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            civProfileImage = itemView.findViewById(R.id.civProfileImage);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvSeenMessage = itemView.findViewById(R.id.tvSeenMessage);
        }
    }

    public static final int MESSAGE_TYPE_LEFT = 0;
    public static final int MESSAGE_TYPE_RIGHT = 1;

    private Context context;
    private List<Chat> chats;
    private String imageUrl;

    private FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Chat> chats, String imageUrl) {
        this.context = context;
        this.chats = chats;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_TYPE_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = chats.get(position);

        holder.tvMessage.setText(chat.getMessage());

        if (imageUrl.equals("default"))
            holder.civProfileImage.setImageResource(R.mipmap.ic_launcher);
        else
            Glide.with(context)
            .load(imageUrl)
            .into(holder.civProfileImage);

        if (position == chats.size() - 1) {
            if (chat.isSeen()) {
                holder.tvSeenMessage.setText("seen");
            } else {
                holder.tvSeenMessage.setText("delivered");
            }
        } else {
            holder.tvSeenMessage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (chats.get(position).getSender().equals(firebaseUser.getUid()))
            return MESSAGE_TYPE_RIGHT;
        else
            return MESSAGE_TYPE_LEFT;
    }
}
