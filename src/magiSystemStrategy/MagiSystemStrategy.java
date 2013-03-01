package magiSystemStrategy;

import game.GameMaster;
import game.GameState;
import game.Move;
import game.OthelloMoveException;
import game.Player;
import game.Strategy;

//１手だけ先読みするシンプルな戦略。評価関数は石を置けるマスの数（手数）と角のみを考慮。
public class MagiSystemStrategy extends Strategy {

    final static int PenaltyCorner = 100; // 相手に角をとられてしまう戦略に対するペナルティー
    final static int corners[][] = { { 0, 0 }, { 0, GameMaster.SIZE - 1 },
	    { GameMaster.SIZE - 1, 0 },
	    { GameMaster.SIZE - 1, GameMaster.SIZE - 1 } }; // 角の位置

    public MagiSystemStrategy(Player _thisPlayer, int size) { // コンストラクタ
	super(_thisPlayer, size);
    }

    @Override
    // 親クラスgame.Strategyの抽象メソッドを実装
    public Move nextMove(GameState currentState, int remainingTime) {
	Move bestMove = new Move();

	// 角が取れるなら取る
	for (int i = 0; i < 4; i++) {
	    if (currentState.isLegal(thisPlayer, corners[i][0], corners[i][1])) {
		bestMove.x = corners[i][0];
		bestMove.y = corners[i][1];
		return bestMove;
	    }
	}

	// 開放度理論に基づく打ち方
	int[][] SubMove = this.BestKaihoMoveDepth2(currentState);
	bestMove.x = SubMove[0][0];
	bestMove.y = SubMove[0][1];

	System.out.println("CPU's move:" + bestMove.toString()); // デバック用。コンソールにCPUの手を表示。
	return bestMove; // 最良の手を返す
    }

    // 開放度を評価するメソッド
    public int KaihoScore(GameState nPulus1State, GameState nState) {
	// nState　現在の盤面
	// nPulus1State 次の盤面

	int KaihoSum = 0; // 開放度そのもの
	// 開放度を評価するためのループ
	for (int i = 0; i < SIZE; i++) {
	    for (int j = 0; j < SIZE; j++) {
		int KaihoValue = 0; // 開放度を評価するパラメーター
		// 盤面上の石の色が変わっていれば、その石の周りの開放度を求める.黒から白、白から黒の場合は数えるが、空白から石を置いたマスはカウントしない。
		if ((nPulus1State.cells[i][j].getValue() - nState.cells[i][j]
			.getValue())
			* (nPulus1State.cells[i][j].getValue() - nState.cells[i][j]
				.getValue()) == 4) {
		    for (int k = i - 1; k <= i + 1; k++) {
			for (int l = j - 1; l <= j + 1; l++) {
			    if (nState.isWithinRange(k, l) == false) {
				continue;
			    }
			    if (nState.isEmpty(k, l) == true) {
				KaihoValue--; // ひっくり返ったコマの周りが空白ならその分KaihoValueを小さくする
			    }
			}
		    }
		}
		KaihoSum += KaihoValue;
	    }
	}
	return KaihoSum;
    }

