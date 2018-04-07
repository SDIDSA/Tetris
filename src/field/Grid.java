package field;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Grid extends Pane {
	public Grid(int w, int h, Color col) {
		for (int i = 0; i <= w; i += w / 10) {
			Line line = new Line();
			line.setStartX(i);
			line.setStartY(0);
			line.setEndX(i);
			line.setEndY(h);
			line.setStroke(col);
			getChildren().add(line);
		}
		for (int i = 0; i <= h; i += h / 20) {
			Line line = new Line();
			line.setStartY(i);
			line.setStartX(0);
			line.setEndY(i);
			line.setEndX(w);
			line.setStroke(col);
			getChildren().add(line);
		}
	}

	public Grid(int w, int h) {
		this(w, h, Color.GRAY);
	}
}
