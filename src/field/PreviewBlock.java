package field;

import javafx.scene.paint.Color;

public class PreviewBlock extends Block {

	public PreviewBlock(Position pos) {
		super(pos);
		setFill(Color.GRAY);
	}
	
	@Override
	public void setPosition(Position pos) {
		setTranslateX(pos.getX() * SIZE + 2);
		setTranslateY(pos.getY() * SIZE + 2);
		position = pos;
	}
}
