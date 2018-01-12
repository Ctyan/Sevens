package game;

import player.Player;

public class GameManager{

	private final int MAX_PLAYER_VALUE = 6;

	private Player[] playerList;
	private int playerCount;

	public GameManager() {
		playerList = new Player[MAX_PLAYER_VALUE];
		playerCount = 0;
	}

	public boolean setPlayer(Player player) {
		if(playerCount<MAX_PLAYER_VALUE) {
			playerList[playerCount] = player;
			playerCount++;
			return true;
		}
		return false;
	}

	public boolean deleatePlayer(int n) {
		if(n<0 || MAX_PLAYER_VALUE<=n) return false;

		if(n == MAX_PLAYER_VALUE-1) {
			playerList[n] = null;
			return true;
		}
		System.arraycopy(playerList, n+1, playerList, n, MAX_PLAYER_VALUE-n);
		return true;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public void gameStart() {

	}
}
