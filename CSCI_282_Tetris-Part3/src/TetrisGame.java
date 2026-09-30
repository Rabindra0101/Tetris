/*
 * Rabindra Neupane
 * Creating subclass for the TetrisWindow, TetrisGame.
 * 04/25/2024
 */

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class TetrisGame {
    
    private TetrisBrick fallingBrick;
    private int[][] background;
    private int rows;
    private int cols;
    private int numBrickTypes = 7;
    private Random randomGen;
    private int color;
    private int score = 0;
    private int state;
    
    private int scoreIncrOneRow;
    private int scoreIncrTwoRow;
    private int scoreIncrThreeRow;
    private int scoreIncrFourRow;
    
    public TetrisGame(int rows, int cols)
    {
        this.rows = rows;
        this.cols = cols;
        
        this.randomGen = new Random();
        this.background = new int[rows][cols];
        
        spawnBrick();   
    }
    
    public void initBoard(int startX, int startY, int cellSize, Graphics g)
    {
        
    }
    
    public void newGame()
    {
        highScoreToFile(getScore());
        this.background = new int[rows][cols];
        this.score = 0;
        spawnBrick();
    }
    
    public int fetchBoardPosition(int row, int col)
    {
        return background[row][col];
    }
    
    public void transferColor()
    {      
        int [][] fallingBrickPosition = this.fallingBrick.getPosition();
        
        for (int seg = 0; seg < this.fallingBrick.getNumSegments(); seg++)
        {
            int row = fallingBrickPosition[seg][0];
            int col = fallingBrickPosition[seg][1];
            if(row >= 0)
            {
                this.background[row][col] = this.getFallingBrickColor();
            }   
        }      
    }
    
    public void spawnBrick() 
    {
        int brickOrientation = 0;
        int randomBrick = randomGen.nextInt(numBrickTypes);

        switch (randomBrick) 
        {
            case 0:
                fallingBrick = new ElBrick(1, this.cols, brickOrientation);
                break;
            case 1:
                fallingBrick = new EssBrick(2, this.cols, brickOrientation);
                break;
            case 2:
                fallingBrick = new JayBrick(3, this.cols, brickOrientation);
                break;
            case 3:
                fallingBrick = new LongBrick(4, this.cols, brickOrientation);
                break;
            case 4:
                fallingBrick = new SquareBrick(5, this.cols, brickOrientation);
                break;
            case 5:
                fallingBrick = new StackBrick(6, this.cols, brickOrientation);
                break;
            case 6:
                fallingBrick = new ZeeBrick(7, this.cols, brickOrientation);
                break;
            default:
                break;
        }
    }
           
    public void makeMove(int key) 
    {
        int[][] brickPosition = this.fallingBrick.getPosition();
        int numSegments = this.fallingBrick.getNumSegments();
        
        scoreIncrOneRow = 100;
        scoreIncrTwoRow = 300;
        scoreIncrThreeRow = 600;
        scoreIncrFourRow = 1200;

        if (key == 1) //check move right
        {
            for (int seg = 0; seg < numSegments; seg++) 
            {
                int segRow = brickPosition[seg][0];
                int segCol = brickPosition[seg][1];

                if (segCol >= this.cols - 1) 
                {
                    return;
                }
                
                if (segRow >= 0) 
                {
                    for (int col = this.cols - 1; col > segCol; col--) 
                    {
                        if (this.background[segRow][col] > 0) 
                        {
                            return;
                        }
                    }
                }    
            }
            
            this.fallingBrick.moveRight();   
        } 
        
        else if (key == 2) //check move left
        {
            for (int seg = 0; seg < numSegments; seg++) 
            {
                int segRow = brickPosition[seg][0];
                int segCol = brickPosition[seg][1];
                
                if (segCol <= 0) 
                {
                    return;
                }
                
                if (segRow >= 0) 
                {
                    for (int col = 0; col < segCol; col++) 
                    {
                        if (this.background[segRow][col] > 0) 
                        {
                            return;
                        }
                    }
                }    
            }
            this.fallingBrick.moveLeft();
        }   
        
        else if (key == 3) //check move BottomEdge
        {
            if (this.fallingBrick.getBottomEdge() < this.rows - 1)
            {
                for (int seg = 0; seg < numSegments; seg++)
                {
                    int nxtCol = brickPosition[seg][1];
                    int nxtRow = brickPosition[seg][0] + 1;
                    if (nxtRow >= 0 && this.background[nxtRow][nxtCol] > 0)
                    {
                        transferColor();
                        spawnBrick();
                        return;
                    }      
                }                
                this.fallingBrick.moveDown();
            }
            else
            {
                transferColor();
                spawnBrick();
            }    
        }
        
        else if(key == 4) //check rotation
        {
            for (int seg=0; seg < numSegments; seg++)
            {
                int segRow = brickPosition[seg][0];
                int segCol = brickPosition[seg][1];
                               
                if (segRow <= 0)
                {
                    return;
                }
                
                if(segRow >= this.rows || segCol >= this.cols || segCol <= 0 || segRow <= 0)
                {
                    this.fallingBrick.unrotate();
                    break;
                }
            }

            if (this.fallingBrick.orientation == 0 && validateMove())
            {
                this.fallingBrick.orientation++;
                if (!validateMove()) 
                {
                    this.fallingBrick.orientation--; 
                }
            }

            else if(validateMove())
            {
                 this.fallingBrick.rotate();
            }
        }
        
        else if(key == 5) // clear the lines
        {
            int linesCleared = 0;
            
            for (int r = this.rows - 1; r >= 0; r--) 
            {
                boolean lineFilled = true;

                for (int c = 0; c < this.cols; c++) 
                {
                    if (this.background[r][c] == 0) 
                    {
                        lineFilled = false;
                        break;
                    }
                }

                if (lineFilled)
                {
                    linesCleared++;
                    clearLine(r);
                    r++;
                }
            }

            // score increment with switch
            if(linesCleared == 1)
            {
                this.score += this.scoreIncrOneRow;
            }
            else if(linesCleared == 2)
            {
                this.score += this.scoreIncrTwoRow;
            }
            else if (linesCleared == 3)
            {
                this.score += this.scoreIncrThreeRow;
            }
            else if(linesCleared == 4 && linesCleared == 5)
            {
                this.score += this.scoreIncrFourRow;
            }
        } 
    }
        
    protected boolean validateMove()
    {   
        int[][] brickPosition = this.fallingBrick.getPosition();
        int numSegments = this.fallingBrick.getNumSegments();
        
        for (int seg = 0; seg < numSegments; seg++)
        {
            int segRow = brickPosition[seg][0];
            int segCol = brickPosition[seg][1];
            
            
            if (this.fallingBrick.getBottomEdge() >= this.rows-1)
            {
                return false;
            }
            if (segRow+1 >= 0 && this.background[segRow+1][segCol] > 0)
            {
                return false;
            }  
           
            // checking right            
            if(segRow > 0)
            {
                for(int col = this.cols-1; col > segCol; col--)
                {
                    if (this.background[segRow][col] > 0)
                    {
                        return false;
                    }
                }
            }  
            //checking left            
            if(segRow >= 0)
            {
                for(int col = 0; col < segCol; col++)
                {
                    if (this.background[segRow][col] > 0)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
      
    public boolean isBrickOutOfBounds()
    {
        int topRow = 0;
        for(int col = 0; col < this.cols; col++)
        {
            if(this.background[topRow][col] > 0)
            {
                return true;
            }
        }
        return false;
    }  

    protected TetrisBrick getFallingBrick() 
    {
        return this.fallingBrick; 
    }

    public int getFallingBrickColor() 
    {
        return this.fallingBrick.getColorNumber(); 
    } 
        
    public int getNumSegs()
    {
        return this.fallingBrick.numSegments;
    }
    
    public int getState() 
    {
        return state;
    }
    
    public int getScore()
    {
        return score;
    }

    public int getSegRow(int segment) 
    {
        return this.fallingBrick.position[segment][0]; 
    }
    
    public int getSegCol(int segment) 
    {
        return this.fallingBrick.position[segment][1]; 
    }
    
    public int getRows() 
    {
        return rows; 
    }
    
    public int getCols() 
    {
        return cols; 
    }
    
    public int backgroundRows() 
    { 
        return this.background.length;
    }
    
    public int backgroundCols() 
    {
        return this.background[0].length; 
    }
    
    public int getSingleGridColor(int row, int col) 
    {
        return this.background[row][col];
    }  

    public void clearLine(int r)
    {
        //for clearing the line
        for(int i=0; i < this.cols; i++)
        {
            this.background[r][i] = 0;
        }
        
        //for shifting the brick down after the line is cleared.
        for (int tempRow = r; tempRow>0; tempRow--) 
        {
            for(int tempCol = 0; tempCol < this.cols; tempCol++)
            {
                this.background[tempRow][tempCol] = this.background[tempRow - 1][tempCol];
            }
        }
    }
    
    private String randomFileNameGenerator()
    {
        UUID randomString = UUID.randomUUID();
        return randomString.toString().replaceAll(("_"), "");
    }
    
    public void highScoreToFile(int highScore)
    {
        if (highScore <= 0)
            return;
        File file = new File("highScore.csv");
        
        try(FileWriter writer = new FileWriter(file, true);)
        {           
            writer.write(highScore+"");
            writer.write("\n");
        }
        catch(IOException e)
        {
            String errmsg = "Warning: Cannot write high score data ";
            JOptionPane.showMessageDialog(null, errmsg, "Write Fail" , 2);
        }
    }
    
    public void highScoreLeaderboard() 
    {
        File leaderboardFile = new File("highScore.csv");
        StringBuilder leaderboard = new StringBuilder();

        try (Scanner lineScanner = new Scanner(leaderboardFile).useDelimiter("[,\n]"))
        {
            int scoresNums = 0;

            while (lineScanner.hasNextLine()) 
            {
                lineScanner.nextLine();
                scoresNums++;
            }
            
            if (scoresNums > 0) 
            {
                int[] highScores = new int[scoresNums];

                Scanner scanner = new Scanner(leaderboardFile).useDelimiter("[,\n]");

                for (int i = 0; i < scoresNums; i++) 
                {
                    highScores[i] = scanner.nextInt();
                }

                for (int i = 0; i < scoresNums - 1; i++) 
                {
                    int maxIndex = i;
                    for (int j = i + 1; j < scoresNums; j++) 
                    {
                        if (highScores[j] > highScores[maxIndex]) 
                        {
                            maxIndex = j;
                        }
                    }
                    int temp = highScores[maxIndex];
                    highScores[maxIndex] = highScores[i];
                    highScores[i] = temp;
                }

                for (int score : highScores) 
                {
                    leaderboard.append("<html><b><u>").append(score).append("</u></b></html>\n");
                }
                JOptionPane.showMessageDialog(null, leaderboard.toString(), "Your High Scores!!!", 1);
            }
        } 
        catch (IOException e) 
        {
            String errMsg = "Warning: Unable to get the data!!";
            JOptionPane.showMessageDialog(null, errMsg, "Read Fail", JOptionPane.WARNING_MESSAGE);
        }
    } 
    
    public void clearHighScoreFile()
    {
        File file = new File("highScore.csv");
        try(PrintWriter writer = new PrintWriter(file);)
        {
            writer.print("");
            writer.close();
            JOptionPane.showMessageDialog(null, "You all high scores has been cleared",
                                          "High Score Cleared!", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(IOException e)
        {
            String errMsg = "Warning: Unable to clear high scores!!";
            JOptionPane.showMessageDialog(null, errMsg, "Clear Fail", JOptionPane.WARNING_MESSAGE);
        }       
    }
    
    public void saveToFile()
    {
        String randomFileName = this.randomFileNameGenerator();
        File file = new File(randomFileName+".csv");
        
        try(PrintWriter saveGame = new PrintWriter(file))
        {   
            for(int r = 0; r < this.rows; r++)
            {
                for(int c=0; c < this.cols; c++)
                {
                    saveGame.write(this.background[r][c]+",");
                }
                saveGame.write("\n");
            }
            saveGame.close();
        }
        catch(IOException e)
        {
            String errMsg = "Warning: Unable to save to file!!";
            JOptionPane.showMessageDialog(null, errMsg, "Save Fail", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void retrieveFromFile(String filename)
    {    
        File file = new File(filename);
        try(Scanner scan = new Scanner(file).useDelimiter("[,\n]"))
        {   
            for(int r=0; r < this.rows; r++)
            {
                for(int c=0; c < this.cols; c++)
                {
                    this.background[r][c] = scan.nextInt();
                }
                scan.nextLine();
            }
            scan.close();
        }
        catch(IOException e)
        {
            String errMsg = "Warning: Unable to Retrieve from file!!";
            JOptionPane.showMessageDialog(null, errMsg, "Retrieve Fail", JOptionPane.WARNING_MESSAGE);
        }
    }
}
