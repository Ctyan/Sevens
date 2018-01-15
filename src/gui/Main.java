package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	private GUIManager manager = GUIManager.getInstance();
	public static RuleController ruleCon;
	public static WaitController waitCon;//wait画面キャンセル用
	public static PlayController playCon;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			/*
			 * Start
			 * RuleSettings
			 * Wait
			 * Play
			 * Ranking
			 */
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Start.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			//画面遷移に必要
			manager.setThisStage(primaryStage);
			//表示
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void nextScene(String scene) {
		Platform.runLater(() -> {
			manager.nextScene(scene);
		});
	}
	
	public void playedCardByOtherPlayer(String card, boolean isJoker) {
		Platform.runLater(() -> {
			playCon.boardDraw(card);
		});
	}
	
	public void updatePlayBoard() {
		Platform.runLater(() -> {
			playCon.updateSceneParam();
		});
	}
	
	public void updateTurnLabel(String turnMsg) {
		Platform.runLater(() -> {
			playCon.updateTurnLabel(turnMsg);
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void setGUIManger(GUIManager m) {
		manager = m;
	}

}
