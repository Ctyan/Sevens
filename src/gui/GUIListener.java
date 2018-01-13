package gui;

public interface GUIListener {

	public void joinGame(String username);
	
	public void cancelGame(boolean cancel);
	
	public void registarRule(int round, int passNum, boolean joker, boolean tunnel);
	
	public void sendChat(String chat);
	
	public void sendCard(String card);
	
	public void usedPass(boolean pass);

}
