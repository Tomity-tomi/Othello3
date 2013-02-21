package game;

/**
 * 各参加者が実装した{@link game.Strategy}の子クラスのオブジェクトを生成するfactoryの抽象クラス。 
 * 各参加者はこのクラスも実装せねばならない。createStrategy()を呼ぶと、自分の作った戦略のオブジェクトが返されるように
 * すること。
 * 各参加者が作成する子クラスは必ず、本クラスのコンストラクタと同様に、(Player thisPlayer, int size)の二つのみを引数と
 * するpublicなコンストラクタを設けること。
 * どうして戦略クラスのみならずfactoryを作るかについては、<a href="http://www.nulab.co.jp/designPatterns/designPatterns2/designPatterns2-2.html" target="new"><b>ここらへん</b></a>を参照。
 * @author Hiro
 *
 */
public abstract class StrategyFactory {
	protected Player thisPlayer;
	protected int SIZE;
	
	/**
	 * コンストラクタ。子クラスのコンストラクタの引数は本コンストラクタと同様に(Player thisPlayer, int size)の二つのみとし、必要な設定（探索の深さなど）は子クラスのコンストラクタの中で行うこと。
	 * @param _thisPlayer
	 * @param size
	 */
	public StrategyFactory(Player _thisPlayer, int size){
		thisPlayer = _thisPlayer;
		SIZE = size;
	}
	
	/**
	 * 各参加者はこのメソッドをかならず実装すること。このメソッドは自分が作った{@link Strategy}の子クラスのオブジェクトを返す。
	 * @return
	 */
	abstract public Strategy createStrategy();
}
