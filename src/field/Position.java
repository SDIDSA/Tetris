package field;

public class Position {
	private int x, y;

	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Position up() {
		return (new Position(getX(), getY() - 1));
	}

	public Position down() {
		return (new Position(getX(), getY() + 1));
	}

	public Position right() {
		return (new Position(getX() + 1, getY()));
	}

	public Position left() {
		return (new Position(getX() - 1, getY()));
	}

	public boolean isUnder(Position p) {
		return this.getX() == p.getX() && this.getY() == p.getY() + 1;
	}

	public boolean isLeft(Position p) {
		return this.getX() == p.getX() - 1 && this.getY() == p.getY();
	}

	public boolean isRight(Position p) {
		return this.getX() == p.getX() + 1 && this.getY() == p.getY();
	}

	public Position sub(Position pos) {
		return new Position(getX() - pos.getX(), getY() - pos.getY());
	}

	public Position add(Position pos) {
		return new Position(getX() + pos.getX(), getY() + pos.getY());
	}

	public boolean equals(Position p) {
		return getX() == p.getX() && getY() == p.getY();
	}

	public String toString() {
		return "Position : X = " + x + "\tY = " + y;
	}

}
