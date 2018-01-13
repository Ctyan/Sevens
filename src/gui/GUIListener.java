package gui;

public interface GUIListener {

	public void joinGame(String username);

	public void cancelGame(boolean cancel);

	public void registarRule(int round, int passNum, boolean joker, boolean tunnel);

	public void sendChat(String chat);

	public void sendCard(String card);

	/**メインプレイヤーがパスを押した通知*/
	public void usedPass(boolean pass);

	/**プレイヤーが手札を全て使った通知＝勝利*/
	public void winGame(boolean win);

	/**次のラウンドへが押された通知*/
	public void nextGame(boolean next);

	/**ゲームの終了の通知*/
	public void exitGame(boolean exit);

}
