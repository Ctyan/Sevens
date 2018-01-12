package item;

import java.util.ArrayList;
import java.util.List;

public class PlayingCard {

	private List<Card> cardList;

	public PlayingCard(boolean joker) {
		cardList = new ArrayList<Card>();
		for(int t=0;t<4;t++) {
			for(int n=0;n<13;n++) {
				cardList.add(new Card(t+1, n+1));
			}
		}
		if(joker) cardList.add(new Card(Card.JOKER_NUMBER, Card.JOKER_TYPE));
	}

	public List<Card> getCardList() {
		return cardList;
	}
}
