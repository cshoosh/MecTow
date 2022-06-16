package com.example.mectow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static int MSG_TYPE_LEFT=0;
    public static int MSG_TYPE_RIGHT=1;
    private Context mContext;
    private List<Chat> mChat;
    FirebaseUser fuser;
    String photo;
    public MessageAdapter(Context mContext,List<Chat> mChat)
    {
        this.mContext=mContext;
        this.mChat=mChat;
    }
    @NonNull
    @Override

    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            MessageAdapter.ViewHolder vh = new MessageAdapter.ViewHolder(view);
            return vh;
        }
        else
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            MessageAdapter.ViewHolder vh = new MessageAdapter.ViewHolder(view);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat=mChat.get(position);
        holder.show_message.setText(chat.getMessage());

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message=(TextView) itemView.findViewById(R.id.show_message);
        }
    }
    @Override
    public int getItemViewType(int position)
    {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }
}
