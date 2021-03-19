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
    private static ArrayList<String> allsymptoms; // contains all 15 symptoms possible.
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

                        String string1 = new String("All");
                        if ("All".equals(diseasename)){
                            allsymptoms=arraysymptoms;

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
                        Log.d("ChatbotActivity", "Diseasename:" + key);
                        ArrayList<String> value = entry.getValue();
                        for (String s : value){
                            Log.d("ChatbotActivity", "Symptom:" + s);

                        }

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
        
        //the very first message the chatbot displays
        ResponseMessage initializeMessage = new ResponseMessage("Hi, what symptoms are you experiencing today?", false);
        responseMessageList.add(initializeMessage);
        
        userInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override


            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //TODO show symptom before allowing user to key in.
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    ResponseMessage chatbotMessage;

                    ResponseMessage userMessage = new ResponseMessage(userInput.getText().toString(), true);
                    responseMessageList.add(userMessage);
                    String userinput = userInput.getText().toString();


                    //for (String s:allsymptoms){
                    //    Log.d("ChatbotActivity", "Symptom:" + s);
                    //}
                    //for checking all symptoms in the list


                    //for (String s:alldisease){
                            //Log.d("ChatbotActivity", "Disease:" + s);
                        //}
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
                            ArrayList<String> possiblediseases= new ArrayList<String>();
                            possiblediseases=alldisease; //start with all the disease
                            for (String s:possiblediseases){
                                Log.d("Possible diseases", "startDisease:" + s);}
                            int matchcount=0;
                            int highestcount=0;
                            String highestcountdisease=null;

                            for (Map.Entry<String,ArrayList<String>> entry: myMap.entrySet()) { //access hash map.
                                String diseasename1 = entry.getKey();
                                ArrayList<String> Symptomsarray = entry.getValue();
                                matchcount=0;
                                for (int i = 0; i < tempList.size(); i++) {
                                    if (!Symptomsarray.contains(tempList.get(i))&& (matchcount==0)){
                                        possiblediseases.remove(diseasename1);
                                        break;
                                    }
                                    else{
                                        matchcount=matchcount+1;
                                        if (matchcount>highestcount){
                                            highestcount=matchcount;
                                            highestcountdisease=diseasename1;
                                        }

                                    }





                                }


                                }
                            Log.d("ChatbotActivity", "highestcount:" + highestcount);
                            Log.d("ChatbotActivity", "highestcountdisease:" + highestcountdisease);
                            Log.d("ChatbotActivity", "matchcount:" + matchcount);
                            chatbotMessage = new ResponseMessage("Your are at risk of: " + highestcountdisease + "with a matchcount of "+matchcount, false);
                            responseMessageList.add(chatbotMessage);


                          
                            for (String s:possiblediseases){
                                Log.d("Possible diseases", "Disease:" + s);}
                            //TODO risk level assessement, recommend pharmacy or clinic




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
