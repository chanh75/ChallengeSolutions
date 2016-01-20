package com.mohawk.challenges.book;

public class MazeSolver {

	private static class CompleteException extends Exception {
		
	}
	
	private enum Position {
		TOP, RIGHT, BOTTOM, LEFT
	}
	
	public String[] solve(String[] lines) throws Exception {
		
		if (lines.length == 0 || lines[0].length() == 0)
			return new String[0];
		
		// read into 2-dim array
		char[][] arr = new char[lines.length][lines[0].length()];
		for (int r = 0; r < lines.length; r++) {
			int clen = lines[r].length();
			for (int c = 0; c < clen; c++)
				arr[r][c] = lines[r].charAt(c);
		}
		
		// find place to start
		int yS = 0, xS = 0;
		for (int i = 0; i < lines[0].length(); ++i) {
			if (lines[0].charAt(i) == ' ')
				xS = i;
		}
		 
		// move
		try {
			move(arr, Position.TOP, yS, xS);
		} catch (CompleteException e) {
			
		}
			
		// change char[][] array back to line array
		String[] retLines = new String[arr.length];
		for (int r = 0; r < arr.length; r++)
			retLines[r] = new String(arr[r]);
		
		
		return retLines;
	}
	
	private void move(char[][] arr, Position pos, int y, int x) throws Exception {
		mark(arr, y, x);
		
		// go clockwise
		int len = arr.length - 1;
		while (len != y) { // repeat until we get to the bottom of the maz
			
			if (pos != Position.RIGHT && arr[y][x + 1] == ' ') { // right
				move(arr, Position.LEFT, y, x + 1);
				pos = Position.RIGHT;
			} else if (pos != Position.BOTTOM && arr[y + 1][x] == ' ') { // down
				move(arr, Position.TOP, y + 1, x);
				pos = Position.BOTTOM;
			} else if (pos != Position.LEFT && arr[y][x - 1] == ' ') { // left
				move(arr, Position.RIGHT, y, x - 1);
				pos = Position.LEFT;
			} else if (pos != Position.TOP && arr[y - 1][x] == ' ') { // up
				move(arr, Position.BOTTOM, y - 1, x);
				pos = Position.TOP;
			} else { // dead end, go back
				
				unmark(arr, y, x);
				return;
			}
		}
		
		 // solved
		throw new CompleteException();
	}
	
	private void unmark(char[][] arr, int y, int x) throws Exception {
		mark0(arr, y, x, ' ');
	}
	private void mark(char[][] arr, int y, int x) throws Exception {
		mark0(arr, y, x, '*');
	}
	private void mark0(char[][] arr, int y, int x, char c) throws Exception {
		if (arr[y][x] != ' ' && arr[y][x] != '*')
			throw new Exception("Current position is not empty to mark>> " + y + ":" + x);
		
		arr[y][x] = c;
	}
	
	public static void main(String[] args) {
		String[] maze = new String[] {
			"----- +----+-------+",
			"|     |    |       |",
			"| --+ | -+ | | ----+",
			"|   | |  | | |     |",
			"+-  | +--+ | +---+ |",
			"|   |      |     | |",
			"| --+--+---+-+- +--+",
			"|   |  |   | |     |",
			"+-+ +- | +-+ +---+ |",
			"| | |    | |     | |",
			"|   | ++-+ +---- | |",
			"| +-+    |         |",
			"| |   || +---- +-  |",
			"|   + ++ |     |   |",
			"+---+ ||-+---- | | |",
			"|     ||       | +-+",
			"| ----++---- +-+ | |",
			"|            |     |",
			"+------------+ ----+"
		};
		try {
			String[] solved = (new MazeSolver()).solve(maze);
			
			System.out.println("Solved>>");
			for (String s : solved)
				System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
