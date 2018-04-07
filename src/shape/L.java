package shape;

import field.Block;
import field.Position;

public class L extends Shape {
	public L(Position pos) {
		super(pos);
		offSets[0] = new Position(0, 0);
		offSets[1] = offSets[0].down();
		offSets[2] = offSets[1].down();
		offSets[3] = offSets[2].right();

		one = new Block(pos.add(offSets[0]));
		two = new Block(pos.add(offSets[1]));
		three = new Block(pos.add(offSets[2]));
		four = new Block(pos.add(offSets[3]));
		addAll(one, two, three, four);
	}

	@Override
	public void setPosition(Position pos) {
		one.setPosition(pos.add(offSets[0]));
		two.setPosition(pos.add(offSets[1]));
		three.setPosition(pos.add(offSets[2]));
		four.setPosition(pos.add(offSets[3]));
		this.pos = pos;
	}

}
