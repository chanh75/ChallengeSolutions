package com.mohawk.challenges.book;


public class Minesweeper {

	public String solve(String[] input) throws Exception {
		
		int problemCounter = 1;
		int rowCounter = 0;
		StringBuffer output = new StringBuffer();
		int[] dims = null;
		char[][] grid = null;
		
		// read size line
		for (String line : input) {
			char[] lineChars = line.toCharArray();
			
			if (dims == null) {
				dims = parseSize(lineChars); //System.out.println(dims[0] + ":" + dims[1]);
				output.append("Field #" + problemCounter + '\n');
				problemCounter++;
				
				// define the grid
				grid = new char[dims[0]][dims[1]];
			} else {
				if (rowCounter <= dims[0]) {
					for (int c = 0; c < dims[1]; ++c)
						grid[rowCounter][c] = 
							(lineChars[c] == '*') ? '*' : '0';
					//System.out.println(Arrays.toString(grid[rowCounter]));
					rowCounter++;
				}
				if (rowCounter == dims[0]) {	
					char[][] revealed = showNumbers(grid);
					for (char[] r : revealed)
						output.append(String.valueOf(r)).append('\n');
					
					// reset the counters
					dims = null;
					rowCounter = 0;
				}
			}
		}
		return output.toString();
	}
	
	private char[][] showNumbers(char[][] grid) {
		
		boolean[] template = new boolean[8];
		final int[][] pos = new int[][] { 
				{ -1, -1 },
				{ -1, 0 },
				{ -1, 1 },
				{ 0, -1 },
				{ 0, 1 },
				{ 1, -1 },
				{ 1, 0 },
				{ 1, 1 }
		};
		
		for (int r = 0; r < grid.length; r++) { // rows
			for (int c = 0 ; c < grid[r].length; c++) {
				if (r == 0) {
					template[0] = true; 
					template[1] = true; 
					template[2] = true;
				} else if (r == grid.length - 1) {
					template[5] = true; 
					template[6] = true; 
					template[7] = true;
				}
				if (c == 0) {
					template[0] = true; 
					template[3] = true; 
					template[5] = true;
				} else if (c == grid[r].length - 1) {
					template[2] = true; 
					template[4] = true; 
					template[7] = true;
				}
				if (grid[r][c] == '*') {
					// add 1 to surrounding grid
					for (int i = 0; i < 8; i++) {
						if (!template[i])
							addOne(grid, r + pos[i][0], c + pos[i][1]);
					}
				}
				template = new boolean[8];
			}
		}
		
		return grid;
	}
	
	private void addOne(char[][] grid, int row, int col) {
		char c = grid[row][col];
		if (c != '*') {
			int val = ((int) c) + 1;
			grid[row][col] = (char) (val);
			//System.out.println("new>> " + grid[row][col]);
		}
	}
	
	private int[] parseSize(char[] line) throws Exception {
		int[] dims = new int[2];
		int counter = 0;
		for (char c : line) {
			if (c != ' ') {
				dims[counter] = Integer.parseInt(String.valueOf(c));
				counter++;
			}
			if (counter == 2)
				return dims;
		}
		return null;
	}
	
	public static void main(String[] args) {
		
		String[] input = { 
				"4 4",
				"*...",
				"....",
				".*..",
				"....",
				"3 5",
				"**...",
				".....",
				".*...",
				"0 0"
		};
		
		try {
			Minesweeper m = new Minesweeper();
			System.out.println(m.solve(input));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
