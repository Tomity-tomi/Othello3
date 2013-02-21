package simpleStrategies;
import game.*;

//１手だけ先読みするシンプルな戦略。評価関数は石を置けるマスの数（手数）と角のみを考慮。
public class KaihoStrategy extends Strategy {
	
	final static int PenaltyCorner = 100; //相手に角をとられてしまう戦略に対するペナルティー
	final static int corners[][] = {{0,0}, {0, GameMaster.SIZE-1}, {GameMaster.SIZE-1,0}, {GameMaster.SIZE-1,GameMaster.SIZE-1}}; //角の位置

	public KaihoStrategy(Player _thisPlayer, int size) { //コンストラクタ
		super(_thisPlayer, size);
	}

	@Override //親クラスgame.Strategyの抽象メソッドを実装
	public Move nextMove(GameState currentState, int remainingTime) { 
		Move bestMove = new Move();
		
		//角が取れるなら取る
		for(int i=0; i<4; i++){
			if(currentState.isLegal(thisPlayer, corners[i][0], corners[i][1])){
				bestMove.x = corners[i][0]; bestMove.y = corners[i][1];
				return bestMove;
			}
		}
		
		//取れなければ、石を打った後の盤面のスコアがもっとも良い戦略を選ぶ
		int maxScore = Integer.MIN_VALUE;
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				if(currentState.isLegal(thisPlayer,i,j)){ //もしも(i,j)に自分の石を置けるならば
					try {
						GameState expectedState = currentState.nextState(thisPlayer, i, j); //そこに石を置いた場合の盤面を取得
						int score = computeScore(expectedState, currentState); //その盤面の評価値を計算
						if(score > maxScore){ //もしこれが最善の評価地なら、この手を記憶
							maxScore = score;
							bestMove.x = i; 	bestMove.y = j;
						}
					} catch (OthelloMoveException e) { //エラー処理。このエラーが発生することはないはず
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("CPU's move:" + bestMove.toString()); //デバック用。コンソールにCPUの手を表示。
		return bestMove; //最良の手を返す
	}
	
	//盤面のスコアを評価するメソッド
	private int computeScore(GameState expectedState, GameState currentState){
		int KaihoSum = 0; //開放度そのもの
		int scoreCorner = 0; //スコアコーナー
		//開放度を評価するためのループ
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				int KaihoValue=0; //開放度を評価するパラメーター
				//盤面上の石の色が変わっていれば、その石の周りの開放度を求める
				if(expectedState.cells[i][j].getValue()-currentState.cells[i][j].getValue() != 0){
					for(int k=i-1; k<=i+1; k++){
						for(int l=j-1; l<=j+1; l++){
							if(expectedState.isWithinRange(k,l) == false){
								continue;
							}
							if(expectedState.isEmpty(k, l) == true){
									KaihoValue--; //ひっくり返ったコマの周りが空白ならその分KaihoValueを小さくする
							}
						}
					}
				}
				KaihoSum += KaihoValue; 
			}
		}
		for(int i=0; i<4; i++){ //もし次の盤面で相手が角に石を置けるなら、その評価値をPenaltyCornerだけ減じる
			if(expectedState.isLegal(thisPlayer.oppositePlayer(), corners[i][0], corners[i][1]))
				scoreCorner = -PenaltyCorner;
		}
		System.out.println(KaihoSum);//デバッグ用
		return KaihoSum + scoreCorner;
	}
}