package com.example.loginapp.Control;
import com.example.loginapp.Entity.Chatstats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ChatbotController is a control class used to derive the end output of the chatbot after the user enters
 * their symptoms.
 * End output includes : the user's symptom match rate, risk level and possible diseases
 *
 * @author Goh Shan Ying, Jonathan Chang, Lee Xuanhui, Luke Chin Peng Hao, Lynn Masillamoni, Russell Leung
 */

public class ChatbotController {

    /**
     * checks user's risk level
     * @param possiblesymptomSize number of possible symptoms
     * @param highestcount number of valid symptoms that the user has
     * @param possibleSymptomsCount the smallest number of symptoms for all the diseases the user is at risk
     * @return risk level
     */
    public String highriskLevel(int possiblesymptomSize, int highestcount, int possibleSymptomsCount) {


        if (possiblesymptomSize <= 3 && (float) highestcount / possibleSymptomsCount < 0.5) {
            return "false";
        } else {
            return "true";
        }
    }

    /**
     * gets list and number of diseases user might have, number of recognized user's symptoms and highest number of possible symptoms from the diseases the user might have
     * @param possiblediseases list and number of diseases user might have
     * @param myMap keys are the infectious diseases and their values are the symptoms for each disease
     * @param tempList list of the user's valid symptoms
     * @return recommendation for user based on
     */
    //gets list and number of diseases user might have, number of recognized user's symptoms and highest number of possible symptoms from the diseases the user might have
    public Chatstats getRecommend(ArrayList<String> possiblediseases, HashMap<String, ArrayList<String>> myMap, List<String> tempList) {
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
                } else {
                    matchcount = matchcount + 1;
                    if ((matchcount > highestcount) && (matchcount == tempList.size())) {
                        highestcount = matchcount;
                        highestcountdiseasearray.clear();
                        highestcountdiseasearray.add(diseasename1);
                    } else if ((matchcount == highestcount) && (matchcount == tempList.size())) {
                        highestcountdiseasearray.add(diseasename1);
                    }
                }
            }
        }


        int possibleSymptomsCount = 15;
        int tempPossibleSymptomsCount = 0;
        List<Integer> symptomsForPossibleDiseases = new ArrayList<>();
        for (Map.Entry<String, ArrayList<String>> entry : myMap.entrySet()) {
            String diseasename2 = entry.getKey();
            for (int s = 0; s < highestcountdiseasearray.size(); s++) {
                if (diseasename2.equals(highestcountdiseasearray.get(s))) {
                    ArrayList<String> arraysymptoms = entry.getValue(); //arraylist of all the possible symptoms for selected disease


                    for (int t = 0; t < arraysymptoms.size(); t++) {
                        if (arraysymptoms.get(t).equals("fatigue") || arraysymptoms.get(t).equals("nausea") || arraysymptoms.get(t).equals("swollen glands") || arraysymptoms.get(t).equals("rash") || arraysymptoms.get(t).equals("headache") || arraysymptoms.get(t).equals("abdominal pain") || arraysymptoms.get(t).equals("appetite loss") || arraysymptoms.get(t).equals("fever") || arraysymptoms.get(t).equals("dark urine") || arraysymptoms.get(t).equals("joint pain") || arraysymptoms.get(t).equals("jaundice") || arraysymptoms.get(t).equals("flu") || arraysymptoms.get(t).equals("diarrhea") || arraysymptoms.get(t).equals("cough") || arraysymptoms.get(t).equals("red eyes")) {
                            tempPossibleSymptomsCount++;
                        }
                    }
                    symptomsForPossibleDiseases.add(tempPossibleSymptomsCount);
                    tempPossibleSymptomsCount = 0;
                }
            }
        }
        for (int i = 0; i < symptomsForPossibleDiseases.size(); i++) {
            if (symptomsForPossibleDiseases.get(i) < possibleSymptomsCount)
                possibleSymptomsCount = symptomsForPossibleDiseases.get(i);
        }

        Chatstats cs= new Chatstats(highestcountdiseasearray, possiblediseases.size(), highestcount, possibleSymptomsCount);
        return cs;
    }


    /**
     * checks if user has previously entered any symptoms
     * @param templist list of symptoms user have entered
     * @return true if user have entered at least 1 valid symptom
     */
    //checks if user has previously entered any symptoms
    public boolean checkEmpty(List<String>templist) {
        if (templist.size() == 0) {
            return true;

        } else
            return false;
    }
}

