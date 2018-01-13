package gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIManager {
	private static GUIManager instance = new GUIManager();
	GUIListener listener;
	Stage thisStage;
	int memberCount = 0;
	boolean ruleSceneFlag = true; //ルール画面への判定

	//test
	String[] playername = new String[] {"a1","a2","a3","a4","a5"};
	String[] playercard = new String[] {"club1", "heart1", "spade1", "diamond1","club11"};

	private GUIManager(){
		instance = this;
	}

	public static GUIManager getInstance(){
		return instance;
	}

	public void setThisStage(Stage stage){
		this.thisStage = stage;
	}

	public void gameStart(){
		this.nextScene("play.fxml");
	}

	public void setRuleSceneFlag(boolean ruleSceneFlag) {
		this.ruleSceneFlag = ruleSceneFlag;
	}

	public boolean getRuleSceneFlag() {
		return ruleSceneFlag;
	}

	public void setPlayerName(String[] playername){
		this.playername = playername;
	}

	public String[] getPlayerName(){
		return this.playername;
	}

	public void setPlayerCard(String[] playercard){
		this.playercard = playercard;
	}

	public String[] getPlayerCard(){
		return playercard;
	}

	public void setGUIListener(GUIListener listener) {
		this.listener = listener;
	}

	public GUIListener getGUIListener() {
		return this.listener;
	}

	public void nextScene(String nextScene){
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nextScene));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			thisStage.setScene(scene);
			thisStage.show();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}

