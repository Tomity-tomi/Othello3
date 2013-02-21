package graphix;

import game.GameMaster;

import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import manualStrategies.StrategyGUI;

/**
 * オセロのグラフィックスの抽象クラス。
 * 実装上の注意：enableMouseEventが呼ばれ、strategyがnullではないときは、this.mousePressed()呼び出し時に、
 * strategy.pickCell()を呼び出して、押されたセルを伝達すること。
 * @author Hiro
 *
 */
public abstract class OthelloGraphics extends JPanel implements Observer, MouseListener{
	
	protected ArrayList<StrategyGUI> strategy;
	protected boolean flagMouseEnabled = false;
	
	public OthelloGraphics(GameMaster gm){
		strategy = new ArrayList<StrategyGUI>();
		gm.addObserver(this);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		repaint();
	}
	
	public void registerStrategy(StrategyGUI s){
		strategy.add(s);
	}


}
