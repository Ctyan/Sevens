package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

	@FXML
	protected void okButtonAction(ActionEvent e){
		System.out.println("ok");
		System.out.println(Round.getSelectedToggle());
		System.out.println(Pass.getSelectedToggle());
		System.out.println(Joker.getSelectedToggle());
		System.out.println(Tunnel.getSelectedToggle());

		Main.manager.nextScene("Wait.fxml");
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
