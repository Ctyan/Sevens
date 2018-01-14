package protocol;

import java.io.Serializable;

public class GameRule implements Serializable{
	private static final long serialVersionUID = 717700048603385261L;
	private int settingPlayerID;
	private String settingPlayerName;
	private int round;
	private int pass;
	private boolean joker;
	private boolean tunnel;
	
	public GameRule(int settingPlayerID, String settingPlayerName ,int round, int pass, boolean joker, boolean tunnel) {
		this.settingPlayerID = settingPlayerID;
		this.settingPlayerName = settingPlayerName;
		this.round = round;
		this.pass = pass;
		this.joker = joker;
		this.tunnel = tunnel;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getPass() {
		return pass;
	}

	public void setPass(int pass) {
		this.pass = pass;
	}

	public boolean isJoker() {
		return joker;
	}

	public void setJoker(boolean joker) {
		this.joker = joker;
	}

	public boolean isTunnel() {
		return tunnel;
	}

	public void setTunnel(boolean tunnel) {
		this.tunnel = tunnel;
	}
	
	public String toString() {
		return "Round="+this.round+", Pass="+this.pass+", Joker="+this.joker+", Tunnel="+this.tunnel;
	}

	public int getSettingPlayerID() {
		return settingPlayerID;
	}

	public void setSettingPlayerID(int settingPlayerID) {
		this.settingPlayerID = settingPlayerID;
	}

	public String getSettingPlayerName() {
		return settingPlayerName;
	}

	public void setSettingPlayerName(String settingPlayerName) {
		this.settingPlayerName = settingPlayerName;
	}
}
