package protocol;

public class PlayersRankingProtocol extends Protocol{
	private static final long serialVersionUID = 1L;
	private PlayersRanking playersRanking;
	
	public PlayersRankingProtocol(PlayersRanking ranking) {
		super(Protocol.RANKING);
		this.setPlayersRanking(ranking);
		
	}

	public PlayersRanking getPlayersRanking() {
		return playersRanking;
	}

	public void setPlayersRanking(PlayersRanking playersRanking) {
		this.playersRanking = playersRanking;
	}
	
}
