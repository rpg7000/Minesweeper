package com.company;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoreBoard {
    public ScoreBoard(){

    }
    public ArrayList<ArrayList<String>> getScoreBoard(){
        ArrayList<ArrayList<String>> aL = new ArrayList<>();
        aL.add(getScoreBoard('e'));
        aL.add(getScoreBoard('m'));
        aL.add(getScoreBoard('h'));
        return aL;
    }
    public ArrayList<String> getScoreBoard(char c){
        //HashMap<char,int> hM = new HashMap<char, int>();
        ArrayList<String> aL = new ArrayList<>();
        return aL;
    }
}
