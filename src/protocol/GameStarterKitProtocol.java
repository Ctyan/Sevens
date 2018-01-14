package protocol;

public class GameStarterKitProtocol extends Protocol{
	private static final long serialVersionUID = 2739777496829756431L;
	private GameStarterKit starterKit;
	
	public GameStarterKitProtocol(GameStarterKit starterKit) {
		super(Protocol.GAME_STARTER_KIT);
		this.setStarterKit(starterKit);
	}

	public GameStarterKit getStarterKit() {
		return starterKit;
	}

	public void setStarterKit(GameStarterKit starterKit) {
		this.starterKit = starterKit;
	}

}
