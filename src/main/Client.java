package main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import gui.*;
import protocol.*;
import item.*;

/**起動すると指定されたサーバへ接続します*/
public class Client implements GUIListener{
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 5001;
	//TODO GUImanager, Listener, etc...
	Player player;
	
	GUIManager guimanager;
	gui.Main guimain;
	
	String name;
	Socket socket;

	ObjectOutputStream oos;
	ObjectInputStream ois;

	public static void main(String[] args){
		try{
			Client client = new Client();
			client.guimain.main(args);
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public Client() {
		this.guimanager = GUIManager.getInstance();
		this.guimanager.setGUIListener(this);
		this.guimanager.setRuleSceneFlag(false);
		this.guimain = new gui.Main();
	}

	public Client(String name){
		this.name = name;
		this.guimanager = GUIManager.getInstance();
		this.guimanager.setGUIListener(this);
		this.guimain = new gui.Main();
	}

	/**指定したサーバに接続する*/
	public void connectServer(String serverName, int port) {
		try {
			socket = new Socket(serverName, 5001);
			System.out.print("サーバへ接続成功");
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			new ClientReciever(ois, this).start();
			//接続後, user_nameを送る
			Protocol prot = new PlayerEntryProtocol(new PlayerEntry(this.name));
			prot.setProtocol_Bool(true);
			send(prot);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**Protocolのサブクラスで梱包されたデータをサーバへ送信する*/
	public void send(Protocol sendmsg){
		try {
			oos.writeObject(sendmsg);
			oos.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**チャットを送る*/
	public void sendChat(Chat chat){
		send(new ChatProtocol(chat));
		System.out.println("send:"+chat);
	}
	
	/**ゲームに関する情報を送る*/
	public void sendGame(Game game){
		send(new GameProtocol(game));
		System.out.println("send:"+game);
	}
	
	/**受け取ったChatProtocolの中身を見る, GUIへ通知させる処理を追加する*/
	public void recvChat(ChatProtocol cp){
		Chat chat = cp.getChat();
		System.out.println("resv:"+chat);
	}
	
	/**PlayerEntryの中身を見る
	 * @throws IOException */
	public void recvPlayerEntry(PlayerEntryProtocol prot) throws IOException {
		PlayerEntry pe = prot.getPlayerEntry();
		if(prot.isProtocol_Bool()) {
			if(pe.isEntry()) {
				guimanager.setMyId(pe.getPlayer_id());
				if(pe.isFirst()) {
					guimain.nextScene("RuleSettings.fxml");
				}
			}
		}
		else {
			this.name = null;
		}
		System.out.println("recv:"+pe);
	}
	
	//Listener Method
	@Override
	public void joinGame(String username) {
		if(username!=null) {
			this.name = username;
			connectServer(this.SERVER_IP, 5001);
		}
	}

	@Override
	public void registarRule(int round, int passNum, boolean joker, boolean tunnel) {
		send(new GameRuleProtocol(new GameRule( this.guimanager.getMyId(), this.name, round, passNum, joker, tunnel)));
	}

	@Override
	public void cancelGame(boolean cancel) {
		guimanager.nextScene("Start.fxml");
		int id = guimanager.getMyId();
		PlayerEntry pe = new PlayerEntry(this.name);
		pe.setPlayer_id(id);
		Protocol prot = new PlayerEntryProtocol(pe);
		prot.setProtocol_Bool(false);
		send(prot);
	}

	@Override
	public void sendChat(String chat) {
		
	}

	@Override
	public void sendCard(String card) {
		
	}

	@Override
	public void usedPass(boolean pass) {
		
	}
	
	//TODO 
	public void recvGameRule(GameRuleProtocol prot) {
		if(prot.isProtocol_Bool()) {
			GameRule gr = prot.getGameRule();
			System.out.println("recv"+gr);
			//GameRuleが送られてきたら, キャンセルボタンをロック
			//GameRuleを記述したのが自分自身なら, Wait画面へ遷移し, キャンセルボタンをロック
			if(gr.getSettingPlayerID()==this.guimanager.getMyId()
					&& gr.getSettingPlayerName().equals(this.name)) {
				//Rule画面からWait画面へ
			}
			//Wait画面のキャンセルをロック
		}
		else {
			//待機継続
		}
		
	}

	@Override
	public void winGame(boolean win) {
		
	}

	@Override
	public void nextGame(boolean next) {
		
	}

	@Override
	public void exitGame(boolean exit) {
		
	}

	public void recvGameStartable(Protocol prot) {
		//RuleControllerのゲーム開始ボタンを押せるように変化させる
		System.out.println("ChangeStartable!");
		boolean startable = prot.isProtocol_Bool();
		if(Main.ruleCon!=null)
			Main.ruleCon.changeButton(startable);
	}

	public void recvGameStarterKit(GameStarterKitProtocol prot) {
		//TODO ここからゲーム開始画面へ
		GameStarterKit sk = prot.getStarterKit();
		//GUIManagerに情報をすべて渡してから, ゲーム画面へ遷移させる
		if(prot.isProtocol_Bool()&&sk.getPlayer_id()==this.guimanager.getMyId()) {
			System.out.println("Complete_StarterKit!");	
			//ゲームルール情報
			GameRule gr = sk.getGameRule();
			String[] rule = {String.valueOf(gr.getPass()), String.valueOf(gr.getRound())
							,String.valueOf(gr.isJoker()), String.valueOf(gr.isTunnel())
							};
			this.guimanager.setRule(rule);
			
			//ゲームのプレイヤーたちの情報
			for(String h: sk.getHands()) {
				System.out.println("Hand:"+h);
			}
			this.guimanager.setMyHand((ArrayList<String>)sk.getHands());
			this.guimanager.setMemberNum(sk.getPlayerIDList().size());
			this.guimanager.setPlayerName(sk.getPlayers_name());
			this.guimanager.setPlayerCardNum(sk.getPlayers_card_num());
			this.guimanager.setPlayerPassNum(sk.getPlayersPassNum());
			this.guimain.nextScene("Play.fxml");
		}
		//自分のIDと一致しないとき
		else {
			System.out.println("Invalid_StarterKit");
			prot.setProtocol_Bool(false);
		}
		sk.setGameRule(null);
		sk.setHands(null);
		sk.setPlayerIDList(null);
		sk.setPlayers_card_num(null);
		sk.setPlayers_name(null);
		sk.setPlayersPassNum(null);
		send(prot);
	}
}


/**
 * サーバからの情報を逐一受け付けるクライアントのスレッドクラス
 **/
class ClientReciever extends Thread{
	Client owner;
	ObjectInputStream ois;

	public ClientReciever(ObjectInputStream ois, Client owner){
		this.ois = ois;
		this.owner = owner;
	}

	@Override
	public void run(){
		while(ois!=null){
			try {
				recv();
			}
			catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
	}

	private void recv() throws Exception{
		Protocol recvmsg = (Protocol)ois.readObject();
		readProtocol(recvmsg);
	}

	private void readProtocol(Protocol prot) throws IOException{
		switch(prot.getProtocol_ID()){
		//Chat
		case 0:
			owner.recvChat((ChatProtocol)prot);
			break;
		//Game
		case 1:

			break;
		//PlayerEntry
		case 2:
			owner.recvPlayerEntry((PlayerEntryProtocol)prot);
			break;
		//GameRule
		case 3:
			owner.recvGameRule((GameRuleProtocol)prot);
			break;
		//GameStartable
		case 4:
			owner.recvGameStartable(prot);
			break;
		//GameStarterKit
		case 5:
			owner.recvGameStarterKit((GameStarterKitProtocol)prot);
			break;
		default:
			break;
		}
	}
}
