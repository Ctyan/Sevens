package game;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import item.Card;
import item.PlayingCard;
import player.Player;

public class GameManager{

	private final int MAX_PLAYER_VALUE = 6;
	private final int MIN_PLAYER_VALUE = 3;

	private Player[] playerList;
	private int playerCount;
	private PlayingCard playingCard;
	private GamePlayable gamePlayable;
	private int startPlayerNumber;
	private List<Card> playCardList;

	public GameManager() {
		playerList = new Player[MAX_PLAYER_VALUE];
		playerCount = 0;
		startPlayerNumber = -1;
	}

	public boolean setPlayer(Player player) {
		if(playerCount<MAX_PLAYER_VALUE) {
			playerList[playerCount] = player;
			playerCount++;
			return true;
		}
		return false;
	}

	public boolean deleatePlayer(int n) {
		if(n<0 || MAX_PLAYER_VALUE<=n) return false;

		if(n == MAX_PLAYER_VALUE-1) {
			playerList[n] = null;
		}else{
			System.arraycopy(playerList, n+1, playerList, n, MAX_PLAYER_VALUE-n);
		}
		playerCount--;
		return true;
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public boolean isPlayerCountOK() {
		return MIN_PLAYER_VALUE <= playerCount;
	}

	public Player[] getPlayerList() {
		return playerList;
	}

	public boolean setGamePlayable(int roundValue, int passValue, boolean joker, boolean tunnel) {
		if(!isPlayerCountOK()) return false;

		gamePlayable = new GamePlayable(roundValue, passValue, joker, tunnel);
		return true;
	}

	public boolean gameStart() {
		if(gamePlayable == null) return false;

		// プレイカードの用意
		playingCard = new PlayingCard(gamePlayable.isJoker());
		playCardList = playingCard.getCardList();

		// カードから7を抜き出す
		playCardList.remove(new Card(Card.SPADE_TYPE, 7));
		playCardList.remove(new Card(Card.HEART_TYPE, 7));
		playCardList.remove(new Card(Card.CLUB_TYPE, 7));
		playCardList.remove(new Card(Card.DIA_TYPE, 7));

		// カードをシャッフルする
		Collections.shuffle(playCardList);

		// 開始プレイヤーを決める
		Random r = new Random();
		startPlayerNumber = r.nextInt() % playerCount;

		return true;
	}

	public int getStartPlayerNumber() {
		return startPlayerNumber;
	}

	public List<Card> getPlayerCardList(int playerNumber) {
		if(playCardList == null) return null;
		if(playerNumber<0 || playerCount<=playerNumber) return null;

		int index = (int)(playCardList.size() / playerCount);
		List<Card> returnList = playCardList.subList(index*playerNumber, index*(playerNumber+1)-1);

		int mod = playCardList.size() - index*playerCount;
		if(0<mod && playerNumber<mod) {
			returnList.add(playCardList.get(index*playerCount+playerNumber));
		}

		return returnList;
	}
}
