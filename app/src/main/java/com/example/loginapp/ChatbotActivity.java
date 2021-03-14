package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatbotActivity extends AppCompatActivity {

    EditText userInput;
    RecyclerView recyclerView;
    List<ResponseMessage> responseMessageList;
    MessageAdapter messageAdapter;
    List<String> tempList =new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        userInput = findViewById(R.id.userInput);
        recyclerView = findViewById(R.id.conversation);
        responseMessageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(responseMessageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(messageAdapter);
        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    ResponseMessage userMessage = new ResponseMessage(userInput.getText().toString(), true);
                    responseMessageList.add(userMessage);
                    String userinput = userInput.getText().toString();
                    tempList.add(userinput);
                    String symptomString = tempList.get(0);

                    ResponseMessage chatbotMessage;
                    if(!userInput.getText().toString().equals("stop")) {
                        if(responseMessageList.size() == 1) {
                            chatbotMessage = new ResponseMessage("Your symptoms are: " + userInput.getText().toString() + ". Enter 'stop' if you do not have anymore symptoms to add", false);
                            responseMessageList.add(chatbotMessage);
                        }

                        else {
                            for (int i = 1; i < tempList.size(); i++) {
                                symptomString = symptomString + ", " + tempList.get(i);
                            }
                            chatbotMessage = new ResponseMessage("Your symptoms are: " + symptomString + ". Enter 'stop' if you do not have anymore symptoms to add", false);
                            responseMessageList.add(chatbotMessage);

                        }
                    }
                    else {
                        chatbotMessage = new ResponseMessage("You have entered stop.", false);
                        responseMessageList.add(chatbotMessage);
                    }

                    messageAdapter.notifyDataSetChanged();

                    if(!isVisible()){
                        recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
                    }
                    userInput.getText().clear();
                }
                return true;
            }

        });
    }


    public boolean isVisible(){
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int positionOfLastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        int itemCount = recyclerView.getAdapter().getItemCount();
        return (positionOfLastVisibleItem>=itemCount);
    }
}
