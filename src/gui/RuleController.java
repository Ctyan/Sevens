package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class RuleController {
	GUIManager manager = GUIManager.getInstance();
	GUIListener listener = manager.listener;
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
	//Button
	public Button startButton;

	/**ゲーム開始ボタンを押せるようにするメソッド*/
	public void changeButton(){
		Platform.runLater(() -> {
			startButton.setDisable(true);
		});
	}

	@FXML
	protected void okButtonAction(ActionEvent e){
		System.out.println("ok");
		int round = Integer.valueOf((String) Round.getSelectedToggle().getUserData());
		int pass = Integer.valueOf((String) Pass.getSelectedToggle().getUserData());
		boolean joker = Boolean.valueOf((String) Joker.getSelectedToggle().getUserData());
		boolean tunnel = Boolean.valueOf((String) Tunnel.getSelectedToggle().getUserData());
		System.out.println(round);
		System.out.println(pass);
		System.out.println(joker);
		System.out.println(tunnel);

		if(listener != null) listener.registarRule(round, pass, joker, tunnel);
		//manager.nextScene("play.fxml");
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
