package item;

public class Card {

	public final static int SPADE_TYPE = 1;
	public final static int HEART_TYPE = 2;
	public final static int CLUB_TYPE = 3;
	public final static int DIA_TYPE = 4;

	public final static int JOKER_NUMBER = 0;
	public final static int JOKER_TYPE = 0;

	private int cardType;
	private int cardNumber;

	public Card(int cardType, int cardNumber) {
		this.cardNumber = cardNumber;
		this.cardType = cardType;
	}

	public int getNumber() {
		return cardNumber;
	}

	public int getType() {
		return cardType;
	}

	public boolean equal(Card card) {
		if(this.cardType == card.cardType && this.cardNumber == card.cardNumber) {
			return true;
		}
		return false;
	}

	public String getMark(int t) {
		switch (t) {
		case SPADE_TYPE:
			return "S";
		case HEART_TYPE:
			return "H";
		case CLUB_TYPE:
			return "C";
		case DIA_TYPE:
			return "D";
		}
		return null;
	}
	
	public String toString() {
		return this.getMark(this.cardType)+", "+this.cardNumber;
	}
}
