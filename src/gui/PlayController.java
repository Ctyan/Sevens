package gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class PlayController implements Initializable {
	GUIManager manager = GUIManager.getInstance();
	GUIListener listener = manager.listener;
	public AnchorPane board; // ボード
	public VBox vbox; // 他プレイヤーの表示ボックス
	public AnchorPane anchorpane; // 全体パネル
	private String filepath = "src/gui/resources/";	//filepath
	// card image
	ArrayList<ImageView> imageview = new ArrayList<ImageView>();
	//player
	public Label myPass;
	public Label myName;
	// vplayerの表示枠
	public Pane[] vPane;
	public Pane vpane1, vpane2, vpane3, vpane4, vpane5;
	public Label[] vName;
	public Label vname1, vname2, vname3, vname4, vname5;
	public Label[] vCard;
	public Label vcard1, vcard2, vcard3, vcard4, vcard5;
	public Label[] vPass;
	public Label vpass1, vpass2, vpass3, vpass4, vpass5;
	// rule
	public HBox rulehbox;
	// chat
	public HBox chathbox;
	public MenuItem stamp1, stamp2, stamp3, stamp4;
	ArrayList<Label> chat = new ArrayList<Label>();
	private int chatCount = 0;
	//all player status
	Map<Integer, String> playerName = new HashMap<>(); //ID　プレイヤー名
	Map<Integer, Integer> playerCardNum = new HashMap<>(); //ID　カード枚数
	Map<Integer, Integer> playerPassNum = new HashMap<>(); //ID　パス回数
	ArrayList<String> myHand = new ArrayList<String>(); //プレイヤーの手札
	List<Integer> listId = new ArrayList<Integer>(); //プレイヤーIDのリスト
	int myId; //メインプレイヤーのID
	int memberNum; //　全体の人数
	String[] rule; //ルールの詳細


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setArray(); //変数、配列の初期化
		createVPlayer(); //vplayerの名前、カード枚数、パス回数の表示
		createPlayerHand(); //playerの手札表示
		createPlayer(); //playerの名前、パス回数の表示
		createRule(); //ルールの表示
	}

	public void setArray() {
    	//all player status
		playerName = manager.getPlayerName();
    	playerCardNum = manager.getPlayerCardNum();
    	playerPassNum = manager.getPlayerPassNum();
    	for(Integer id : playerName.keySet()) listId.add(id);
    	//vplayer status
    	vPane = new Pane[] { vpane1, vpane2, vpane3, vpane4, vpane5 };
		vName = new Label[] { vname1, vname2, vname3, vname4, vname5 };
		vCard = new Label[] { vcard1, vcard2, vcard3, vcard4, vcard5 };
		vPass = new Label[] { vpass1, vpass2, vpass3, vpass4, vpass5 };
		//player hand
		myHand = manager.getMyHand();
		myId = manager.getMyId();
		memberNum = manager.getMemberNum();
		rule = manager.getRule();
	}

    //vplayer
	private void createVPlayer() {
		int i = 0;
		for(Integer id : listId){
			if(id == myId) continue;
			vPane[i].setVisible(true);
			vName[i].setText(playerName.get(id));
			vCard[i].setText(String.valueOf(playerCardNum.get(id)));
			vPass[i].setText(String.valueOf(playerPassNum.get(id)));
			i ++;
		}
	}

	//player
	private void createPlayerHand() {
		Image cardImage = null;
		ImageView iv = null;
		Lighting lighting = new Lighting();
		double x = 262;
		double y = 507;
		int i = 0;

		for (String hand : myHand) {
			try {
				cardImage = new Image(new FileInputStream(filepath + hand + ".gif"));
			} catch (FileNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			iv = new ImageView(cardImage);
			iv.setFitHeight(120);
			iv.setFitWidth(100);
			iv.setLayoutX(x);
			iv.setLayoutY(y);
			iv.setEffect(lighting);

			imageview.add(iv);
			anchorpane.getChildren().addAll(imageview.get(i));
			x += 40;
			imageview.get(i).setOnMouseClicked((MouseEvent e) -> {
				if(manager.getCardClickFlag()){
					System.out.println(e.getSource());
					removeCard((ImageView)e.getSource());
				}
			});
		}
	}

	private void createPlayer() {
		String userName = playerName.get(myId);
		int userPassNum = playerPassNum.get(myId);
		myName.setText(userName);
		myPass.setText(String.valueOf(userPassNum));
	}

	// Rule
	private void createRule() {
		Label passCou = new Label("パス制限：" + rule[0]+ "回");
		passCou.setId("ruletext");
		Label roundCou = new Label("Round：" + rule[1]);
		roundCou.setId("ruletext");
		Label jokeris = new Label("ジョーカー：" + rule[2]);
		jokeris.setId("ruletext");
		Label tunnelis = new Label("トンネル：" + rule[3]);
		tunnelis.setId("ruletext");
		rulehbox.getChildren().addAll(passCou, roundCou, jokeris, tunnelis);
	}

	/**選択された手札の削除するメソッド*/
	public void removeCard(ImageView iv){
		String text = myHand.remove(imageview.indexOf(iv));
		System.out.println("remove"+text);
		int index = imageview.indexOf(iv);
		double x = imageview.get(index).getLayoutX();
		imageview.remove(iv);
		anchorpane.getChildren().remove(iv);
		updateHand(index,x);
		boardDraw(text);
		if(listener != null) listener.sendCard(text);
	}

	/**手札を再描画するメソッド*/
	public void updateHand(int index, double x){
		double newX = x;
		for(int i=0; i<imageview.size()-index; i++){
			imageview.get(index+i).setLayoutX(newX);
			newX += 40;
		}
	}

	/**選択されたカードをボードに表示するメソッド*/
	public void boardDraw(String text){
		String mark = text.replaceFirst("[1-9]+", "");
		String number = text.replaceFirst("[a-z]+", "");
		int num = Integer.valueOf(number);
		ImageView boardIv = null;
		try {
			boardIv = new ImageView(new Image(new FileInputStream(filepath + text + ".gif")));
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		boardIv.setFitHeight(90);
		boardIv.setFitWidth(60);
		if(mark.equals("spade")) boardIv.setLayoutY(0);
		else if(mark.equals("heart")) boardIv.setLayoutY(90);
		else if(mark.equals("club")) boardIv.setLayoutY(180);
	    else if(mark.equals("diamond")) boardIv.setLayoutY(270);

		if(num == 1) boardIv.setLayoutX(0);
		else if(num == 2) boardIv.setLayoutX(60);
		else if(num == 3) boardIv.setLayoutX(120);
		else if(num == 4) boardIv.setLayoutX(180);
		else if(num == 5) boardIv.setLayoutX(240);
		else if(num == 6) boardIv.setLayoutX(300);
		//else if(num == 7) boardIv.setLayoutX(360);
		else if(num == 8) boardIv.setLayoutX(420);
		else if(num == 9) boardIv.setLayoutX(480);
		else if(num == 10) boardIv.setLayoutX(540);
		else if(num == 11) boardIv.setLayoutX(600);
		else if(num == 12) boardIv.setLayoutX(660);
		else if(num == 13) boardIv.setLayoutX(720);

		board.getChildren().addAll(boardIv);
	}

	@FXML
	public void StampAction(ActionEvent e) {
		String text = "";
		if(e.getTarget().equals(stamp1)) text = stamp1.getText();
		else if(e.getTarget().equals(stamp2)) text = stamp2.getText();
		else if(e.getTarget().equals(stamp3)) text = stamp3.getText();
		else if(e.getTarget().equals(stamp4)) text = stamp4.getText();
		chat.add(new Label(playerName.get(myId) + "：" + text));
		chat.get(chatCount).setId("chattext");
		chathbox.getChildren().addAll(chat.get(chatCount));
		chatCount++;
		if (chatCount > 7) {
			chathbox.getChildren().remove(chat.remove(0));
			chatCount--;
		}
		if(listener != null) listener.sendChat(text);
	}

	@FXML
	public void PassCount(ActionEvent e) {
		if(manager.getPassClickFlag()){
			if(listener != null) listener.usedPass(true);
			int userPassNum = playerPassNum.get(myId);
			myPass.setText(String.valueOf(userPassNum));
		}
	}

}