package kmatsStrategies;

import game.Player;
import game.Strategy;
import game.StrategyFactory;

public class KmatsStrategyFactory extends StrategyFactory {

	public KmatsStrategyFactory(Player playerColor, int size) {
		super(playerColor, size);
	}

	@Override
	public Strategy createStrategy() {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		return new KmatsStrategy(thisPlayer, SIZE);
	}

}
