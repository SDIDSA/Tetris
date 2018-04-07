package shape;

import java.util.ArrayList;

import field.Block;
import field.Position;

public class O extends AbstractShape {
	public O(Position pos) {
		super(pos);
		offSets[0] = new Position(0, 0);
		offSets[1] = offSets[0].down();
		offSets[2] = offSets[1].right();
		offSets[3] = offSets[0].right();

		one = new Block(pos.add(offSets[0]));
		two = new Block(pos.add(offSets[1]));
		three = new Block(pos.add(offSets[2]));
		four = new Block(pos.add(offSets[3]));
		addAll(one, two, three, four);
	}

	public void setPosition(Position pos) {
		one.setPosition(pos.add(offSets[0]));
		two.setPosition(pos.add(offSets[1]));
		three.setPosition(pos.add(offSets[2]));
		four.setPosition(pos.add(offSets[3]));
		this.pos = pos;
	}

	public ArrayList<Position> preRotate() {
		ArrayList<Position> res = new ArrayList<Position>();
		for (Block b : blocks) {
			res.add(b.getPosition());
		}
		return res;
	}

	@Override
	public void Rotate() {

	}

}
