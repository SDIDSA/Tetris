package scoreSaving;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class FileHandler {
	final static int NOT_FOUND = 0, FOUND = 1;
	private static final String Folder = "\\Tetris HighScores", File = "\\Scores.tet";
	private static String Location = "", myDocuments = "", Data;

	static int init() {
		int res = 0;
		try {
			Process p = Runtime.getRuntime().exec("reg query \"HKCU\\Software\\Microsoft\\"
					+ "Windows\\CurrentVersion\\Explorer\\Shell Folders\" /v personal");
			p.waitFor();

			InputStream in = p.getInputStream();
			byte[] b = new byte[in.available()];
			in.read(b);
			in.close();

			myDocuments = new String(b);
			myDocuments = myDocuments.split("\\s\\s+")[4];

		} catch (Throwable t) {
			t.printStackTrace();
		}

		Location = myDocuments + Folder + File;
		File ff = new File(myDocuments + Folder);
		if (ff.exists() && ff.isDirectory()) {
			File fff = new File(Location);
			if (fff.exists() && !fff.isDirectory()) {
				res = FOUND;
			} else {
				try {
					Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Location), "utf-8"));
					writer.close();
					res = NOT_FOUND;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			new File(myDocuments + Folder).mkdir();
			try {
				Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Location), "utf-8"));
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			res = NOT_FOUND;
		}
		return res;
	}

	public static ArrayList<Score> getScores() {
		ArrayList<Score> res = new ArrayList<Score>();
		String data = readFile();
		String[] scores = data.split("#score#");
		for (String score : scores) {
			String[] infos = score.split("#info#");
			try {
				String name = infos[0];
				int value = Integer.parseInt(infos[1]);
				String[] dates = infos[2].split("-");
				LocalDate date = LocalDate.of(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]),
						Integer.parseInt(dates[2]));
				String[] times = infos[3].split(":");
				LocalTime time = LocalTime.of(Integer.parseInt(times[0]), Integer.parseInt(times[1]));
				Score Score = new Score(name, value, date, time);
				res.add(Score);
			} catch (ArrayIndexOutOfBoundsException ex) {
			}

		}
		Collections.sort(res);
		return res;
	}

	public static String readFile() {
		File temp = new File(Location);
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(temp), "UTF-8"))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			String res = sb.toString().substring(0, sb.toString().length() - 1);
			return res;
		} catch (FileNotFoundException | StringIndexOutOfBoundsException e) {
			return "";
		} catch (IOException e1) {
			return "";
		}
	}

	public static void deleteLast() {
		Data = "";
		ArrayList<Score> scores = getScores();
		scores.forEach(sc -> {
			if (scores.indexOf(sc) != 4) {
				Data += sc.toSave();
			}
		});
		updateFile(Data);
	}

	public static void delete(Score s) {
		Data = "";
		getScores().forEach(sc -> {
			if (!sc.equals(s)) {
				Data += sc.toSave();
			}
		});
		updateFile(Data);
	}

	public static void updateFile(String data) {
		try {
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Location), "utf-8"));
			writer.write(data);
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Please report this to zinou.teyar@gmail.com :\nError while trying to save the file");
			alert.setHeaderText("Severe Error");
			alert.showAndWait();
		}
	}

	public static void addScore(Score s) {
		Data = readFile();
		updateFile(Data + s.toSave());
	}
}
