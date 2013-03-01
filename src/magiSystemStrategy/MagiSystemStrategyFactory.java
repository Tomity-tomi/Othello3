package magiSystemStrategy;

import game.Player;
import game.Strategy;
import game.StrategyFactory;

public class MagiSystemStrategyFactory extends StrategyFactory {

    public MagiSystemStrategyFactory(Player playerColor, int size) {
	super(playerColor, size);
    }

    @Override
    public Strategy createStrategy() {
	return new MagiSystemStrategy(thisPlayer, SIZE);
    }

}
