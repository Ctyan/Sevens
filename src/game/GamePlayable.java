package game;

import java.util.ArrayList;
import java.util.List;

import item.Card;

public class GamePlayable{

	private GameField gameField;
	private int[][] rimCardsNum;

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
		rimCardsNum = new int[4][2];
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
		if(card.getNumber()==7)return true;
		int type = card.getType()-1;
		int number = card.getNumber()-1;
		
		if(gameField.isFrontCard(type, number) || gameField.isBackCard(type, number)) {
			return true;
		}

		return false;
	}

	/**引数のカードを置く*/
	public boolean setCard(Card card) {
		boolean result = gameField.setCard(card.getType(), card.getNumber());
		int t = card.getType()-1;
		int n = card.getNumber();
		System.out.println(card);
		if(t < 0 || t >= 4)return false;
		if(n == rimCardsNum[t][0]-1)rimCardsNum[t][0] = n;
		else if(n == rimCardsNum[t][1]+1)rimCardsNum[t][1] = n;
		//tunnel&&13or1
		else if(rimCardsNum[t][0]==1&&tunnel&&rimCardsNum[t][1]<n)rimCardsNum[t][0]=13;
		else if(rimCardsNum[t][1]==13&&tunnel&&rimCardsNum[t][0]>n)rimCardsNum[t][1]=1;
		else {
			//7とか
			System.out.println("else n:"+card);
			rimCardsNum[t][0] = n;//left rim
			rimCardsNum[t][1] = n;//right rim
		}
		return result;
	}

	public List<Card> getRimCardsList() {
		int[][] rimCardsNum = getRimCards();
		List<Card> rimCards = new ArrayList<>();
		for(int t = 0; t < rimCardsNum.length; t++) {
			for(int i = 0; i < rimCardsNum[t].length; i++) {
				rimCards.add(new Card((t+1), rimCardsNum[t][i]));
			}
		}
		return rimCards;
	}
	
	public List<Card> getPlayableCards(){
		int[][] rim = getRimCards();
		List<Card> playableCards = new ArrayList<>();
		for(int t = 0; t < rim.length; t++) {
			if(rim[t][0]==rim[t][1]&&rim[t][0]!=7)continue;//行は埋まっている
			int left = rim[t][0]-1;
			int right = rim[t][1]+1;
			
			if(tunnel&&right==14)right=1;
			if(tunnel&&left==0) left=13;
			if(left!=14&&left!=0)
				playableCards.add(new Card((t+1), left));
			if(left==right)continue;
			if(right!=14&&right!=0)
				playableCards.add(new Card((t+1), right));
		}
		
		return playableCards;
	}

	public int[][] getRimCards(){
		return this.rimCardsNum;
	}
}
