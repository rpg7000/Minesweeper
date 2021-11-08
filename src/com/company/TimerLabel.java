package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;

public class TimerLabel extends JLabel {
    String clockIcon;
    int time;
    Timer timer;
    public TimerLabel(){
        clockIcon = "⏱";//replace with emoji
        time = 0;
        timer = new Timer();
        updateLabel();
        setForeground(Color.white);
    }
    public void updateLabel(){
        String t = "";
        if(time >= 1000){
            t = "999";
        }
        else{
            t = "" + time;
            while (t.length() < 3){
                t = "0" + t;
            }
        }
        setText(clockIcon + t);
    }
    public void reset(){
        time = 0;
        updateLabel();
    }
    public void start(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                time += 1;
                updateLabel();
            }
        }, 1000, 1000);
    }
    public int stop(){
        timer.cancel();
        timer = new Timer();
        return time;
    }
}
