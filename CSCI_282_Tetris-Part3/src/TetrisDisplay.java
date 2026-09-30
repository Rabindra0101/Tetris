/*
 * Rabindra Neupane
 * Display page for positioning sizing of display
 * 04/25/2024
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TetrisDisplay extends JPanel  
{
    
    private TetrisGame game;
    private int start_x;
    private int start_y;
    private int cell_size = 10;
    private int speed;
    private boolean pause;
    private Timer timer;
    
    private Color[] colors = {Color.BLACK, Color.RED, Color.ORANGE, Color.YELLOW,
                              Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN};  
    
    
    public TetrisDisplay(TetrisGame gam, int winHeight, int winWidth)
    {
        game = gam;
        start_x = (winWidth/2) - ((game.getCols() * cell_size)/2);
        start_y = (winHeight/2) - ((game.getRows() * cell_size)/2) - cell_size;
         
        cycleMove();
    }
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        drawWell(g);
        drawFallingBrick(g, this.game.getFallingBrick().position, 
                         this.game.getFallingBrick().getColorNumber());
        
        drawBackground(g);
        scoreBoard(g);
        
        if(game.isBrickOutOfBounds())
        {
            gameOverGraphics(g);
        }
    }
    
    public void drawWell(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillRect(start_x - cell_size, start_y, cell_size,
                   cell_size * game.getRows());
        g.fillRect(start_x - cell_size, start_y + game.getRows() * cell_size,
                   cell_size * game.getCols() + cell_size * 2, cell_size);
        g.fillRect(start_x + game.getCols() * cell_size, start_y, cell_size,
                   cell_size * game.getRows());
        g.drawLine(start_x, start_y, (start_x + (game.getCols() * cell_size)),
                   start_y);

        g.setColor(Color.WHITE);
        g.fillRect(start_x, start_y, game.getCols() * cell_size, 
                   game.getRows() * cell_size);        
    }
    
    private void drawBackground(Graphics g)
    {
        for (int row=0; row < game.backgroundRows(); row++)
        {
            for (int col=0; col < game.backgroundCols(); col++)
            {
                int gridColorCode = game.getSingleGridColor(row, col);
                if (gridColorCode > 0)
                {
                    int drawBrickX = start_x + col * cell_size;
                    int drawBrickY = start_y + row * cell_size;
                    
                    drawGridSquare(g, gridColorCode, drawBrickX, drawBrickY);
                }  
            }
        }
    }
    
    private void drawFallingBrick(Graphics g, int[][] brickPosition, int colorNum)
    {        
        for (int row = 0; row < brickPosition.length; row++)
        {
            int drawBrickX = start_x + (brickPosition[row][1] * cell_size);
            int drawBrickY = start_y + ((brickPosition[row][0] * cell_size));
            drawGridSquare(g, colorNum, drawBrickX, drawBrickY);
        }         
    }
    
    public int translateKey(KeyEvent key)
    {
        
        int keyCode = key.getKeyCode();
       
        if (keyCode == KeyEvent.VK_RIGHT)
        {
            return 1;
        }
        
        else if (keyCode == KeyEvent.VK_LEFT)
        {
            return 2;
        }
        
        else if (keyCode == KeyEvent.VK_SPACE)
        {
            if(this.timer.isRunning())
            {
                pauseGame();
            }
            else
            {
                resumeGame();
            }
        }
        
        else if (keyCode == KeyEvent.VK_N)
        {
           game.newGame();
           this.timer.start();
        }
        
        else if (keyCode == KeyEvent.VK_UP)
        {
            if(this.timer.isRunning())
            {
                game.makeMove(4);
            }
            else
            {
                game.makeMove(4);
                repaint();
            }
        }
        return 0;
    }
    
    private void cycleMove()
    {
        int timerSpeed = 250;
        
        this.timer = new Timer(timerSpeed, new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent ae)
            { 
                if(game.isBrickOutOfBounds())
                {
                    pauseGame();
                }
                else
                {
                    game.makeMove(3);
                    game.makeMove(5);
                    repaint();
                }  
            }
        });
        
        this.timer.start();
        
        this.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent key)
            {
                int keyCode = translateKey(key);
                game.makeMove(keyCode);
            }
        });

        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(true); 
    }
        
    private void drawGridSquare(Graphics g, int colorNum, int x, int y)
    {  
        if( y >= this.start_y )
        {
            g.setColor(colors[colorNum]);
            g.fillRect(x, y, cell_size, cell_size);
            g.setColor(colors[0]);
            g.drawRect(x, y, cell_size, cell_size);
        }   
    }
   
    public void pauseGame()
    {
        this.timer.stop();
    }
    public void resumeGame()
    {
        this.timer.start();
    }
   
    public void gameOverGraphics(Graphics g)           
    {
        int contentX = 130;
        int contentY = 120;
        int boxWidth= 240;
        int boxHeight= 100;
        int contentFontX = 145;
        int contentFontY = 155;
        int newGameFontX = 138;
        int newGameFontY = 190;
        Font gameOverFontStyle = new Font("Arial", Font.BOLD, 32);
        Font newGameFont = new Font("Arial", Font.BOLD, 18);

        Graphics2D g2 = (Graphics2D)g;
        
        g2.setColor(Color.BLACK);
        g2.fillRect(contentX, contentY, boxWidth, boxHeight);

        g2.setColor(Color.WHITE);
        g2.drawRect(contentX, contentY, boxWidth, boxHeight);

        g2.setFont(gameOverFontStyle);
        g2.setColor(Color.WHITE);
        g2.drawString("Game Over !!!", contentFontX, contentFontY);

        g2.setFont(newGameFont);
        g2.setColor(Color.GREEN);
        g2.drawString("Press N to start new game", newGameFontX, newGameFontY);
    }
    
   public void scoreBoard(Graphics g)
   {
        int scoreX = 1;
        int scoreY = 1;
        int boxWidth = 140;
        int boxHeight = 30;
        int contentX = 20;
        int contentY = 22;
        Font scoreFont = new Font("Arial", Font.BOLD, 18);
        int strokeWidth=2;

        Graphics2D g2 = (Graphics2D)g;
        g2.setFont(scoreFont);

        g2.setColor(Color.BLACK);
        g2.fillRect(scoreX, scoreY, boxWidth, boxHeight);
     
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(strokeWidth));
        g2.drawRect(scoreX, scoreY, boxWidth, boxHeight);

        g2.setColor(Color.WHITE);
        String score = "Score: " + game.getScore();
        g2.drawString(score, contentX, contentY);
    }
}
