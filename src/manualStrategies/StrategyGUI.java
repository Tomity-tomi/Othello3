package manualStrategies;

import game.Player;
import game.Strategy;
import graphix.OthelloGraphics;

public abstract class StrategyGUI extends Strategy {
	
	OthelloGraphics g;
	

	public StrategyGUI(Player _thisPlayer, int size) {
		super(_thisPlayer, size);
		// TODO Auto-generated constructor stub
	}
	
	abstract public void pickCell(int i, int j);

}
