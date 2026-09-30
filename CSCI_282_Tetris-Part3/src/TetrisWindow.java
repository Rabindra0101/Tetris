/*
 * Rabindra Neupane
 * Code for tetris game window size, width, number of rows and columns and main method
 * 04/25/2024
 */

import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class TetrisWindow extends javax.swing.JFrame
{
    private TetrisGame game;
    private TetrisDisplay display;
    private final int win_height = 400;
    private final int win_width = 500;
    private final int game_rows = 20;
    private final int game_cols = 15;
    
    public TetrisWindow() 
    { 
        this.setTitle("Tetris Game: Rabindra Neupane");
        this.setSize(this.win_width, this.win_height);
        
        game = new TetrisGame(game_rows, game_cols);
        display = new TetrisDisplay(game, this.win_height, this.win_width);
        this.add(display);
        
        this.setResizable(false);

        this.setLocationRelativeTo(null);
        
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent event) 
            {
                game.highScoreToFile(game.getScore());
            }
        });
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        gameMenu();
        game.spawnBrick();    
    }
    
    public int getWinWidth()
    {
        return win_width; 
    }
    
    public int getWinHeight()
    { 
        return win_height; 
    }
    
    public void gameMenu()
    {
        JMenuBar menu = new JMenuBar();
        this.setJMenuBar(menu);
        
        JMenu gamePullDown = new JMenu("Game");
        menu.add(gamePullDown);
        
        JMenuItem newGame = new JMenuItem("New Game");
        gamePullDown.add(newGame);
        newGame.addActionListener(this::startNewGame);
        
        
        JMenuItem saveGame = new JMenuItem("Save Game");
        gamePullDown.add(saveGame);
        saveGame.addActionListener(this::saveGameToFile);
        
        JMenuItem retrieveGame = new JMenuItem("Retrieve Game");
        gamePullDown.add(retrieveGame);
        retrieveGame.addActionListener(this::retrieveGameFromFile);
        
        JMenu scorePullDown = new JMenu("Score");
        menu.add(scorePullDown);
        
        JMenuItem leaderboard = new JMenuItem("Leaderboard");
        scorePullDown.add(leaderboard);
        leaderboard.addActionListener(this::showHighScoreLeaderboard);
        
        JMenuItem clearScore = new JMenuItem("Clear Score");
        scorePullDown.add(clearScore);
        clearScore.addActionListener(this::clearHighScoreFile);   
    }
    
    private void startNewGame(ActionEvent e)
    {
        game.newGame();
    }
    
    private void saveGameToFile(ActionEvent e) 
    {
        game.saveToFile();
    }
    
    private void retrieveGameFromFile(ActionEvent e) 
    {
        JFileChooser explorer = new JFileChooser();
        explorer.setCurrentDirectory(new File("."));
        explorer.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int result = explorer.showOpenDialog(TetrisWindow.this);
        if (result == JFileChooser.APPROVE_OPTION)
        {
            File gameFile = explorer.getSelectedFile();
            String g = gameFile.toString();
            String[] filePathArr = g.split("/");
            String filename = filePathArr[filePathArr.length - 1];
            game.retrieveFromFile(filename);
        }
    }
    
    private void showHighScoreLeaderboard(ActionEvent e) 
    {
        game.highScoreLeaderboard();
    }
    
    private void clearHighScoreFile(ActionEvent e) 
    {
        game.clearHighScoreFile();
    }
    
    public static void main(String args[]) 
    {
        new TetrisWindow();
    }
}
