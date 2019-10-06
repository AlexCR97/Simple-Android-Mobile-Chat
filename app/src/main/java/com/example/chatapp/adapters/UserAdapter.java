package com.example.chatapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.R;
import com.example.chatapp.activities.MessageActivity;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView civProfileImage;
        private TextView tvUsername;
        private CircleImageView civStatusOnline;
        private CircleImageView civStatusOffline;
        private TextView tvLastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            civProfileImage = itemView.findViewById(R.id.civProfileImage);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            civStatusOnline = itemView.findViewById(R.id.civStatusOnline);
            civStatusOffline = itemView.findViewById(R.id.civStatusOffline);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
        }
    }

    private Context context;
    private List<User> users;
    private boolean isOnline;

    private String lastMessage;

    public UserAdapter(Context context, List<User> users, boolean isOnline) {
        this.context = context;
        this.users = users;
        this.isOnline = isOnline;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = users.get(position);

        holder.tvUsername.setText(user.getUsername());

        if (user.getImageUrl().equals("default")) {
            holder.civProfileImage.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context)
                    .load(user.getImageUrl())
                    .into(holder.civProfileImage);
        }

        if (isOnline) {
            lastMessage(user.getId(), holder.tvLastMessage);
        } else {
            holder.tvLastMessage.setVisibility(View.GONE);
        }

        if (isOnline) {
            if (user.getStatus().equals("online")) {
                holder.civStatusOnline.setVisibility(View.VISIBLE);
                holder.civStatusOffline.setVisibility(View.GONE);
            } else {
                holder.civStatusOnline.setVisibility(View.GONE);
                holder.civStatusOffline.setVisibility(View.VISIBLE);
            }
        } else {
            holder.civStatusOnline.setVisibility(View.GONE);
            holder.civStatusOffline.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userId", user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    private void lastMessage(final String userId, final TextView tvLastMessage) {
        lastMessage = "default";

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);

                    assert chat != null;
                    assert firebaseUser != null;
                    if (chat.getReciever().equals(firebaseUser.getUid()) && chat.getSender().equals(userId) ||
                        chat.getReciever().equals(userId) && chat.getSender().equals(firebaseUser.getUid()))
                    {
                        lastMessage = chat.getMessage();
                    }
                }

                switch (lastMessage) {
                    case "default": {
                        tvLastMessage.setText("No messages");
                        break;
                    }

                    default: {
                        tvLastMessage.setText(lastMessage);
                        break;
                    }
                }

                lastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
