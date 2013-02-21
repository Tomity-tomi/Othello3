package graphix;

import game.CellState;
import game.GameMaster;
import game.Player;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;

import manualStrategies.StrategyGUI;

/**
 * 簡単なオセロのグラフィックス。
 * 以下のサイトを参考に作成しました：http://d.hatena.ne.jp/poor_code/20090711/1247269299
 * @author Hiro
 *
 */
public class SimpleGraphics extends OthelloGraphics {

	private static final int GRID_SIZE = 50;
	private static final int MARGIN_WIDTH = 50;
	private static int BOARD_WIDTH;
	
	GameMaster game;

	public SimpleGraphics(GameMaster gm){
		super(gm);
		game = gm;
		
		addMouseListener(this);
		
		BOARD_WIDTH = GRID_SIZE*GameMaster.SIZE;
		setPreferredSize(new Dimension(BOARD_WIDTH+2*MARGIN_WIDTH, BOARD_WIDTH+2*MARGIN_WIDTH));
	}		

	public void paintComponent(Graphics g){
		
		//背景
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, BOARD_WIDTH+2*MARGIN_WIDTH, BOARD_WIDTH+2*MARGIN_WIDTH);
		
		//盤
		g.setColor(new Color(51, 153, 51));
		g.fillRect(MARGIN_WIDTH, MARGIN_WIDTH, BOARD_WIDTH, BOARD_WIDTH);
		
		//線
		g.setColor(Color.BLACK);
		for(int i=0; i<=game.SIZE; i++){
			g.drawLine(MARGIN_WIDTH, i*GRID_SIZE+MARGIN_WIDTH, BOARD_WIDTH+MARGIN_WIDTH, i*GRID_SIZE+MARGIN_WIDTH);
			g.drawLine(i*GRID_SIZE+MARGIN_WIDTH, MARGIN_WIDTH, i*GRID_SIZE+MARGIN_WIDTH, BOARD_WIDTH+MARGIN_WIDTH);
		}
		g.setColor(Color.DARK_GRAY);
		//g.drawRect(SIZE*2, SIZE*2, SIZE*4, SIZE*4);
		
		//駒
		for(int y=0; y<game.SIZE; y++){
			for(int x=0; x<game.SIZE; x++){
				if(game.getState(x, y) == CellState.Black){
					g.setColor(Color.BLACK);
					g.fillOval(x*GRID_SIZE+MARGIN_WIDTH, y*GRID_SIZE+MARGIN_WIDTH, GRID_SIZE, GRID_SIZE);
				}else if(game.getState(x, y) == CellState.White){
					g.setColor(Color.WHITE);
					g.fillOval(x*GRID_SIZE+MARGIN_WIDTH, y*GRID_SIZE+MARGIN_WIDTH, GRID_SIZE, GRID_SIZE);
				}
			}
		}
		
		//データ表示
		if(game.isInGame()){
			g.setColor(Color.BLACK);
			g.drawString("In Game", MARGIN_WIDTH, BOARD_WIDTH+MARGIN_WIDTH+20);
		}else{
			g.setColor(Color.RED);
			String result;
			if(game.getWinner() == Player.Black){
				result = "黒の勝ちです。";
			}else if(game.getWinner() == Player.White){
				result = "白の勝ちです。";
			}else{
				result = "引き分けです。";				
			}
			g.drawString("Game Over. " + result, MARGIN_WIDTH, BOARD_WIDTH+MARGIN_WIDTH+20);
		}
		
		g.setColor(Color.BLACK);
		if(game.getCurrentPlayer() == Player.Black){
			g.drawString("黒の手番です", MARGIN_WIDTH, BOARD_WIDTH+MARGIN_WIDTH+40);
		}else{
			g.drawString("白の手番です", MARGIN_WIDTH, BOARD_WIDTH+MARGIN_WIDTH+40);
		}
		
		//行・列番号表示
		g.setColor(Color.BLACK);
		String[] alphabet = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
		for(int i=0; i<GameMaster.SIZE; i++){
			g.drawString(alphabet[i], MARGIN_WIDTH+i*GRID_SIZE+GRID_SIZE/2-5, MARGIN_WIDTH-5);
			g.drawString(""+(i+1), MARGIN_WIDTH-10, MARGIN_WIDTH+i*GRID_SIZE+GRID_SIZE/2+5);
		}
		
		//石の数表示
		g.drawString("黒：" + game.getNumPieces(Player.Black), MARGIN_WIDTH + BOARD_WIDTH - GRID_SIZE*2, BOARD_WIDTH+MARGIN_WIDTH+20);
		g.drawString("白：" + game.getNumPieces(Player.White), MARGIN_WIDTH + BOARD_WIDTH - GRID_SIZE*2, BOARD_WIDTH+MARGIN_WIDTH+40);
		
		//残り時間表示
		if(game.getRemainingTime(Player.Black)<0){
			g.drawString("時間無制限", MARGIN_WIDTH + BOARD_WIDTH - GRID_SIZE*1, BOARD_WIDTH+MARGIN_WIDTH+20);			
		}else{
			g.drawString("残り" + game.getRemainingTime(Player.Black)/1000 + "秒", MARGIN_WIDTH + BOARD_WIDTH - GRID_SIZE*1, BOARD_WIDTH+MARGIN_WIDTH+20);
		}
		if(game.getRemainingTime(Player.White)<0){
			g.drawString("時間無制限", MARGIN_WIDTH + BOARD_WIDTH - GRID_SIZE*1, BOARD_WIDTH+MARGIN_WIDTH+40);			
		}else{
			g.drawString("残り" + game.getRemainingTime(Player.White)/1000 + "秒", MARGIN_WIDTH + BOARD_WIDTH - GRID_SIZE*1, BOARD_WIDTH+MARGIN_WIDTH+40);
		}
		
//		g.drawString("TURN = "+state.turn, 10, 30);
//		g.drawString("PLAYER = "+state.player, 10, 50);
//		g.drawString("DISC = "+state.black+" : " +state.white, 10, 70);
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {

		System.out.println("MouseClicked");	
		if(strategy.size() != 0){
			int x = e.getX();
			int y = e.getY();
			
			int i = (x - MARGIN_WIDTH)/GRID_SIZE;
			int j = (y - MARGIN_WIDTH)/GRID_SIZE;
			System.out.println("(i,j)=("+i+","+j+")");	
			
			Iterator<StrategyGUI> itr = strategy.iterator();
			while(itr.hasNext()){
				itr.next().pickCell(i, j);
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


}
