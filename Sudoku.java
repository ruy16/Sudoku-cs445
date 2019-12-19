/** Project sudoku solver using backtracking
@Auther runyuan Yan
**/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Sudoku {
	//Check if a given solution is complete
	static boolean isFullSolution(int[][] board) {
		for(int i = 0; i < board.length;i++)
		{
			for(int j = 0;j< board[0].length;j++)
			{
				if(board[i][j] == 0){return false;}
			}
		}

		return true;
	}
	//check if the board is violating any rules
	static boolean reject(int[][] board) {
		if(!checkRule1_Row(board) || !checkRule2_Col(board) || !checkRule3_Region(board))
			{
				return true;
			}
		return false;
	}
	//if not futher extend
	static int[][] extend(int[][] board) {
		int[][] newArray = new int[9][9];
		for(int rows = 0; rows <board.length; rows++)
		{
			
			for(int cols = 0;cols<board.length; cols++)
			{
				newArray[rows][cols] = board[rows][cols];
			}
		}
		for(int rows = 0; rows <newArray.length; rows++)
		{
			
			for(int cols = 0;cols<newArray.length; cols++)
			{		
				if(newArray[rows][cols] == 0 )
				{
					newArray[rows][cols] = -1;
					return newArray;
				}
					
			}
		}
		return null;
	}
 
	static int[][] next(int[][] board) {

        for(int j = board.length - 1; j>=0; j-- )
        {
            for(int i = board.length - 1; i>=0; i--)
            {
                if(board[j][i] == -9)
                    return null;
                else if(board[j][i] < 0)
                {
                    board[j][i] --;
                    return board;
                }
            }
        }

        return null;
            
    }

	static void testIsFullSolution() {
		int[][] board = readBoard("testIsfullSolution1.text");
		printBoard(board);
		if(isFullSolution(board))
		{System.out.println("You have a complete solution");}
		
		int[][] board1 = readBoard("testIsfullSolution2.text");
		printBoard(board1);
		if(!isFullSolution(board1))
		{System.out.println("This is not a complete solution");}
		
	}

	static void testReject() {
		//duplicates in a row 
		int[][] board0 = readBoard("testReject0.text");
		printBoard(board0);
		if(reject(board0)) {
			System.out.println("Rejected! Duplicates in a row ");
		}
		else
		{System.out.println("valid");}
		
		//duplicates in a column
		int[][] board1 = readBoard("testReject1.text");
		printBoard(board1);
		if(reject(board1)) {
			System.out.println("Rejected! Duplicates in a column");
		}
		else
		{System.out.println("valid");}
		//duplicate in a region
		int[][] board2 = readBoard("testReject2.text");
		printBoard(board2);
		if(reject(board2)) {
			System.out.println("Rejected!Duplicate in a region");
		}
		else
		{System.out.println("valid");}
		//valid board
		int[][] board3 = readBoard("testReject3.text");
		printBoard(board3);
		if(reject(board3)) {
			System.out.println("Rejected!");
		}
		else
		{System.out.println("valid");}
		
	}

	static void testExtend() {
		int[][] board = readBoard("testExtend.text");
		printBoard(board);
		System.out.println("\n");
		printBoard(extend(board));
		System.out.println("Board extended");
	}

	static void testNext() {
		int[][] board = {
	            {0, -1, 0, 0, 0, 0, 0, 7,0},
	            {0, 4, 6, 0, 0, 5, 2, 0,0},
	            {0, 8, 0, 1, 2, 0, 0, 0,3},
	            {0, 2, 0, 0, 0, 0, 0, 0,0},
	            {0, 0, 8, 7, 0, 0, 1, 2,0},
	            {0, 5, 0, 0, 0, 0, 0, 9,0},
	            {0, 0, 0, 0, 0, 0, 0, 0,0},
	            {0, 0, 0, 0, 0, 0, 0, 0,0},
	            {0, 0, 0, 0, 0, 0, 0, 0,0},
	        };
		printBoard(board);
		int[][] attempBoard = next(board);
		
		System.out.println("\n");
		if(attempBoard == null)
		{
			System.out.println("No possible value in this position");
		}
		else
		{
			printBoard(attempBoard);
			System.out.println("changed to the next possible value");
		}
	}

	static void printBoard(int[][] board) {
		if(board == null) {
			System.out.println("No assignment");
			return;
		}
		for(int i = 0; i < 9; i++) {
			if(i == 3 || i == 6) {
				System.out.println("----+-----+----");
			}
			for(int j = 0; j < 9; j++) {
				if(j == 2 || j == 5) {
					System.out.print(board[i][j] + " | ");
				} else {
					System.out.print(board[i][j]);
				}
			}
			System.out.print("\n");
		}
	}
	//read the board from a file
	static int[][] readBoard(String filename) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(Paths.get(filename), Charset.defaultCharset());
		} catch (IOException e) {
			return null;
		}
		int[][] board = new int[9][9];
		int val = 0;
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 9; j++) {
				try {
					val = Integer.parseInt(Character.toString(lines.get(i).charAt(j)));
				} catch (Exception e) {
					val = 0;
				}
				board[i][j] = val;
			}
		}
		return board;
	}	

	static int[][] solve(int[][] board) {
		if(reject(board)) return null;
		if(isFullSolution(board)) return board;
		int[][] attempt = extend(board);
	
		
		while (attempt != null) {
			int[][] solution = solve(attempt);
			if(solution != null) return solution;
			attempt = next(attempt);
		
		}
		return null;
	}
	//check row
	private static boolean checkRule1_Row(int[][] board)
	{
		for(int i = 0; i<board.length;i++)
		{
			for(int j = 0; j<board[i].length; j++)
			{
				if(board[i][j]!=0)
				{
					int lastNum = board[i][j];
					for(int k = j+1 ; k < board.length ; k++)
				{
					if(Math.abs(lastNum) == Math.abs(board[i][k]))
					{
						return false;
					}
				}
				}
				
			}	
		}
		return true;
	}
	//check collumn 
	private static boolean checkRule2_Col(int[][] board)
	{   
        Set <Integer> testCol = new HashSet<>();
        for(int j = 0; j < board.length; j ++)
        {

            for(int i = 0; i < board.length; i++)
            {
                if(!testCol.add(Math.abs(board[i][j])))
                {   
                	if(board[i][j] == 0)
                		continue;
                	else
                        testCol.clear();
                   	 	return false;
                }
            }
            testCol.clear();

        }
        return true;
	}
	//check region
	private static boolean checkRule3_Region(int[][] board)
	{
		for(int i = 0 ; i < board.length; i+=3)
		{
			for(int j = 0 ; j < board.length ; j+=3)
			{
				ArrayList<Integer> regionArray= new ArrayList<Integer>(board.length);
				for(int k = i ; k<i+3 ; k++)
				{
					for(int l = j ; l< j+3 ; l++)
					{
						if(board[k][l] != 0)
						{
						if(regionArray.contains(Math.abs(board[k][l])))
						{
							return false;
						}
						else
						{
							regionArray.add(Math.abs(board[k][l]));
						}
						}
					}
					}
				regionArray.clear();
				
					
				}
				}
			
	return true;
	}
	//change the values in the board back to position
	private static int[][] changeToPosi(int[][] board)
	{
		for(int x=0; x< board.length ;x++)
		{
			for(int y = 0; y<board.length;y++)
			{
				if(board[x][y] < 0)
				{
					board[x][y] = Math.abs(board[x][y]);
				}
			}
		}
		return board;
	}
	public static void main(String[] args) {
		if(args[0].equals("-t")) {
			testIsFullSolution();
			testReject();
			testExtend();
			testNext();
		} else {
			int[][] board = readBoard(args[0]);
			printBoard(board);
			System.out.println("Solution:");
			printBoard(changeToPosi(solve(board)));
		}
	}
}


