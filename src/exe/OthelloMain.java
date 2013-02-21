package exe;

import game.*;
import graphix.*;

import javax.swing.*;

import simpleStrategies.*;
import manualStrategies.*;

/**
 * オセロゲームのメイン・クラス。
 * @author Hiro
 *
 */
public class OthelloMain extends JFrame {
	
	final static int blackAllottedTime = -1; //黒プレーヤーの持ち時間 [ミリ秒] 1分 = 60000 msec　負の値を設定すると時間無制限になる
	final static int whiteAllottedTime = -1; //白プレーヤーの持ち時間 [ミリ秒] 1分 = 60000 msec　負の値を設定すると時間無制限になる
	
	static GameMaster game;
	static StrategyFactory blackFactory, whiteFactory; //戦略オブジェクトを生成するオブジェクト
	static Strategy blackStrategy, whiteStrategy;

	public OthelloMain(){
		setTitle("Othello");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//新しいゲームを生成
		game = new GameMaster(blackAllottedTime, whiteAllottedTime);

		//グラフィックスを準備
		OthelloGraphics panel = new SimpleGraphics(game);
		getContentPane().add(panel);
		pack();
		
		//先手の戦略を選択。用いたい戦略のコメントアウトを外す。
//		blackFactory = new SimpleStrategyFactory(Player.Black, GameMaster.SIZE); //簡易CPU
//		blackFactory = new KaihoStrategyFactory(Player.Black, GameMaster.SIZE); //開放度理論
		blackFactory = new ManualGUIStrategyFactory(Player.Black, GameMaster.SIZE, panel); //人間 
//		blackFactory = new ManualStrategyFactory(Player.Black, GameMaster.SIZE); //人間 

		//後手の戦略を選択。用いたい戦略のコメントアウトを外す。
//		whiteFactory = new SimpleStrategyFactory(Player.White, GameMaster.SIZE); //簡易CPU
		whiteFactory = new KaihoStrategyFactory(Player.White, GameMaster.SIZE); //開放度理論
//		whiteFactory = new ManualGUIStrategyFactory(Player.White, GameMaster.SIZE, panel);  //人間

		blackStrategy = blackFactory.createStrategy();
		whiteStrategy = whiteFactory.createStrategy();
			
	}
	
	public static void main(String[] args){
	
		OthelloMain app = new OthelloMain();
		app.setVisible(true);

		try{
			while(game.isInGame()){ //ゲーム終了まで繰り返し
				if(game.getCurrentPlayer() == Player.Black){ //黒の手番
					game.startTimer(Player.Black); //タイマーをスタート
					while(!game.putPiece(blackStrategy.nextMove(game.getGameState(),game.getRemainingTime(Player.Black)))); //黒の戦略を呼び出し、次の手を打つ。もし石を置いてはいけない場所に石が置かれたら、ちゃんとした場所に置かれるまで繰り返す。
					game.stopTimer(Player.Black); //石が置かれたら、タイマーを止める。
				}else{ //白の手版
					game.startTimer(Player.White);
					while(!game.putPiece(whiteStrategy.nextMove(game.getGameState(),game.getRemainingTime(Player.White)))); //白の戦略を呼び出し、次の手を打つ。もし石を置いてはいけない場所に石が置かれたら、ちゃんとした場所に置かれるまで繰り返す。
					game.stopTimer(Player.White);
				}
			}
		}catch(Exception e){
			//TODO
		}
		
		//各戦略クラスの終了処理メソッドを呼び出す。
		blackStrategy.terminate();
		whiteStrategy.terminate();

	}
	
}