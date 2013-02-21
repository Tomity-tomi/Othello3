package game;

import game.OthelloMoveException.ErrorState;

/**
 * 盤面の状態を表す変数およびそれを操作するメソッドを含むクラス。各マスが空か、黒か、白かはpublic変数であるcellsに格納されている。また、それぞれのマスに白、黒の石を置くことができるかの情報も保存しており、
 * isLegal()でその情報を取り出すことができる。
 * 
 * 各メンバ関数で指定するマスのインデックス(i,j)は0始まりであることに注意。盤の大きさが8x8のとき、左上の隅が[0][0]、右上の隅が[7][0]、左下の隅が[0][7]、右下の隅が[7][7]となる。
 * @author Hiro
 *
 */
public class GameState{
	final private int SIZE;
	
	/**
	 * 盤面の状態を表す変数。{@link game.CellState}を参照。
	 */
	public CellState[][] cells;
	private boolean[][] legalForBlack;
	private boolean[][] legalForWhite;
	/**
	 * 現在はどちらの手番かを表す変数。{@link game.Player}を参照。
	 */
	public Player currentPlayer; 
	/**
	 * 現在のゲームの状態。trueならまだ決着がついていない。falseならゲームが終了している。
	 */
	public boolean inGame; 
	private int nOpen; //空いているますの数
	
