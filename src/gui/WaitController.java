package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class WaitController {
	Stage thisStage;
	GUIManager manager = GUIManager.getInstance();
	GUIListener listener;

	public void setThisStage(Stage stage){
		thisStage = stage;
	}

	@FXML
	protected void cancelButtonAction(ActionEvent e){
		System.out.println("cancel");
		manager.nextScene("Start.fxml");
		if(listener != null) manager.listener.cancelGame(true);
	}
}
