package com.example.loginapp.Control;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.Entity.ResponseMessage;
import com.example.loginapp.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.CustomViewHolder> {

    private OnNoteListener onNoteListener;

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        OnNoteListener onNoteListener;

        public CustomViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.textMessage);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }
    public interface OnNoteListener{
        void onNoteClick(int position);

    }

    List<ResponseMessage> responseMessageList;

    public MessageAdapter(List<ResponseMessage> responseMessageList, OnNoteListener onNoteListener) {
        this.responseMessageList = responseMessageList;
        this.onNoteListener = onNoteListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (responseMessageList.get(position).isUser()) {
            return R.layout.user_bubble;
        }
        return R.layout.chatbot_bubble;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false), onNoteListener);

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.CustomViewHolder holder, int position) {
        holder.textView.setText(Html.fromHtml(responseMessageList.get(position).getTextMessage()));
    }

    @Override
    public int getItemCount() {
        return responseMessageList.size();
    }
}
