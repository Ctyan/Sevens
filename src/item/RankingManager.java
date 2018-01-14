package item;

import java.util.ArrayList;

public class RankingManager {

	private ArrayList<Ranking> rankingList;

	public RankingManager() {
		rankingList = new ArrayList<>();
	}

	public void addRanking(Ranking r) {
		rankingList.add(r);
	}

	public ArrayList<Ranking> getRankingList() {
		return rankingList;
	}
}
