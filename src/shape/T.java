package shape;

import field.Block;
import field.Position;

public class T extends Shape {
	public T(Position pos) {
		super(pos);
		offSets[0] = new Position(-1, 1);
		offSets[1] = offSets[0].right();
		offSets[2] = offSets[1].up();
		offSets[3] = offSets[1].right();

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
