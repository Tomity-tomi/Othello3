package kmatsStrategies;

import game.GameState;
import game.Move;
import game.OthelloMoveException;
import game.Player;
import game.Strategy;

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
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		BestData bestData = endSearch(currentState, thisPlayer, initialDepth, false);
		Util.stdOut("CPU's move:" + bestData.bestMove.toString());
		return bestData.bestMove;
	}

	// ���݂̐΂̐��̍���]���l�Ƃ��ĕԂ�
	public int eval0(GameState state, Player player) {
		return state.numPieces(player) - state.numPieces(player.oppositePlayer());
	}

	// �p�����ꂽ��y�i���e�B���ۂ��悤�ɂ���
	public int eval1(GameState state, Player player) {
		int checkValue = Util.checkCorner(state, player.oppositePlayer());

		return checkValue * -100;
	}

	// �p���������{�[�i�X���ۂ��悤�ɂ���
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

		// �����ڂ��Ă���Ֆʁi�m�[�h�j�̕]���l�̍œK�l���i�[���邽�߂̕ϐ�
		BestData bestData = new BestData(Integer.MIN_VALUE, null);
		// ���̔ՖʂőłĂ�肪���邩�ǂ���������
		// ������prevPassed�͍��̔Ֆʂ̑O�i�e�m�[�h�j�Ńp�X�������ǂ���������
		boolean noMove = true;

		// �t�ɒB�����̂ŔՖʂ̕]�������Ēl��Ԃ�
		if (depth == 0) {
			BestData res = new BestData(evalMethod(currentState, player), null);
			return res;
		}

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				// Util.stdOut("(i, j) = (" + i + ", " + j + ")");
				if (currentState.isLegal(player, i, j)) { // (i, j)�̈ʒu�ɐ΂�u����ꍇ
					if (noMove) {
						noMove = false;
					}
					try {
						// ���̏�Ԃ����
						GameState nextState = currentState.nextState(player, i, j);
						Player nextPlayer = player.oppositePlayer();
						int nextDepth = depth - 1;
						boolean passed = false;

						// �ċA�Ăяo��
						int evaluatedValue = -1 * endSearch(nextState, nextPlayer, nextDepth, passed).bestValue;

						// ���K���������������œK�l�ƍœK����X�V����
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

		// �łĂ�肪�Ȃ������ꍇ
		if (noMove) {
			if (prevPassed) { // ���O�ɑ��肪���Ƀp�X�����ꍇ
				bestData = new BestData(evalMethod(currentState, player), null);
			} else { // ����̓p�X���Ă��Ȃ��ꍇ
				// ���̏�Ԃ����
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
