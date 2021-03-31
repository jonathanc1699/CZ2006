package com.example.loginapp.Control;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginapp.Boundary.MainActivity;
import com.example.loginapp.Boundary.NearestClinic;
import com.example.loginapp.Boundary.NearestPharmacy;
import com.example.loginapp.Control.MessageAdapter;
import com.example.loginapp.Control.ResponseMessage;
import com.example.loginapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    List<String> tempList =new ArrayList<String>();
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
                        }

                        else {
                            symptomString = arraylistToString(symptomString, (ArrayList<String>) tempList);
                            sendMessage("Your symptoms are: " + symptomString + ". Enter 'stop' if you do not have anymore symptoms to add", false);

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
                                printriskLevel(cs.getHighestcountdiseasearray(),cs.getPossiblediseasesize(), cs.getHighestcount(), cs.getIdk());
                                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                sendMessage("Chat has been disabled. To talk to chatbot again, click here", false);
                            }
                        }
                        else if (tempList.contains(userinput)){
                            sendMessage("Incorrect symptom. You entered a repeat symptom:" + userInput.getText().toString() + ". Enter 'stop' if you do not have anymore symptoms to add", false);
                        }

                        else if (!(allsymptoms.contains(userinput))){
                            sendMessage("Incorrect symptom. Does not belong to symptom list. You entered:" + userInput.getText().toString() + ". Enter 'stop' if you do not have anymore symptoms to add", false);
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

    public void printriskLevel(ArrayList<String> highestcountdiseasearray, int possiblediseasesSize, int highestcount, int idk){
        String highestcountdiseasestring = highestcountdiseasearray.get(0);
        highestcountdiseasestring = arraylistToString(highestcountdiseasestring, highestcountdiseasearray);
        sendMessage("You are at risk of: " + highestcountdiseasestring + " with a symptom match rate of " + (float) highestcount/idk * 100 + "%", false);
        String highrisk= "true";
        highrisk=cb.highriskLevel(possiblediseasesSize,highestcount,idk);


        if (highrisk=="false") {
            sendMessage("Your risk level is estimated to be low, we recommend that you visit a pharmacy to purchase medication and self-medicate.", false);
            sendMessage("Click here to be redirected to the pharmacy page.", false);
            clinic = "false";

        }
        else {
            sendMessage("Your risk level is estimated to be high, we recommend that you visit a clinic to consult a doctor immediately.", false);
            sendMessage("Click here to be redirected to the clinic page.", false);
            clinic = "true";
        }
    }

    /*public void getRecommend(ArrayList<String> possiblediseases) {
        //start with all the disease
        int matchcount = 0;
        int highestcount = 0;
        ArrayList<String> highestcountdiseasearray = new ArrayList<String>();

        for (Map.Entry<String, ArrayList<String>> entry : myMap.entrySet()) { //access hash map.
            String diseasename1 = entry.getKey();
            ArrayList<String> Symptomsarray = entry.getValue();
            matchcount = 0;

            for (int i = 0; i < tempList.size(); i++) {
                if (!Symptomsarray.contains(tempList.get(i))) {
                    possiblediseases.remove(diseasename1);
                    break;
                }
                else {
                    matchcount = matchcount + 1;
                    if ((matchcount > highestcount) && (matchcount == tempList.size())) {
                        highestcount = matchcount;
                        highestcountdiseasearray.clear();
                        highestcountdiseasearray.add(diseasename1);
                    }
                    else if ((matchcount == highestcount) && (matchcount == tempList.size())){
                        highestcountdiseasearray.add(diseasename1);
                    }
                }

            }
        }


        int idk = 0;
        for (Map.Entry<String, ArrayList<String>> entry : myMap.entrySet()) {
            String diseasename2 = entry.getKey();
            for (int s = 0; s < highestcountdiseasearray.size(); s++) {
                if (diseasename2.equals(highestcountdiseasearray.get(s))) {
                    ArrayList<String> highestcountdiseasesymptoms = new ArrayList<String>();
                    ArrayList<String> arraysymptoms= entry.getValue();
                    idk = 0;
                    for (int t = 0; t < arraysymptoms.size(); t++) {

                        if (arraysymptoms.get(t).equals("fatigue") || arraysymptoms.get(t).equals("nausea") || arraysymptoms.get(t).equals("swollen glands") || arraysymptoms.get(t).equals("rash") || arraysymptoms.get(t).equals("headache") || arraysymptoms.get(t).equals("abdominal pain") || arraysymptoms.get(t).equals("appetite loss") || arraysymptoms.get(t).equals("fever") || arraysymptoms.get(t).equals("dark urine") || arraysymptoms.get(t).equals("joint pain") || arraysymptoms.get(t).equals("jaundice") || arraysymptoms.get(t).equals("flu") || arraysymptoms.get(t).equals("diarrhea") || arraysymptoms.get(t).equals("cough") || arraysymptoms.get(t).equals("red eyes")) {
                            highestcountdiseasesymptoms.add(arraysymptoms.get(t));
                            idk++;
                        }
                    }
                }
            }
        }
        printriskLevel(highestcountdiseasearray, possiblediseases.size(), highestcount, idk);
    }*/

    @Override
    public void onNoteClick(int position) {
        if (responseMessageList.size() - position - 2 == 0) {
            if (clinic.equals("false")) {
                startActivity(new Intent(getApplicationContext(), NearestPharmacy.class));
                finish();
            } else if (clinic.equals("true")) {
                startActivity(new Intent(getApplicationContext(), NearestClinic.class));
                finish();
            }
        else if (responseMessageList.size() - position - 1 == 0){

                //finish();
                //overridePendingTransition( 0, 0);
                //startActivity(getIntent());
                //overridePendingTransition( 0, 0);
                this.recreate();


            }
        }
    }
}