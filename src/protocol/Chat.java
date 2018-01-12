package protocol;
import java.io.Serializable;

public class Chat implements Serializable{
	private static final long serialVersionUID = 4419728520565180630L;
	private String user;
	private String text;
	private Integer emo_id;

	public Chat(){

	}

	public Chat(String text, int emo_id){
		this.text = text;
		this.emo_id = emo_id;
	}

	public Chat(String text, int emo_id, String user){
		this.text = text;
		this.emo_id = emo_id;
		this.setUser(user);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getEmo_id() {
		return emo_id;
	}

	public void setEmo_id(int emo_id) {
		this.emo_id = emo_id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}


	@Override
	public String toString(){
		return "user="+this.user+", text="+this.text+", emo_id="+this.emo_id;
	}

}
