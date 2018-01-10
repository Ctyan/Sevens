package item;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import protocol.ChatProtocol;
import protocol.Protocol;

public class Server implements Runnable{
	Map<Socket, ServerThread> clients;
	ServerSocket serversocket;

	public Server(){
		clients = new HashMap<>();
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
				System.out.println("Waiting for new connection at port:10001");
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
	}
}


/**
 * 接続クライアントごとにスレッドを立ち上げる.
 * スレッドの主な役割は各クライアントからの受信を監視する
 **/
class ServerThread extends Thread{
	Server mainserver;
	Socket client;
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

		default:
			break;
		}
	}
}
