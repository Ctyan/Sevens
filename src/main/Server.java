package main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.GameManager;
import item.Card;
import item.Player;
import item.Ranking;
import protocol.ChatProtocol;
import protocol.Game;
import protocol.GameProtocol;
import protocol.GameRule;
import protocol.GameRuleProtocol;
import protocol.GameStarterKit;
import protocol.GameStarterKitProtocol;
import protocol.PlayerEntry;
import protocol.PlayerEntryProtocol;
import protocol.PlayersRanking;
import protocol.PlayersRankingProtocol;
import protocol.Protocol;

public class Server implements Runnable{
	Map<Socket, ServerThread> clients;
	Map<Player, ServerThread> players;
	List<Player> inGamePlayers;
	Player[] roundPlayerRanking;
	ServerSocket serversocket;
	
	GameManager gamemanager;
	
	int accessNum = 0;
	
	public Server(){
		clients = new HashMap<>();
		players = new HashMap<>();
		gamemanager = new GameManager();
	}

	public static void main(String[] args){
		Server server = new Server();
		server.run();
	}

	/**サーバのメイン処理, サーバは新しいクライアントの接続を待ち受ける*/
	@Override
	public void run(){
		try {
			//access port:5001
			//クライアントの接続を待つ部分
			serversocket = new ServerSocket(5001);
			while(true){
				Socket socket = serversocket.accept();
				ServerThread st = new ServerThread(socket, this);
				st.start();
				clients.put(socket, st);
				System.out.println("Waiting for new connection at port:5001");
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**受け取ったチャットデータを送り主以外のクライアントへ送信*/
	public void recvChat(ChatProtocol cp, ServerThread sender){
		System.out.println(cp.getChat());
		for(ServerThread st: clients.values()){
			if(!sender.equals(st))st.send(cp);
		}
	}

	/**接続が切れたと思われるクライアントを取除く*/
	public void removeClient(ServerThread st){
		try {
			st.client.close();
			clients.remove(st.client);
			st.ois.close();
			st.oos.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		players.remove(st.player);
		System.out.println("remove_connection:"+st);
	}
	
	/**Clientが最初に送るuser_nameを受け付け, gameへplayerとして登録する,　送り主へ登録結果を送信する
	 * @throws IOException */
	public void recvPlayerEntry(PlayerEntryProtocol prot, ServerThread sender) throws IOException {
		boolean p_bool = prot.isProtocol_Bool();
		
		PlayerEntry pe = prot.getPlayerEntry();
		String name = pe.getPlayer_name();
		if(p_bool) {
			//参加処理
			int player_no = accessNum++;
			Player player = new Player(name, player_no);
			System.out.println("Entry_Player_ID:"+(player_no)+", Name:"+player.getUserName());
			if(gamemanager.setPlayer(player)) {
				players.put(player, sender);
				sender.player=player;
				pe.setEntry(true);
				pe.setPlayer_id(player_no);
				if(player.getPlayerID()==0)pe.setFirst(true);
			}
			else {
				pe.setEntry(false);
			}
		}
		else {
			//キャンセル処理
			int cancel_player_id = sender.player.getPlayerID();
			int remove_index = 1;
			Player[] players = gamemanager.getPlayerList();
			if(players.length > 1) {
				//キャンセルユーザを見つける
				for(int i = 0; i < players.length; i++) {
					Player p = players[i];
					if(p != null) {
						int player_id = players[i].getPlayerID();
						if(cancel_player_id==player_id) {
							remove_index = i;
						}
						System.out.println("index:"+i+", player="+p.getUserName());
					}
					else {
						System.out.println("index:"+i+", player="+p);
					}
				}
				Player removePlayer = players[remove_index];
				if(removePlayer!=null) {
					gamemanager.removePlayer(remove_index+1);
					System.out.println("remove_index:"+remove_index+", player_id:"+removePlayer.getPlayerID());
				}
			}
		}
		System.out.println("entry-player:"+pe);
		prot.setPlayerEntry(pe);
		sender.send(prot);
		
		//ゲーム設定をしているユーザへ, ゲーム開始可能かを通知
		System.out.println("getPlayerCnt:"+gamemanager.getPlayerCount());
		Player[] players = gamemanager.getPlayerList();
		ServerThread firstPlayer;
		if(players!=null&&players.length>=1) {
			firstPlayer = this.players.get(players[0]);
			boolean bool = gamemanager.isPlayerCountOK();
			firstPlayer.send(new Protocol(Protocol.GAME_STARTABLE, bool));
			System.out.println("firstPlayer-Ready:"+bool);
		}
		System.out.println();
	}
	
	/**ゲームルール送信受け取り部分, 受取れたらゲームを開始*/
	public void recvGameRule(GameRuleProtocol prot, ServerThread sender) {
		GameRule gr = prot.getGameRule();
		boolean isPlayable = gamemanager.setGamePlayable(gr.getRound(), gr.getPass(), gr.isJoker(), gr.isTunnel());
		if(isPlayable) {
			//送られてきたGameRuleをすぐに全ユーザに通知
			for(ServerThread st: clients.values()) {
				st.send(prot);
			}
			
			Ranking ranking = new Ranking(gamemanager.getPlayerCount());
			//Start GameStarterKitへ必要な情報を入れて各Clientへ送信
			//カードを配る, ゲームルールを配って, 開始
			gamemanager.gameStart(ranking);
			List<Integer> playerIDList = new ArrayList<>();
			Map<Integer, String> players_name = new HashMap<>();
			Map<Integer, Integer> players_card_num = new HashMap<>();
			Map<Integer, List<String>> players_strcards = new HashMap<>();
			Map<Integer, Integer> players_pass_num = new HashMap<>();
			prot.setProtocol_Bool(isPlayable);
			Player[] player_array = gamemanager.getPlayerList();
			for(int i = 0; i < player_array.length && player_array[i]!=null; i++) {
				List<Card> cards = gamemanager.getPlayerCardList(i);
				Collections.sort(cards, new Comparator<Card>() {
					@Override
					public int compare(Card o1, Card o2) {
						int type = o1.getType() - o2.getType();
						if(type != 0)return type;
						int num = o1.getNumber() - o2.getNumber();
						return num;
					}
				});
				Player p = player_array[i];
				int pid = p.getPlayerID();
				String pname = p.getUserName();
				
				System.out.println("user:"+p.getUserName()+", cards_num:"+cards);
				playerIDList.add(p.getPlayerID());
				players_name.put(pid, pname);
				players_card_num.put(pid, cards.size());
				players_strcards.put(pid, playersCardToString(cards));
				players_pass_num.put(pid, p.getPass());
			}
			
			//全員に送信
			for(int i = 0; i < player_array.length && player_array[i]!=null; i++) {
				Player p = player_array[i];
				int pid = p.getPlayerID();
				GameStarterKit starterkit = new GameStarterKit(gr);
				starterkit.setHands(players_strcards.get(pid));
				starterkit.setPlayerIDList(playerIDList);
				starterkit.setPlayers_name(players_name);
				starterkit.setPlayers_card_num(players_card_num);
				starterkit.setPlayersPassNum(players_pass_num);
				Protocol starterkit_prot = new GameStarterKitProtocol(starterkit);
				starterkit_prot.setProtocol_Bool(true);
				this.players.get(p).send(starterkit_prot);
			}
		}
		else {
			//false:送り主に開始できないことを送信
			//sender.send(sendmsg)
			prot.setProtocol_Bool(isPlayable);
			sender.send(prot);
		}
		System.out.println(sender+", GameRule:"+gr);
	}
	
	/**Game送り主はターンプレイヤー, ターンプレイヤー以外は送ってこない, ターンプレイヤが上がったらRankingして,ターンプレイヤに通知*/
	public void recvGame(GameProtocol prot, ServerThread sender) {
		//TODO Game進行に関する情報の受け取り
		Game game = prot.getGame();
		
		String playCard = game.getPlayCard();
		boolean playJoker = game.isPlayJoker();
		boolean playPass = game.isPlayPass();
		int playerHandsNum = game.getTurnPlayerHandsNum();
		Card card = null;
		if(playCard!=null||playPass==true) {
			if(!playPass) {
				//ターンプレイヤーがカードを出した
				String cardstr = game.getPlayCard();
				card = cardstringToCard(cardstr);
				//if(playJoker);
				System.out.println("player put:"+card);
				gamemanager.getGamePlayable().setCard(card);
			}
			for(ServerThread st: players.values()) {
				//何をしたか知らせる
				//if(!(playPass&&sender.player.getPlayerID()==game.getTurnPlayerId()))
					st.send(prot);
			}
		}
		//gamemanager.turnProcessing(card, playJoker, playPass, 0);
		//if(playPass)gamemanager.doPass(sender.player);
		//あがり?
		if(playCard!=null&&playPass==false&&playerHandsNum==0) {
			//このラウンドでの初めてのランキング生成
			if(roundPlayerRanking==null)
				roundPlayerRanking = new Player[inGamePlayers.size()];
			
			Integer ranking = rankingPlayer(roundPlayerRanking, sender.player, playJoker);
			inGamePlayers.remove(sender.player);
			
			//アガリを通知
			sender.player.endGame();
			game.setTurnPlayerRanking(ranking);
			game.setPlayCard(null);
			game.setPlayJoker(false);
			prot.setProtocol_Bool(false);
			sender.send(prot);
		}
		
		//プレイヤーがいるなら次ターン
		if(inGamePlayers.size()!=0) {
			sendPlayersNextTurn(sender.player);
		}
		else {
			//ラウンドが終了!ランキング画面へ
			int[] id_ranking = new int[roundPlayerRanking.length];
			int i = 0;
			for(Player p: roundPlayerRanking) {
				id_ranking[i]=p.getPlayerID();
				i++;
			}
			PlayersRanking pr = new PlayersRanking(id_ranking);
			Protocol p = new PlayersRankingProtocol(pr);
			for(ServerThread st: players.values()) {
				st.send(p);
			}
		}
	}
	
	private int rankingPlayer(Player[] ranking, Player player, boolean playJoker) {
		int i = 0;
		int d = 1;
		if(playJoker) {
			i = ranking.length-1;
			d = -1;
		}
		for(;0 <= i && i < ranking.length; i += d) {
			if(ranking[i]==null) {
				ranking[i] = player;
				break;
			}
		}
		return i+1;
	}

	public void sendPlayersNextTurn(Player turnPlayer) {
		//int playerIndex = gamemanager.nextThisTurnPlayerNumber();		
		/**/
		int listindex = inGamePlayers.indexOf(turnPlayer);
		if(0 <= listindex && listindex < inGamePlayers.size()-1)
			turnPlayer = inGamePlayers.get(listindex+1);
		else
			turnPlayer = inGamePlayers.get(0);
		
		//this.gamemanager.nextThisTurnPlayerNumber();
		//turnPlayer = this.gamemanager.getThisTurnPlayer();
		
		List<Card> playableCards = gamemanager.getGamePlayable().getPlayableCards();
		List<String> playables = playersCardToString(playableCards);
		
		System.out.println("NextTurn, ID:"+turnPlayer.getPlayerID()+", Name:"+turnPlayer.getUserName());
		Game game = new Game(turnPlayer.getPlayerID(), turnPlayer.getUserName());
		Protocol gameProt = new GameProtocol(game);
		game.setPlayableCards(playables);
		gameProt.setProtocol_Bool(true);
		
		for(ServerThread st: players.values()) {
			//Game情報を送信
			st.send(gameProt);
		}
	}
	
	/**card文字列をCardインスタンスへ*/
	private Card cardstringToCard(String cardstr) {
		Card card = null;
		String type = cardstr.replaceAll("[0-9]+", "");
		String numstr = cardstr.replaceAll("[a-z]+", "");
		int typenum = 0;
		int num = Integer.valueOf(numstr);
		if(type.equals("spade"))typenum = Card.SPADE_TYPE;
		else if(type.equals("heart"))typenum = Card.HEART_TYPE;
		else if(type.equals("club"))typenum = Card.CLUB_TYPE;
		else if(type.equals("diamond"))typenum = Card.DIA_TYPE;
		
		if(typenum==Card.JOKER_TYPE) {
			card = new Card(Card.JOKER_TYPE, Card.JOKER_NUMBER);
		}
		else {
			card = new Card(typenum, num);
		}
		return card;
	}
	
	/**Card list を String listへ*/
	private List<String> playersCardToString(List<Card> cards){
		List<String> results = new ArrayList<>();
		for(Card c: cards) {
			String cardstr = "joker";
			int t = c.getType();
			String type = "";
			if(Card.SPADE_TYPE==t)type = "spade";
			else if(Card.HEART_TYPE==t)type="heart";
			else if(Card.CLUB_TYPE==t)type="club";
			else if(Card.DIA_TYPE==t)type="diamond";
			cardstr = type + String.valueOf(c.getNumber());
			if(Card.JOKER_TYPE==t||Card.JOKER_NUMBER==c.getNumber())
				cardstr = "x1";	//GUIのジョーカーのファイル名に依存
			
			results.add(cardstr);
		}
		return results;
	}

	public void recvGameStarterKit(GameStarterKitProtocol prot, ServerThread sender) {
		GameStarterKit sk = prot.getStarterKit();
		if(prot.isProtocol_Bool()) {
			//
			System.out.println("StarterKit_OK:"+sk.getPlayer_id()+", ");
			if(inGamePlayers==null)
				inGamePlayers = new ArrayList<>();
			inGamePlayers.add(sender.player);
		}
		else {
			System.out.println("StarterKit_Failed:"+sk.getPlayer_id()+", ");
		}
		//全員準備できたら, 一番最初のターン人の情報を全員へ送信
		if(inGamePlayers.size()==gamemanager.getPlayerCount()) {
//			Player[] ps = gamemanager.getPlayerList();
//			int index = gamemanager.getThisTurnPlayerNumber();
			
//			Player turnPlayer = this.gamemanager.getThisTurnPlayer();
//			if(turnPlayer.equals(ps[index]))
//				System.out.println("Playerあってる！");
			
			
			List<Card> playableCards = gamemanager.getGamePlayable().getPlayableCards();
			List<String> playables = playersCardToString(playableCards);
			
			Player turnPlayer = inGamePlayers.get(0);
			System.out.println("ターンプレイヤー, ID:"+turnPlayer.getPlayerID()+", Player:"+turnPlayer.getUserName());
			Game game = new Game(turnPlayer.getPlayerID(), turnPlayer.getUserName());
			Protocol gameProt = new GameProtocol(game);
			game.setPlayableCards(playables);
			gameProt.setProtocol_Bool(true);
			for(ServerThread st: players.values()) {
				//Game情報を送信
				st.send(gameProt);
			}
			roundPlayerRanking = null;
		}
	}

}


/**
 * 接続クライアントごとにスレッドを立ち上げる.
 * スレッドの主な役割は各クライアントからの受信を監視する
 **/
class ServerThread extends Thread{
	Server mainserver;
	Socket client;
	Player player;
	ObjectOutputStream oos;
	ObjectInputStream ois;

	public ServerThread(Socket client, Server mainserver) throws IOException{
		this.client = client;
		this.mainserver = mainserver;
		oos = new ObjectOutputStream(client.getOutputStream());
		ois = new ObjectInputStream(client.getInputStream());

	}

	/**逐一受信確認部分*/
	@Override
	public void run(){
		while(ois!=null){
			try {
				recv();
			}
			catch (Exception e) {
				e.printStackTrace();
				mainserver.removeClient(this);
				break;
			}
		}
	}

	/**受信部分*/
	private void recv() throws Exception{
		Protocol recvmsg = (Protocol)ois.readObject();
		readProtocol(recvmsg);
	}

	/**送信部分*/
	public void send(Protocol sendmsg){
		try {
			oos.writeObject(sendmsg);
			oos.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**受信情報を読み取る
	 * @throws IOException */
	private void readProtocol(Protocol prot) throws IOException{
		switch(prot.getProtocol_ID()){
		//Chat
		case 0:
			mainserver.recvChat((ChatProtocol)prot, this);
			break;
		//Game
		case 1:
			mainserver.recvGame((GameProtocol)prot, this);
			break;
		
		//PlayerEntry
		case 2:
			mainserver.recvPlayerEntry((PlayerEntryProtocol)prot, this);
			break;
		//GameRule
		case 3:
			mainserver.recvGameRule((GameRuleProtocol)prot, this);
			break;
		//GameStartable
		case 4:
			break;
		//GameStarterKit
		case 5:
			mainserver.recvGameStarterKit((GameStarterKitProtocol)prot, this);
			break;
		default:
			break;
		}
	}
	
	public String toString() {
		return this.player.getPlayerID()+", "+this.player.getUserName();
	}
}
