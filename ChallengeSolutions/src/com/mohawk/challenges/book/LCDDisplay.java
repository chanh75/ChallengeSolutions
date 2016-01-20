package com.mohawk.challenges.book;

import java.util.ArrayList;
import java.util.Arrays;

public class LCDDisplay {
	
	private enum Move {
		RIGHT1, RIGHT1_DOWN1, RIGHT2_DOWN1, 
		DOWN1, DOWN2, LEFT1_DOWN1, LEFT1_UP1, UP2, 
		RIGHT1_UP1, NONE, SET_INDEX, MOVE_TO_INDEX
	}
	
	private enum Draw {
		RIGHT, DOWN, LEFT, UP, NONE
	}
	
	private static class LCDVector {
		private ArrayList<Object[]> _vectors = new ArrayList<>();
		private int _wlegs, _wbuffer;
		private int _hlegs, _hbuffer;
		public LCDVector setWidthComps(int wlegs, int wbuffer) {
			_wlegs = wlegs;
			_wbuffer = wbuffer;
			return this;
		}
		public LCDVector setHeightComps(int wlegs, int wbuffer) {
			_hlegs = wlegs;
			_hbuffer = wbuffer;
			return this;
		}
		public LCDVector draw(Move move, Draw draw) {
			_vectors.add(new Object[] { move, draw } );
			return this;
		}
		public LCDVector setIndex() {
			_vectors.add(new Object[] { Move.SET_INDEX, Draw.NONE });
			return this;
		}
		public LCDVector moveToIndex() {
			_vectors.add(new Object[] { Move.MOVE_TO_INDEX, Draw.NONE });
			return this;
		}
		public ArrayList<Object[]> getVectors() {
			return _vectors;
		}
		public int getWidth(int size) {
			if (_wlegs == 0)
				return 1;
			else
				return (size * _wlegs) + _wbuffer;
		}
		public int getHeight(int size) {
			if (_hlegs == 0)
				return 1;
			else
				return (size * _hlegs) + _hbuffer;
		}
	}
	
