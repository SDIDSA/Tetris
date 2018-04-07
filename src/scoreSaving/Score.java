package scoreSaving;

import java.time.LocalDate;
import java.time.LocalTime;

public class Score implements Comparable<Score> {
	private int value;
	private String name;
	private LocalDate date;
	private LocalTime time;

	public Score(int v, String n) {
		value = v;
		name = n;
		date = LocalDate.now();
		time = LocalTime.now();
	}

	public Score(String n, int v, LocalDate d, LocalTime t) {
		value = v;
		name = n;
		date = d;
		time = t;
	}

	public int getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public String getDate() {
		return date.toString();
	}

	public String getTime() {
		return time.toString();
	}

	public boolean equals(Score s) {
		return (name.equals(s.name) && value == s.value && date.equals(s.date) && time.getHour() == s.time.getHour()
				&& time.getMinute() == s.time.getMinute());
	}

	public String toSave() {
		String res = "";
		res += "#score#" + name + "#info#" + value + "#info#" + date.toString() + "#info#"
				+ time.toString().substring(0, 5);
		return res;
	}

	@Override
	public int compareTo(Score sc) {
		if (value < sc.value) {
			return 1;
		} else if (value > sc.value) {
			return -1;
		} else {
			if (time.isBefore(sc.time)) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
