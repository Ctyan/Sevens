package gui;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIManager {
	Stage thisStage;
	int memberCount = 0;
	//test
	String[] playername = new String[] {"a1","a2","a3","a4","a5"};
	String[] playercard = new String[] {"club1", "heart1", "spade1", "diamond1"};

	public void setThisStage(Stage stage){
		this.thisStage = stage;
	}

	public void plusMember(){
		this.memberCount ++;
	}

	public void minusMember(){
		this.memberCount --;
	}

	public void gameStart(){
		this.nextScene("play.fxml");
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
	
	public void nextScene(String nextScene){
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nextScene));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			thisStage.setScene(scene);
			thisStage.show();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}

