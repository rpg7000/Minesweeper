package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

public class Tile extends JButton {
    private boolean mine, clicked, flagged;
    private int value, row, col;
    JFrame parentFrame;


    public Tile(int r,int c) {


        row = r;
        col = c;

        if ((row + col) % 2 ==0){
            setBackground(Color.green);
        }
        else{
            setBackground(new Color(65,212,8));
        }

        parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        mine = false;
        clicked = false;
        flagged = false;
        value = 0;
        setMargin(new Insets(0,0,0,0));

        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (getWidth() < getHeight()) {
                    setFont(getFont().deriveFont((float) (getWidth() / 2)));
                }
                else{
                    setFont(getFont().deriveFont((float) (getHeight() / 2)));
                }
                //setMargin(new Insets(getHeight()/4,getWidth()/4,getHeight()/4,getWidth()/4));
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });

    }

    public boolean isClicked() {
        return clicked;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public boolean isMine() {
        return mine;
    }
    public int getRow(){
        return row;
    }
    public int getCol(){
        return col;
    }
    public int getValue(){
        return value;
    }
    public void setClicked(boolean c){
        clicked = c;
    }
    public void setMine(boolean m){
        mine = m;
    }
    public void setFlagged(boolean f){
        flagged = f;
    }

    public void setAsMine() {
        value = 0;
        mine = true;
    }

    public void increment() {
        if (mine == false){
            value++;
        }
    }

}