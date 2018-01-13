package game;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import item.Card;
import item.Player;
import item.PlayingCard;
import item.Ranking;

/**Cardクラスのみ1から数え上げ*/
public class GameManager{

	private final int MAX_PLAYER_VALUE = 6;
	private final int MIN_PLAYER_VALUE = 3;

	private PlayingCard playingCard;
	private GamePlayable gamePlayable;
	private Ranking ranking;

	private int playerCount;
	private int thisTurnPlayerNumber;

	private Player[] playerList;
	private List<Card> playCardList;

	public GameManager(Ranking ranking) {
		this.ranking = ranking;
		playerList = new Player[MAX_PLAYER_VALUE];
		playerCount = 0;
		thisTurnPlayerNumber = -1;
	}

	/**プレイヤーの登録*/
	public boolean setPlayer(Player player) {
		if(playerCount<MAX_PLAYER_VALUE) {
			playerList[playerCount] = player;
			playerCount++;
			return true;
		}
		return false;
	}

	/**指定番目のプレイヤーの削除*/
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

	/**登録したプレイヤー数*/
	public int getPlayerCount() {
		return playerCount;
	}

	/**ゲームが開始できる人数かどうか判断*/
	public boolean isPlayerCountOK() {
		return MIN_PLAYER_VALUE <= playerCount;
	}

	/**プレイヤーのリストを返す（null列あり)*/
	public Player[] getPlayerList() {
		return playerList;
	}

	/**ゲームルールの登録（ゲーム開始できる人数が条件）*/
	public boolean setGamePlayable(int roundValue, int passValue, boolean joker, boolean tunnel) {
		if(!isPlayerCountOK()) return false;

		gamePlayable = new GamePlayable(roundValue, passValue, joker, tunnel);
		return true;
	}

	/**ゲームルールの取得*/
	public GamePlayable getGamePlayable() {
		return gamePlayable;
	}

	/**ゲーム開始手続き*/
	public boolean gameStart() {
		if(gamePlayable == null) return false;

		/**プレイカードの用意*/
		playingCard = new PlayingCard(gamePlayable.isJoker());
		playCardList = playingCard.getCardList();

		/**カードから7を抜き出す*/
		playCardList.remove(new Card(Card.SPADE_TYPE, 7));
		playCardList.remove(new Card(Card.HEART_TYPE, 7));
		playCardList.remove(new Card(Card.CLUB_TYPE, 7));
		playCardList.remove(new Card(Card.DIA_TYPE, 7));

		/**カードをシャッフルする*/
		Collections.shuffle(playCardList);

		/**開始プレイヤーを決める*/
		Random r = new Random();
		thisTurnPlayerNumber = r.nextInt() % playerCount;

		return true;
	}

	/**現在のターンのプレイヤーナンバーを取得*/
	public int getThisTurnPlayerNumber() {
		return thisTurnPlayerNumber % playerCount;
	}

	/**現在のターンのプレイヤーを取得*/
	public Player getThisTurnPlayer() {
		return playerList[getThisTurnPlayerNumber()];
	}

	/**次のターンへ移し、そのターンのプレイヤーナンバーを取得*/
	public int nextThisTurnPlayerNumber() {
		while (true) {
			++thisTurnPlayerNumber;
			if(!playerList[thisTurnPlayerNumber].getEndGameFlag()) {
				break;
			}
		}
		return getThisTurnPlayerNumber();
	}

	/**ターン数を取得*/
	public int getHowManyTurn() {
		return (thisTurnPlayerNumber - getThisTurnPlayerNumber()) / playerCount;
	}

