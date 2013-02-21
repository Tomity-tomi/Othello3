package game;

/**
 * 各セルの状態を表す型。マスが空の状態(Empty)は0、白が置かれている状態(White)は-1、
 * 黒が置かれている状態(Black)は1の値が割り当てられており、この値はgetValue()で取り出す
 * ことが出来る。
 * @author Hiro
 *
 */
public enum CellState {
	/** 何も石が置かれておらず、黒も白も置くことができない */
	Empty(0),
	/** 白が置かれている */
	White(-1),
	/** 黒が置かれている */
	Black(1);
	/** 何も石が置かれておらず、黒石を置くことができるが白石は置けない*/
	
	private int value;
	
	private CellState(int n){
		this.value=n;
	}
	
	/**
	 * この状態に割り当てられている値を取り出す。マスが空の状態(Empty)は0、白が置かれている状態(White)は-1、
	 * 黒が置かれている状態(Black)は1の値が割り当てられている。
	 * @return
	 */
	public int getValue(){
		return this.value;
	}
	
}
