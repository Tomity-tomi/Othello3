package simpleStrategies;

import game.Player;
import game.Strategy;
import game.StrategyFactory;

public class KaihoStrategyFactory extends StrategyFactory {

	public KaihoStrategyFactory(Player playerColor, int size) {
		super(playerColor, size);
	}

	@Override
	public Strategy createStrategy() {
		return new KaihoStrategy(thisPlayer, SIZE);
	}

}
