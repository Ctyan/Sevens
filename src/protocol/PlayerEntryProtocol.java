package protocol;

public class PlayerEntryProtocol extends Protocol{
	private static final long serialVersionUID = 7792073048877564109L;
	PlayerEntry playerEntry;
	
	public PlayerEntryProtocol(PlayerEntry pe) {
		super(Protocol.PLAYER_ENTRY);
		this.playerEntry = pe;
	}
	
	public PlayerEntry getPlayerEntry() {
		return playerEntry;
	}
	
	public void setPlayerEntry(PlayerEntry playerEntry) {
		this.playerEntry = playerEntry;
	}
	
}
