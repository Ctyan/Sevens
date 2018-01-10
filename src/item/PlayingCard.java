package item;

public class PlayingCard {

	private final String url = "";

	private Card[] cardList;

	public PlayingCard(int jokerValue) {
		if(jokerValue<0) jokerValue = 0;
		cardList = new Card[52+jokerValue];
		for(int t=0;t<4;t++) {
			for(int n=0;n<13;n++) {
				cardList[t*13+n] = new Card(t+1, n+1, url);
			}
		}
		for(int j=0;j<jokerValue;j++) {
			cardList[52+j] = new Card(url);
		}
	}

	public Card[] getCardList() {
		return cardList;
	}
}
