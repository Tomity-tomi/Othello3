package game;

import java.util.Observable;

/**
 * ゲームの進行を行うクラス。通常、参加者がこのクラスのメソッドを用いることはない。
 * @author Hiro
 *
 */
public class GameMaster extends Observable {
	

	public final static int SIZE = 8; //盤面のタテ・ヨコのサイズ
	private GameState state;
	final static int ThreadSleepTime = 10;

	
	private int blackRemainingTime, whiteRemainingTime; //黒、白の残り時間 [ミリ秒]
	Thread blackTimer, whiteTimer; //タイマー用のスレッド
	private boolean flagBlackTimerRunning, flagWhiteTimerRunning; //このフラグがtrueの時に持ち時間が減る
	private boolean flagBlackTimeUp, flagWhiteTimeUp;

	
	
	/**
	 * コンストラクタ。
	 * @param blackAllottedTime 黒プレーヤーの持ち時間 [ミリ秒]
	 * @param whiteAllotedTime　白プレーヤーの持ち時間 [ミリ秒]
	 */
	public GameMaster(int blackAllottedTime, int whiteAllottedTime){
		//Initialize the game
		state = new GameState(SIZE);
		
		//タイマーのセットアップ
		blackRemainingTime = blackAllottedTime;
		whiteRemainingTime = whiteAllottedTime;
		flagBlackTimeUp = false;	flagWhiteTimeUp = false;
		flagBlackTimerRunning = false; flagWhiteTimerRunning = false;

		blackTimer = new Thread() {
			public void run(){
				try{
					while(blackRemainingTime>0){
						Thread.sleep(ThreadSleepTime); //10ミリ秒スリープ
						if(flagBlackTimerRunning){
							blackRemainingTime = blackRemainingTime - ThreadSleepTime;
							if(blackRemainingTime%1000 < ThreadSleepTime){ 
								setChanged(); notifyObservers();//1秒ごとにグラフィックスを再描画するシグナルを送る
							}
						}
					}
					state.inGame = false; 
					flagBlackTimeUp = true;
					setChanged(); notifyObservers();//グラフィックスを再描画するシグナルを送る
				}catch(InterruptedException ex){
					System.err.println(ex.getStackTrace());
				}
			}
		};
		
		whiteTimer = new Thread() {
			public void run(){
				try{
					while(whiteRemainingTime>0){
						Thread.sleep(ThreadSleepTime); //10ミリ秒スリープ
						if(flagWhiteTimerRunning){
							whiteRemainingTime = whiteRemainingTime - ThreadSleepTime;
							if(whiteRemainingTime%1000 < ThreadSleepTime){ 
								setChanged(); notifyObservers();//1秒ごとにグラフィックスを再描画するシグナルを送る
							}
						}
					}
					state.inGame = false; 
					flagWhiteTimeUp = true;
					setChanged(); notifyObservers();//グラフィックスを再描画するシグナルを送る
				}catch(InterruptedException ex){
					System.err.println(ex.getStackTrace());
				}
			}
		};

		//タイマーを始動。ただし、もし持ち時間が負に設定されていたら、タイマーを動かさない。
		if(blackAllottedTime >= 0){
			blackTimer.setPriority(Thread.MAX_PRIORITY);
			blackTimer.start();
		}
		if(whiteAllottedTime >= 0){
			whiteTimer.setPriority(Thread.MAX_PRIORITY);
			whiteTimer.start();
		}

	}
	
	/**
	 * playerの残り時間をミリ秒単位で返す
	 * @param player
	 * @return
	 */
	public int getRemainingTime(Player player){
		if(player == Player.Black){
			return blackRemainingTime;
		}else{
			return whiteRemainingTime;
		}
	}
	
	
	public void stopTimer(Player player){
		if(player == Player.Black){
			flagBlackTimerRunning = false;
		}else{
			flagWhiteTimerRunning = false;			
		}
	}
	
	public void startTimer(Player player) throws Exception{
		if(player == Player.Black && !flagWhiteTimerRunning){
			flagBlackTimerRunning = true;
		}else if(player == Player.White && !flagBlackTimerRunning){
			flagWhiteTimerRunning = true;			
		}else{
			throw new Exception("他方のタイマーがとまっとらん");
		}
	}

	
	public int getSize(){
		return SIZE;
	}

	
	/**
	 * 現在どちらの手番かを返す
	 * @return
	 */
	public Player getCurrentPlayer(){
		return state.currentPlayer;
	}
	
	/**
	 * 引数playerで与えられたプレーヤーの色の石の数を数える。
	 * @param player
	 * @return
	 */
	public int getNumPieces(Player player){
		return state.numPieces(player);
	}
	
	/**
	 * もしゲーム続行中ならtrue,そうでなければfalse.
	 * @return
	 */
	public boolean isInGame(){
		return state.inGame;
	}
	
	
//	/**
//	 * (x, y)に現在のプレーヤーの石を置くことで裏返せる石をすべて裏返す。
//	 * @param player
//	 * @param x
//	 * @param y
//	 */
//	void reverseStones(Player player, int x, int y){
//		state.isValid(player, x, y, true);
//	}
	
	/**
	 * 石を置く
	 */
	public boolean putPiece(Move move){
		return putPiece(move.x, move.y);
	}
	
	/**
	 * 石を置く
	 */
	public boolean putPiece(int i, int j){
		
		//i,jに現在のプレーヤーの石を置く。
		try {
			state.putPiece(state.currentPlayer, i, j);
		} catch (OthelloMoveException e) {
			e.printStackTrace();
			return false;
		}
		
		//グラフィックスを再描画するシグナルを送る
		setChanged();
		notifyObservers();
		
		return true;
	}
	
	/**
	 * Returns the array of the state of all cells
	 * @return
	 */
	public GameState getGameState(){
		return state.deepCopy();
	}
	
	public CellState getState(int x, int y){
		return state.cells[x][y];
	}

	/**
	 * ゲームが終了している場合、勝者を返す。まだゲームが終了していない、または引き分けならばnullを返す。
	 * @return
	 */
	public Player getWinner(){
		if(state.inGame){
			return null;
		}else{
			if(flagBlackTimeUp){//黒時間切れ
				return Player.White;
			}else if(flagWhiteTimeUp){//白時間切れ
				return Player.Black;
			}else if(getNumPieces(Player.Black) > getNumPieces(Player.White)){
				return Player.Black;
			}else if(getNumPieces(Player.Black) < getNumPieces(Player.White)){
				return Player.White;
			}else{
				return null;  //引き分け
			}
		}
	}
}
