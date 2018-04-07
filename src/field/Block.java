package field;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import shape.AbstractShape;

public class Block extends Pane implements Comparable<Block> {
	public static int SIZE;
	public Position position;
	public boolean b;
	private static double time = 0.05;
	boolean brust = false;
	Rectangle rec;
	public static void init(int s) {
		SIZE = s;
	}

	public Block(Position pos, Color col) {
		super();
		setMaxSize(SIZE,SIZE);
		setMinSize(SIZE,SIZE);
		setTranslateX(pos.getX() * SIZE);
		setTranslateY(pos.getY() * SIZE);
		rec = new Rectangle(SIZE-4,SIZE-4);
		rec.setTranslateX(2);
		rec.setTranslateY(2);
		rec.setFill(col);
		position = pos;
		rec.setStroke(Color.BLACK);
		ImageView view = new ImageView(new Image("textures/block.png"));
		view.setFitHeight(SIZE);
		view.setFitWidth(SIZE);
		this.getChildren().addAll(rec,view);
	}

	public Block(Position pos) {
		this(pos, Color.BLACK);
	}

	public void setPosition(Position pos) {
		if(brust) {
			setTranslateX(pos.getX() * SIZE);
			setTranslateY(pos.getY() * SIZE);
		}else {
			Timeline move = new Timeline(
					new KeyFrame(Duration.seconds(time), new KeyValue(translateXProperty(), pos.getX() * SIZE),
							new KeyValue(translateYProperty(), pos.getY() * SIZE)));
			move.playFromStart();
		}
		
		this.position = pos;
	}

	public Position getPosition() {
		return position;
	}

	public String toString() {
		return position.toString();
	}

	@Override
	public int compareTo(Block b) {
		if (position.getX() > b.position.getX())
			return 1;
		else if (position.getX() < b.position.getX())
			return -1;
		else if (position.getY() > b.position.getY())
			return 1;
		else if (position.getY() < b.position.getY())
			return -1;
		else
			return 0;
	}

	public void setFill(Color col) {
		rec.setFill(col);
	}
	public boolean isFrom(AbstractShape s) {
		return s.contains(this);
	}

}
