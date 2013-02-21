package game;

/**
 * オセロの戦略を表す抽象クラス。各参加者はこのクラスを拡張し、抽象メソッドnextMove()を実装したクラスを作成することで、
 * 作戦を実装する。
 * 
 * @author Hiro
 *
 */
public abstract class Strategy {
	/**
	 * このプレーヤーが白か黒か。
	 */
	protected final Player thisPlayer; 
	
	/**
	 * ゲームのサイズ。つまり、盤の一片のマス数。
	 */
	protected final int SIZE;

	/**
	 * コンストラクタ。子クラスよりsuper()で呼び出される。盤面の大きさとどちらのプレーヤーかを指定する。
	 * @param _thisPlayer
	 * @param size
	 */
	public Strategy(Player _thisPlayer, int size){
		thisPlayer = _thisPlayer;
		SIZE = size;
	}
	
	/**
	 * 各参加者はこのクラスを必ず実装すること。これが戦略の根幹となる関数。現在の盤面 currentStateおよび
	 * 残り時間 remainingTimeを受け取り、次の一手を返す。
	 * 
	 * @param currentState 現在の盤面の状態
	 * @param remainingTime 残り時間 [ミリ秒]
	 * @return　次の一手
	 */
	abstract public Move nextMove(GameState currentState, int remainingTime);
	
	
	/**
	 * ゲーム終了時に呼ばれる。終了処理が必要な場合、このクラスをオーバーライドする。たとえば、ログファイルをクローズするときとか。
	 * 終了処理が必要でない場合はこのクラスは実装しなくてよい。
	 */
	public void terminate(){
		
	}
	

	
}
