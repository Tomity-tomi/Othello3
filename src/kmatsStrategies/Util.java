package kmatsStrategies;

import game.GameState;
import game.Player;

public class Util {
	public static void stdOut(String str) {
		System.out.println(str);
	}

	public static int checkCorner(GameState state, Player player) {
		int res = 0;
		int color = 0;
		int SIZE = 8;

		if (player == Player.Black) {
			color = 1;
		} else {
			color = -1;
		}

		if (state.cells[0][0].getValue() == color) // ����
			res += 1;

		if (state.cells[0][SIZE - 1].getValue() == color) // ����
			res += 1;

		if (state.cells[SIZE - 1][0].getValue() == color) // �E��
			res += 1;

		if (state.cells[SIZE - 1][SIZE - 1].getValue() == color) // �E��
			res += 1;

		return res;
	}
}
