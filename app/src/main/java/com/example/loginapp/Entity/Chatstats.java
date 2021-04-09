package com.example.loginapp.Entity;

import java.util.ArrayList;

/**
 * This class implements the Chatstats entity with the attributes highestcountdiseasearray, highestcount, possibleSymptomsCount, possiblediseasesize
 *
 * @author Goh Shan Ying, Jonathan Chang, Lee Xuanhui, Luke Chin Peng Hao, Lynn Masillamoni, Russell Leung
 */

public class Chatstats {
    private ArrayList<String> highestcountdiseasearray;
    private int highestcount;
    private int possibleSymptomsCount;
    private int possiblediseasesize;

    /**
     * Constructor for Chatstats.
     * @param highestcountdiseasearray arraylist of the diseases the user might have
     * @param possiblediseasesize number of possible disease
     * @param highestcount number of valid symptoms that the user has
     * @param possibleSymptomsCount the smallest number of symptoms for all the diseases the user is at risk
     */
    public Chatstats(ArrayList<String> highestcountdiseasearray,int possiblediseasesize, int highestcount, int possibleSymptomsCount ) {
        this.highestcountdiseasearray = highestcountdiseasearray;
        this.possiblediseasesize = possiblediseasesize;
        this.highestcount = highestcount;
        this.possibleSymptomsCount = possibleSymptomsCount;

    }


    /**
     * number of possible disease
     * @param possiblediseasesize number of possible disease
     */
    public Chatstats(int possiblediseasesize) {
        this.possiblediseasesize = possiblediseasesize;
    }

    /**
     * Get the possible number of disease
     * @return possible number of disease
     */
    public int getPossiblediseasesize() {
        return possiblediseasesize;
    }

    /**
     * Set the possible number of disease
     * @param possiblediseasesize possible number of disease
     */
    public void setPossiblediseasesize(int possiblediseasesize) {
        this.possiblediseasesize = possiblediseasesize;
    }


    /**
     * Get the arraylist of the diseases the user might have
     * @return arraylist of the diseases the user might have
     */
    public ArrayList<String> getHighestcountdiseasearray() {
        return highestcountdiseasearray;
    }

    /**
     * Set the arraylist of the diseases the user might have
     * @param highestcountdiseasearray arraylist of the diseases the user might have
     */
    public void setHighestcountdiseasearray(ArrayList<String> highestcountdiseasearray) {
        this.highestcountdiseasearray = highestcountdiseasearray;
    }

    /**
     * Get the number of valid symptoms that the user has
     * @return number of valid symptoms that the user has
     */
    public int getHighestcount() {
        return highestcount;
    }

    /**
     * Set the number of valid symptoms that the user has
     * @param highestcount number of valid symptoms that the user has
     */
    public void setHighestcount(int highestcount) {
        this.highestcount = highestcount;
    }

    /**
     * Get the smallest number of symptoms for all the diseases the user is at risk
     * @return the smallest number of symptoms for all the diseases the user is at risk
     */
    public int getPossibleSymptomsCount() {
        return possibleSymptomsCount;
    }

    /**
     * Set the smallest number of symptoms for all the diseases the user is at risk
     * @param possibleSymptomsCount the smallest number of symptoms for all the diseases the user is at risk
     */
    public void setPossibleSymptomsCount(int possibleSymptomsCount) {
        this.possibleSymptomsCount = possibleSymptomsCount;
    }





}

