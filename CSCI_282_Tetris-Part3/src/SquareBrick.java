/*
 * Rabindra Neupane
 * This is subclass of TetrisBrick, SquareBrick
 * 04/25/2024
 */

public class SquareBrick extends TetrisBrick 
{
    public SquareBrick(int colorNum, int colNums, int orient)
    {
        super(colorNum, colNums, orient);
    }
    
    @Override
    public int[][] initPosition(int center_x, int center_y) 
    {
        int[][] blockPosition = new int[this.numSegments][2];
        
        blockPosition[0][0] = center_y-2;
        blockPosition[0][1] = center_x;
        blockPosition[1][0] = center_y-2;
        blockPosition[1][1] = center_x + 1;
        blockPosition[2][0] = center_y-1;
        blockPosition[2][1] = center_x;
        blockPosition[3][0] = center_y-1;
        blockPosition[3][1] = center_x + 1;
  
        return blockPosition;
    }
    
    @Override
    public void rotate()
    {
        //no rotation needed for square.
    }
    
    @Override
    public void unrotate()
    {
        //no unrotation needed for square.
    }
}
