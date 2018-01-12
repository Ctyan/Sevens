package game;

public class GamePlayable{

	private int roundValue;
	private int passValue;
	private boolean joker;
	private boolean tunnel;

	public GamePlayable(int roundValue, int passValue, boolean joker, boolean tunnel) {
		this.roundValue = roundValue;
		this.passValue = passValue;
		this.joker = joker;
		this.tunnel = tunnel;
	}

	public int getRoundValue() {
		return roundValue;
	}

	public int getPassValue() {
		return passValue;
	}

	public boolean isJoker() {
		return joker;
	}

	public boolean isTunnel() {
		return tunnel;
	}

	public boolean isRound(int r) {
		return r <= roundValue;
	}

	public boolean isPass(int p) {
		return p < passValue;
	}
}
