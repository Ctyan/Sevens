package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class RuleController {

	//Toggle Group
	public ToggleGroup Round;
	public ToggleGroup Pass;
	public ToggleGroup Joker;
	public ToggleGroup Tunnel;
	//Default Toggle
	public RadioButton radio1;
	public RadioButton radio2;
	public RadioButton radio3;
	public RadioButton radio4;
	//alert
	Alert alert = new Alert(AlertType.INFORMATION);
	int member = 3;

	@FXML
	protected void okButtonAction(ActionEvent e){
		System.out.println("ok");
		System.out.println(Round.getSelectedToggle());
		System.out.println(Pass.getSelectedToggle());
		System.out.println(Joker.getSelectedToggle());
		System.out.println(Tunnel.getSelectedToggle());

		if(member >= 3 ){
			Main.manager.nextScene("play.fxml");
		}else{
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("人数が足りていません");
			alert.showAndWait();
		}
	}

	@FXML
	protected void defaultButtonAction(ActionEvent e){
		System.out.println("deafult");
		radio1.setSelected(true);
		radio2.setSelected(true);
		radio3.setSelected(true);
		radio4.setSelected(true);
	}
}
