package CLIENT;

import java.util.ArrayList;
import MAIN.Server;


public class GameRoom {
	
	private boolean done;
	private byte[] gameData;
	
	Player player1, player2;
	int[][] board;
	int w, h;
	
	long startTime, goTime;
	int startDelayMilli;
	public int sleepDelay;
	boolean gameStarted;
	
	public GameRoom(){
		sleepDelay = 100;
		gameStarted = false;
		startTime = 0; goTime = 0;
		done = false;
		startDelayMilli = 6000;
		gameData = new byte[6];
		
		player1 = new Player(40, 20);
		player2 = new Player(20, 10);
		w = 50;
		h = 30;
		board = new int[w][h];
	}
	
	public boolean isDone(){
		return done;
	}
	
	public boolean isGameStarted(){
		return gameStarted;
	}
	
	
	public byte[] getGameData(){
		Server.setByteData(gameData, 0, 1, (byte) 0);
		if(!gameStarted && startTime != 0){
			Server.setByteData(gameData, 0, 1, (byte) 1);
			Server.setByteData(gameData, 2, 8, (byte) player1.x);
			Server.setByteData(gameData, 9, 15, (byte) player1.y);
			Server.setByteData(gameData, 16, 22, (byte) player2.x);
			Server.setByteData(gameData, 23, 29, (byte) player2.y);
			int timeLeftCounter = (int) (goTime - System.currentTimeMillis());
			timeLeftCounter = timeLeftCounter/1000 + 1;
			Server.setByteData(gameData, 30, 37, (byte) timeLeftCounter); //Time left b4 start
		//	System.out.println("TEST: " + timeLeftCounter + "_" + Server.getByteData(gameData, 30, 45));
		}
		
		if(gameStarted){ //InGame Data
			Server.setByteData(gameData, 0, 1, (byte) 2);
			Server.setByteData(gameData, 2, 8, (byte) player1.x);
			Server.setByteData(gameData, 9, 15, (byte) player1.y);
			Server.setByteData(gameData, 16, 22, (byte) player2.x);
			Server.setByteData(gameData, 23, 29, (byte) player2.y);
			Server.setByteData(gameData, 30, 47, (byte) 0);
			for(int i = 0; i < 3 && player1.recentMoves.size() > i; i++){
				Server.setByteData(gameData, 30+i*3, 32+i*3, (byte) player1.recentMoves.get(i).getValue());
			}
			for(int i = 0; i < 3 && player2.recentMoves.size() > i; i++){
				Server.setByteData(gameData, 39+i*3, 41+i*3, (byte) player2.recentMoves.get(i).getValue());
			}
			
		}
		
		if(done){
			Server.setByteData(gameData, 0, 1, (byte) 3);
			if(player1.dead && player2.dead){
				Server.setByteData(gameData, 40, 47, (byte) 3);
			}else if(player1.dead){
				Server.setByteData(gameData, 40, 47, (byte) 2);
			}else if(player2.dead){
				Server.setByteData(gameData, 40, 47, (byte) 1);
			}else{
				Server.setByteData(gameData, 40, 47, (byte) 0);
			}
			
		}
		
		return gameData;
	}
	
	private enum Move{
		DOWN(1), LEFT(2), RIGHT(3), UP(4);
		
		  private int value;    

		  private Move(int value) {
		    this.value = value;
		  }

		  public int getValue() {
		    return value;
		  }
	}
	
	private class Player {
		public int x, y, dir;
		boolean dead;

		public Player(int x, int y) {
			this.x = x;
			this.y = y;
			dir = 1;
			dead = false;
			recentMoves = new ArrayList<Move>();
		}
		
		public void setRecentMove(Move move){
			recentMoves.add(0, move);
			if(recentMoves.size() >= 4){
				recentMoves.remove(3);
			}
		}
		ArrayList<Move> recentMoves;
	}

	
	private void updatePlayerMove(int direction, Player player){
		if (direction == 1 && player.dir != 4) {
			player.dir = direction;
		} else if (direction == 2 && player.dir != 3) {
			player.dir = direction;
		} else if (direction == 3 && player.dir != 2) {
			player.dir = direction;
		} else if (direction == 4 && player.dir != 1) {
			player.dir = direction;
		}
	}

	public void updatePlayerCommands(byte[] data1, byte[] data2) {
		int direction = 0;
		if(data1[0] != 0){
			direction = Server.getByteData(data1, 2, 7);
			updatePlayerMove(direction, player1);
		}
		if(data2[0] != 0){
			direction = Server.getByteData(data2, 2, 7);
			updatePlayerMove(direction, player2);
		}
	}

	void movePlayer(Player player) {
		
		if (player.dir == Move.UP.getValue()) {
			player.y -= 1;
			player.setRecentMove(Move.UP);
		}
		if (player.dir == Move.LEFT.getValue()) {
			player.x -= 1;
			player.setRecentMove(Move.LEFT);
		}
		if (player.dir == Move.RIGHT.getValue()) {
			player.x += 1;
			player.setRecentMove(Move.RIGHT);
		}
		if (player.dir == Move.DOWN.getValue()) {
			player.y += 1;
			player.setRecentMove(Move.DOWN);
		}
		

	}
	
	public void checkCollision(Player player){
        if(player.x == 0 && player.dir == Move.LEFT.value){
            player.dead = true;
        }
        if(player.x == w-1 && player.dir == Move.RIGHT.value){
            player.dead = true;
        }
        if(player.y == 0 && player.dir == Move.UP.value){
            player.dead = true;
        }
        if(player.y == h-1 && player.dir == Move.DOWN.value){
            player.dead = true;
        }
		
		if(board[player.x][player.y] != 0){
			player.dead = true; //Hit himself or other player
		}

	}

	public void process() {
		
		if (done) {
			return;
		}
		
		if(!gameStarted){
			if(startTime == 0){
				sleepDelay = 100;
				startTime = System.currentTimeMillis();
				goTime = startTime + startDelayMilli;
			}else if(System.currentTimeMillis() > goTime){
				gameStarted = true;
				sleepDelay = 1100;
			}
			return;
		}
		
		//System.out.println("DONE: "  + done + " _" + "x: " + player1.x + " y: " + player1.y );

		checkCollision(player1);
		checkCollision(player2);
		
		if(player1.dead || player2.dead){
			done = true;
			return;
		}

		board[player1.x][player1.y] = 1;
		board[player2.x][player2.y] = 1;
		
		movePlayer(player1);
		movePlayer(player2);

	}	


}
