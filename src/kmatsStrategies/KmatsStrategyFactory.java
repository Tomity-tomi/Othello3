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
		// TODO 自動生成されたメソッド・スタブ
		return new KmatsStrategy(thisPlayer, SIZE);
	}

}
