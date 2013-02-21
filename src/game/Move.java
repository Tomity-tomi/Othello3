package game;

/**
 * 手、すなわちどこのセルに石を置くかを表すクラス。左上を原点とし、横軸が右向きにx、縦軸が下向きにy。インデックスはゼロはじまり。盤の大きさが8x8のとき、左上の隅が(x,y)=(0,0)、右上の隅が(7,0)、左下の隅が(0,7)、右下の隅が(7,7)となる。
 * @author Hiro
 *
 */
public class Move {
	public int x, y;
	static final String[] alphabet = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

	
	public Move(){}
	
	public Move(int _x, int _y){
		x = _x;
		y = _y;
	}
	
	public String toString(){
		return "(" + alphabet[x] + "," + (y+1) + ")";
	}
}
