package main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import gui.*;
import protocol.*;


/**起動すると指定されたサーバへ接続します*/
public class Client implements GUIListener{
	private static final String SERVER_IP = "localhost";
	private static final int SERVER_PORT = 5001;
	//TODO GUImanager, Listener, etc...
	GUIManager guimanager;
	gui.Main guimain;
	
	String name;
	Socket socket;

	ObjectOutputStream oos;
	ObjectInputStream ois;

	public static void main(String[] args){
		BufferedReader  reader = new BufferedReader(new InputStreamReader(System.in));
		try{
			//reader = new BufferedReader(new InputStreamReader(System.in));
			//System.out.println("Your name >");
			Client client = new Client();
			//System.out.print("Server name(localhost or 133.27.....)? >");
			//String serverName = SERVER_IP;
			//client.connectServer(serverName, 5001);
			/* */
			client.guimain.main(args);
			//マルチコンソールチャット テスト用
			/* */
			while(true){
				String line = reader.readLine();
				client.sendChat(new Chat(line, line.length(), client.name));
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public Client() {
		this.guimanager = GUIManager.getInstance();
		this.guimanager.setGUIListener(this);
		this.guimain = new gui.Main();
		this.guimain.setGUIManger(this.guimanager);
	}

	public Client(String name){
		this.name = name;
		this.guimanager = GUIManager.getInstance();
		this.guimanager.setGUIListener(this);
		this.guimain = new gui.Main();
		this.guimain.setGUIManger(this.guimanager);
	}

	/**指定したサーバに接続する*/
	public void connectServer(String serverName, int port) {
		Socket socket;
		try {
			socket = new Socket(serverName, 5001);
			System.out.print("サーバへ接続成功");
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			new ClientReciever(ois, this).start();
			
			//接続後, user_nameを送る
			send(new PlayerEntryProtocol(new PlayerEntry(this.name)));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**受け取ったChatProtocolの中身を見る, GUIへ通知させる処理を追加する*/
	public void recvChat(ChatProtocol cp){
		Chat chat = cp.getChat();
		System.out.println("resv:"+chat);
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
	
	
	//Listener Method
	@Override
	public void joinGame(String username) {
		this.name = username;
		connectServer(this.SERVER_IP, 5001);
	}

	@Override
	public void registarRule(int round, int passNum, boolean joker, boolean tunnel) {
		
	}

	@Override
	public void cancelGame(boolean cancel) {
		
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
				Thread.sleep(500);
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

	private void readProtocol(Protocol prot){
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
			break;
			
			
		default:
			break;
		}
	}
}
