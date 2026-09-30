/*
 * Rabindra Neupane
 * This is subclass of TetrisBrick, LongBrick
 * 04/25/2024
 */

public class LongBrick extends TetrisBrick
{
    public LongBrick(int colorNum, int colNums, int orient)
    {
        super(colorNum, colNums, orient);
    }
    
    @Override
    public int[][] initPosition(int center_x, int center_y) 
    {
        int[][] blockPosition = new int[this.numSegments][2];
        
        blockPosition[0][0] = center_y-1;
        blockPosition[0][1] = center_x - 2;
        blockPosition[1][0] = center_y-1;
        blockPosition[1][1] = center_x - 1;
        blockPosition[2][0] = center_y-1;
        blockPosition[2][1] = center_x;
        blockPosition[3][0] = center_y-1;
        blockPosition[3][1] = center_x + 1;
        
        return blockPosition;
    }

    @Override
    public void rotate()
    {
        if(this.orientation == 1)
        {
            this.position[0][0] -= 2;
            this.position[0][1] += 2;
            this.position[1][0] -= 1;
            this.position[1][1] += 1;
            this.position[3][0] += 1;
            this.position[3][1] -= 1;
            
            this.orientation = 2;
        }
        else
        {
            unrotate();
        }
    }
    
    @Override
    public void unrotate()
    {
        this.position[0][0] += 2;
        this.position[0][1] -= 2;
        this.position[1][0] += 1;
        this.position[1][1] -= 1;
        this.position[3][0] -= 1;
        this.position[3][1] += 1;
            
        this.orientation = 0;
    }
}
