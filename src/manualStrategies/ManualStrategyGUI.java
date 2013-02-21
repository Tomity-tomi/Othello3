package manualStrategies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import game.*;
import graphix.OthelloGraphics;

public class ManualStrategyGUI extends StrategyGUI{

	String playerName;
	BufferedReader br;
	
//	volatile boolean flagWaitingForMouseClick;
	
	Move next = new Move();
	
	final int int_a = 'a', int_A= 'A';//'a'と'A'の文字コードを代入
	
	public ManualStrategyGUI(Player _thisPlayer, int size, OthelloGraphics gui) {
		super(_thisPlayer, size);
		
		gui.registerStrategy(this);

		//コンソールからの入力ストリームの準備
		br = new BufferedReader(new InputStreamReader(System.in));
		
//		flagWaitingForMouseClick = false;
		
		if(thisPlayer == Player.Black)
			playerName = "黒";
		else
			playerName = "白";

	}


	@Override
	public synchronized Move nextMove(GameState currentState, int remainingTime) {
//		if(errorState != ErrorState.None){
//			System.err.println("この場所に石を置くことはできません。");
//		}
		
		System.out.print(playerName+"の石を置く場所をクリックして選んでください。");
		
		try {
			wait();
		} catch (InterruptedException e) {
			System.out.println(e);
		}
		
		return next;
	}
	

	@Override
	public synchronized void pickCell(int i, int j) {
		next.x = i;
		next.y = j;
		notifyAll();
	}
	

}
