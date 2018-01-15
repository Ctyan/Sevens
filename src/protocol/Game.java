package protocol;

import java.io.Serializable;
import java.util.List;

public class Game implements Serializable{
	private static final long serialVersionUID = -5117815579758499080L;
	private int turnPlayerId;
	private String turnPlayerName;
	private boolean playPass;
	private String playCard;
	private boolean playJoker;
	private List<String> playableCards;
	
	public Game(int turnPlayerId, String turnPlayerName){
		this.turnPlayerId = turnPlayerId;
		this.turnPlayerName = turnPlayerName;
	}

	public int getTurnPlayerId() {
		return turnPlayerId;
	}

	public void setTurnPlayerId(int turnPlayerId) {
		this.turnPlayerId = turnPlayerId;
	}

	public String getTurnPlayerName() {
		return turnPlayerName;
	}

	public void setTurnPlayerName(String turnPlayerName) {
		this.turnPlayerName = turnPlayerName;
	}

	public boolean isPlayPass() {
		return playPass;
	}

	public void setPlayPass(boolean playPass) {
		this.playPass = playPass;
	}

	public String getPlayCard() {
		return playCard;
	}

	public void setPlayCard(String playCard) {
		this.playCard = playCard;
	}

	public List<String> getPlayableCards() {
		return playableCards;
	}

	public void setPlayableCards(List<String> playableCards) {
		this.playableCards = playableCards;
	}

	public boolean isPlayJoker() {
		return playJoker;
	}

	public void setPlayJoker(boolean playJoker) {
		this.playJoker = playJoker;
	}

	
}
