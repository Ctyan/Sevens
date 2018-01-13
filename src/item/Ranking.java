package item;

public class Ranking {

	private Player[] ranking;
	private int topCount;
	private int bottomCount;

	public Ranking(int playerValue) {
		ranking = new Player[playerValue];
		topCount = 0;
		bottomCount = playerValue-1;
	}

	public boolean registerWinPlayer(Player p) {
		if(bottomCount <= topCount) return false;

		ranking[topCount++] = p;
		return true;
	}

	public boolean registerLoserPlayer(Player p) {
		if(bottomCount <= topCount) return false;

		ranking[bottomCount++] = p;
		return true;
	}

	public Player[] getRanking() {
		return ranking;
	}
}
