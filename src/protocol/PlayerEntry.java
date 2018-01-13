package protocol;

import java.io.Serializable;

public class PlayerEntry implements Serializable{
	private static final long serialVersionUID = 764086852042470980L;
	private String player_name;
	
	public PlayerEntry(String player_name) {
		this.player_name = player_name;
	}
	
	@Override
	public String toString() {
		return this.player_name;
	}
}
