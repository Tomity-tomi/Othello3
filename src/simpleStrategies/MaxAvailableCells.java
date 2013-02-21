package simpleStrategies;

import game.GameState;
import game.Move;
import game.OthelloMoveException;
import game.Player;
import game.Strategy;

public class MaxAvailableCells extends Strategy {

	public MaxAvailableCells(Player _thisPlayer, int size) {
		super(_thisPlayer, size);
	}

	@Override
	public Move nextMove(GameState currentState, int remainingTime) {
		int maxScore = Integer.MIN_VALUE;
		Move bestMove = new Move();
		
		for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				if(currentState.isLegal(thisPlayer,i,j)){
					try {
						GameState expectedState = currentState.nextState(thisPlayer, i, j);
						int score = expectedState.numAvailable(thisPlayer) - expectedState.numAvailable(thisPlayer.oppositePlayer());
						if(score > maxScore){
							maxScore = score;
							bestMove.x = i;
							bestMove.y = j;
						}
					} catch (OthelloMoveException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return bestMove;
	}

}
