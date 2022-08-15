package com.paulfries.bridgesbuilder;

import java.io.PrintWriter;
import java.util.Random;

public class BridgesBuilder {

	public static final char HORIZONTAL1 = '\u2015';
	public static final char VERTICAL1 = '|';
	public static final char HORIZONTAL2 = '\u2550';
	public static final char VERTICAL2 = '\u2016';
	public static final char ISLAND = 'O';
	
	
	static int sizeX = 30;
	static int sizeY = 30;
	static int biggestNumber = 100;
	static char[][] layout = new char[sizeX][sizeY];
	static Random rand = new Random(); //seed with bug: 793654608
	static PrintWriter printWriter = new PrintWriter(System.out,true);
	
	class Coordinate {
		private int x;
		private int y;
		
		public Coordinate(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
		
		void move(Direction direction) {
			x = x + direction.getDeltaX();
			y = y + direction.getDeltyY();
		}
		
		char getValueInLayout(char[][] layout) {
			return layout[x][y]; 
		}

		void setValueInLayout(char[][] layout, char newValue) {
			layout[x][y] = newValue; 
		}
		public boolean isInside() {
			return x >= 0 && x < sizeX && y >= 0 && y < sizeY;
		}
	}

	class Direction {
		private int deltaX;
		private int deltaY;
		public Direction(int deltaX, int deltyY) {
			super();
			this.deltaX = deltaX;
			this.deltaY = deltyY;
		}
		public int getDeltaX() {
			return deltaX;
		}
		public int getDeltyY() {
			return deltaY;
		}
		public char getChar() {
			if(deltaX != 0) return '\u2015';
			else return '|';
		}
		public char getDoubleChar() {
			if(deltaX != 0) return '\u2550';
			else return '\u2016';
		}
	}

	public void run() {
		initLayout();
		
		int positionX = rand.nextInt(sizeX);
		int positionY = rand.nextInt(sizeY);
		
		// Set initial island
		Coordinate coordinate = new Coordinate(positionX, positionY);
		coordinate.setValueInLayout(layout, 'O');
		
		while(countIslands() < biggestNumber) {
			double fiftyFifty = rand.nextDouble();
			int directionRandom = rand.nextInt(4);
			Direction direction = null;
			int lengthOfBridge = 0;
			if(directionRandom == 0) {
				if(coordinate.getX() >= sizeX-2) continue;
				direction = new Direction( 1, 0);
				lengthOfBridge = rand.nextInt(sizeX-positionX-1);
			}
			if(directionRandom == 1) {
				if(coordinate.getX() <= 1) continue;
				direction = new Direction(-1, 0);
				lengthOfBridge = rand.nextInt(positionX-1);
			}
			if(directionRandom == 2) {
				if(coordinate.getY() >= sizeY-2) continue;
				lengthOfBridge = rand.nextInt(sizeY-positionY-1);
				direction = new Direction( 0, 1);
			}
			if(directionRandom == 3) {
				if(coordinate.getY() <= 1) continue;
				direction = new Direction( 0,-1);
				lengthOfBridge = rand.nextInt(positionY-1);
			}
			if(lengthOfBridge == 0) continue;

			boolean bridgeIsPossible = true;
			
			// Check the whole way (bridge) and the end point (+1)
			for(int a = 1; a <= lengthOfBridge+1; a++) {
				coordinate.move(direction);
				if(!coordinate.isInside()) {
					throw new RuntimeException("coordinate outside (should not happen)");
				}

				char value = coordinate.getValueInLayout(layout);
				if(value != ' ') {
					if(value == 'O') {
						lengthOfBridge = a - 1;
						break;
					}
					else {
						bridgeIsPossible = false;
						break;
					}
				}
			}

			if(lengthOfBridge == 0) bridgeIsPossible = false;
			
			if(!bridgeIsPossible) {
				// Retry from other starting value
				int coordinates[] = randomIsland();
				positionX = coordinates[0];
				positionY = coordinates[1];
				coordinate = new Coordinate(positionX, positionY);
				continue;
			}
			
			if(bridgeIsPossible == true) {
				coordinate = new Coordinate(positionX, positionY);
				for(int a = 1; a <= lengthOfBridge; a++) {
					coordinate.move(direction);
					if(fiftyFifty < 0.5) {
						coordinate.setValueInLayout(layout, direction.getChar());
					} else coordinate.setValueInLayout(layout, direction.getDoubleChar());
				}
				coordinate.move(direction);
				coordinate.setValueInLayout(layout, 'O');
				positionX = coordinate.getX();
				positionY = coordinate.getY();
			}
			System.out.println(lengthOfBridge);								
		
			for(int y=0; y<sizeY; y++) {
				for(int x=0; x<sizeX; x++) {
					System.out.print(layout[x][y]);
				}
				System.out.println();
			}
			System.out.println(countIslands());
		}
		
		for(int i = 0; i < sizeX; i++) {
			for(int j = 0; j < sizeX; j++) {
				if(layout[i][j] == 'O') {
					int connections = 0;
					if(i + 1 < sizeX) connections = connections + countConnectionsX(i + 1, j); 
					if(i > 0) connections = connections + countConnectionsX(i - 1, j); 
					if(j + 1 < sizeY) connections = connections + countConnectionsY(i, j + 1); 
					if(j > 0) connections = connections + countConnectionsY(i, j - 1); 
					
					layout[i][j] = (char) ( 48 + connections);
				}
			}
		}
		for(int y=0; y<sizeY; y++) {
			for(int x=0; x<sizeX; x++) {
				System.out.print(layout[x][y]);
			}
			System.out.println();
		}
		System.out.println(countIslands());
		
	}
	
	public int countConnectionsY(int coordinateX, int coordinateY) {
		if(layout[coordinateX][coordinateY] == '\u2016') return 2;
		else if(layout[coordinateX][coordinateY] == '|') return 1;
		return 0;
	}

	public int countConnectionsX(int coordinateX, int coordinateY) {
		if(layout[coordinateX][coordinateY] == '\u2550') return 2;
		else if(layout[coordinateX][coordinateY] == '\u2015') return 1;
		return 0;
	}
	
	private void initLayout() {
		for(int y=0; y<sizeY; y++) {
			for(int x=0; x<sizeX; x++) {
				layout[x][y] = ' ';
			}
		}
	}

	public static int[] randomIsland() {
		while(true) {
			// Start looking from a random point
			int rx = rand.nextInt(sizeX);
			int ry = rand.nextInt(sizeY);
			for(int a = ry; a < sizeY; a++) {
				for(int i = rx; i < sizeX; i++) {
					if(layout[i][a] == 'O') {
						int[] coordinates = {i, a};
						return coordinates;
					}
				}
			}
		}
	}
	
	public static int countIslands() {
		int numberOfIslands = 0;
		for(int y = 0; y < sizeY; y++) {
			for(int x = 0; x < sizeX; x++) {
				if(layout[y][x] == 'O') {
					numberOfIslands++;
				}
			}
		}
		return numberOfIslands;
	}
	
	public char[][] handOverLayout()  {
		run();
		return layout;
	}
}
