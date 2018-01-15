package protocol;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import item.Player;

public class GameStarterKit implements Serializable{
	private static final long serialVersionUID = 9138036041514881803L;
	private int player_id;
	private List<Integer> playerIDList;
	private Map<Integer, String> players_name;
	private Map<Integer, Integer> players_card_num;
	private Map<Integer, Integer> players_pass_num;
	private List<String> hands;
	private GameRule rule;
	
	public GameStarterKit() {
		
	}
	
	public GameStarterKit(GameRule rule) {
		this.rule = rule;
	}

	public GameStarterKit(int pid, GameRule gr) {
		this.player_id = pid;
		this.rule = gr;
	}

	public Map<Integer, String> getPlayers_name() {
		return players_name;
	}

	public void setPlayers_name(Map<Integer, String> players_name) {
		this.players_name = players_name;
	}

	public Map<Integer, Integer> getPlayers_card_num() {
		return players_card_num;
	}

	public void setPlayers_card_num(Map<Integer, Integer> players_card_num) {
		this.players_card_num = players_card_num;
	}

	public List<String> getHands() {
		return hands;
	}

	public void setHands(List<String> hands) {
		this.hands = hands;
	}

	public GameRule getGameRule() {
		return rule;
	}
	
	public void setGameRule(GameRule rule) {
		this.rule = rule;
	}

	public int getPlayer_id() {
		return player_id;
	}

	public void setPlayer_id(int player_id) {
		this.player_id = player_id;
	}

	public List<Integer> getPlayerIDList() {
		return playerIDList;
	}

	public void setPlayerIDList(List<Integer> playerIDList) {
		this.playerIDList = playerIDList;
	}

	public Map<Integer, Integer> getPlayersPassNum() {
		return players_pass_num;
	}

	public void setPlayersPassNum(Map<Integer, Integer> players_pass_num) {
		this.players_pass_num = players_pass_num;
	}

	
}
