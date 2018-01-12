package game;

public class GameField {

	private boolean[][] cardBlocks;

	public GameField() {
		cardBlocks = new boolean[4][13];
	}

	public boolean setCard(int t, int n) {
		if(t<0 || 4<= t || n<0 || 13<=n) return false;
		if(isCard(t, n)) return false;

		cardBlocks[t][n] = true;
		return true;
	}

	public boolean isCard(int t, int n) {
		if(t<0 || 4<= t || n<0 || 13<=n) return false;

		return cardBlocks[t][n];
	}
}
