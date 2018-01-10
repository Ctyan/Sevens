package protocol;
import chat.Chat;

public class ChatProtocol extends Protocol{
	private static final long serialVersionUID = -2345792527523383072L;
	private Chat chat;

	public ChatProtocol(Chat chat){
		super(Protocol.CHAT);
		this.setChat(chat);
	}

	public Chat getChat() {
		return chat;
	}

	public void setChat(Chat chat) {
		this.chat = chat;
	}

}
