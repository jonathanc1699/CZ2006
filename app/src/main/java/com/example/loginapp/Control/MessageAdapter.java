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

/**
 * This class implements the MessageAdapter controller which will a the backbone of the chatbot function
 * It creates the UI for the chatbot to work, takes the messages and
 * displays them along with the chat bubbles and ensure that they're on the correct side
 *
 *@author Goh Shan Ying, Jonathan Chang, Lee Xuanhui, Luke Chin Peng Hao, Lynn Masillamoni, Russell Leung
 */

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

    /**
     *
     * @param responseMessageList  list of messages sent by the user and chatbot to be displayed in the chat
     * @param onNoteListener check if the send button in the keyboard was pressed then can get what the user typed in
     */
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

    /**
     *creates the layout of the chat when the chatbot is first opened
     *
     * @param parent parent of view group
     * @param viewType view type
     * @return layout of chat
     */
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false), onNoteListener);

    }

    /**
     *binds the messages to be displayed in the chatbot to the chat
     *
     * @param holder position of the message to be displayed in the responseMessage list
     * @param position position to get text message
     */
    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.CustomViewHolder holder, int position) {
        holder.textView.setText(Html.fromHtml(responseMessageList.get(position).getTextMessage()));
    }

    /**
     * get the number of messages to be displayed
     * @return size of response message list
     */
    @Override
    public int getItemCount() {
        return responseMessageList.size();
    }
}
