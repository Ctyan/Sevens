package game;

public class GameField {

	private boolean[] spadeLine;
	private boolean[] heartLine;
	private boolean[] clubLine;
	private boolean[] diaLine;

	public GameField() {
		spadeLine = new boolean[13];
		heartLine = new boolean[13];
		clubLine = new boolean[13];
		diaLine = new boolean[13];
	}

	public boolean setCard(int t, int n) {
		if(isCard(t, n)) return false;

		switch (t) {
		case 1:
			spadeLine[n] = true;
			break;

		case 2:
			heartLine[n] = true;
			break;

		case 3:
			clubLine[n] = true;
			break;

		case 4:
			diaLine[n] = true;
			break;

		default:
			break;
		}
		return true;
	}

	public boolean isCard(int t, int n) {
		boolean b = false;;
		switch (t) {
		case 1:
			b = spadeLine[n];
			break;

		case 2:
			b = heartLine[n];
			break;

		case 3:
			b = clubLine[n];
			break;

		case 4:
			b = diaLine[n];
			break;

		default:
			break;
		}
		return b;
	}
}
