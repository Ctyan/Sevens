package protocol;

import java.io.Serializable;
import item.Player;

public class PlayerEntry implements Serializable{
	private static final long serialVersionUID = 764086852042470980L;
	private String player_name;
	private Integer player_id;
	private boolean isEntry;
	private boolean isFirst;
	
	public PlayerEntry(String player_name) {
		this.player_name = player_name;
	}
	
	public String getPlayer_name() {
		return player_name;
	}

	public void setPlayer_name(String player_name) {
		this.player_name = player_name;
	}

	public boolean isEntry() {
		return isEntry;
	}

	public void setEntry(boolean isEntry) {
		this.isEntry = isEntry;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}
	
	@Override
	public String toString() {
		return this.player_name+", Entry="+isEntry+", First="+isFirst;
	}

	public Integer getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(Integer player_id) {
		this.player_id = player_id;
	}

}
