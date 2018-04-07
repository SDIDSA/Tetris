package field;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import shape.AbstractShape;
import shape.Size;
import sound.SoundSystem;
import test.UI;
import java.util.ArrayList;
import java.util.Collections;

public class Field extends Pane {
	boolean ready = true;
	int count;
	int i = 0;
	boolean bools[][] = new boolean[10][20];
	AbstractShape selected;
	ArrayList<Block> blocks = new ArrayList<Block>();
	boolean res;
	Grid g;
	Pane border;
	Pane pane;
	int j;
	int removing;
	Size size;
	Rectangle blend;
	Line line = new Line();
	Line lin = new Line();
	Line wit = new Line();
	public void rigel(int sh) {
		pane.setMaxHeight((sh - (size.getHeight() + 12)) / 2 + 1);
		pane.setMinHeight((sh - (size.getHeight() + 12)) / 2 + 1);
		pane.setTranslateY(-pane.getMaxHeight() - 4);
		line.setStartX(0);
		line.setStartY(0);
		line.setEndX(size.getWidth());
		line.setEndY(0);
		line.setStrokeWidth(2);
		
		lin.setStartX(0);
		lin.setStartY(pane.getMaxHeight());
		lin.setEndX(size.getWidth());
		lin.setEndY(pane.getMaxHeight());
		lin.setStrokeWidth(2);
		
		wit.setStartX(0);
		wit.setEndX(size.getWidth());
		wit.setStartY(pane.getMaxHeight() + 2);
		wit.setEndY(pane.getMaxHeight() + 2);
		wit.setStrokeWidth(2);
	}
	
	public Field(int w, int h, int sh) {
		size = new Size(w, h);
		setMaxSize(w, h);
		setMinSize(w, h);
		blend = new Rectangle(w,h);
		blend.setFill(Color.BLACK);
		blend.setBlendMode(BlendMode.DIFFERENCE);
		border = new Pane();
		border.setMaxSize(w + 20, h + 10);
		border.setMinSize(w + 20, h + 10);
		border.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
		border.setTranslateX(-10);
		border.setTranslateY(-5);
		border.setStyle("-fx-background-color: white");
		ImageView view = new ImageView(new Image("textures/background.jpg"));
		ColorAdjust co = new ColorAdjust();
		co.setBrightness(0.5);
		view.setEffect(co);
		view.setFitWidth(border.getMaxWidth()-4);
		view.setFitHeight(border.getMaxHeight()-4);
		view.setTranslateX(2);
		view.setTranslateY(2);
		border.getChildren().add(view);
		pane = new Pane();
		pane.setMaxHeight((sh - (h + 12)) / 2 + 1);
		pane.setMinHeight((sh - (h + 12)) / 2 + 1);
		pane.setMaxWidth(w);
		pane.setMinWidth(w);
		pane.setTranslateY(-pane.getMaxHeight() - 4);
		pane.setStyle("-fx-background-color: lightgray");
		
		line.setStartX(0);
		line.setStartY(0);
		line.setEndX(w);
		line.setEndY(0);
		line.setStrokeWidth(2);
		
		lin.setStartX(0);
		lin.setStartY(pane.getMaxHeight());
		lin.setEndX(w);
		lin.setEndY(pane.getMaxHeight());
		lin.setStrokeWidth(2);
		wit.setStartX(0);
		wit.setEndX(w);
		wit.setStartY(pane.getMaxHeight() + 2);
		wit.setEndY(pane.getMaxHeight() + 2);
		wit.setStroke(Color.rgb(121, 202, 249,1));
		wit.setEffect(co);
		wit.setStrokeWidth(2);
		pane.getChildren().addAll(line, lin, wit);
		g = new Grid(w, h,Color.GRAY);
		getChildren().addAll(border, g, pane, blend);
	}

	public void add(AbstractShape s) {
		getChildren().addAll(s.getBlocks());
		blocks.addAll(s.getBlocks());
		pane.toFront();
		blend.toFront();
		selected = s;
	}

