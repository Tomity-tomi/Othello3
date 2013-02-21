package manualStrategies;

import game.*;
import graphix.OthelloGraphics;

public class ManualGUIStrategyFactory extends StrategyFactory {

	OthelloGraphics g;
	
	public ManualGUIStrategyFactory(Player playerColor, int size, OthelloGraphics g) {
		super(playerColor, size);
		this.g = g;
	}

	@Override
	public Strategy createStrategy() {
		return new ManualStrategyGUI(thisPlayer, SIZE, g);
	}

}
