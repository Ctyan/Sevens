package protocol;

import java.io.Serializable;

public class PlayersRanking implements Serializable{
	private static final long serialVersionUID = 7667852882526660897L;
	private int[] playersRanking;
	
	public PlayersRanking() {
		
	}

	public PlayersRanking(int[] playersRanking) {
		this.setPlayersRanking(playersRanking);
	}

	public int[] getPlayersRanking() {
		return playersRanking;
	}

	public void setPlayersRanking(int[] playersRanking) {
		this.playersRanking = playersRanking;
	}
}
