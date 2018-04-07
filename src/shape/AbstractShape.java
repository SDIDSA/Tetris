package shape;

import java.util.ArrayList;

import field.Block;
import field.Field;
import field.Position;
import javafx.scene.paint.Color;

public abstract class AbstractShape {
	Block one, two, three, four;
	int face;
	Position[] offSets = new Position[4];
	ArrayList<Block> blocks = new ArrayList<Block>();
	Position pos;

	public Position[] getOffSets() {
		return offSets;
	}
	
	public AbstractShape(Position pos) {
		this.pos = pos;
	}

	public void setFill(Color p) {
		blocks.forEach(block -> {
			block.setFill(p);
		});
	}

	public Position getPosition() {
		return pos;
	}

	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	public abstract void setPosition(Position pos);

	void addAll(Block... blocks) {
		for (Block b : blocks) {
			this.blocks.add(b);
		}
	}

	public ArrayList<Block> getBottoms() {
		ArrayList<Block> res = new ArrayList<Block>();
		for (Block b : this.getBlocks()) {
			b.b = true;
		}
		this.getBlocks().forEach(block1 -> {
			this.getBlocks().forEach(block2 -> {
				if (block2.getPosition().isUnder(block1.getPosition())) {
					block1.b = false;
				}
			});
		});
		this.getBlocks().forEach(block -> {
			if (block.b) {
				res.add(block);
			}
		});
		return res;
	}

	public abstract ArrayList<Position> preRotate();

	public abstract void Rotate();

	public ArrayList<Block> getRights() {
		ArrayList<Block> res = new ArrayList<Block>();
		for (Block b : this.getBlocks()) {
			b.b = true;
		}
		this.getBlocks().forEach(block1 -> {
			this.getBlocks().forEach(block2 -> {
				if (block2.getPosition().isRight(block1.getPosition())) {
					block1.b = false;
				}
			});
		});
		this.getBlocks().forEach(block -> {
			if (block.b) {
				res.add(block);
			}
		});
		return res;
	}

	public ArrayList<Block> getLefts() {
		ArrayList<Block> res = new ArrayList<Block>();
		for (Block b : this.getBlocks()) {
			b.b = true;
		}
		this.getBlocks().forEach(block1 -> {
			this.getBlocks().forEach(block2 -> {
				if (block2.getPosition().isLeft(block1.getPosition())) {
					block1.b = false;
				}
			});
		});
		this.getBlocks().forEach(block -> {
			if (block.b) {
				res.add(block);
			}
		});
		return res;
	}

	public boolean canFall(Field f) {
		return f.canFall(this);
	}
	
	public boolean contains(Block block) {
		return block==one||block==two||block==three||block==four;
	}
	
	public PreviewShape copy() {
		PreviewShape s = new PreviewShape(this);
		return s;
	}
}
