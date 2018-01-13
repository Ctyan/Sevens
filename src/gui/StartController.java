package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class StartController {
	GUIManager manager = GUIManager.getInstance();
	GUIListener listener = manager.listener;
	public TextField text;
	Alert alert = new Alert(AlertType.INFORMATION);

	@FXML
	protected void StartGame(ActionEvent e) {
		System.out.println("start");
		String username = text.getText();
		if (username.equals("")) {
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("ユーザ名を入力してください");
			alert.showAndWait();
		} else {
			System.out.println(username);
			if (listener != null){
				listener.joinGame(username);
				//if (manager.getRuleSceneFlag()) manager.nextScene("RuleSettings.fxml");
				//else manager.nextScene("Wait.fxml");
			}
		}
	}

}