	/**引数に指定したプレイヤーの手持ちカードを取得*/
	public List<Card> getPlayerCardList(int playerNumber) {
		/*
		 * カードリストをプレイヤー人数の倍数で区切る
		 * 余りを先頭から順に1枚ずつ配る
		 * <---1---><---2---><---3---><---4---><---5--->1234
		 * 12345689TJQK12345689TJQK12345689TJQK12345689TJQKj
		 * 結果=>5番目だけ9枚になる。
		 */
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

	/**引数に指定したプレイヤーの手持ちカード数を取得*/
	public int getPlayerCardValue(int playerNumber) {
		List<Card> list = getPlayerCardList(playerNumber);
		int re = list.size();
		for(Card c : list) {
			if(c==null) re--;
		}
		return re;
	}

	/**ゲームルールのラウンド制限数*/
	public int getRoundValue() {
		return gamePlayable.getRoundValue();
	}

	/**次のラウンドがあるかどうか*/
	public boolean isRound(int r) {
		return gamePlayable.isRound(r);
	}

	/**引数のカードがおけるか判断し、おけるならおく*/
	public boolean setCard(Card card) {
		if(gamePlayable.canSetCard(card)) {
			return gamePlayable.setCard(card);
		}
		return false;
	}

	/**ゲームルールのパス制限数*/
	public int getPassValue() {
		return gamePlayable.getPassValue();
	}

	/**引数のプレイヤーがパスができるかどうか判断し、できるならパスをする*/
	public boolean doPass(Player p) {
		if(gamePlayable.isPass(p.getPass())) {
			p.doPass();
			return true;
		}
		return false;
	}

	/**引数のカードがジョーカーかどうか*/
	public boolean isJokerCard(Card card) {
		return card.getType() == Card.JOKER_TYPE;
	}

	/**引数に指定したプレイヤーのゲームを終了<br>
	 * 以後ターンを飛ばされ、ランキングに追加される*/
	public void setEndGamePlayer(Player p, boolean win) {
		p.endGame();
		ranking.registerPlayer(p, win);
	}

	/**ゲームが終了する条件か確認する*/
	public boolean cheackEndGame() {
		int counter = 0;
		for(int i=0;i<playerList.length;i++) {
			if(playerList[i].getEndGameFlag()) {
				counter++;
			}
		}
		if(playerCount <= counter+1) {
			return true;
		}
		return false;
	}

	/**１ターンの処理（出したカード、ジョーカーの有無、パスの有無、ジョーカーのおいた位置）<br>
	 * ( null,       false, true,  0 )<br>
	 * ( new Card(), false, false, 0 )<br>
	 * ( new Card(), true,  false, n )<br>
	 * */
	public void turnProcessing(Card card, boolean joker, boolean pass, int jokerN) {
		if(card == null && !joker && pass) {
			// パスの場合
			if(!doPass(getThisTurnPlayer())) {
				// パスの残り回数がない場合
				setEndGamePlayer(getThisTurnPlayer(), false);
				// そのプレイヤーの手持ちを全て置く
				List<Card> list = getPlayerCardList(thisTurnPlayerNumber);
				for(Card c : list) {
					if(c != null) {
						playCardList.set(playCardList.indexOf(c), c);
						c = null;
					}
				}
			}
		}else if(card != null && !joker && !pass) {
			// カード1枚のみの場合
			if(setCard(card)) {
				// カードが置けた場合
				// カードを手持ちから消す(null)
				playCardList.set(playCardList.indexOf(card), null);
			}
			if(getPlayerCardValue(getThisTurnPlayerNumber()) == 0) {
				// 手持ちカードが無くなったら
				setEndGamePlayer(getThisTurnPlayer(), true);
			}
		}else if(card != null && joker && !pass) {
			// ジョーカーとカードの2枚の場合
			// ジョーカーの位置がおけるかどうか
			if(gamePlayable.canSetCard(new Card(card.getType(), jokerN))) {
				if(setCard(card)) {
					// 置けた場合カードを手持ちから消す(null)
					playCardList.set(playCardList.indexOf(card), null);
				}
				// ジョーカーのとこのカードを強制的に捨てる
				playCardList.set(playCardList.indexOf(new Card(card.getType(), jokerN)), null);
				// *ジョーカーを今のプレイヤーから別のプレイヤーに渡す処理
			}
		}else{
			// ありえない状態の場合
		}
	}
}
