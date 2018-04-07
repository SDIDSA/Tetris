package field;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import shape.PreviewShape;

public class Preview extends StackPane {
	int width, height;

	public Preview(int h, int w) {
		setAlignment(Pos.CENTER);
		width = w;
		height = w;
		setMaxSize(w, w);
		setMinSize(w, w);
		setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(20), new Insets(0))));
		setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(18), new BorderWidths(2))));
		setOnMousePressed(e->{
			setRotate(getRotate()+90);
		});
	}

	public void setShape(PreviewShape s) {
		this.getChildren().clear();
		s.getBlocks().forEach(b -> {
			getChildren().add(b);
		});
	}

	public int getWidths() {
		return width;
	}

	public int getHeights() {
		return height;
	}

}
