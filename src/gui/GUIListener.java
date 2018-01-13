package gui;

interface GUIListener {

	void joinGame(String username);
	
	void registarRule(int round, int passNum, boolean joker, boolean tunnel);
}
