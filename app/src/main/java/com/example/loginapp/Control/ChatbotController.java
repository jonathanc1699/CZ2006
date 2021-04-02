package com.example.loginapp.Control;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatbotController {

    public String highriskLevel(int possiblesymptomSize, int highestcount, int symptomsMatchedAgainstDisease) {


        if (possiblesymptomSize <= 3 || (float) highestcount / symptomsMatchedAgainstDisease < 0.5) {
            return "false";
        } else {
            return "true";
        }
    }

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


        int symptomsMatchedAgainstDisease = 0;
        for (Map.Entry<String, ArrayList<String>> entry : myMap.entrySet()) {
            String diseasename2 = entry.getKey();
            for (int s = 0; s < highestcountdiseasearray.size(); s++) {
                if (diseasename2.equals(highestcountdiseasearray.get(s))) {
                    ArrayList<String> highestcountdiseasesymptoms = new ArrayList<String>();
                    ArrayList<String> arraysymptoms = entry.getValue();
                    symptomsMatchedAgainstDisease = 0;
                    for (int t = 0; t < arraysymptoms.size(); t++) {

                        if (arraysymptoms.get(t).equals("fatigue") || arraysymptoms.get(t).equals("nausea") || arraysymptoms.get(t).equals("swollen glands") || arraysymptoms.get(t).equals("rash") || arraysymptoms.get(t).equals("headache") || arraysymptoms.get(t).equals("abdominal pain") || arraysymptoms.get(t).equals("appetite loss") || arraysymptoms.get(t).equals("fever") || arraysymptoms.get(t).equals("dark urine") || arraysymptoms.get(t).equals("joint pain") || arraysymptoms.get(t).equals("jaundice") || arraysymptoms.get(t).equals("flu") || arraysymptoms.get(t).equals("diarrhea") || arraysymptoms.get(t).equals("cough") || arraysymptoms.get(t).equals("red eyes")) {
                            highestcountdiseasesymptoms.add(arraysymptoms.get(t));
                            symptomsMatchedAgainstDisease++;
                        }
                    }
                }
            }
        }
        Chatstats cs= new Chatstats(highestcountdiseasearray, possiblediseases.size(), highestcount, symptomsMatchedAgainstDisease);
        return cs;
        //printriskLevel(highestcountdiseasearray, possiblediseases.size(), highestcount, symptomsMatchedAgainstDisease);
    }




    public boolean checkEmpty(List<String>templist) {
        if (templist.size() == 0) {
            return true;

        } else
            return false;
    }
}

