package main;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import game.*;
import item.*;
import protocol.*;

public class Server implements Runnable{
	Map<Socket, ServerThread> clients;
	Map<Player, ServerThread> players;
	ServerSocket serversocket;
	
	GameManager gamemanager;

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
		clients.remove(st.client);
		gamemanager.removePlayer(st.player.getPlayerID());
		players.remove(st.player);
	}
	
	/**Clientが最初に送るuser_nameを受け付け, gameへplayerとして登録する,　送り主へ登録結果を送信する*/
	public void recvPlayerEntry(PlayerEntryProtocol prot, ServerThread sender) {
		PlayerEntry pe = prot.getPlayerEntry();
		String name = pe.getPlayer_name();
		int player_no = gamemanager.getPlayerCount();
		Player player = new Player(name, player_no);
		if(gamemanager.setPlayer(player)) {
			players.put(player, sender);
			sender.player=player;
			pe.setEntry(true);
			pe.setPlayer_id(player_no);
			if(gamemanager.getPlayerCount()==1)
				pe.setFirst(true);
			if(gamemanager.isPlayerCountOK()) {
				Player[] players = gamemanager.getPlayerList();
				if(players!=null&&players.length>=1) {
					ServerThread settingPlayer = this.players.get(players[0]);
					settingPlayer.send(new Protocol(Protocol.GAME_STARTABLE));
				}
			}
		}
		else {
			pe.setEntry(false);
		}
		System.out.println("entry-player:"+pe);
		sender.send(new PlayerEntryProtocol(pe));
	}
	
	/***/
	public void recvGameRule(GameRuleProtocol prot, ServerThread sender) {
		GameRule gr = prot.getGameRule();
		boolean isPlayable = gamemanager.setGamePlayable(gr.getRound(), gr.getPass(), gr.isJoker(), gr.isTunnel());
		if(isPlayable) {
			for(ServerThread st: clients.values()) {
				st.send(prot);
			}
		}
		else {
			//false:送り主に開始できないことを送信
			//sender.send(sendmsg);
		}
	}
	
	public void recvGame(GameProtocol prot, ServerThread sender) {
		//Game進行に関する情報の受け取り
		Game game = prot.getGame();
		
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
				Thread.sleep(500);
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

	/**受信情報を読み取る*/
	private void readProtocol(Protocol prot){
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
}
