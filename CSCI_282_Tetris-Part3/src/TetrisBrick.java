/*
 * Rabindra Neupane
 * This is the SuperClass TetrisBrick, which will be responsible for movement
   and positioning of TetrisBricks 
 * 04/25/2024
 */

public abstract class TetrisBrick 
{
    protected int numSegments = 4;
    protected int[][] position = new int[numSegments][2];
    protected int colorNum;
    protected int orientation;
    
    private int colNum;
    
    
    public TetrisBrick(int colorNum, int colNum, int orient)
    {
        this.colorNum = colorNum;
        this.colNum = colNum;
        this.position = this.initPosition((this.colNum/2), 0);
        
        this.orientation = orient++;
    }   
    
    public int getColorNumber() 
    { 
        return this.colorNum; 
    }
    
    public abstract int[][] initPosition(int center_x, int center_y);
     
    public abstract void rotate();
    
    public abstract void unrotate();
      
    public void moveLeft()
    {
        for( int seg = 0; seg < numSegments; seg++)
        {
            if (position[seg][1] < 1)
            {
                break;
            }else{
                position[seg][1] -= 1;
            }
        }
    }
    
    public void moveRight()
    {
        for (int seg = 0; seg < numSegments; seg++)
        {
            if (position[seg][1] < this.colNum)
            {
                position[seg][1] += 1;
            }else{
                break;
            }
        }    
    }
    
    public void moveUp()
    {
        for ( int row = 0; row < numSegments; row++)
        {
            position[row][0] -= 1;
        }
    }
    
    public void moveDown()
    {
        for ( int row = 0; row < numSegments; row++)
        {
            position[row][0] += 1;
        }
    }
    
    public int[][] getPosition() 
    { 
        return this.position; 
    }
    
    public int getNumSegments()
    {
        return this.numSegments;
    }
    
    public int getOrientation() 
    {
        return this.orientation;
    }
    
    public int getBottomEdge()
    {
        int bottomRow = 0;
        for (int seg = 0; seg < this.numSegments; seg++)
        {                       
            if (this.position[seg][0] > bottomRow)
            {
                bottomRow = this.position[seg][0];
            }
        }
        return bottomRow;
    }
}
