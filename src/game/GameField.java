package game;

public class GameField {

	private boolean[][] cardField;
	
	
	/**場の管理<br>初期値はfalse*/
	public GameField() {
		cardField = new boolean[4][13];
		for(int t=0;t<4;t++) {
			for(int n=0;n<13;n++) {
				cardField[t][n] = false;
			}
		}
	}

	/**(t, n)をtrueにする*/
	public boolean setCard(int t, int n) {
		if(t<0 || 4<= t || n<0 || 13<=n) return false;
		if(isCard(t, n)) return false;

		cardField[t][n] = true;
		return true;
	}

	/**(t, n)を取得*/
	public boolean isCard(int t, int n) {
		if(t<0 || 4<= t || n<0 || 13<=n) return false;

		return cardField[t][n];
	}

	/**(t, n)の1つ前を取得*/
	public boolean isFrontCard(int t, int n) {
		if(t<0 || 4<= t || n<0 || 13<=n) return false;

		boolean front = false;

		switch (n) {
		case 12:
			front = isCard(t, 0);
			break;

		default:
			front = isCard(t, n+1);
			break;
		}

		return front;
	}

	/**(t, n)の1つ後を取得*/
	public boolean isBackCard(int t, int n) {
		if(t<0 || 4<= t || n<0 || 13<=n) return false;

		boolean back = false;

		switch (n) {
		case 0:
			back = isCard(t, 12);
			break;

		default:
			back = isCard(t, n-1);
			break;
		}

		return back;
	}
	
}