	public boolean canRotate(AbstractShape s) {
		res = true;
		updateBools();
		Position[] poss = new Position[4];
		i = 0;
		s.preRotate().forEach(pos -> {
			poss[i] = pos;
			i++;
		});
		for (int i = 0; i < 4; i++) {
			if (poss[i].getX() < 0 || poss[i].getX() > 9 || poss[i].getY() > 19) {
				return false;
			} else {
				if (poss[i].getY() > 0 && bools[poss[i].getX()][poss[i].getY()]) {
					res = false;
					ArrayList<Block> blocks = s.getBlocks();
					for (Block block : blocks) {
						if (block.getPosition().equals(poss[i])) {
							res = true;
						}
					}
					if (!res) {
						return false;
					}
				}
			}
		}
		return res;
	}

	public boolean canFall(AbstractShape s) {

		res = true;
		updateBools();

		for (Block b : s.getBlocks()) {
			if (b.getPosition().getY() == 19) {
				res = false;
			}
		}

		if (res) {
			s.getBottoms().forEach(b -> {
				try {
					if (bools[b.getPosition().getX()][b.getPosition().getY() + 1]) {
						res = false;
					}
				} catch (ArrayIndexOutOfBoundsException ex) {
				}
			});
		}
		return res;
	}

	public boolean canLeft(AbstractShape s) {

		res = true;
		updateBools();

		for (Block b : s.getBlocks()) {
			if (b.getPosition().getX() == 0) {
				res = false;
			}
		}

		if (res) {
			s.getLefts().forEach(b -> {
				try {
					if (bools[b.getPosition().getX() - 1][b.getPosition().getY()]) {
						res = false;
					}
				} catch (ArrayIndexOutOfBoundsException x) {
				}

			});
		}
		return res;
	}

	public boolean canRight(AbstractShape s) {

		res = true;
		updateBools();

		for (Block b : s.getBlocks()) {
			if (b.getPosition().getX() == 9) {
				res = false;
			}
		}

		if (res) {
			s.getRights().forEach(b -> {
				try {
					if (bools[b.getPosition().getX() + 1][b.getPosition().getY()]) {
						res = false;
					}
				} catch (ArrayIndexOutOfBoundsException x) {
				}
			});
		}
		return res;
	}

	public void checkForRemovableLines(SoundSystem sm, UI ui) {
		ArrayList<Integer> toRemove = new ArrayList<Integer>();
		for (int i = 0; i < 20; i++) {
			boolean removable = true;
			for (int j = 0; j < 10; j++) {
				if (!bools[j][i])
					removable = false;
			}
			if (removable) {
				toRemove.add(i);
			}
		}
		if (!toRemove.isEmpty()) {
			ready = false;
			removing = 0;
			Timeline line = new Timeline(new KeyFrame(Duration.seconds(.3)));
			line.setOnFinished(e -> {
				if (removing < toRemove.size()) {
					removeLine(toRemove.get(removing));
					if (removing == 1) {
						sm.line2();
						addScore(20, ui, 40);
					} else if (removing == 2) {
						sm.line3();
						addScore(40, ui, 50);
					} else {
						sm.line4();
						addScore(100, ui, 60);
					}
					removing++;
					line.playFromStart();
				}
				if (removing >= toRemove.size()) {
					ready = true;
				}
			});
			sm.line1();
			removeLine(toRemove.get(0));
			addScore(10, ui, 30);
			removing++;
			line.playFromStart();
		}
	}

