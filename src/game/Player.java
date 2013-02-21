package game;

/** 
 * プレーヤーを定義する型。
 *  
 */
public enum Player {
	/**　黒プレーヤー (先手）*/
	Black(1),
	/** 白プレーヤー （後手）*/
	White(2);
	
	int value;
	
	private Player(int n){
		this.value=n;
	}
	
	/** 
	 * 引数で与えられたstoneColorがこのプレーヤーと同じ色ならばtrue、そうでなければfalseを返す。
	 * @param stoneColor
	 * @return
	 */
	public boolean isSameColor(CellState stoneColor){
		if(this.value == 1 && stoneColor==CellState.Black){
			return true;
		}else if(this.value == 2 && stoneColor==CellState.White){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * このプレーヤーと逆のプレーヤーを返す。
	 * @return
	 */
	public Player oppositePlayer(){
		if(this.value == 1){
			return Player.White;
		}else{
			return Player.Black;
		}
	}
}
