package com.example.loginapp.Boundary;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.Control.ChatbotController;
import com.example.loginapp.Control.MessageAdapter;
import com.example.loginapp.Entity.ResponseMessage;
import com.example.loginapp.Entity.Chatstats;
import com.example.loginapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatbotActivity extends AppCompatActivity implements MessageAdapter.OnNoteListener {

    EditText userInput;
    RecyclerView recyclerView;
    List<ResponseMessage> responseMessageList;
    MessageAdapter messageAdapter;
    List<String> tempList =new ArrayList<String>(); //user's valid symptoms that have been verified against db
    //private static ArrayList<String> diseaseDB =new ArrayList<String>();
    private static HashMap<String, ArrayList<String>> myMap = new HashMap<String, ArrayList<String>>(); // hashmap created.
    private static ArrayList<String> allsymptoms = new ArrayList<String>(); // contains all 15 symptoms possible.
    private static ArrayList<String> alldisease=new ArrayList<String>(); // contains all possible diseases
    private String clinic = "null";
    private ChatbotController cb= new ChatbotController();

    private FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    private CollectionReference diseaseRef = fstore.collection("infectdisease");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        createHashMap();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        userInput = findViewById(R.id.userInput);
        recyclerView = findViewById(R.id.conversation);
        responseMessageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(responseMessageList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(messageAdapter);

        sendMessage("Welcome to the infectious disease bot! Tap on the chat field to input your symptoms", false);
        sendMessage("List of possible symptoms: fatigue, nausea, swollen glands, rash, headache, abdominal pain, appetite loss, fever, dark urine, joint pain, jaundice, flu, diarrhea, cough, red eyes.", false);


        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    sendMessage(userInput.getText().toString(), true);
                    String userinput = userInput.getText().toString();


                    if((!userInput.getText().toString().equals("stop")) && (allsymptoms.contains(userinput))&& (!tempList.contains(userinput)))  {
                        tempList.add(userinput);
                        String symptomString = tempList.get(0);
                        if(responseMessageList.size() == 1) {
                            sendMessage("Your symptoms are: " + userInput.getText().toString() + ". Enter 'stop' if you do not have anymore symptoms to add", false);
                            sendMessage("List of possible symptoms: fatigue, nausea, swollen glands, rash, headache, abdominal pain, appetite loss, fever, dark urine, joint pain, jaundice, flu, diarrhea, cough, red eyes.", false);
                        }

                        else {
                            symptomString = arraylistToString(symptomString, (ArrayList<String>) tempList);
                            sendMessage("Your symptoms are: " + symptomString + ". Enter 'stop' if you do not have anymore symptoms to add", false);
                            sendMessage("List of possible symptoms: fatigue, nausea, swollen glands, rash, headache, abdominal pain, appetite loss, fever, dark urine, joint pain, jaundice, flu, diarrhea, cough, red eyes.", false);

                        }
                    }
                    else { // if there is an error, or user keys stop
                        if (userInput.getText().toString().equals("stop")) {
                            if (cb.checkEmpty(tempList)){
                                sendMessage("You have entered stop. There are no symptoms entered. Please key in your symptoms.", false);
                                sendMessage("List of possible symptoms: fatigue, nausea, swollen glands, rash, headache, abdominal pain, appetite loss, fever, dark urine, joint pain, jaundice, flu, diarrhea, cough, red eyes.", false);
                            }
                            else {
                                sendMessage("You have entered stop. Processing symptoms", false);
                                userInput.setFocusable(false);
                                ArrayList<String> possiblediseases = new ArrayList<String>();
                                possiblediseases = (ArrayList<String>) alldisease.clone();

                                Chatstats cs= cb.getRecommend(possiblediseases,myMap,tempList);
                                printriskLevel(cs.getHighestcountdiseasearray(),tempList.size(), cs.getHighestcount(), cs.getPossibleSymptomsCount());
                                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                sendMessage("Chat has been disabled. To talk to chatbot again, <i><b><u>click here</u></b></i> ", false);
                            }
                        }
                        else if (tempList.contains(userinput)){
                            sendMessage("Incorrect symptom. You entered a repeat symptom: " + userInput.getText().toString() + ". Enter 'stop' if you do not have anymore symptoms to add", false);
                        }

                        else if (!(allsymptoms.contains(userinput))){
                            sendMessage("Incorrect symptom. Does not belong to symptom list. You entered: " + userInput.getText().toString() + ". Enter 'stop' if you do not have anymore symptoms to add", false);
                            sendMessage("List of possible symptoms: fatigue, nausea, swollen glands, rash, headache, abdominal pain, appetite loss, fever, dark urine, joint pain, jaundice, flu, diarrhea, cough, red eyes.", false);
                        }

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

    public void sendMessage(String message, boolean isUser) {
        ResponseMessage newMessage = new ResponseMessage(message, isUser);
        responseMessageList.add(newMessage);
    }

    public String arraylistToString(String convertedString, ArrayList<String> arraylist) {
        for (int x = 1; x < arraylist.size(); x++) {
            convertedString = convertedString + ", " + arraylist.get(x);
        }
        return convertedString;
    }

    public void createHashMap() {
        diseaseRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot DiseaseList : task.getResult()) { //gothrough index of diseaselist
                        ArrayList<String> arraysymptoms = new ArrayList<String>();
                        for (int i = 1; i<=15; i++) {
                            String string = "Symptom" + i;
                            arraysymptoms.add(DiseaseList.getString(string));
                        }
                        String diseasename = DiseaseList.getString("Disease ");


                        if ("All".equals(diseasename)){
                            allsymptoms = (ArrayList<String>)arraysymptoms.clone();
                        }
                        else{
                            myMap.put(diseasename, arraysymptoms); //create key value pair in hashmap
                        }
                    }
                    for (Map.Entry<String,ArrayList<String>> entry: myMap.entrySet()) {
                        String key = entry.getKey();
                        alldisease.add(key);
                    }

                }
            }

        });
    }

    public void printriskLevel(ArrayList<String> highestcountdiseasearray, int possiblesymptomSize, int highestcount, int possibleSymptomsCount){

        if ((highestcountdiseasearray.size())>0) {
            String highestcountdiseasestring = highestcountdiseasearray.get(0);
            highestcountdiseasestring = arraylistToString(highestcountdiseasestring, highestcountdiseasearray);
             float symptomMatchRate = (float) highestcount / possibleSymptomsCount * 100;
            sendMessage("You are at risk of: " + highestcountdiseasestring + " with a symptom match rate of up to " + (String.format("%.2f", symptomMatchRate)) + "%", false);
        }

        else if (highestcountdiseasearray.size()==0) {
            sendMessage("There are no diseases in the database that match your symptoms.", false);
        }


        String highrisk= "true";
        highrisk=cb.highriskLevel(possiblesymptomSize,highestcount,possibleSymptomsCount);


        if (highrisk=="false") {
            sendMessage("Your risk level is estimated to be low, we recommend that you visit a pharmacy to purchase medication and self-medicate.", false);
            sendMessage("<i><b><u>Click here</u></b></i> to be redirected to the pharmacy page. ", false);
            clinic = "false";

        }
        else {
            sendMessage("Your risk level is estimated to be high, we recommend that you visit a clinic to consult a doctor immediately.", false);
            sendMessage("<i><b><u>Click here</u></b></i> to be redirected to the clinic page.", false);
            clinic = "true";
        }
    }
    



    @Override
    public void onNoteClick(int position) {
        if (responseMessageList.size() - position - 1 == 0) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            }
        else if (responseMessageList.size() - position - 2 == 0){
            if (clinic.equals("false")) {
                startActivity(new Intent(getApplicationContext(), MapsActivityPharmacy.class));
                finish();
            } else if (clinic.equals("true")) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                finish();
            }
        }
    }
}