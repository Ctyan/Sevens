package item;

public class Card {

	public final static int SPADE_TYPE = 1;
	public final static int HEART_TYPE = 2;
	public final static int CLUB_TYPE = 3;
	public final static int DIA_TYPE = 4;

	public final static int JOKER_NUMBER = 0;
	public final static int JOKER_TYPE = 0;

	private int cardNumber;
	private int cardType;

	public Card(int cardNumber, int cardType) {
		this.cardNumber = cardNumber;
		this.cardType = cardType;
	}

	public int getNumber() {
		return cardNumber;
	}

	public int getType() {
		return cardType;
	}
}
