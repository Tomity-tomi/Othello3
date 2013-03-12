package ishizakiStrategies;

import game.Player;
import game.Strategy;
import game.StrategyFactory;

public class IshizakiStrategyFactory extends StrategyFactory {

	public IshizakiStrategyFactory(Player playerColor, int size) {
		super(playerColor, size);
	}

	@Override
	public Strategy createStrategy() {
		return new IshizakiNoSore(thisPlayer, SIZE);
	}

}
