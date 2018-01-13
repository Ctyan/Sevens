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
	//rule
	int round;
	int pass;
	boolean joker;
	boolean tunnel;
	GUIManager manager = GUIManager.getInstance();
	GUIListener listener;

	@FXML
	protected void okButtonAction(ActionEvent e){
		System.out.println("ok");
		round = Integer.valueOf((String) Round.getSelectedToggle().getUserData());
		pass = Integer.valueOf((String) Pass.getSelectedToggle().getUserData());
		joker = Boolean.valueOf((String) Joker.getSelectedToggle().getUserData());
		tunnel = Boolean.valueOf((String) Tunnel.getSelectedToggle().getUserData());
		System.out.println(round);
		System.out.println(pass);
		System.out.println(joker);
		System.out.println(tunnel);

		if(member >= 3 ){
			manager.nextScene("play.fxml");
			if(listener != null) manager.listener.registarRule(round, pass, joker, tunnel);
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
