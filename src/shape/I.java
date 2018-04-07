package shape;

import java.util.ArrayList;

import field.Block;
import field.Position;

public class I extends Shape {
	boolean rotated = false;

	public I(Position pos) {
		super(pos);
		offSets[0] = new Position(0, 0);
		offSets[1] = offSets[0].down();
		offSets[2] = offSets[1].down();
		offSets[3] = offSets[2].down();

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
		if (rotated) {
			res.add(pos.add(new Position(0, 0)));
			res.add(pos.add(new Position(0, 1)));
			res.add(pos.add(new Position(0, 2)));
			res.add(pos.add(new Position(0, 3)));
		} else {
			res.add(pos.add(new Position(-1, 1)));
			res.add(pos.add(new Position(0, 1)));
			res.add(pos.add(new Position(1, 1)));
			res.add(pos.add(new Position(2, 1)));
		}
		return res;
	}

	public void Rotate() {
		if (rotated) {
			offSets[0] = new Position(0, 0);
			offSets[1] = new Position(0, 1);
			offSets[2] = new Position(0, 2);
			offSets[3] = new Position(0, 3);
			rotated = false;
		} else {
			offSets[0] = new Position(-1, 1);
			offSets[1] = new Position(0, 1);
			offSets[2] = new Position(1, 1);
			offSets[3] = new Position(2, 1);
			rotated = true;
		}
		blocks.forEach(b -> {
			b.setPosition(offSets[blocks.indexOf(b)].add(pos));
		});
	}

}
