package game;

import item.Card;

public class GamePlayable{

	private GameField gameField;

	private int roundValue;
	private int passValue;
	private boolean joker;
	private boolean tunnel;

	public GamePlayable(int roundValue, int passValue, boolean joker, boolean tunnel) {
		gameField = new GameField();
		this.roundValue = roundValue;
		this.passValue = passValue;
		this.joker = joker;
		this.tunnel = tunnel;
	}

	public int getRoundValue() {
		return roundValue;
	}

	public int getPassValue() {
		return passValue;
	}

	public boolean isJoker() {
		return joker;
	}

	public boolean isTunnel() {
		return tunnel;
	}

	public boolean isRound(int r) {
		return r <= roundValue;
	}

	public boolean isPass(int p) {
		return p < passValue;
	}

	public boolean canSetCard(Card card) {
		int type = card.getType()-1;
		int number = card.getNumber()-1;

		if(gameField.isFrontCard(type, number) || gameField.isBackCard(type, number)) {
			return true;
		}

		return false;
	}

	public boolean setCard(Card card) {
		return gameField.setCard(card.getType(), card.getNumber());
	}
}
