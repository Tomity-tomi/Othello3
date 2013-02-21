package exe;

import game.*;
import graphix.*;


import java.applet.Applet;
import java.awt.Color;

import manualStrategies.ManualGUIStrategyFactory;
import simpleStrategies.SimpleStrategyFactory;

/**
 * SimpleStrategyと人間が対戦するプログラムのアプレット版
 * @author Hiro
 *
 */
public class OthelloApplet extends Applet {
	
	final static int blackAllottedTime = 3*60000; //黒プレーヤーの持ち時間 [ミリ秒] 1分 = 60000 msec　負の値を設定すると時間無制限になる
	final static int whiteAllottedTime = 5*60000; //白プレーヤーの持ち時間 [ミリ秒] 1分 = 60000 msec　負の値を設定すると時間無制限になる
	
	static GameMaster game;
	static StrategyFactory blackFactory, whiteFactory; //戦略オブジェクトを生成するオブジェクト
	static Strategy blackStrategy, whiteStrategy;

	public void init(){

		setBackground(Color.WHITE);
		
		game = new GameMaster(blackAllottedTime, whiteAllottedTime);
		
		OthelloGraphics panel = new SimpleGraphics(game);
		add(panel);
		
		blackFactory = new SimpleStrategyFactory(Player.Black, GameMaster.SIZE);

		whiteFactory = new ManualGUIStrategyFactory(Player.White, GameMaster.SIZE, panel); 

		blackStrategy = blackFactory.createStrategy();
		whiteStrategy = whiteFactory.createStrategy();
		
		Thread gameThread = new Thread() {
			public void run(){
				try{
					while(game.isInGame()){
						if(game.getCurrentPlayer() == Player.Black){
							game.startTimer(Player.Black);
							while(!game.putPiece(blackStrategy.nextMove(game.getGameState(),game.getRemainingTime(Player.Black)))); //もし石を置いてはいけない場所に石が置かれたら、ちゃんとした場所に置かれるまで繰り返す。
							game.stopTimer(Player.Black);
						}else{
							game.startTimer(Player.White);
							while(!game.putPiece(whiteStrategy.nextMove(game.getGameState(),game.getRemainingTime(Player.White)))); //もし石を置いてはいけない場所に石が置かれたら、ちゃんとした場所に置かれるまで繰り返す。
							game.stopTimer(Player.White);
						}
					}
				}catch(Exception e){
					//TODO
				}				
			}
		};
		gameThread.start();
			

	}

}
