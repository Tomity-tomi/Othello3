package manualStrategies;

import game.Player;
import game.Strategy;
import game.StrategyFactory;

public class ManualStrategyFactory extends StrategyFactory {

	public ManualStrategyFactory(Player playerColor, int size) {
		super(playerColor, size);
	}

	@Override
	public Strategy createStrategy() {
		return new ManualStrategyCLI(thisPlayer, SIZE);
	}

}
