package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class WaitController {
	Stage thisStage;

	public void setThisStage(Stage stage){
		thisStage = stage;
	}

	@FXML
	protected void cancelButtonAction(ActionEvent e){
		System.out.println("cancel");
		Main.manager.minusMember();
		Main.manager.nextScene("Start.fxml");
	}
}