	/**
	 * コンストラクタ。盤面のサイズをsizeで指定する。
	 * @param 盤面のサイズ。通常の8x8の盤面ならば8を入れる。
	 */
	public GameState(int size){
		SIZE = size;
		cells = new CellState[SIZE][SIZE];
		legalForBlack = new boolean[SIZE][SIZE];
		legalForWhite = new boolean[SIZE][SIZE];
				
		//Initialize the game
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				cells[i][j] = CellState.Empty; 
				legalForBlack[i][j] = false;
				legalForWhite[i][j] = false;
			}
		}
		cells[SIZE/2-1][SIZE/2-1] = CellState.White;
		cells[SIZE/2][SIZE/2] = CellState.White;
		cells[SIZE/2-1][SIZE/2] = CellState.Black;
		cells[SIZE/2][SIZE/2-1] = CellState.Black;
		updateStates();
		
		//先手は黒
		currentPlayer = Player.Black;
		inGame = true;
		nOpen = 60;
	}
	
	
	/**
	 * マス(i,j)にplayerで指定した色の石が置けるならばtrue、そうでなければfalseを返す。
	 * @param player　プレーヤーの色
	 * @param i　セルの横座標
	 * @param j　セルの縦座標
	 * @return
	 */
	public boolean isLegal(Player player, int i, int j){
		
		if(player == Player.Black){
			return legalForBlack[i][j];
		}else if(player == Player.White){
			return legalForWhite[i][j];
		}else{
			return false;
		}
	}
	
	/**
	 * マス(i,j)が空のマスならばtrue、そうでないならfalseを返す。
	 * @param i
	 * @param j
	 * @return
	 */
	public boolean isEmpty(int i, int j){
		if(cells[i][j]  == CellState.Empty){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * x,yで指定された位置が盤内にあればtrueを、そうでなければfalseを返す。
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isWithinRange(int x, int y){
		if(x<0 || y<0 || x>=SIZE || y>=SIZE){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 現在の盤面で、playerで指定した色の石が置けるマスの個数を返す。主に戦略の評価関数で使用する。
	 * @param player
	 * @return
	 */
	public int numAvailable(Player player){
		int n=0;
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				if(isLegal(player,i,j))
					n=n+1;
			}
		}
		return n;
	}
	
	/**
	 *  引数playerで与えられたプレーヤーの色の石の数を数える。
	 * @param player
	 * @return
	 */
	public int numPieces(Player player){
		int n=0;
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				if(player.isSameColor(cells[i][j])){
					n=n+1;
				}
			}
		}
		return n;
	}
	
	/**
	 * この状態の盤面で、playerが(i,j)に石を置いた時に現れる盤面を返す。返される盤面は、元のオブジェクトをディープ・コピーした
	 * オブジェクトを操作したものである。よって、呼び出し元のオブジェクトには変更は加えられない。また、石をいけないマスが指定された場合は
	 * 例外{@link game.OthelloMoveException}を投げる。
	 * @param player
	 * @param x
	 * @param y
	 * @return
	 */
	public GameState nextState(Player player, int i, int j) throws OthelloMoveException{
		GameState newState = this.deepCopy();
			
		newState.putPiece(player, i, j);
		
		return newState;
	}
	
	/**
	 * マス(i,j)にplayerで指定された色の石を置く。このメソッドは呼び出し元のオブジェクトを更新する。また、石をいけないマスが指定された場合は
	 * 例外{@link game.OthelloMoveException}を投げる。
	 * @param player
	 * @param i
	 * @param j
	 * @throws OthelloMoveException
	 */
	public void putPiece(Player player, int i, int j)  throws OthelloMoveException{
		
		if(!inGame){
			throw new OthelloMoveException(ErrorState.NotInGame);
		}
		//この場所に置けるかをチェック
		if(!isWithinRange(i,j)){
			throw new OthelloMoveException(ErrorState.OutOfRange);
		}else if(!isLegal(player, i, j)){
			throw new OthelloMoveException(ErrorState.NotAvailable);
		}
		
		//もしこの場所に置けるなら、挟まれた石をひっくりかえす
		isValid(currentPlayer,i, j,true);
			
		//石を置く
		if(currentPlayer == Player.White){
			cells[i][j] = CellState.White;
		}else if(currentPlayer == Player.Black){
			cells[i][j] = CellState.Black;
		}
		
		nOpen = nOpen-1;		
		updateStates();
		
	}
	
	/** 
	 * 盤面の状態を更新する。また、ゲームが終了していないか判定する。
	 */
	private void updateStates(){
		//ゲームの状態を更新
		boolean blackAvailable = false;
		boolean whiteAvailable = false;
		boolean existsEmptyCell = false;

		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				legalForBlack[i][j] = false;
				legalForWhite[i][j] = false;
				if(cells[i][j]==CellState.Empty){
					existsEmptyCell = true; //空いているセルがある
					legalForBlack[i][j] = false;
					legalForWhite[i][j] = false;
					if(isValid(Player.Black,i,j,false)){
						blackAvailable = true; //黒のプレーヤーが置く場所がある
						legalForBlack[i][j] = true;
					}
					if(isValid(Player.White,i,j,false)){
						whiteAvailable = true;//白のプレーヤーが置く場所がある
						legalForWhite[i][j] = true;
					}
				}
			}
		}
		
		//次の手番がどちらかをチェックする。また、ゲームが終了していないかもチェックする。
		Player nextPlayer = currentPlayer;
		if(!existsEmptyCell){
			inGame = false; //ゲーム終了
		}else if(currentPlayer == Player.Black){
			if(whiteAvailable)
				nextPlayer = Player.White;
			else if(blackAvailable)
				nextPlayer = Player.Black;
			else
				inGame = false;
		}else if(currentPlayer == Player.White){
			if(blackAvailable)
				nextPlayer = Player.Black;
			else if(whiteAvailable)
				nextPlayer = Player.White;
			else
				inGame = false;
		}
		currentPlayer = nextPlayer;
		
	}
	
	/**
	 * マス(i, j)にplayerの色の石を置くことによって一つ以上の相手の石を裏返すことができるならばtrue,　そうでなければfalseを返す。
	 * また、もしreversePiecesがtrueに指定された場合、挟まれた石をひっくり返す。
	 * 以下のサイトを参考に作成しました：http://d.hatena.ne.jp/poor_code/20090711/1247269299
	 * @param player
	 * @param i
	 * @param j
	 * @param revercePieces　これがtrueに指定された場合、挟まれた石をひっくり返す。falseの場合はひっくり返さない。通常はfalseで使う。
	 * @return
	 */
	private boolean isValid(Player player, int i, int j, boolean revercePieces){
		int dir[][] = {
				{-1,-1}, {0,-1}, {1,-1},
				{-1, 0},         {1, 0},
				{-1, 1}, {0, 1}, {1, 1}
		};
		
		boolean reversed = false;
		
		for(int ii=0; ii<8; ii++){
			//隣のマスをチェック
			int x0 = i+dir[ii][0];
			int y0 = j+dir[ii][1];
			if(!isWithinRange(x0,y0)){
				continue;
			}
			if(player.isSameColor(cells[x0][y0])){
//				System.out.println("隣が同色: " +x0 +","+ y0);
				continue;
			}else if(isEmpty(x0,y0)){
//				System.out.println("隣は空: " +x0 +","+ y0);
				continue;
			}else{
//				System.out.println("隣は敵: " +x0 +","+ y0);
			}
			
			//隣の隣から端まで走査して、自分の色があればリバース
			int jj = 2;
			while(true){
			
				int x1 = i + (dir[ii][0]*jj);
				int y1 = j + (dir[ii][1]*jj);
				if(!isWithinRange(x1,y1)){
					break;
				}
				
				//自分の駒があったら、リバース
				if(player.isSameColor(cells[x1][y1])){
//					System.out.println("ひっくりかえす: " +x1 +","+ y1);					
					if(revercePieces){
						for(int k=1; k<jj; k++){
							int x2 = i + (dir[ii][0]*k);
							int y2 = j + (dir[ii][1]*k);
							if(cells[x2][y2]==CellState.Black)
								cells[x2][y2]=CellState.White;
							else if(cells[x2][y2]==CellState.White)
								cells[x2][y2]=CellState.Black;
//							System.out.println("reversed: " +x2 +","+ y2);
						}
					}
					reversed = true;
					break;
				}
				
				//空白があったら、終了
				if(isEmpty(x1,y1)){
					break;
				}
				
				jj++;
				
			}
			
		}
		
		return reversed;

	}

	/**
	 * 現在空いているセルの数を返す。
	 * @return
	 */
	public int getNumberOfAvailableCells(){
		return this.nOpen;
	}
	
	/**
	 * このオブジェクトのディープ・コピーを返す。「ディープ・コピー」とは何かについては、ぐぐってみてください。
	 */
	public GameState deepCopy(){
		GameState copiedState = new GameState(SIZE);
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				copiedState.cells[i][j] = this.cells[i][j];
				copiedState.legalForBlack[i][j] = this.legalForBlack[i][j];
				copiedState.legalForWhite[i][j] = this.legalForWhite[i][j];
			}
		}
		copiedState.currentPlayer = this.currentPlayer;
		copiedState.inGame = this.inGame;
		copiedState.nOpen = this.nOpen;
		return copiedState;
	}

}
