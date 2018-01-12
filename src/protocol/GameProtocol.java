package protocol;

public class GameProtocol extends Protocol{
	private static final long serialVersionUID = 8726301106850620523L;
	private Game game;

	public GameProtocol(Game Game) {
		super(Protocol.GAME);
		this.setGame(game);
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

}
