package item;

import java.util.Random;

public class Player {

	private String userName;
	private int playerID;

	private int pass;
	private boolean endGameFlag;

	public Player(String userName, int playerID) {
		this.userName = userName;
		this.playerID = playerID;
		pass = 0;
		endGameFlag = false;
	}

	public String getUserName() {
		return userName;
	}

	public int getPlayerID() {
		return playerID;
	}

	public int getPass() {
		return pass;
	}

	public void doPass() {
		pass++;
	}

	public void endGame() {
		endGameFlag = true;
	}

	public boolean getEndGameFlag() {
		return endGameFlag;
	}
}
