package game;

public class OthelloMoveException extends Exception {
	/**
	 * Errorの種類の定義
	 */
	public enum ErrorState{
		/** エラーなし */
		None,
		/** 指定された場所に自分の石を置けない */
		NotAvailable,
		/** 指定された場所は盤の外である */
		OutOfRange,
		/** 既にゲームオーバー */
		NotInGame;
	}
	
	public ErrorState errorState;
	
	public OthelloMoveException(ErrorState state){
		errorState = state;
	}
}
