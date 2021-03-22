package com.example.loginapp;
//hi

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.FieldValue;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;


import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChatbotActivity extends AppCompatActivity {

    EditText userInput;
    RecyclerView recyclerView;
    List<ResponseMessage> responseMessageList;
    MessageAdapter messageAdapter;
    List<String> tempList =new ArrayList<String>();
    //private static ArrayList<String> diseaseDB =new ArrayList<String>();
    private static HashMap<String, ArrayList<String>> myMap = new HashMap<String, ArrayList<String>>(); // hashmap created.
    private static ArrayList<String> allsymptoms = new ArrayList<String>(); // contains all 15 symptoms possible.
    private static ArrayList<String> alldisease=new ArrayList<String>(); // contains all possible diseases


    private FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    private CollectionReference diseaseRef = fstore.collection("infectdisease");


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        diseaseRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot DiseaseList : task.getResult()) { //gothrough index of diseaselist
                        ArrayList<String> arraysymptoms = new ArrayList<String>();
                        arraysymptoms.add(DiseaseList.getString("Symptom1"));
                        arraysymptoms.add(DiseaseList.getString("Symptom2"));
                        arraysymptoms.add(DiseaseList.getString("Symptom3"));
                        arraysymptoms.add(DiseaseList.getString("Symptom4"));
                        arraysymptoms.add(DiseaseList.getString("Symptom5"));
                        arraysymptoms.add(DiseaseList.getString("Symptom6"));
                        arraysymptoms.add(DiseaseList.getString("Symptom7"));
                        arraysymptoms.add(DiseaseList.getString("Symptom8"));
                        arraysymptoms.add(DiseaseList.getString("Symptom9"));
                        arraysymptoms.add(DiseaseList.getString("Symptom10"));
                        arraysymptoms.add(DiseaseList.getString("Symptom11"));
                        arraysymptoms.add(DiseaseList.getString("Symptom12"));
                        arraysymptoms.add(DiseaseList.getString("Symptom13"));
                        arraysymptoms.add(DiseaseList.getString("Symptom14"));
                        arraysymptoms.add(DiseaseList.getString("Symptom15"));


                        String diseasename = DiseaseList.getString("Disease ");


                        if ("All".equals(diseasename)){
                            allsymptoms = (ArrayList<String>)arraysymptoms.clone();


                        }
                        else{
                            myMap.put(diseasename, arraysymptoms); //create key value pair in hashmap

                        }


                    }

                    //for (ArrayList<String> v : myMap.values()) {
                    //for (String s : v) {
                    //Log.d("ChatbotActivity", "Symptom:" + s);
                    //Log.d("ChatbotActivity", "Diseasename:" + myMap.key());
                    // }
                    // }

                    for (Map.Entry<String,ArrayList<String>> entry: myMap.entrySet()) {
                        String key = entry.getKey();
                        alldisease.add(key);
                        //Log.d("ChatbotActivity", "Diseasename:" + key);
                        ArrayList<String> value = entry.getValue();
                        /*for (String s : value){
                            Log.d("ChatbotActivity", "Symptom:" + s);

                        }*/

                    }

                }
            }

        });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        userInput = findViewById(R.id.userInput);
        recyclerView = findViewById(R.id.conversation);
        responseMessageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(responseMessageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(messageAdapter);

        ResponseMessage initializeMessage1 = new ResponseMessage("Welcome to the chatbot", false);
        responseMessageList.add(initializeMessage1);
        String initializeAllSymptoms = "Please only key in the following listed symptoms ";
        //the very first message the chatbot displays
        //Log.d("initializeAllSymptoms", initializeAllSymptoms);
        for (int x = 0; x < allsymptoms.size(); x++) {
             initializeAllSymptoms = initializeAllSymptoms + ", " + allsymptoms.get(x);
             //Log.d("initializeAllSymptoms", initializeAllSymptoms);
        }

        ResponseMessage initializeMessage = new ResponseMessage("List of possible symptoms: fatigue, nausea, swollen glands, rash, headache, abdominal pain, appetite loss, fever, dark urine, joint pain, jaundice, flu, diarrhea, cough, red eyes.", false);
        //ResponseMessage initializeMessage = new ResponseMessage(initializeAllSymptoms, false);
        responseMessageList.add(initializeMessage);



        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override


            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    ResponseMessage chatbotMessage;

                    ResponseMessage userMessage = new ResponseMessage(userInput.getText().toString(), true);
                    responseMessageList.add(userMessage);
                    String userinput = userInput.getText().toString();


                    /*for (String s:allsymptoms){
                        Log.d("ChatbotActivity", "Symptom:" + s);
                    }*/
                    //for checking all symptoms in the list


                    for (String s:alldisease){
                        Log.d("ChatbotActivity", "Disease:" + s);
                    }
                    //for checking all disease in the list


                    if((!userInput.getText().toString().equals("stop")) && (allsymptoms.contains(userinput))&& (!tempList.contains(userinput)))  {
                        tempList.add(userinput);
                        String symptomString = tempList.get(0);
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
                    else { // if there is an error, or user keys stop
                        if (userInput.getText().toString().equals("stop")) {
                            chatbotMessage = new ResponseMessage("You have entered stop. Processing symptoms", false);
                            responseMessageList.add(chatbotMessage);
                            ArrayList<String> possiblediseases = new ArrayList<String>();
                            possiblediseases = (ArrayList<String>) alldisease.clone();
                            //start with all the disease
                            for (String s : possiblediseases) {
                                Log.d("ChatbotActivity", "possibledisease at the start " + s);
                            }
                            int matchcount = 0;
                            int highestcount = 0;
                            ArrayList<String> highestcountdiseasearray = new ArrayList<String>();

                            for (Map.Entry<String, ArrayList<String>> entry : myMap.entrySet()) { //access hash map.
                                String diseasename1 = entry.getKey();
                                ArrayList<String> Symptomsarray = entry.getValue();
                                matchcount = 0;
                                //highestcount = 0;

                                for (int i = 0; i < tempList.size(); i++) {
                                    if (!Symptomsarray.contains(tempList.get(i))) {
                                        possiblediseases.remove(diseasename1);
                                        break;
                                    }
                                    else {
                                        matchcount = matchcount + 1;
                                        if ((matchcount >= highestcount) && (matchcount == tempList.size())) {
                                            highestcount = matchcount;
                                            highestcountdiseasearray.add(diseasename1);
                                            Log.d("ChatbotActivityAAA", highestcountdiseasearray.get(highestcountdiseasearray.size() - 1));
                                            Log.d("ChatbotActivity", String.valueOf(highestcount));
                                        }
                                        for (int a=0; a < highestcountdiseasearray.size(); a++) {
                                            Log.d("ChatbotActivity", highestcountdiseasearray.get(a));
                                        }


                                    }

                                }
                                //Log.d("ChatbotActivity", "i h8 lyfe");



                            }
                            //if  (possiblediseases.size()==0)
                                //print risk level= low , hence please visit a pharmacy, but alternatively, clinic visit too.
                            //else if (possiblediseases>0)
                                // print possible diseases for user.
                                //calculate number of diseases matched, over total amount of disease: this forms risk A
                                //double risk= (possiblediseases.size())/alldisease.size();
                                //calculate number of symptoms matched for highestcountdisease too. need to access hashmap to get. do note that highestcountdisease
                                //is an array
                                 // after geting disease symptoms size.do highestmatchcount/diseasesymptomsize : this forms risk B
                                // combine risk A and B into a number <= 1.0
                                // another if else statements here.
                                // this is just an eg. eg if (risk>0.5) == high risk, if lower than 0.5 maybe low risk? recommend pharmacy etc
                            //if (risk>0.5)
                              //tell user hi ur risk asssessment level is high , please go to ur nearby clinic asap, link to clinic page perhaps or map?
                            // if not if user risk level is low, hi please visit the pharmacy. can still reocmmend doctor.


                            int idk = 0;
                            for (Map.Entry<String, ArrayList<String>> entry : myMap.entrySet()) {
                                String diseasename2 = entry.getKey();
                                for (int s = 0; s < highestcountdiseasearray.size(); s++) {
                                    if (diseasename2.equals(highestcountdiseasearray.get(s))) {
                                        Log.d("ChatbotActivityBBB", diseasename2);
                                        ArrayList<String> highestcountdiseasesymptoms = new ArrayList<String>();
                                        ArrayList<String> arraysymptoms= entry.getValue();
                                        idk = 0;
                                        for (int t = 0; t < arraysymptoms.size(); t++) {

                                            if (arraysymptoms.get(t).equals("fatigue") || arraysymptoms.get(t).equals("nausea") || arraysymptoms.get(t).equals("swollen glands") || arraysymptoms.get(t).equals("rash") || arraysymptoms.get(t).equals("headache") || arraysymptoms.get(t).equals("abdominal pain") || arraysymptoms.get(t).equals("appetite loss") || arraysymptoms.get(t).equals("fever") || arraysymptoms.get(t).equals("dark urine") || arraysymptoms.get(t).equals("joint pain") || arraysymptoms.get(t).equals("jaundice") || arraysymptoms.get(t).equals("flu") || arraysymptoms.get(t).equals("diarrhea") || arraysymptoms.get(t).equals("cough") || arraysymptoms.get(t).equals("red eyes")) {
                                                Log.d("ChatbotActivityCCC", arraysymptoms.get(t));
                                                highestcountdiseasesymptoms.add(arraysymptoms.get(t));
                                                idk++;
                                            }
                                        }
                                        Log.d("ChatbotActivityDDD", String.valueOf(idk));
                                    }
                                }
                            }





                            for (int y = 0; y < possiblediseases.size(); y++) {
                                Log.d("ChatbotActivity99999", "Disease after calculation: " + possiblediseases.get(y));
                            }

                            for (int x = 0; x < highestcountdiseasearray.size(); x++) {
                                Log.d("ChatbotActivity00000", "highest count disease: " + highestcountdiseasearray.get(x));
                            }

                                Log.d("ChatbotActivity", "highestcount: " + highestcount);
                                Log.d("ChatbotActivity", "highestcountdisease: " + highestcountdiseasearray.get(0));
                                //Log.d("ChatbotActivity", "highestcountdisease:" + highestcountdiseasearray.get(1));
                                //Log.d("ChatbotActivity", "highestcountdisease :" + highestcountdiseasearray.get(2));
                                //Log.d("ChatbotActivity", "matchcount :" + matchcount);

                                //chatbotMessage = new ResponseMessage("You are at risk of: " + highestcountdiseasearray.get(0) + highestcountdiseasearray.get(1) + highestcountdiseasearray.get(2) + "with a highest count of " + highestcount, false);
                                //responseMessageList.add(chatbotMessage);




                            String highestcountdiseasestring = highestcountdiseasearray.get(0);
                            Log.d("ChatbotActivity1111", highestcountdiseasestring);
                            for (int b = 1; b < highestcountdiseasearray.size(); b++) {
                                highestcountdiseasestring = highestcountdiseasestring + ", " + highestcountdiseasearray.get(b);
                                Log.d("ChatbotActivity22222", highestcountdiseasestring);
                            }
                            chatbotMessage = new ResponseMessage("You are at risk of: " + highestcountdiseasestring + " with a symptom match rate of " + (float) highestcount/idk * 100 + "%", false);
                            responseMessageList.add(chatbotMessage);

                            //TODO risk level assessement, recommend pharmacy or clinic
                            /*if (possiblediseases.size()==0) {
                                chatbotMessage = new ResponseMessage("Your risk level is estimated to be low, we recommend that you visit a pharmacy to purchase medication and self-medicate.", false);
                                responseMessageList.add(chatbotMessage);
                            }*/
                            if (possiblediseases.size() <=3 || (float) highestcount/idk <0.5) {
                                chatbotMessage = new ResponseMessage("Your risk level is estimated to be low, we recommend that you visit a pharmacy to purchase medication and self-medicate.", false);
                                responseMessageList.add(chatbotMessage);
                                //Log.d("ChatbotActivity", toString((float) possiblediseases.size()/alldisease.size()));
                            }
                            else {
                                chatbotMessage = new ResponseMessage("Your risk level is estimated to be high, we recommend that you visit a clinic to consult a doctor immediately.", false);
                                responseMessageList.add(chatbotMessage);
                            }




                            /*for (Map.Entry<String,ArrayList<String>> entry: myMap.entrySet()) {
                            String key = entry.getKey();
                            ArrayList<String> value = entry.getValue();
                            for (String s : value){
                                Log.d("ChatbotActivity", "Symptom:" + s);
                        }
                    }*/


                            /*for (int i = 0; i < tempList.size(); i++) {
                                Log.d("ChatbotActivity", "user's symptom" +  tempList.get(i) );
                            }
                            check templist symptoms */


                        }
                        else if (tempList.contains(userinput)){
                            chatbotMessage = new ResponseMessage("Incorrect symptom. You entered a repeat symptom:" + userInput.getText().toString() + ". Enter 'stop' if you do not have anymore symptoms to add", false);
                            responseMessageList.add(chatbotMessage);
                        }

                        else if (!(allsymptoms.contains(userinput))){
                            chatbotMessage = new ResponseMessage("Incorrect symptom. Does not belong to symptom list. You entered:" + userInput.getText().toString() + ". Enter 'stop' if you do not have anymore symptoms to add", false);
                            responseMessageList.add(chatbotMessage);
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
}