package gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
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
	public AnchorPane bord; // ボード
	public VBox vbox; // 他プレイヤーの表示ボックス
	public AnchorPane anchorpane; // anchorpane
	// player
	public Label username;
	public Label userpass;
	int userpassNum = 0;
	// player hand
	ArrayList<ImageView> imageview = new ArrayList<ImageView>();
	ArrayList<Image> image = new ArrayList<Image>();
	// vplayer用vbox
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
	int num = 7;
	int chatCount = 0;
	// manager
	GUIManager manager = new GUIManager();
	// player test
	String player = "a0";
	// vplayer test
	int passNum = 0;
	ArrayList<String> testname = new ArrayList<String>();
	ArrayList<String> testhand = new ArrayList<String>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setArray();
		createVPlayer(5, testname, testhand, passNum);
		createHand(testhand);
		createPlayer(player, userpassNum);
		createRule("3", "1/5", "あり", "あり");
	}

	public void setArray() {
		for (String name : manager.getPlayerName()) {
			testname.add(name);
		}
		for (String hand : manager.getPlayerCard()) {
			testhand.add(hand);
		}
	}

	// player
	public void createHand(ArrayList<String> testhand) {
		Image cardImage = null;
		String filepath = "src/gui/resources/";
		ImageView iv = null;
		Lighting lighting = new Lighting();
		double x = 262;
		double y = 507;

		for (int i = 0; i < testhand.size(); i++) {
			try {
				cardImage = new Image(new FileInputStream(filepath + testhand.get(i) + ".gif"));
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
				System.out.println(e);
				removeCard((ImageView)e.getSource());
			});
		}
	}

	public void removeCard(ImageView iv){
		String text = testhand.remove(imageview.indexOf(iv));
		System.out.println("remove:"+text);
		imageview.remove(iv);
		anchorpane.getChildren().remove(iv);
	}

	private void createPlayer(String player, int userpassNum) {
		username.setText(player);
		userpass.setText(" パス ： " + userpassNum);
	}

	// vplayer
	public void createVPlayer(int member, ArrayList<String> testname, ArrayList<String> testhand, int passNum) {
		vPane = new Pane[] { vpane1, vpane2, vpane3, vpane4, vpane5 };
		vName = new Label[] { vname1, vname2, vname3, vname4, vname5 };
		vCard = new Label[] { vcard1, vcard2, vcard3, vcard4, vcard5 };
		vPass = new Label[] { vpass1, vpass2, vpass3, vpass4, vpass5 };
		for (int i = 0; i < member; i++) {
			vPane[i].setVisible(true);
			vName[i].setText(testname.get(i));
			vCard[i].setText(String.valueOf(testhand.size()));
			vPass[i].setText(String.valueOf(passNum));
		}
	}

	// Rule
	private void createRule(String pass, String round, String joker, String tunnel) {
		Label passCou = new Label("パス制限：" + pass + "回");
		passCou.setId("ruletext");
		Label roundCou = new Label("Round：" + round);
		roundCou.setId("ruletext");
		Label jokeris = new Label("ジョーカー：" + joker);
		jokeris.setId("ruletext");
		Label tunnelis = new Label("トンネル：" + tunnel);
		tunnelis.setId("ruletext");
		rulehbox.getChildren().addAll(passCou, roundCou, jokeris, tunnelis);
	}

	// ActionEvent or MouseEvent
	@FXML
	public void StampAction1(ActionEvent e) {
		chat.add(new Label(player + "：" + stamp1.getText()));
		chat.get(chatCount).setId("chattext");
		chathbox.getChildren().addAll(chat.get(chatCount));
		chatCount++;
		if (chatCount > num) {
			chathbox.getChildren().remove(chat.remove(0));
			chatCount--;
		}
	}

	@FXML
	public void StampAction2(ActionEvent e) {
		chat.add(new Label(player + "：" + stamp2.getText()));
		chat.get(chatCount).setId("chattext");
		chathbox.getChildren().addAll(chat.get(chatCount));
		chatCount++;
		if (chatCount > num) {
			chathbox.getChildren().remove(chat.remove(0));
			chatCount--;
		}
	}

	@FXML
	public void StampAction3(ActionEvent e) {
		chat.add(new Label(player + "：" + stamp3.getText()));
		chat.get(chatCount).setId("chattext");
		chathbox.getChildren().addAll(chat.get(chatCount));
		chatCount++;
		if (chatCount > num) {
			chathbox.getChildren().remove(chat.remove(0));
			chatCount--;
		}
	}

	@FXML
	public void StampAction4(ActionEvent e) {
		chat.add(new Label(player + "：" + stamp4.getText()));
		chat.get(chatCount).setId("chattext");
		chathbox.getChildren().addAll(chat.get(chatCount));
		chatCount++;
		if (chatCount > num) {
			chathbox.getChildren().remove(chat.remove(0));
			chatCount--;
		}
	}

	@FXML
	public void PassCount(ActionEvent e) {
		userpass.setText(" パス ： " + userpassNum++);
	}

}