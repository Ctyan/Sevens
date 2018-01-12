package item;

public class PlayingCard {

	private Card[] cardList;

	public PlayingCard(boolean joker) {
		cardList = new Card[53];
		for(int t=0;t<4;t++) {
			for(int n=0;n<13;n++) {
				cardList[t*13+n] = new Card(t+1, n+1);
			}
		}
		if(joker) cardList[52] = new Card(Card.JOKER_NUMBER, Card.JOKER_TYPE);
	}

	public Card[] getCardList() {
		return cardList;
	}
}