	final private static LCDVector[] VECTOR_TEMPLATE = new LCDVector[10];
	static {
		VECTOR_TEMPLATE[0] = (new LCDVector())
				.setWidthComps(1, 2)
				.setHeightComps(2, 3)
				.draw(Move.RIGHT1, Draw.RIGHT)
				.draw(Move.RIGHT1_DOWN1, Draw.DOWN)
				.draw(Move.DOWN2, Draw.DOWN)
				.draw(Move.LEFT1_DOWN1, Draw.LEFT)
				.draw(Move.LEFT1_UP1, Draw.UP)
				.draw(Move.UP2, Draw.UP);
		VECTOR_TEMPLATE[1] = (new LCDVector())
				.setWidthComps(0, 0)
				.setHeightComps(2, 3)
				.draw(Move.DOWN1, Draw.DOWN)
				.draw(Move.DOWN2, Draw.DOWN);
		VECTOR_TEMPLATE[2] = (new LCDVector())
				.setWidthComps(1, 2)
				.setHeightComps(2, 3)
				.draw(Move.RIGHT1, Draw.RIGHT)
				.draw(Move.RIGHT1_DOWN1, Draw.DOWN)
				.draw(Move.LEFT1_DOWN1, Draw.LEFT)
				.draw(Move.LEFT1_DOWN1, Draw.DOWN)
				.draw(Move.RIGHT1_DOWN1, Draw.RIGHT);
		VECTOR_TEMPLATE[3] = (new LCDVector())
				.setWidthComps(1, 1)
				.setHeightComps(2, 3)
				.draw(Move.NONE, Draw.RIGHT)
				.draw(Move.RIGHT1_DOWN1, Draw.DOWN)
				.setIndex()
				.draw(Move.LEFT1_DOWN1, Draw.LEFT)
				.moveToIndex()
				.draw(Move.DOWN2, Draw.DOWN)
				.draw(Move.LEFT1_DOWN1, Draw.LEFT);
		VECTOR_TEMPLATE[4] = (new LCDVector())
				.setWidthComps(1, 2)
				.setHeightComps(2, 3)
				.draw(Move.DOWN1, Draw.DOWN)
				.draw(Move.RIGHT1_DOWN1, Draw.RIGHT)
				.setIndex()
				.draw(Move.RIGHT1_UP1, Draw.UP)
				.moveToIndex()
				.draw(Move.RIGHT1_DOWN1, Draw.DOWN);
		VECTOR_TEMPLATE[5] = (new LCDVector())
				.setWidthComps(1, 2)
				.setHeightComps(2, 3)
				.setIndex()
				.draw(Move.RIGHT1, Draw.RIGHT)
				.moveToIndex()
				.draw(Move.DOWN1, Draw.DOWN)
				.draw(Move.RIGHT1_DOWN1, Draw.RIGHT)
				.draw(Move.RIGHT1_DOWN1, Draw.DOWN)
				.draw(Move.LEFT1_DOWN1, Draw.LEFT);
		VECTOR_TEMPLATE[6] = (new LCDVector())
				.setWidthComps(1, 2)
				.setHeightComps(2, 3)
				.setIndex()
				.draw(Move.RIGHT1, Draw.RIGHT)
				.moveToIndex()
				.draw(Move.DOWN1, Draw.DOWN)
				.draw(Move.RIGHT1_DOWN1, Draw.RIGHT)
				.draw(Move.RIGHT1_DOWN1, Draw.DOWN)
				.draw(Move.LEFT1_DOWN1, Draw.LEFT)
				.draw(Move.LEFT1_UP1, Draw.UP);
		VECTOR_TEMPLATE[7] = (new LCDVector())
				.setWidthComps(1, 1)
				.setHeightComps(2, 3)
				.draw(Move.RIGHT1, Draw.RIGHT)
				.draw(Move.RIGHT1_DOWN1, Draw.DOWN)
				.draw(Move.DOWN2, Draw.DOWN);
		VECTOR_TEMPLATE[8] = (new LCDVector())
				.setWidthComps(1, 2)
				.setHeightComps(2, 3)
				.draw(Move.RIGHT1, Draw.RIGHT)
				.draw(Move.RIGHT1_DOWN1, Draw.DOWN)
				.draw(Move.LEFT1_DOWN1, Draw.LEFT)
				.setIndex()
				.draw(Move.LEFT1_UP1, Draw.UP)
				.moveToIndex()
				.draw(Move.LEFT1_DOWN1, Draw.DOWN)
				.draw(Move.RIGHT1_DOWN1, Draw.RIGHT)
				.draw(Move.RIGHT1_UP1, Draw.UP);
		VECTOR_TEMPLATE[9] = (new LCDVector())
				.setWidthComps(1, 2)
				.setHeightComps(2, 3)
				.draw(Move.RIGHT1, Draw.RIGHT)
				.draw(Move.RIGHT1_DOWN1, Draw.DOWN)
				.setIndex()
				.draw(Move.LEFT1_DOWN1, Draw.LEFT)
				.draw(Move.LEFT1_UP1, Draw.UP)
				.moveToIndex()
				.draw(Move.DOWN2, Draw.DOWN)
				.draw(Move.LEFT1_DOWN1, Draw.LEFT);
	}
	
	public String display(String input) throws Exception {
		StringBuffer output = new StringBuffer();
		
		String[] lines = input.split(System.lineSeparator());
		for (String line : lines) {
			char[] chars = line.toCharArray();
			int size = 0;
			try {
				size = Integer.parseInt(String.valueOf(chars[0]));
				if (size != 0) {
					if (chars[1] != ' ')
						throw new Exception("Invalid input.");
					output.append(display0(size, Arrays.copyOfRange(chars, 2, chars.length)));
				}
			} catch (NumberFormatException e) {
				throw new Exception("Invalid input>> " + e.getLocalizedMessage());
			}
		}
		
		return output.toString();
	}
	
