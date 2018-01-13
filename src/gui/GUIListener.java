package gui;

public interface GUIListener {

	public void joinGame(String username);
	
	public void registarRule(int round, int passNum, boolean joker, boolean tunnel);
}
