package game;

import item.Card;

public class GamePlayable{

	private GameField gameField;

	private int roundValue;
	private int passValue;
	private boolean joker;
	private boolean tunnel;

	/**ゲームルールを設定（ラウンド数、パス回数、ジョーカーの有無、トンネルの有無）*/
	public GamePlayable(int roundValue, int passValue, boolean joker, boolean tunnel) {
		gameField = new GameField();
		this.roundValue = roundValue;
		this.passValue = passValue;
		this.joker = joker;
		this.tunnel = tunnel;
	}

	/**ラウンド数を取得*/
	public int getRoundValue() {
		return roundValue;
	}

	/**パス回数を取得*/
	public int getPassValue() {
		return passValue;
	}

	/**ジョーカーの有無*/
	public boolean isJoker() {
		return joker;
	}

	/**トンネルの有無*/
	public boolean isTunnel() {
		return tunnel;
	}

	/**次のラウンドがあるかどうか*/
	public boolean isRound(int r) {
		return r < roundValue;
	}

	/**パスができるかどうか*/
	public boolean isPass(int p) {
		return p < passValue;
	}

	/**引数のカードが置けるかどうか*/
	public boolean canSetCard(Card card) {
		int type = card.getType()-1;
		int number = card.getNumber()-1;

		if(gameField.isFrontCard(type, number) || gameField.isBackCard(type, number)) {
			return true;
		}

		return false;
	}

	/**引数のカードを置く*/
	public boolean setCard(Card card) {
		return gameField.setCard(card.getType(), card.getNumber());
	}
}
