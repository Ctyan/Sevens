package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class WaitController {
	GUIManager manager = GUIManager.getInstance();
	GUIListener listener = manager.listener;

	/**Wait画面からPlay画面の遷移の際、呼び出すメソッド*/
	public void readyStart(){
		//manager.gameStart();
	}

	@FXML
	protected void cancelButtonAction(ActionEvent e){
		System.out.println("cancel");
		//manager.nextScene("Start.fxml");
		if(listener != null) listener.cancelGame(true);
	}
}
