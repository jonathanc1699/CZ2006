package com.example.loginapp.Control;

import java.util.ArrayList;

public class Chatstats {
    private ArrayList<String> highestcountdiseasearray;
    private int highestcount;
    private int symptomsMatchedAgainstDisease;
    private int possiblediseasesize;

    public Chatstats(ArrayList<String> highestcountdiseasearray,int possiblediseasesize, int highestcount, int symptomsMatchedAgainstDisease ) {
        this.highestcountdiseasearray = highestcountdiseasearray;
        this.possiblediseasesize = possiblediseasesize;
        this.highestcount = highestcount;
        this.symptomsMatchedAgainstDisease = symptomsMatchedAgainstDisease;

    }



    public Chatstats(int possiblediseasesize) {
        this.possiblediseasesize = possiblediseasesize;
    }


    public int getPossiblediseasesize() {
        return possiblediseasesize;
    }

    public void setPossiblediseasesize(int possiblediseasesize) {
        this.possiblediseasesize = possiblediseasesize;
    }


    public ArrayList<String> getHighestcountdiseasearray() {
        return highestcountdiseasearray;
    }

    public void setHighestcountdiseasearray(ArrayList<String> highestcountdiseasearray) {
        this.highestcountdiseasearray = highestcountdiseasearray;
    }

    public int getHighestcount() {
        return highestcount;
    }

    public void setHighestcount(int highestcount) {
        this.highestcount = highestcount;
    }

    public int getSymptomsMatchedAgainstDisease() {
        return symptomsMatchedAgainstDisease;
    }

    public void setSymptomsMatchedAgainstDisease(int symptomsMatchedAgainstDisease) {
        this.symptomsMatchedAgainstDisease = symptomsMatchedAgainstDisease;
    }





}

