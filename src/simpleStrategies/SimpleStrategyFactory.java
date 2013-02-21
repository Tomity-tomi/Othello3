package simpleStrategies;

import game.Player;
import game.Strategy;
import game.StrategyFactory;

public class SimpleStrategyFactory extends StrategyFactory {

	public SimpleStrategyFactory(Player playerColor, int size) {
		super(playerColor, size);
	}

	@Override
	public Strategy createStrategy() {
		return new MaxAvailableCellsWithCorner(thisPlayer, SIZE);
	}

}
