package item;

import java.util.Random;

public class Player {

	private String userName;
	private int playerID;

	private int pass;

	public Player(String userName) {
		Random r = new Random();
		this.userName = userName;
		playerID = r.hashCode();
		pass = 0;
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
}