	private StringBuffer display0(int size, char[] toDisplay) throws Exception {
		StringBuffer out = new StringBuffer(); System.out.println(Arrays.toString(toDisplay));
		// first create the grid
		int w = 0;
		int h = 0;
		char[][] grid = null;
		for (char c : toDisplay) {
			System.out.println("char>> " + c);
			LCDVector vectors = VECTOR_TEMPLATE[Integer.parseInt(String.valueOf(c))];
			w += vectors.getWidth(size);
			w++; // for space
			if (h == 0)
				h = vectors.getHeight(size);
		}
		System.out.println("dim>> " + w + ":" + h);
		grid = new char[h][w];
		// fill with whitespace
		for (char[] cells : grid)
			Arrays.fill(cells, ' ');
		
		// populate the grid
		int y = 0; int x = 0; int startx = 0;// start at 0,0
		int indexY = 0; int indexX = 0;
		for (char c : toDisplay) {
			System.out.println("draw>> " + c + ":" + x);
			LCDVector vectors = VECTOR_TEMPLATE[Integer.parseInt(String.valueOf(c))];
			for (Object[] vector : vectors.getVectors()) {
				switch((Move) vector[0]) {
					case DOWN1: System.out.println("down 1");
						y += 1;
						break;
					case DOWN2: System.out.println("down 2");
						y += 2;
						break;
					case RIGHT1: System.out.println("right 1");
						x += 1;
						break;
					case RIGHT1_DOWN1:
						y += 1;
						x += 1;
						break;
					case RIGHT1_UP1:
						y -= 1;
						x += 1;
						break;
					case RIGHT2_DOWN1:
						y += 1;
						x += 2;
						break;
					case LEFT1_DOWN1:
						y += 1;
						x -= 1;
						break;
					case LEFT1_UP1:
						y -= 1;
						x -= 1;
						break;
					case UP2:
						y -= 2;
						break;
					case SET_INDEX:
						indexY = y;
						indexX = x;
						break;
					case MOVE_TO_INDEX:
						y = indexY;
						x = indexX;
					case NONE:
						break;
					default:
						throw new Exception("Move not defined>> " + (Move) vector[0]);
				}
				for (int i = 0; i < size; i++) {
					switch((Draw) vector[1]) {
						case DOWN: System.out.println("draw down>> " + x + ":" + y);
							grid[y][x] = '|';
							if (i != size - 1) // don't move on last draw
								y++;
							break;
						case UP: System.out.println("draw up>> " + x + ":" + y);
							grid[y][x] = '|';
							if (i != size - 1) // don't move on last draw
								y--;
							break;
						case RIGHT: System.out.println("draw right>> " + x + ":" + y);
							grid[y][x] = '-';
							if (i != size - 1) // don't move on last draw
								x++;
							break;
						case LEFT: System.out.println("draw left>> " + x + ":" + y);
							grid[y][x] = '-';
							if (i != size - 1) // don't move on last draw
								x--;
							break;
						case NONE:
							break;
						default:
							throw new Exception("Draw not defined>> " + (Draw) vector[1]);
					}
				}
			}
			// add LCD whitespace, and reset y
			System.out.println("width>> " + vectors.getWidth(size));
			startx += vectors.getWidth(size) + 1;
			x = startx;
			y = 0;
		}
		//System.out.println(grid.length);
		for (char[] c1 : grid) {
			for (char c2 : c1)
				out.append(c2);
			out.append(System.lineSeparator());
		}
		//System.out.println(out);
		return out;
	}
	
	public static void main(String[] args) {
		
		String input = "2 12345" + System.lineSeparator() + 
				"3 67890" + System.lineSeparator() +
				"0 0";
		
		try {
			LCDDisplay lcd = new LCDDisplay();
			System.out.println(lcd.display(input));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
