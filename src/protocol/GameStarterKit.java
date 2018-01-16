package protocol;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class GameStarterKit implements Serializable{
	private static final long serialVersionUID = 9138036041514881803L;
	private Map<Integer, String> players_name;
	private Map<Integer, Integer> players_card_num;
	private List<Integer> players_id;
	private List<String> hands;
	private GameRule rule;
	
	public GameStarterKit() {
		
	}
	
	public GameStarterKit(GameRule rule) {
		this.rule = rule;
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

	public List<Integer> getPlayers_id() {
		return players_id;
	}

	public void setPlayers_id(List<Integer> players_id) {
		this.players_id = players_id;
	}

	public List<String> getHands() {
		return hands;
	}

	public void setHands(List<String> hands) {
		this.hands = hands;
	}

	public GameRule getRule() {
		return rule;
	}

	public void setRule(GameRule rule) {
		this.rule = rule;
	}	
}