    // 開放度で評価した場合の善手上位N手を返す。引数は、盤面の状態、どちらの色を打つか、上位何手まで評価するか（NumberOfRanks)
    public int[][] KaihoRankNs(GameState nState, Player currentPlayer,
	    int NumberOfRanks) {
	// 配列の確保と初期化BestKaihoMoves[][0]はi[][1]はj,[][2]は開放度を表す
	int BestKaihoMoves[][];
	BestKaihoMoves = this.ArraysGenerater(NumberOfRanks);

	for (int i = 0; i < SIZE; i++) {
	    for (int j = 0; j < SIZE; j++) {
		if (nState.isLegal(currentPlayer, i, j)) { // もしも(i,j)に自分の石を置けるならば
		    try {
			GameState nPulus1State = nState.nextState(
				currentPlayer, i, j); // そこに石を置いた場合の盤面を取得
			int score = KaihoScore(nPulus1State, nState); // その盤面の開放度を計算
			// 開放度の並べ替え
			for (int k = 0; k < NumberOfRanks; k++) {
			    if (score > BestKaihoMoves[k][2]) {
				for (int l = NumberOfRanks - 2; l > k - 1; l--) {
				    BestKaihoMoves[l + 1][0] = BestKaihoMoves[l][0];
				    BestKaihoMoves[l + 1][1] = BestKaihoMoves[l][1];
				    BestKaihoMoves[l + 1][2] = BestKaihoMoves[l][2];
				}
				BestKaihoMoves[k][0] = i;
				BestKaihoMoves[k][1] = j;
				BestKaihoMoves[k][2] = score;
				break;
			    }
			}

		    } catch (OthelloMoveException e) { // エラー処理。このエラーが発生することはないはず
			e.printStackTrace();
		    }
		}
	    }
	}
	return BestKaihoMoves;
    }

    // 今nStateとcurrentStateが混在しててカオスが置きつつある。要検討。（しかし、統合するとたぶん不都合が起きる。）

    // 自分の手による開放度評価値を最大化し、その次の相手の開放度評価値の最大値を最小化する手を返す。
    public int[][] BestKaihoMoveDepth2(GameState nState) {
	int BestMove[][] = { { -1, -1, -100 } };
	int SubMove[][];// 自分の手の開放度評価パラメータ
	int SubMovePulus1[][];// 相手の手の開放度評価パラメータ
	GameState nPulus1State = new GameState(SIZE);// バグを生みそうな初期化の仕方なので本当はやりたくない。が、やらないと怒られる。
	SubMove = KaihoRankNs(nState, thisPlayer,
		nState.numAvailable(thisPlayer));// 現在の盤面から打てる手の内、開放度的に良い手から順に並べた手一覧を取得

	for (int i = 0; i < nState.numAvailable(thisPlayer); i++) {
	    System.out.println(SubMove[i][0] + "," + SubMove[i][1] + ","
		    + SubMove[i][2]);
	    try {
		nPulus1State = nState.nextState(thisPlayer, SubMove[i][0],
			SubMove[i][1]);
	    } catch (OthelloMoveException e) {
		// TODO 自動生成された catch ブロック
		e.printStackTrace(); // これなんでこんなことしないといけないの？
	    }
	    SubMovePulus1 = KaihoRankNs(nPulus1State,
		    thisPlayer.oppositePlayer(), 1);// 次の盤面で、相手が最も開放度的に良い手を選び出す
	    System.out.println(SubMove[i][2]);
	    System.out.println(SubMovePulus1[0][2]);
	    System.out.println(SubMove[i][2] - SubMovePulus1[0][2]);
	    // もし、(自分の手の開放度)-(相手の開放度)が現在知っている最善手よりよければ、それを最善手として記憶する
	    if (BestMove[0][2] < SubMove[i][2] - SubMovePulus1[0][2]) {
		BestMove[0][0] = SubMove[i][0];
		BestMove[0][1] = SubMove[i][1];
		BestMove[0][2] = SubMove[i][2] - SubMovePulus1[0][2];
	    }
	    System.out.println("現在の最善手は(" + BestMove[0][0] + ","
		    + BestMove[0][1] + ")でその値は" + BestMove[0][2]);
	}
	// BestMoveは[0][0]にi、[0][1]にj、[0][2]にその自分の開放度-相手の開放度が格納されている。
	return BestMove;
    }

    // 開放度の評価などに使う配列を生成
    private int[][] ArraysGenerater(int Length) {
	int Arrays[][];
	Arrays = new int[Length][3];
	for (int i = 0; i < Length; i++) {
	    for (int j = 0; j <= 2; j++) {
		if (j <= 1) {
		    Arrays[i][j] = -1; // 初期座標に(i,j)=(-1,-1)を代入し、その開放度に-100を入れている。
		} else {
		    Arrays[i][j] = -100;
		}
	    }
	}
	return Arrays;
    }
}