package protocol;

public class GameRuleProtocol extends Protocol{
	private static final long serialVersionUID = -2837791737764024106L;
	private GameRule gameRule;
	
	public GameRuleProtocol(GameRule gr) {
		super(Protocol.GAME_RULE);
		this.gameRule = gr;
		
	}

	public GameRule getGameRule() {
		return gameRule;
	}

	public void setGameRule(GameRule gameRule) {
		this.gameRule = gameRule;
	}
	
}
