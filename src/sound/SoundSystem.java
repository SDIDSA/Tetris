package sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SoundSystem {
	int fp;
	double globalVol = 100;
	double soundvolume = 100;
	double musicVol = 100;
	boolean musice = false, sounde = false,paused=true;
	AudioInputStream line1, line2, line3, line4, grounded, play, pause, win, lose, rotate, move, music,pm,SelectedMusic;
	Clip lines1, lines2, lines3, lines4, groundeds, plays, pauses, wins, loses, rotates, moves, musics;
	Thread t;
	boolean worked = false;
	public SoundSystem() {
		try {
			line1 = AudioSystem.getAudioInputStream(new File("src/sound/line1.wav").getAbsoluteFile());
			line2 = AudioSystem.getAudioInputStream(new File("src/sound/line2.wav").getAbsoluteFile());
			line3 = AudioSystem.getAudioInputStream(new File("src/sound/line3.wav").getAbsoluteFile());
			line4 = AudioSystem.getAudioInputStream(new File("src/sound/line4.wav").getAbsoluteFile());
			grounded = AudioSystem.getAudioInputStream(new File("src/sound/grounded.wav").getAbsoluteFile());
			play = AudioSystem.getAudioInputStream(new File("src/sound/play.wav").getAbsoluteFile());
			pause = AudioSystem.getAudioInputStream(new File("src/sound/pause.wav").getAbsoluteFile());
			lose = AudioSystem.getAudioInputStream(new File("src/sound/lose.wav").getAbsoluteFile());
			rotate = AudioSystem.getAudioInputStream(new File("src/sound/rotate.wav").getAbsoluteFile());
			move = AudioSystem.getAudioInputStream(new File("src/sound/move.wav").getAbsoluteFile());
			music = AudioSystem.getAudioInputStream(new File("src/sound/music.wav").getAbsoluteFile());
			pm = AudioSystem.getAudioInputStream(new File("src/sound/mmusic.wav").getAbsoluteFile());
			musics = AudioSystem.getClip();
			SelectedMusic = pm;
			musics.open(SelectedMusic);

			lines1 = AudioSystem.getClip();
			lines1.open(line1);

			lines2 = AudioSystem.getClip();
			lines2.open(line2);

			lines3 = AudioSystem.getClip();
			lines3.open(line3);

			lines4 = AudioSystem.getClip();
			lines4.open(line4);

			groundeds = AudioSystem.getClip();
			groundeds.open(grounded);

			plays = AudioSystem.getClip();
			plays.open(play);

			pauses = AudioSystem.getClip();
			pauses.open(pause);

			loses = AudioSystem.getClip();
			loses.open(lose);

			rotates = AudioSystem.getClip();
			rotates.open(rotate);

			moves = AudioSystem.getClip();
			moves.open(move);
			worked = true;
		} catch (Exception ex) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("Sound will not work because of an Error while initializing the soundSystem. Uninstalling and Reinstalling the program might solve this problem");
			alert.showAndWait();
			ex.printStackTrace();
		}
	}

	public boolean isWorking() {
		return worked;
	}
	
	public void line1() {
		if (sounde) {
			plays(lines1);
		}
	}

	public void line2() {
		if (sounde) {
			plays(lines2);
		}
	}

	public void line3() {
		if (sounde) {
			plays(lines3);
		}
	}

	public void line4() {
		if (sounde) {
			plays(lines4);
		}
	}

	public void pause() {
		if (sounde) {
			plays(pauses);
		}
	}

	public void play() {
		if (sounde) {
			plays(plays);
		}
	}

	public void grounded() {
		if (sounde) {
			plays(groundeds);
		}
	}

	public void lose() {
		if (sounde) {
			plays(loses);
		}
	}

	public void rotate() {
		if (sounde) {
			plays(rotates);
		}
	}

	public void move() {
		if (sounde) {
			plays(moves);
		}
	}

	public void setSounde(boolean b) {
		sounde = b;
	}

	public void setMusice(boolean b) {
		musice = b;
		if(musics!=null) {
			setMusicVol(musicVol);
			if(b) {
				musics.setFramePosition(fp);
				musics.loop(Clip.LOOP_CONTINUOUSLY);
			}else {
				fp = musics.getFramePosition();
				musics.stop();
			}
		}
	}

	public void setVolume(double v) {
		soundvolume = v;
	}
	public void setMusicVol(double v){
		int tar = 0;
		if(paused) {
			tar = 120;
		}else {
			tar = 100;
		}
		if(musics!=null) {
			int fp = musics.getFramePosition();
			musics.stop();
			musicVol = v;
			FloatControl volumes = (FloatControl) musics.getControl(FloatControl.Type.MASTER_GAIN);
			float range = volumes.getMaximum() - volumes.getMinimum();
			float gain = (float) ((range*(globalVol/100)* musicVol / tar) + volumes.getMinimum());
			volumes.setValue(gain);
			musics.setFramePosition(fp);
			if(musice)
			musics.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}

	public void plays(Clip clip) {
		if(clip!=null
				&&!(clip==moves&&musics.isRunning())
				) {
			float target = 0;
			if(clip==groundeds) {
				target = (float) ((globalVol/100) * (soundvolume / 100));
			}else {
				target = (float) ((globalVol/100) * (soundvolume / 120));
			}
			FloatControl volumes = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			float range = volumes.getMaximum() - volumes.getMinimum();
			float gain = (float) (range*target) + volumes.getMinimum();
			volumes.setValue(gain);
			clip.setFramePosition(0);
			clip.start();
		}
	}
	
	public void setGlobalVolume(double v) {
		globalVol = v;
		setMusicVol(musicVol);
		setVolume(soundvolume);
	}
	
	public void setPauseMusic() {
		paused = true;
		musics.close();
		try {
			musics = AudioSystem.getClip();
			pm = AudioSystem.getAudioInputStream(new File("src/sound/mmusic.wav").getAbsoluteFile());
			musics.open(pm);
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		if(musice) {
			setMusicVol(musicVol);
			musics.start();
		}
	}
	public void setPlayMusic() {
		paused = false;
		musics.close();
		try {
			musics = AudioSystem.getClip();
			music = AudioSystem.getAudioInputStream(new File("src/sound/music.wav").getAbsoluteFile());
			musics.open(music);
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		if(musice) {
			setMusicVol(musicVol);
			musics.start();
		}
	}
	
}