	private void removeLine(int i) {
		ArrayList<Block> removables = new ArrayList<Block>();
		blocks.forEach(block -> {
			if (block.getPosition().getY() == i) {
				removables.add(block);
			}
		});
		Collections.sort(removables);
		j = 4;
		Timeline line = new Timeline(new KeyFrame(Duration.seconds(.02),
				new KeyValue(removables.get(j).translateYProperty(), removables.get(j).getTranslateY() + Block.SIZE),
				new KeyValue(removables.get(9 - j).translateYProperty(),
						removables.get(9 - j).getTranslateY() + Block.SIZE)));
		line.setOnFinished(e -> {
			if (j >= 0) {
				blocks.remove(removables.get(j));
				blocks.remove(removables.get(9 - j));
				getChildren().remove(removables.get(j));
				getChildren().remove(removables.get(9 - j));
				if (j > 0) {

					j--;
					line.getKeyFrames()
							.setAll(new KeyFrame(Duration.seconds(.02),
									new KeyValue(removables.get(j).translateYProperty(),
											removables.get(j).getTranslateY() + Block.SIZE),
									new KeyValue(removables.get(9 - j).translateYProperty(),
											removables.get(9 - j).getTranslateY() + Block.SIZE)));
				} else {
					j--;
				}

				line.playFromStart();
			} else if (!blocks.isEmpty()) {
				count = 0;
				blocks.forEach(block -> {
					if (!block.isFrom(selected) && block.getPosition().getY() < i) {
						count++;
					}
				});
				KeyValue[] values = new KeyValue[count];
				blocks.forEach(block -> {
					if (!block.isFrom(selected) && block.getPosition().getY() < i) {
						count--;
						values[count] = new KeyValue(block.translateYProperty(), block.getTranslateY() + Block.SIZE);
					}
				});
				Timeline dow = new Timeline(new KeyFrame(Duration.seconds(.2), values));
				dow.setOnFinished(eve -> {
					blocks.forEach(block -> {
						if (!block.isFrom(selected) && block.getPosition().getY() < i) {
							block.setPosition(block.getPosition().down());
						}
					});
				});
				dow.playFromStart();
			}
		});
		line.playFromStart();
	}

	private void updateBools() {
		for (int i = 0; i < bools.length; i++) {
			for (int j = 0; j < bools[i].length; j++) {
				bools[i][j] = false;
			}
		}
		blocks.forEach(block -> {
			try {
				bools[block.getPosition().getX()][block.getPosition().getY()] = true;
			} catch (ArrayIndexOutOfBoundsException ex) {
			}

		});
	}

	public boolean isLost() {
		res = false;
		blocks.forEach(b -> {
			if (b.getPosition().getY() == 0)
				res = true;
		});
		return res;
	}

	public void addScore(int s, UI ui, int fs) {
		ui.addScore(s);
		Label label = new Label("+" + s);
		label.setFont(Font.font(fs));
		label.setStyle("-fx-font-weight: bold;");
		label.setMinSize(200, 100);
		label.setMaxSize(200, 100);
		label.setTextAlignment(TextAlignment.CENTER);
		label.setAlignment(Pos.CENTER);
		label.setTranslateX(size.getWidth() / 2 - 100);
		label.setTranslateY(size.getHeight() / 2 - 50);
		getChildren().add(label);
		Timeline hide = new Timeline(
				new KeyFrame(Duration.seconds(2), new KeyValue(label.translateYProperty(), label.getTranslateY() - 150),
						new KeyValue(label.opacityProperty(), 0)));
		hide.setOnFinished(e -> {
			getChildren().remove(label);
		});
		hide.playFromStart();
	}

	public boolean isReady() {
		return ready;
	}

	public void reset() {
		blocks.clear();
		getChildren().clear();
		for (int i = 0; i < bools.length; i++) {
			for (int j = 0; j < bools[i].length; j++) {
				bools[i][j] = false;
			}
		}
		Timeline change = new Timeline(new KeyFrame(Duration.seconds(1),new KeyValue(blend.fillProperty(),Color.BLACK)));
		change.playFromStart();
		getChildren().addAll(border, g, pane, blend);
	}

	public void setInvert(boolean b) {
		Color col;
		if (b) {
			col = Color.WHITE;
		} else {
			col = Color.BLACK;
		}
		Timeline change = new Timeline(new KeyFrame(Duration.seconds(1),new KeyValue(blend.fillProperty(),col)));
		change.playFromStart();
	}

}
