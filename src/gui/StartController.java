package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class StartController {
	public TextField text;
	boolean flag = false;
	Alert alert = new Alert(AlertType.INFORMATION);

	@FXML
	protected void StartGame(ActionEvent e){
		System.out.println("start");
		String username = text.getText();
		if(username.equals("")){
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("ユーザ名を入力してください");
			alert.showAndWait();
			flag = false;
		}else{
			System.out.println(username);
			flag = true;
			Main.manager.plusMember();
		}
		if(flag){
			if(Main.manager.memberCount == 1){
				Main.manager.nextScene("RuleSettings.fxml");
			}else{
				Main.manager.nextScene("Wait.fxml");
			}
		}
	}
}
