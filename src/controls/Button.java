package controls;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Button extends javafx.scene.control.Button {
	static Background gray = new Background(new BackgroundFill(Color.DARKGRAY, new CornerRadii(10), new Insets(0))),
			black = new Background(new BackgroundFill(Color.GRAY, new CornerRadii(10), new Insets(0))),
			white = new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), new Insets(0)));
	static DropShadow shad = new DropShadow();
	public static int SCREEN_HEIGHT;

	public Button(String Cont) {
		super(Cont);
		setFont(Font.font(SCREEN_HEIGHT * 0.025));
		setMaxWidth(SCREEN_HEIGHT * 0.2);
		setMinWidth(SCREEN_HEIGHT * 0.2);
		setMaxHeight(SCREEN_HEIGHT * 5.2 / 100);
		setMinHeight(SCREEN_HEIGHT * 5.2 / 100);
		setBackground(white);
		setOnMouseEntered(e -> {
			setBackground(gray);
			setEffect(shad);
		});
		setOnMouseExited(e -> {
			if (!isPressed()) {
				setBackground(white);
				setTextFill(Color.BLACK);
				setEffect(null);
			}
		});
		setOnMousePressed(e -> {
			setBackground(black);
			setTextFill(Color.WHITE);
			setEffect(shad);
		});
		setOnMouseReleased(e -> {
			if (isHover()) {
				setBackground(gray);
				setTextFill(Color.BLACK);
				setEffect(shad);
			} else {
				setBackground(white);
				setTextFill(Color.BLACK);
				setEffect(null);
			}
		});
		setCursor(Cursor.HAND);
	}

	public static void init(int s) {
		SCREEN_HEIGHT = s;
		shad.setHeight(30);
		shad.setWidth(30);
		shad.setSpread(.3);
	}
}
