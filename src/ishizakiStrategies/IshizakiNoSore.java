package ishizakiStrategies;

import game.GameMaster;
import game.GameState;
import game.Move;
import game.OthelloMoveException;
import game.Player;
import game.Strategy;

//�P�肾����ǂ݂���V���v���Ȑ헪�B�]���֐��͐΂�u����}�X�̐��i�萔�j�Ɗp�݂̂��l���B
public class IshizakiNoSore extends Strategy {
	Move bestMove = new Move();
	int playnum = 0;
	int notplaynum = 0;
	final int MAX_VALUE = 64000;
	final int First = 64;// �c�艽�}�X�܂Œ�΂�ł�
	final int MidDepth = 4;// �ʏ퉽��܂œǂނ�
	final int Last = 0;// �c�艽�}�X�Ŋ��S�ǂ��n�߂邩
	final static int PenaltyCorner = 100; // ����Ɋp���Ƃ��Ă��܂��헪�ɑ΂���y�i���e�B�[
	final static int corners[][] = { { 0, 0 }, { 0, GameMaster.SIZE - 1 }, { GameMaster.SIZE - 1, 0 },
			{ GameMaster.SIZE - 1, GameMaster.SIZE - 1 } }; // �p�̈ʒu

	public IshizakiNoSore(Player _thisPlayer, int size) { // �R���X�g���N�^
		super(_thisPlayer, size);
	}

	@Override
	// �e�N���Xgame.Strategy�̒��ۃ��\�b�h������
	public Move nextMove(GameState currentState, int remainingTime) {
		if (thisPlayer == Player.Black) {
			playnum = 1;
			notplaynum = -1;
		} else {
			playnum = -1;
			notplaynum = 1;
		}
		int left;
		left = currentState.getNumberOfAvailableCells();
		if (left > First) {
			// joseki();
		} else if (left < Last) {
			mid(currentState, this.Last, thisPlayer, -1, -1, true);
		} else {
			mid(currentState, this.MidDepth, thisPlayer, -1, -1, true);
		}
		return bestMove;
	}

	private int mid(GameState currentState, int in_depth, Player player, int x, int y, boolean flag) {
		boolean first = false;
		int max = -MAX_VALUE;
		int premax = -MAX_VALUE;
		if (currentState.inGame == false) {
			int score = currentState.numPieces(thisPlayer) - currentState.numPieces(thisPlayer.oppositePlayer());
			return -score;
		} else if (in_depth == 0) {
			int score = computeScore(currentState);
			System.out.println("socore = " + score);
			return -score;
		}
		boolean Check = false;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (currentState.isLegal(player, i, j)) {
					try {
						Check = true;
						GameState expectedState = currentState.nextState(player, i, j);
						if (x == -1 || first == true) {
							System.out.println("aaaaa i;j = " + i + ";" + j);
							x = i;
							y = j;
							first = true;
							premax = mid(expectedState, in_depth - 1, player.oppositePlayer(), i, j, false);
						} else {
							premax = mid(expectedState, in_depth - 1, player.oppositePlayer(), x, y, false);
						}
						System.out.println("premax" + premax);
						if (max < premax) {
							max = premax;
							if (first) {
								System.out.println("max = " + max + " x = " + x + " y = " + y);
								bestMove.x = x;
								bestMove.y = y;
							}
						}
					} catch (OthelloMoveException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		if (Check == false) {// �p�X�̏ꍇ
			System.out.println("dbg");
			for (int i = 0; i < SIZE; i++) {
				for (int j = 0; j < SIZE; j++) {
					if (currentState.isLegal(player.oppositePlayer(), i, j)) {
						try {
							GameState expectedState = currentState.nextState(player.oppositePlayer(), i, j);
							if (x == -1 && y == -1 || first == true) {
								x = i;
								y = j;
								first = true;
								premax = mid(expectedState, in_depth - 1, player, i, j, true);
							} else {
								premax = mid(expectedState, in_depth - 1, player, x, y, true);
							}
							if (max < premax) {
								max = premax;
								if (first) {
									System.out.println("max = " + max);
									bestMove.x = x;
									bestMove.y = y;
								}
							}
						} catch (OthelloMoveException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		System.out.println("dep = " + in_depth + "  " + -max);
		return -max;
	}

	// �Ֆʂ̃X�R�A��]�����郁�\�b�h
	private int computeScore(GameState expectedState) {
		// int scoreMaxAvailable = expectedState.numAvailable(thisPlayer) -
		// expectedState.numAvailable(thisPlayer.oppositePlayer()); //
		// �������΂�u����}�X�̐�
		// -
		int aaa = expectedState.numPieces(thisPlayer) - expectedState.numPieces(thisPlayer.oppositePlayer()); // ���肪�΂�u����}�X�̐�
		int scoreCorner = 0;
		int bb = 0;
		for (int i = 0; i < 4; i++) { // �������̔Ֆʂő��肪�p�ɐ΂�u����Ȃ�A���̕]���l��PenaltyCorner����������
			// if(expectedState.isLegal(thisPlayer.oppositePlayer(),
			// corners[i][0], corners[i][1])){
			// scoreCorner -= PenaltyCorner;
			// }
			if (expectedState.cells[corners[i][0]][corners[i][1]].getValue() == notplaynum) {
				scoreCorner -= 100;
			}
			if (expectedState.cells[corners[i][0]][corners[i][1]].getValue() == playnum) {
				bb += 100;
			}

			// System.out.println(thisPlayer);
		}

		return scoreCorner + aaa + bb;
	}

}