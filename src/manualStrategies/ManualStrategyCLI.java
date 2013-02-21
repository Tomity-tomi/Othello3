package manualStrategies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import game.*;

public class ManualStrategyCLI extends Strategy {

	String playerName;
	BufferedReader br;
	
	Move next = new Move();
	
	final int int_a = 'a', int_A= 'A';//'a'と'A'の文字コードを代入
	
	public ManualStrategyCLI(Player _thisPlayer, int size) {
		super(_thisPlayer, size);

		//コンソールからの入力ストリームの準備
		br = new BufferedReader(new InputStreamReader(System.in));
		
		if(thisPlayer == Player.Black)
			playerName = "黒";
		else
			playerName = "白";

	}

	@Override
	public Move nextMove(GameState currentState, int remainingTime) {
//		if(errorState != ErrorState.None){
//			System.err.println("この場所に石を置くことはできません。");
//		}

		System.out.print(playerName+"の手を入力してください:");
		
		
		try {
			while(!parse(br.readLine()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return next;
	}
	
	private boolean parse(String str){
		if(str.length()!=2){
			System.err.print("入力する文字は、'a2'のように、二文字でなくてはいけません。"+playerName+"の手を入力してください:");
			return false;
		}
		char colStr = str.charAt(0);
		char rowStr = str.charAt(1);
		
		int colInt = colStr;
		if(Character.isUpperCase(colStr)){
			next.x = colInt - int_A;
		}else if(Character.isLowerCase(colStr)){
			next.x = colInt - int_a;			
		}else{
			System.err.print("最初の文字はアルファベットでなくてはいけません。"+playerName+"の手を入力してください:");
			return false;
		}
		
		if(Character.isDigit(rowStr)){
			next.y = Integer.valueOf(String.valueOf(rowStr))-1;
		}else{
			System.err.print("二文字目はアルファベットでなくてはいけません。"+playerName+"の手を入力してください:");
			return false;			
		}
		
		System.out.println("("+next.x+","+next.y+")");
		
		return true;
	}
	

}
