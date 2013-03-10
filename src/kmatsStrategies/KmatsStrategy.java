package kmatsStrategies;

import game.GameState;
import game.Move;
import game.OthelloMoveException;
import game.Player;
import game.Strategy;
import util.Util;

public class KmatsStrategy extends Strategy {

	private int initialDepth = 4; // must be more than 0

	private class BestData {
		public int bestValue;
		public Move bestMove;

		private BestData(int v, Move m) {
			bestValue = v;
			bestMove = m;
		}
	}

	public KmatsStrategy(Player _thisPlayer, int size) {
		super(_thisPlayer, size);
	}

	@Override
	public Move nextMove(GameState currentState, int remainingTime) {
		Util.stdOut("nextMove is called !");
		// TODO 自動生成されたメソッド・スタブ
		BestData bestData = endSearch(currentState, thisPlayer, initialDepth, false);
		Util.stdOut("CPU's move:" + bestData.bestMove.toString());
		return bestData.bestMove;
	}

	// 現在の石の数の差を評価値として返す
	public int eval0(GameState state, Player player) {
		return state.numPieces(player) - state.numPieces(player.oppositePlayer());
	}

	// 角を取られたらペナルティを課すようにする
	public int eval1(GameState state, Player player) {
		int checkValue = Util.checkCorner(state, player.oppositePlayer());

		return checkValue * -100;
	}

	// 角を取ったらボーナスを課すようにする
	public int eval2(GameState state, Player player) {
		int checkValue = Util.checkCorner(state, player);

		return checkValue * 100;
	}

	public int evalMethod(GameState s, Player p) {
		// return eval0(state, player);
		return eval0(s, p) + eval1(s, p) + eval2(s, p);
	}

	public BestData endSearch(GameState currentState, Player player, int depth, boolean prevPassed) {
		// Util.stdOut("endSearch is called !");
		// Util.stdOut("depth is " + depth);

		// 今注目している盤面（ノード）の評価値の最適値を格納するための変数
		BestData bestData = new BestData(Integer.MIN_VALUE, null);
		// 今の盤面で打てる手があるかどうかを示す
		// 引数のprevPassedは今の盤面の前（親ノード）でパスしたかどうかを示す
		boolean noMove = true;

		// 葉に達したので盤面の評価をして値を返す
		if (depth == 0) {
			BestData res = new BestData(evalMethod(currentState, player), null);
			return res;
		}

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				// Util.stdOut("(i, j) = (" + i + ", " + j + ")");
				if (currentState.isLegal(player, i, j)) { // (i, j)の位置に石を置ける場合
					if (noMove) {
						noMove = false;
					}
					try {
						// 次の状態を作る
						GameState nextState = currentState.nextState(player, i, j);
						Player nextPlayer = player.oppositePlayer();
						int nextDepth = depth - 1;
						boolean passed = false;

						// 再帰呼び出し
						int evaluatedValue = -1 * endSearch(nextState, nextPlayer, nextDepth, passed).bestValue;

						// より適した手を見つけたら最適値と最適手を更新する
						if (evaluatedValue > bestData.bestValue) {
							bestData = new BestData(evaluatedValue, new Move(i, j));
							// Util.stdOut("tmp Best Move is (" +
							// bestData.bestMove.x + ", " + bestData.bestMove.y
							// + ")");
						}
					} catch (OthelloMoveException e) {
						e.printStackTrace();
					}
				}
			}
		}

		// 打てる手がなかった場合
		if (noMove) {
			if (prevPassed) { // 直前に相手が既にパスした場合
				bestData = new BestData(evalMethod(currentState, player), null);
			} else { // 相手はパスしていない場合
				// 次の状態を作る
				GameState nextState = currentState.deepCopy();
				Player nextPlayer = player.oppositePlayer();
				int nextDepth = depth;
				boolean passed = true;
				int bestValue = -1 * endSearch(nextState, nextPlayer, nextDepth, passed).bestValue;
				bestData = new BestData(bestValue, null);
			}
		}

		return bestData;
	}
}
