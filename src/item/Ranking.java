package item;

public class Ranking {

	private Player[] ranking;
	private int[] scorerate;
	private int topCount;
	private int bottomCount;

	public Ranking(int playerValue) {
		ranking = new Player[playerValue];
		scorerate = new int[]{30,20,10};
		topCount = 0;
		bottomCount = playerValue-1;
	}

	public boolean registerPlayer(Player p, boolean win) {
		if(bottomCount <= topCount) return false;

		if(win) {
			ranking[topCount++] = p;
		}else{
			ranking[bottomCount--] = p;
		}
		return true;
	}

	public Player[] getRanking() {
		return ranking;
	}

	public int getScore(int i) {
		if(i>2) return 0;
		return scorerate[i];
	}
}
