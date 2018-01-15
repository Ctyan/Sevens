package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class RankingController implements Initializable{
	GUIManager manager = GUIManager.getInstance();
	GUIListener listener = manager.listener;
	public Button button1; //次のラウンドへ
	public Button button2; //終了
	public VBox vbox0,vbox1,vbox2,vbox3,vbox4,vbox5,vbox6; //vbox
	public Label round1,round2,round3,round4,round5,round6,round7; //round
	public Label a_name,b_name,c_name,d_name,e_name,f_name; //player name
	public Label a_score1,a_score2,a_score3,a_score4,a_score5,a_score6,a_score7; //a_player socre
	public Label b_score1,b_score2,b_score3,b_score4,b_score5,b_score6,b_score7; //b_player socre
	public Label c_score1,c_score2,c_score3,c_score4,c_score5,c_score6,c_score7; //c_player socre
	public Label d_score1,d_score2,d_score3,d_score4,d_score5,d_score6,d_score7; //d_player socre
	public Label e_score1,e_score2,e_score3,e_score4,e_score5,e_score6,e_score7; //e_player socre
	public Label f_score1,f_score2,f_score3,f_score4,f_score5,f_score6,f_score7; //f_player socre
	public Label a_sum,b_sum,c_sum,d_sum,e_sum,f_sum; //player score sum
	int n=0;
	public Label[] name,one_round_score,two_round_score,three_round_score,four_round_score,
	five_round_score,six_round_score,seven_round_score;
	Map<Integer, String> playerName = new HashMap<>(); //ID　プレイヤー名
	Map<Integer, Integer> playerScore = new HashMap<>(); //ラウンドのスコア
	List<Integer> listId = new ArrayList<Integer>(); //プレイヤーIDのリスト
	int myId; //メインプレイヤーのID
	int aSum=0,bSum=0,cSum=0,dSum=0,eSum=0,fSum=0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(manager.getChangeButtonflag()) changeButton();
		if(n==0) setArray();
		createRanking();
		setScore(manager.getRound());
	}

	public void setArray(){
		name = new Label[] {a_name,b_name,c_name,d_name,e_name,f_name};
		one_round_score = new Label[] {a_score1,b_score1,c_score1,d_score1,e_score1,f_score1};
		two_round_score = new Label[] {a_score2,b_score2,c_score2,d_score2,e_score2,f_score2};
		three_round_score = new Label[] {a_score3,b_score3,c_score3,d_score3,e_score3,f_score3};
		four_round_score = new Label[] {a_score4,b_score4,c_score4,d_score4,e_score4,f_score4};
		five_round_score = new Label[] {a_score5,b_score5,c_score5,d_score5,e_score5,f_score5};
		six_round_score = new Label[] {a_score6,b_score6,c_score6,d_score6,e_score6,f_score6};
		seven_round_score = new Label[] {a_score7,b_score7,c_score7,d_score7,e_score7,f_score7};
		playerName = manager.getPlayerName();
		playerScore = manager.getPlayerScore();
		myId = manager.getMyId(); //メインプレイやのID
		for(Integer id : playerName.keySet()) listId.add(id); //プレイヤーのIDリスト
		n ++;
	}

	public void createRanking(){
		int i = 0;
		for(Integer id : listId){
			name[i].setText(playerName.get(id));
			i ++;
		}
	}

	public void setScore(int round){
		int i = 0;
		for(Integer score : manager.getRoundScore()){
			if(round == 1) one_round_score[i].setText(String.valueOf(score));
			else if(round == 2) two_round_score[i].setText(String.valueOf(score));
			else if(round == 3) three_round_score[i].setText(String.valueOf(score));
			else if(round == 4) four_round_score[i].setText(String.valueOf(score));
			else if(round == 5) five_round_score[i].setText(String.valueOf(score));
			else if(round == 6) six_round_score[i].setText(String.valueOf(score));
			else if(round == 7) seven_round_score[i].setText(String.valueOf(score));
			calcSum(score ,i);
			i ++;
		}
	}

	public void calcSum(int score, int i){
		if(i==0){
			aSum += score;
			a_sum.setText(String.valueOf(aSum));
		}else if(i==1){
			bSum += score;
			b_sum.setText(String.valueOf(bSum));
		}else if(i==2){
			cSum += score;
			c_sum.setText(String.valueOf(cSum));
		}else if(i==3){
			dSum += score;
			d_sum.setText(String.valueOf(dSum));
		}else if(i==4){
			eSum += score;
			e_sum.setText(String.valueOf(eSum));
		}else if(i==5){
			fSum += score;
			f_sum.setText(String.valueOf(fSum));
		}
	}

	/**全員が次のラウンドへを押した場合このメソッドを呼び出して次のラウンドへ進む*/
	public void nextRound(){
		//manager.nextScene("play.fxml");
	}

	@FXML
	protected void nextRound(ActionEvent e){
		System.out.println("次のラウンドへ");
		listener.nextGame(true);
	}

	@FXML
	protected void exitRound(ActionEvent e){
		System.out.println("終了");
		listener.exitGame(true);
		//manager.nextScene("Start.fxml");
	}

	/**ボタンの切り替え　次のラウンドへ➡終了*/
	public void changeButton(){
		button1.setVisible(false);
		button2.setVisible(true);
	}

}
