package scoreSaving;

import java.util.ArrayList;

import controls.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Board {
	double initx, inity;
	Score toDelete;
	boolean added;
	boolean full;

	public boolean isHighScore(int v) {
		if (FileHandler.getScores().size() < 5) {
			return true;
		}
		for (Score s : FileHandler.getScores()) {
			if (v > s.getValue()) {
				return true;
			}
		}
		return false;
	}

	HBox loadScore(Score s) {
		HBox root = new HBox();
		root.getChildren().addAll(new Label(s.getName()), new Label(s.getValue() + ""), new Label(s.getDate()),
				new Label(s.getTime().substring(0, 5)));
		root.setAlignment(Pos.CENTER);
		return root;
	}

	HBox saveScore(int v) {
		HBox root = new HBox();
		TextField name = new TextField();
		name.setMaxWidth(100);
		name.setMinWidth(100);
		Label score = new Label(v + "");
		Button ok = new Button("OK");
		ok.setOnAction(e -> {
			Score s = new Score(v, name.getText());
			if (FileHandler.getScores().size() == 5) {
				FileHandler.deleteLast();
			}
			FileHandler.addScore(s);
			root.getChildren().clear();
			root.getChildren().addAll(new Label(s.getName()), new Label(s.getValue() + ""), new Label(s.getDate()),
					new Label(s.getTime().substring(0, 5)));
		});
		name.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			if(e.getCode().equals(KeyCode.ENTER)) {
				ok.fire();
			}
		});
		root.getChildren().addAll(name, score, ok);
		root.setAlignment(Pos.CENTER);
		return root;
	}

	public void ShowBoard(Stage owner) {
		Stage s = new Stage();

		VBox root = new VBox(30);

		VBox scores = new VBox(10);
		added = false;
		ArrayList<Score> scors = FileHandler.getScores();
		scors.forEach(sc -> {
			scores.getChildren().add(loadScore(sc));
		});

		root.setAlignment(Pos.CENTER);
		root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(20), new Insets(0))));
		root.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(18), new BorderWidths(2))));
		HBox buttons = new HBox(20);
		Button reset = new Button("Reset");
		reset.setOnAction(e -> {
			FileHandler.updateFile("");
			scores.getChildren().clear();
		});
		Button close = new Button("Close");
		close.setOnAction(e -> {
			s.close();
		});
		buttons.getChildren().addAll(close, reset);
		buttons.setAlignment(Pos.CENTER);
		root.getChildren().addAll(scores, buttons);
		root.setOnMousePressed(e -> {
			initx = e.getSceneX();
			inity = e.getSceneY();
		});
		root.setOnMouseDragged(e -> {
			s.setX(e.getScreenX() - initx);
			s.setY(e.getScreenY() - inity);
		});
		Scene scene = new Scene(root, 600, 300);
		scene.setFill(Color.TRANSPARENT);
		s.setScene(scene);
		s.initStyle(StageStyle.TRANSPARENT);
		s.initOwner(owner);
		s.initModality(Modality.WINDOW_MODAL);
		s.showAndWait();
	}

	public void ShowSavingStage(Stage owner, int val) {
		Stage s = new Stage();

		VBox root = new VBox(30);

		VBox scores = new VBox(10);
		added = false;
		ArrayList<Score> scors = FileHandler.getScores();
		if (scors.isEmpty()) {
			scores.getChildren().add(saveScore(val));
		} else {
			scors.forEach(sc -> {
				if (val > sc.getValue() && !added) {
					scores.getChildren().add(saveScore(val));
					added = true;
				}
				scores.getChildren().add(loadScore(sc));
			});
			if (!added) {
				scores.getChildren().add(saveScore(val));
			}
			if (scores.getChildren().size() > 5) {
				scores.getChildren().remove(5);
			}
		}

		root.setAlignment(Pos.CENTER);
		root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(20), new Insets(0))));
		root.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(18), new BorderWidths(2))));
		HBox buttons = new HBox(20);
		Button reset = new Button("Reset");
		reset.setOnAction(e -> {
			FileHandler.updateFile("");
			scores.getChildren().clear();
		});
		Button close = new Button("Close");
		close.setOnAction(e -> {
			s.close();
		});
		buttons.getChildren().addAll(close, reset);
		buttons.setAlignment(Pos.CENTER);
		root.getChildren().addAll(scores, buttons);
		root.setOnMousePressed(e -> {
			initx = e.getSceneX();
			inity = e.getSceneY();
		});
		root.setOnMouseDragged(e -> {
			s.setX(e.getScreenX() - initx);
			s.setY(e.getScreenY() - inity);
		});
		Scene scene = new Scene(root, 500, 300);
		scene.setFill(Color.TRANSPARENT);
		s.setScene(scene);
		s.initStyle(StageStyle.TRANSPARENT);
		s.initOwner(owner);
		s.initModality(Modality.WINDOW_MODAL);
		s.showAndWait();
	}

	class Label extends javafx.scene.control.Label {
		public Label(String c) {
			super(c);
			setFont(Font.font(16));
			setMaxWidth(100);
			setMinWidth(100);
			setTranslateX(50);
		}
	}

	class TextField extends javafx.scene.control.TextField {
		public TextField() {
			setFont(Font.font(16));
			setMaxWidth(100);
			setMinWidth(100);
		}
	}

	public void init() {
		FileHandler.init();
	}

}
