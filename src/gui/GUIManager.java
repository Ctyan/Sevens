package gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIManager {
	private static GUIManager instance = new GUIManager();
	GUIListener listener;
	Stage thisStage;
	//StartController
	boolean ruleSceneFlag = true; //ルール画面への判定
	//RuleController
	private int  memberNum; //参加人数
	//PlayController
	Map<Integer, String> playerName = new HashMap<>(); //ID　プレイヤー名
	Map<Integer, Integer> playerCardNum = new HashMap<>(); //ID　カード枚数
	Map<Integer, Integer> playerPassNum = new HashMap<>(); //ID　パス回数
	ArrayList<String> myHand = new ArrayList<String>(); //メインプレイヤーの手札
	private int myId; //メインID
	private String[] rule; //ルールの詳細　順番：{"パス回数","ラウンド数","ジョーカーの有無","トンネルの有無"}
	private boolean cardClickFlag; //カードを押せるかの判定
	private boolean passClickFlag; //パスが押せるかの判定
	private boolean changeButtonflag; //ランキング画面のボタンの切り替えの判定

	private GUIManager(){
		instance = this;
	}

	public static GUIManager getInstance(){
		return instance;
	}

	public void setRuleSceneFlag(boolean ruleSceneFlag) {
		this.ruleSceneFlag = ruleSceneFlag;
	}

	public boolean getRuleSceneFlag() {
		return ruleSceneFlag;
	}

	public void setMemberNum(int memberNum) {
		this.memberNum = memberNum;
	}

	public int getMemberNum() {
		return memberNum;
	}

	public Map<Integer, String> getPlayerName() {
		return playerName;
	}

	public void setPlayerName(Map<Integer, String> playerName) {
		this.playerName = playerName;
	}

	public void setPlayerCardNum(Map<Integer, Integer> playerCardNum){
		this.playerCardNum = playerCardNum;
	}

	public Map<Integer, Integer> getPlayerCardNum(){
		return this.playerCardNum;
	}

	public void setPlayerPassNum(Map<Integer, Integer> playerPassNum) {
		this.playerPassNum = playerPassNum;
	}

	public Map<Integer, Integer> getPlayerPassNum() {
		return playerPassNum;
	}

	public void setMyHand(ArrayList<String> myHand) {
		this.myHand = myHand;
	}

	public ArrayList<String> getMyHand() {
		return myHand;
	}

	public void setMyId(int myId) {
		this.myId = myId;
	}

	public int getMyId() {
		return myId;
	}

	public void setRule(String[] rule) {
		this.rule = rule;
	}

	public String[] getRule() {
		return rule;
	}

	public void setCardClickFlag(boolean cardClickFlag) {
		this.cardClickFlag = cardClickFlag;
	}

	public boolean getCardClickFlag() {
		return cardClickFlag;
	}

	public void setPassClickFlag(boolean passClickFlag) {
		this.passClickFlag = passClickFlag;
	}

	public boolean getPassClickFlag() {
		return passClickFlag;
	}

	public void setChangeButtonflag(boolean changeButtonflag) {
		this.changeButtonflag = changeButtonflag;
	}

	public boolean getChangeButtonflag() {
		return changeButtonflag;
	}

	public void setGUIListener(GUIListener listener) {
		this.listener = listener;
	}

	public GUIListener getGUIListener() {
		return this.listener;
	}

	/**ステージのセット*/
	public void setThisStage(Stage stage){
		this.thisStage = stage;
	}

	/**Play画面の読み込み*/
	public void gameStart(){
		this.nextScene("play.fxml");
	}

	/**画面遷移のメソッド*/
	public void nextScene(String nextScene){
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(nextScene));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			thisStage.setScene(scene);
			thisStage.show();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}

