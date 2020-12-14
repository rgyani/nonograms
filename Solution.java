class Solution
{
    public static void main(String[] args)
    {
        //        String[] nonogram = {"C BA CB BB F AE F A B", "AB CA AE GA E C D C"};
        //        String[] nonogram = {"D C BB A A", "AC C B C A"};
        String[] nonogram = {"C AAA C AA AA", "AA AB C AB AA"};

        boolean[][] grid = solve(nonogram[0], nonogram[1]);
        if(grid != null)
        {
            for(int i = 0;i<grid.length;i++)
            {
                for (int j = 0; j < grid[0].length; j++)
                    System.out.print(grid[i][j] ? "# " : ". ");

                System.out.print(" ");

                for (int j = 0; j < nonogram[0].split(" ")[i].length(); j++)
                    System.out.print((nonogram[0].split(" ")[i].toCharArray()[j] - 'A' + 1) + " ");
                System.out.println("");
            }

            int len = 0;
            for(String x : nonogram[1].split(" "))
                len = Math.max(len, x.length());

            for(int i = 0;i<len;i++)
            {
                for(int j = 0;j<nonogram[1].split(" ").length;j++)
                {
                    System.out.print(
                            nonogram[1].split(" ")[j].toCharArray().length > i ?
                                    nonogram[1].split(" ")[j].toCharArray()[i] - 'A' + 1 : " ");

                    System.out.print(" ");
                }
                System.out.println("");
            }

        }
        else
            System.out.println("No result found");
    }

    static boolean[][] solve(String xRequirements, String yRequirements)
    {
        boolean[][] grid = new boolean[xRequirements.split(" ").length][yRequirements.split(" ").length];

        if(fillHorizontal(xRequirements.split(" "), yRequirements.split(" "), grid, 0, 0, 0))
            return grid;

        return null;
    }


    /***
     * We solve the nonogram using Backtracking, that means, for each row, we place the 1's based on the horizontal row requirements,
     * and then verify whether it leads to the correct vertical requirements. If correct, then we move to the next row, else,
     * we backtrack and try a different horizontal filling.
     * @param xRequirements The Array of horizontal requirements
     * @param yRequirements The Array of vertical requirements
     * @param grid          The nonogram grid containing 0s initially
     * @param i             Current horizontal position on the grid
     * @param j             Current vertical position on the grid
     * @param x             Current Horizontal requirement index
     * @return
     */
    static boolean fillHorizontal(String[] xRequirements, String[] yRequirements, boolean[][]grid, int i, int j, int x)
    {
        int height = grid.length;
        int width = grid[0].length;

        // Check whether we have crossed the last row
        if(j == height)
            return true;

        // If we filled all the requirements for this row, move to the next row
        if(xRequirements[j].length() == x)
            return fillHorizontal(xRequirements, yRequirements, grid, 0, j + 1, 0);

        // How many consecutive 1's we need now
        int len = xRequirements[j].toCharArray()[x] - 'A' + 1;

        // Make sure we have enough space
        if( i + len > width)
            return false;

        // Fill the grid with the required number of 1's
        for(int k=0;k<len;k++)
            grid[j][i+k] = true;

        // Verify the vertical requirements are not broken due to this filling
        if(verifyVertical(xRequirements, yRequirements, grid, i , j, len) &&
                // Try to fill the next horizontal requirement, by incrementing current position and current horizontal reqt.
                fillHorizontal(xRequirements, yRequirements, grid, i + len + 1, j, x+1))
            return true;
        else
        {
            // IF we failed, we reset the last grid state
            for(int k=0;k<len;k++)
                grid[j][i+k] = false;

            // And try to fill the horizontal reqt at current position + 1
            i+=1;
            return fillHorizontal(xRequirements, yRequirements, grid, i, j, x);
        }
    }

    private static boolean verifyVertical(String[] xRequirements, String[] yRequirements, boolean[][] grid, int i, int j, int len)
    {
        int height = grid.length;
        int width = grid[0].length;

        //We marked len grid positions at (i, j), so now we check those vertical positions
        for(int l=0;l<len;l++)
        {
            boolean start = false;
            int length = 0;
            int index = -1;

            //scan the column i+l
            for (int k = 0; k < height; k++)
            {
                if (grid[k][i+l])
                {
                    //make sure we have something to match against
                    if (yRequirements[i+l].length() == 0)
                        return false;

                    //find start and length
                    index += !start ? 1 : 0;
                    length += 1;
                    start = true;
                }
                else
                {
                    if (start)
                    {
                        //make sure we have something to match against
                        if (yRequirements[i+l].length() <= index)
                            return false;

                        //if consecutive vertical 1's count is equal to the vertical requirement for this column
                        if (yRequirements[i+l].toCharArray()[index] - 'A' + 1 - length < 0)
                            return false;
                    }

                    start = false;
                    length = 0;
                }
            }

            //IF we reached end of grid, and still have 1s running
            if (start)
            {
                if (yRequirements[i+l].length() <= index)
                    return false;

                // We match this against the last vertical reqt
                if (yRequirements[i+l].toCharArray()[index] - 'A' +1 - length < 0)
                    return false;
            }
        }
        return true;
    }
}
