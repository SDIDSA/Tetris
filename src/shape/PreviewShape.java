package shape;

import java.util.ArrayList;

import field.Block;
import field.PreviewBlock;

public class PreviewShape {
	ArrayList<PreviewBlock> blocks = new ArrayList<PreviewBlock>();
	int minX;
	int maxX;
	int minY;
	int maxY;
	boolean hr, vr;

	public PreviewShape(AbstractShape s) {
		PreviewBlock one = new PreviewBlock(s.offSets[0]);
		PreviewBlock two = new PreviewBlock(s.offSets[1]);
		PreviewBlock three = new PreviewBlock(s.offSets[2]);
		PreviewBlock four = new PreviewBlock(s.offSets[3]);
		blocks.add(one);
		blocks.add(two);
		blocks.add(three);
		blocks.add(four);
		rigel();
		blocks.forEach(b -> {
			b.setTranslateX(b.getTranslateX() - getSize().getWidth() / 2);
			b.setTranslateY(b.getTranslateY() - getSize().getHeight() / 2);
		});
	}

	private void rigel() {
		hr = false;
		vr = false;
		blocks.forEach(block -> {
			if (block.getPosition().getX() < 0) {
				hr = true;
			} else if (block.getPosition().getY() < 0) {
				vr = true;
			}
		});
		if (hr) {
			blocks.forEach(block -> {
				block.setPosition(block.getPosition().right());
			});
		}
		if (vr) {
			blocks.forEach(block -> {
				block.setPosition(block.getPosition().down());
			});
		}
	}

	public Size getSize() {
		int height = 0;
		int width = 0;
		minX = 0;
		maxX = 0;
		minY = 0;
		maxY = 0;
		blocks.forEach(b -> {
			if (b.getPosition().getX() < minX) {
				minX = b.getPosition().getX();
			}
			if (b.getPosition().getX() > maxX) {
				maxX = b.getPosition().getX();
			}
			if (b.getPosition().getY() < minY) {
				minY = b.getPosition().getY();
			}
			if (b.getPosition().getY() > maxY) {
				maxY = b.getPosition().getY();
			}
		});
		height = (int) ((maxY - minY) * Block.SIZE);
		width = (int) ((maxX - minX) * Block.SIZE);
		Size res = new Size(width, height);
		return res;
	}

	public ArrayList<PreviewBlock> getBlocks() {
		return blocks;
	}
}
