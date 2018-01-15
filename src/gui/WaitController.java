package gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WaitController {
	GUIManager manager = GUIManager.getInstance();
	GUIListener listener = manager.listener;
	@FXML private Button cancelbutton;
	
	/**Wait画面からPlay画面の遷移の際、呼び出すメソッド*/
	public void readyStart(){
		manager.gameStart();
	}
	
	/**ゲーム開始ボタンを押せるようにするメソッド*/
	public void changeButton(boolean able){
		Platform.runLater(() -> {
			cancelbutton.setVisible(able);
			System.out.println("ChangeCancelButton!");
		});
	}
	
	@FXML
	protected void cancelButtonAction(ActionEvent e){
		System.out.println("cancel");
		//manager.nextScene("Start.fxml");
		if(listener != null) listener.cancelGame(true);
	}
	
	
	
}
