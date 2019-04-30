package test;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import controls.Button;
import field.Block;
import field.Field;
import field.Position;
import field.Preview;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCombination;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import scoreSaving.Board;
import shape.AbstractShape;
import shape.I;
import shape.J;
import shape.L;
import shape.O;
import shape.S;
import shape.T;
import shape.Z;
import sound.SoundSystem;

public class UI extends Application {
	double initx, inity;
	SoundSystem sound;
	long then = 0;
	int pieceNo = 0;
	String fifty = "";
	public int scores = 0;
	Color stock = Color.PINK;
	Color colors[] = new Color[5];
	double time = 0.5;
	int times = 0;
	Preview preview;
	Board board;
	Stage ps;
	boolean soundWorks = false;
	static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
			SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight(),
			SCENE_HEIGHT = 5 * SCREEN_HEIGHT / 6, SCENE_WIDTH = 4 * SCENE_HEIGHT / 3, FIELD_HEIGHT = SCENE_HEIGHT - 50,
			FIELD_WIDTH = FIELD_HEIGHT / 2;
	AbstractShape l;
	Position init = new Position(4, -4);
	AnimationTimer timer;
	boolean paused = true;
	Field field;
	Label points;
	Button p;
	Button r;

	@Override
	public void start(Stage stage) {
		Button.init(SCREEN_HEIGHT);
		ps = stage;
		board = new Board();
		board.init();
		sound = new SoundSystem();
		soundWorks = sound.isWorking();
		shuffle();
		initColors();
		HBox root = new HBox(SCREEN_WIDTH * 0.03);
		root.setAlignment(Pos.CENTER);
		Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

		Block.init(FIELD_WIDTH / 10);

		field = new Field(Block.SIZE*10, Block.SIZE*20, SCENE_HEIGHT);

		VBox controls = new VBox(SCREEN_HEIGHT * 0.04);

		HBox cent = new HBox(20);
		cent.setAlignment(Pos.CENTER);
		preview = new Preview(Block.SIZE * 5, Block.SIZE * 5);
		cent.getChildren().addAll(preview);

		HBox score = new HBox(10);
		score.setAlignment(Pos.CENTER);
		points = new Label("Score\n" + scores);
		points.setTextAlignment(TextAlignment.CENTER);
		points.setFont(Font.font(SCREEN_WIDTH * 0.01464));
		Button showScores = new Button("HighScores");
		showScores.setOnAction(e -> {
			board.ShowBoard(ps);
		});
		score.getChildren().addAll(points, showScores);

		VBox buttons = new VBox(10);
		buttons.setAlignment(Pos.CENTER);

		p = new Button("Play");
		p.setDisable(true);
		p.setOnAction(e -> {
			if (!paused) {
				pause();
			} else {
				play();
			}
		});
		r = new Button("Start");
		r.setOnAction(e -> {
			r.setText("Restart");
			p.setDisable(false);
			field.reset();
			l = generate();
			field.add(l);
			scores = 0;
			points.setText("Score\n" + scores);
			play();
		});
		Button ex = new Button("Exit");
		ex.setOnAction(e -> {
			exit();
		});

		buttons.getChildren().addAll(p, r, ex);

		VBox volume = new VBox(10);
		
		Label mvol = new Label("Global Volume");
		Slider mvolumeSlider = new Slider();
		mvolumeSlider.setMax(100);
		mvolumeSlider.setValue(100);
		mvolumeSlider.setMaxWidth(SCREEN_WIDTH * 0.088);
		mvolumeSlider.setMinWidth(SCREEN_WIDTH * 0.088);
		mvolumeSlider.setOnMouseReleased(e-> {
			sound.setGlobalVolume(mvolumeSlider.getValue());
		});
		VBox mlabeled = new VBox();
		mlabeled.setAlignment(Pos.CENTER);
		mlabeled.getChildren().addAll(mvol, mvolumeSlider);
		
		Label vol = new Label("Sound Volume");
		Slider volumeSlider = new Slider();
		volumeSlider.setMax(100);
		volumeSlider.setValue(100);
		volumeSlider.setMaxWidth(SCREEN_WIDTH * 0.088);
		volumeSlider.setMinWidth(SCREEN_WIDTH * 0.088);
		volumeSlider.setOnMouseReleased(e-> {
			sound.setVolume(volumeSlider.getValue());
		});
		VBox labeled = new VBox();
		labeled.setAlignment(Pos.CENTER);
		labeled.getChildren().addAll(vol, volumeSlider);

		Label svol = new Label("Music Volume");
		Slider svolumeSlider = new Slider();
		svolumeSlider.setMax(100);
		svolumeSlider.setValue(100);
		svolumeSlider.setMaxWidth(SCREEN_WIDTH * 0.088);
		svolumeSlider.setMinWidth(SCREEN_WIDTH * 0.088);
		svolumeSlider.setOnMouseReleased(e->{
			sound.setMusicVol(svolumeSlider.getValue());
		});
		VBox slabeled = new VBox();
		slabeled.setAlignment(Pos.CENTER);
		slabeled.getChildren().addAll(svol, svolumeSlider);

		CheckBox mute = new CheckBox("Mute");
		CheckBox sounde = new CheckBox("Sounds");
		CheckBox musice = new CheckBox("Music");
		musice.setOnAction(e -> {
			if (soundWorks) {
				if (!musice.isSelected() && !sounde.isSelected()) {
					mute.setSelected(true);
				} else {
					mute.setSelected(false);
				}
				svolumeSlider.setDisable(!musice.isSelected());
				mvolumeSlider.setDisable(!sounde.isSelected()&&!musice.isSelected());
				sound.setMusice(musice.isSelected());
			}
		});
		sounde.setOnAction(e -> {
			if (soundWorks) {
				if (!musice.isSelected() && !sounde.isSelected()) {
					mute.setSelected(true);
				} else {
					mute.setSelected(false);
				}
				volumeSlider.setDisable(!sounde.isSelected());
				mvolumeSlider.setDisable(!sounde.isSelected()&&!musice.isSelected());
				sound.setSounde(sounde.isSelected());
			}
		});
		mute.setOnAction(e -> {
			if (soundWorks) {
				musice.setSelected(!mute.isSelected());
				volumeSlider.setDisable(mute.isSelected());
				sound.setMusice(!mute.isSelected());
				sounde.setSelected(!mute.isSelected());
				svolumeSlider.setDisable(mute.isSelected());
				sound.setSounde(!mute.isSelected());
				mvolumeSlider.setDisable(mute.isSelected());
			}
		});
		if (!soundWorks) {
			volumeSlider.setDisable(true);
			svolumeSlider.setDisable(true);
			mvolumeSlider.setDisable(true);
			mute.setDisable(true);
			sounde.setDisable(true);
			musice.setDisable(true);
		}
		HBox checks = new HBox(10);
		checks.setAlignment(Pos.CENTER);
		checks.getChildren().addAll(mute, sounde, musice);

		volume.getChildren().addAll(mlabeled,labeled, slabeled, checks);
		volume.setAlignment(Pos.CENTER);

		controls.getChildren().addAll(score, cent, volume, buttons);

		controls.setAlignment(Pos.CENTER);

	

	

		root.getChildren().addAll(field, controls);

		l = generate();
		field.add(l);

		scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
			switch (e.getCode()) {
			case RIGHT:
				if (field.canRight(l) && !paused) {
					l.setPosition(l.getPosition().right());
				}
				break;
			case LEFT:
				if (field.canLeft(l) && !paused) {
					l.setPosition(l.getPosition().left());
				}
				break;
			case DOWN:
				if (field.isReady()) {
					sound.move();
					Down();
				}
				break;
			case ENTER:
				if(e.isAltDown()) {
					if(ps.isFullScreen()) {
						root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(30), new Insets(0))));
						root.setBorder(new Border(
								new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(27), new BorderWidths(2))));
						
					}else {
						root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(00), new Insets(0))));
						root.setBorder(new Border(
								new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
						
					}
					ps.setFullScreen(!ps.isFullScreen());
					field.rigel((int) scene.getHeight());
					e.consume();
					break;
				}else {
					
				}
			case UP:
				if (field.canRotate(l) && !paused) {
					sound.rotate();
					l.Rotate();
				}
				break;
			case ESCAPE:
				if (!paused) {
					pause();
				} else {
					play();
				}
			break;
			default:
				break;
			}
		});

		timer = new AnimationTimer() {
			double sum = 0;

			@Override
			public void handle(long now) {
				long dt = now - then;

				field.requestFocus();
				if (!ps.isFocused()) {
					pause();
				}
				sum += dt;
				if (sum / 1000000000 >= time) {
					sum = 0;
					if (field.isReady()) {
						sound.move();
						Down();
					}
				}
				then = now;
			}
		};

		root.setOnMousePressed(e -> {
			initx = e.getSceneX();
			inity = e.getSceneY();
		});
		root.setOnMouseDragged(e -> {
			ps.setX(e.getScreenX() - initx);
			ps.setY(e.getScreenY() - inity);
		});
		mute.fire();
		ps.setOnCloseRequest(e -> {
			e.consume();
			exit();
		});
		ps.initStyle(StageStyle.TRANSPARENT);
		scene.setFill(Color.TRANSPARENT);
		root.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(30), new Insets(0))));
		root.setBorder(new Border(
				new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(27), new BorderWidths(2))));
		scene.getStylesheets().addAll("test/checkbox.css","test/slider.css");
		ps.setFullScreenExitKeyCombination(KeyCombination.keyCombination("CTRL+ALT+f"));
		ps.setFullScreenExitHint("ALT + Enter to exit full screen mode");
		ps.setScene(scene);
		ps.setTitle("Tetris Wannabe");
		ps.show();
	}

	public AbstractShape getNext() {
		int nextPieceNo;
		if (pieceNo > 49) {
			shuffle();
			nextPieceNo = 0;
		} else {
			nextPieceNo = pieceNo;
		}
		int type = getI(nextPieceNo);
		AbstractShape s;
		switch (type) {
		case 0:
			s = new L(init);
			break;
		case 1:
			s = new J(init);
			break;
		case 2:
			s = new I(init);
			break;
		case 3:
			s = new O(init);
			break;
		case 4:
			s = new S(init);
			break;
		case 5:
			s = new Z(init);
			break;
		case 6:
			s = new T(init);
			break;
		default:
			s = new L(init);
		}
		s.setFill(colors[(int) (Math.random() * 5)]);
		return s;
	}

	public AbstractShape generate() {
		int type = getI(pieceNo);
		pieceNo++;
		if (pieceNo > 49) {
			pieceNo = 0;
		}
		AbstractShape s;
		switch (type) {
		case 0:
			s = new L(init);
			break;
		case 1:
			s = new J(init);
			break;
		case 2:
			s = new I(init);
			break;
		case 3:
			s = new O(init);
			break;
		case 4:
			s = new S(init);
			break;
		case 5:
			s = new Z(init);
			break;
		case 6:
			s = new T(init);
			break;
		default:
			s = new L(init);
		}
		s.setFill(colors[(int) (Math.random() * 5)]);
		preview.setShape(getNext().copy());
		return s;
	}

	void lose() {
		time = 0.5;
		p.setDisable(true);
		r.setText("Start");
		sound.lose();
		soundLessPause();
		if (board.isHighScore(scores)) {
			Alert alert = new Alert(AlertType.NONE);
			alert.initOwner(ps);
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.setContentText(
					"You Lose ! Your Score Is " + scores + ", Which Is A HighScore! Do You Want To Save It ?");
			alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
			Runnable run = new Runnable() {
				@Override
				public void run() {
					alert.showAndWait();
					if (alert.getResult().equals(ButtonType.YES)) {
						board.ShowSavingStage(ps, scores);
					}
					field.reset();
					l = generate();
					field.add(l);
					scores = 0;
					points.setText("Score\n" + scores);
				}
			};
			Platform.runLater(run);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(ps);
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.setContentText("You Lose ! your score is " + scores);
			alert.setOnHidden(e -> {
				field.reset();
				l = generate();
				field.add(l);
				scores = 0;
				points.setText("Score\n" + scores);
			});
			Platform.runLater(alert::showAndWait);
		}
	}

	void initColors() {
		colors[0] = Color.CYAN;
		colors[1] = Color.YELLOW;
		colors[2] = Color.LIGHTGREEN;
		colors[3] = Color.LIGHTBLUE;
		colors[4] = Color.LIGHTPINK;
	}

	@SuppressWarnings("hiding")
	public static <T> Stream<T> stream(T[] elements) {
		return Stream.iterate(Arrays.asList(elements), list -> {
			Collections.shuffle(list = new ArrayList<>(list));
			return list;
		}).skip(1).flatMap(List::stream);
	}

	public void shuffle() {
		fifty = stream("0123456".split("")).limit(50).collect(Collectors.joining());
	}

	public int getI(int posi) {
		return Integer.parseInt(fifty.charAt(posi) + "");
	}

	public void addScore(int s) {
		scores += s;
		if (scores >= 100) {
			if (scores < 200) {
				time = 0.4;
				field.setInvert(true);
			} else if (scores < 400) {
				time = 0.3;
				field.setInvert(false);
			} else if (scores < 600) {
				time = 0.2;
				field.setInvert(true);
			} else if (scores < 800){
				time = 0.1;
				field.setInvert(false);
			}else  {
				field.setInvert(true);
			}
		}
		points.setText("Score\n" + scores);
	}

	void pause() {
		p.setText("Play");
		timer.stop();
		sound.pause();
		paused = true;
		sound.setPauseMusic();
	}

	void soundLessPause() {
		p.setText("Play");
		timer.stop();
		paused = true;
		sound.setPauseMusic();
	}

	void play() {
		p.setDisable(false);
		p.setText("Pause");
		then = System.nanoTime();
		timer.start();
		sound.play();
		paused = false;
		sound.setPlayMusic();
	}

	void Down() {
		if (field.canFall(l) && !paused) {
			l.setPosition(l.getPosition().down());
		} else if (!field.canFall(l) && !paused) {
			sound.grounded();
			l.setFill(stock);
			l = generate();
			if (field.isLost())
				lose();
			else
				field.add(l);

			field.checkForRemovableLines(sound, this);
		}
	}

	void exit() {
		Alert alert = new Alert(AlertType.NONE);
		alert.initOwner(ps);
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
		alert.setTitle("Exit Confirmation");
		alert.setContentText("Are You Sure?");
		alert.showAndWait();
		if (alert.getResult().equals(ButtonType.YES)) {
			Platform.exit();
		}
	}
}
