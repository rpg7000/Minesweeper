package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GUI extends JFrame{
    private JPanel menu,game,gameMenuBar,board;
    private JButton startGame, back2Menu, playAgain;
    private AbstractAction start,toMenu, onClick;
    private CardLayout cl = new CardLayout();
    private Map<String, Map<String,Integer>> map = new HashMap<String, Map<String, Integer>>();
    private HashMap<Integer, Color> colorHashMap;
    private String difficulty;
    private Tile[][] tiles;
    private int numFlags, numTilesLeft;
    private JLabel flags, title;
    private boolean firstTurn,gameOver;
    private JComboBox difficultySelector;
    private JDialog gameOverMenu;
    private JLabel gameOverStatus;
    private TimerLabel tL;

    public GUI(){
        //initializing difficulty settings hashmap
        map.put("e",new HashMap<String, Integer>());
        map.get("e").put("rows",9);//traditionally 8x8, but that's same difficulty as medium so it's now normally 9x9
        map.get("e").put("cols",9);
        map.get("e").put("mines",10);
        map.put("m",new HashMap<String,Integer>());
        map.get("m").put("rows",16);
        map.get("m").put("cols",16);
        map.get("m").put("mines",40);
        map.put("h",new HashMap<String, Integer>());
        map.get("h").put("rows",16);
        map.get("h").put("cols",30);
        map.get("h").put("mines",99);
        //sets up colors hash map
        colorHashMap = new HashMap<Integer,Color>();
        colorHashMap.put(1,Color.blue);
        colorHashMap.put(2,Color.green);
        colorHashMap.put(3,Color.red);
        colorHashMap.put(4,new Color(127,0,255));
        colorHashMap.put(5,new Color(128,0,0));
        colorHashMap.put(6,Color.cyan);
        colorHashMap.put(7,Color.black);
        colorHashMap.put(8,Color.gray);
        //sets default difficulty to medium
        difficulty = "m";
        //initialize JFrame
        setTitle("twiafh plays Minesweeper\uD83D\uDCA3");
        setLayout(cl);
        //buttons action listener
        onClick = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Tile t = (Tile) e.getSource();
                if(firstTurn){
                    if (t.isMine() || t.getValue() != 0) {
                        setupBoard();
                        tiles[t.getRow()][t.getCol()].doClick(0);
                        return;
                    }
                    else{
                        firstTurn = false;
                        tL.start();
                    }
                }
                if (gameOver) return;
                if(!t.isFlagged() && !t.isClicked()){
                    t.setClicked(true);
                    if (!t.isMine()) {

                        //if(gameOver) return;
                        if ((t.getRow() + t.getCol()) % 2 ==0){
                            t.setBackground(new Color(158,132,73));
                        }
                        else{
                            t.setBackground(new Color(117,106,81));
                        }
                        if (t.getValue() != 0) {
                            t.setText("" + t.getValue());
                            t.setForeground(colorHashMap.get(t.getValue()));
                        }
                        else{
                            for(int i = t.getRow() - 1; i <= t.getRow()+1;i++){
                                for(int j = t.getCol()-1;j<=t.getCol()+1;j++){
                                    if(i >= 0 && i <tiles.length && j >=0 && j < tiles[0].length){
                                        tiles[i][j].doClick(0);
                                    }
                                }
                            }
                        }
                        numTilesLeft -=1;
                        if(numTilesLeft <= 0){
                            tL.stop();
                            gameOver = true;
                            gameOverStatus.setText("You Win");
                            gameOverMenu.setVisible(true);
                        }
                    }
                    else{
                        //insert some game over stuff
                        if(!gameOver){
                            tL.stop();
                            gameOver = true;
                            for(int r = 0; r < tiles.length;r++){
                                for(int c = 0; c < tiles[r].length;c++){
                                    if (tiles[r][c].isFlagged()){
                                        tiles[r][c].setFlagged(false);
                                        tiles[r][c].setText("");
                                        tiles[r][c].setForeground(Color.black);
                                        }
                                    if(tiles[r][c].isMine()){
                                        //tiles[r][c].doClick(0);
                                        tiles[r][c].setText("\uD83D\uDCA3");
                                        tiles[r][c].updateUI();
                                    }
                                }
                            }
                            gameOverStatus.setText("You Lose");
                            gameOverMenu.setVisible(true);
                        }
                        t.setText("\uD83D\uDCA3");
                    }
                }
            }
        };
        //menu setup
        menu = new JPanel();
        menu.setBackground(new Color(23,92,12));
        GroupLayout layout = new GroupLayout(menu);
        menu.setLayout(layout);
        add(menu,"menu");
        title = new JLabel("<html><h1 style = 'color : white;'>Minesweeper</h1></html>",SwingConstants.CENTER);

        menu.add(title);
        startGame = new JButton("Start Game");
        start = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tL.reset();
                cl.show(getContentPane(),"game");
                setupBoard();
            }
        };
        startGame.addActionListener(start);
        menu.add(startGame);
        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER).addComponent(title).addComponent(startGame)));
        layout.setVerticalGroup(layout.createSequentialGroup().addComponent(title).addComponent(startGame));
        //Game JPanel Setup
        game = new JPanel();
        game.setLayout(new BorderLayout());
        add(game,"game");
        //Game Menu Bar
        gameMenuBar = new JPanel();
        gameMenuBar.setBackground(new Color(23,92,12));
        game.add(gameMenuBar,BorderLayout.NORTH);
        flags = new JLabel();
        back2Menu = new JButton("Back To Menu");
        toMenu = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(getContentPane(),"menu");
                if (gameOverMenu.isVisible()){
                    gameOverMenu.setVisible(false);
                }
            }
        };
        back2Menu.addActionListener(toMenu);

        difficultySelector = new JComboBox(new String[]{"Easy","Medium","Hard"});
        difficultySelector.setSelectedIndex(Arrays.asList(new String[]{"e","m","h"}).indexOf(difficulty));
        difficultySelector.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox source = (JComboBox) e.getSource();
                Object selected = source.getSelectedItem();
                if(!difficulty.equals(selected.toString().substring(0,1).toLowerCase())) {
                    difficulty = selected.toString().substring(0, 1).toLowerCase();
                    source.updateUI();//line that allows everything to update without the user having to click on the combo box a second time
                    setupBoard();
                }
                tL.stop();
                tL.reset();
            }
        });
        tL = new TimerLabel();
        gameMenuBar.add(difficultySelector);
        gameMenuBar.add(back2Menu);
        gameMenuBar.add(flags);
        gameMenuBar.add(tL);

        //Game Grid
        board = new JPanel();
        game.add(board,BorderLayout.CENTER);
        //Game Over Menu
        gameOverMenu = new JDialog();
        gameOverMenu.setLayout(new FlowLayout());

        JPanel statusContainer = new JPanel();
        JPanel buttonsContainer = new JPanel();
        gameOverStatus = new JLabel();
        statusContainer.add(gameOverStatus);
        gameOverMenu.add(statusContainer);

        JButton back2Menu2 = new JButton("Back To Menu");
        back2Menu2.addActionListener(toMenu);
        buttonsContainer.add(back2Menu2);

        playAgain = new JButton("Play Again");
        playAgain.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tL.reset();
                setupBoard();
                gameOverMenu.setVisible(false);
                difficultySelector.updateUI();

            }
        });
        buttonsContainer.add(playAgain);
        gameOverMenu.add(buttonsContainer);
        gameOverMenu.setTitle("Game Over");
        gameOverMenu.setLocation((getLocation().x+ getWidth())/2 - gameOverMenu.getWidth()/2,(getLocation().y + getHeight())/2 - gameOverMenu.getHeight()/2);
        gameOverMenu.setSize(250,200);
        gameOverMenu.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        gameOverMenu.setAlwaysOnTop(true);
        gameOverMenu.setVisible(false);
        //moves game over menu to middle of screen
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                gameOverMenu.setLocation((getLocation().x+ getWidth())/2 - gameOverMenu.getWidth()/2,(getLocation().y + getHeight())/2 - gameOverMenu.getHeight()/2);

            }

            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);
                gameOverMenu.setLocation(getLocation().x+ getWidth()/2 - gameOverMenu.getWidth()/2,getLocation().y + getHeight()/2 - gameOverMenu.getHeight()/2);
            }
        });
        //jframe setup
        setSize(400,400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }




    private void setupBoard(){
        board.removeAll();
        int rows = map.get(difficulty).get("rows");
        int cols = map.get(difficulty).get("cols");
        int mines = map.get(difficulty).get("mines");
        numTilesLeft = rows * cols - mines;
        numFlags = mines;
        flags.setText("\uD83D\uDEA9's: "+numFlags);
        flags.setForeground(Color.red);
        //flags.setFont(flags.getFont().deriveFont(50));
        gameOver = false;
        firstTurn = true;
        board.setLayout(new GridLayout(rows,cols));
        tiles = new Tile[rows][cols];
        for (int r = 0; r < rows; r++){
            for (int c = 0; c < cols; c++){
                tiles[r][c] = new Tile(r, c);
                tiles[r][c].addActionListener(onClick);
                tiles[r][c].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Tile button = (Tile) e.getSource();
                        if (e.getButton() == MouseEvent.BUTTON3){
                            if(gameOver){
                                return;
                            }
                            if(!button.isClicked()){
                                button.setFlagged(!button.isFlagged());

                                if(button.isFlagged()){
                                    if(numFlags ==0){
                                        button.setFlagged(!button.isFlagged());
                                        return;
                                    }

                                    button.setText("\uD83D\uDEA9");
                                    button.setForeground(Color.red);
                                    numFlags -= 1;
                                }
                                else {
                                    button.setText("");
                                    button.setForeground(Color.black);
                                    numFlags+=1;
                                }
                                flags.setText("\uD83D\uDEA9's: "+numFlags);
                            }
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased (MouseEvent e){

                    }

                    @Override
                    public void mouseEntered (MouseEvent e){

                    }

                    @Override
                    public void mouseExited (MouseEvent e){

                    }
                });
                board.add(tiles[r][c]);
            }
        }
        ArrayList<Integer> nums = new ArrayList<Integer>();
        for (int i = 0; i < rows * cols; i++){
            nums.add(i);
        }
        Collections.shuffle(nums);
        for (int j = 0; j < mines; j++){
            tiles[nums.get(j) / cols][nums.get(j) % cols].setAsMine();
            for(int k = nums.get(j) / cols - 1; k <= nums.get(j) / cols + 1; k++){
                for(int l = nums.get(j) % cols - 1; l <= nums.get(j) % cols + 1; l++){
                    if (k >= 0 && k < rows && l >= 0 && l < cols){
                        tiles[k][l].increment();
                    }
                }
            }
        }
        repaint();//line to prevent half tile visual glitch on hard difficulty
    }
}
