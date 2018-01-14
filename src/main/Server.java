package main;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.*;
import item.*;
import protocol.*;

public class Server implements Runnable{
	Map<Socket, ServerThread> clients;
	Map<Player, ServerThread> players;
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
			Ranking ranking = new Ranking(gamemanager.getPlayerCount());
			gamemanager.gameStart(ranking);
			prot.setProtocol_Bool(isPlayable);
			Player[] p_ary = gamemanager.getPlayerList();
			for(int i = 0; i < p_ary.length && p_ary[i]!=null; i++) {
				//TODO
				List<Card> cards = gamemanager.getPlayerCardList(i);
				Player p = p_ary[i];
				System.out.println("user:"+p.getUserName());
				for(String c: playersCardToString(cards)) {
					System.out.println(c);
				}
			}
			//st.send(prot);
				
				//st.send();
		}
		else {
			//false:送り主に開始できないことを送信
			//sender.send(sendmsg)
			prot.setProtocol_Bool(isPlayable);
			sender.send(prot);
		}
		System.out.println(sender+", GameRule:"+gr);
	}
	
	public void recvGame(GameProtocol prot, ServerThread sender) {
		//Game進行に関する情報の受け取り
		Game game = prot.getGame();
		
	}
	
	public List<String> playersCardToString(List<Card> cards){
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
				cardstr = "joker";	
			
			results.add(cardstr);
		}
		return results;
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
		default:
			break;
		}
	}
	
	public String toString() {
		return this.player.getPlayerID()+", "+this.player.getUserName();
	}
}
