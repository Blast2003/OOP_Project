package ui;

import static utilz.Constants.UI.PauseButtons.SOUND_SIZE;
import static utilz.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utilz.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import Main.Game;

public class AudioOptions {

	private SoundButton musicButton, sfxButton; 
	private VolumeButton volumeButton;
	
	private Game game;
	
	public AudioOptions(Game game) {
		this.game = game;
		createSoundButtons();
		createVolumeButton();
	}
	
	private void createVolumeButton() {
		int vX = (int)(309*Game.SCALE);
		int vY = (int)(278*Game.SCALE);
		volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
		
	}
	
	private void createSoundButtons() {
		int soundX = (int)(450 * Game.SCALE);
		int musicY = (int)(140 * Game.SCALE);
		int sfxY = (int) (186 * Game.SCALE);
		
		musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
		sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
	}
	
	public void update() {
		
		// Sound Button
		musicButton.update();
		sfxButton.update();
		
		// volume button
		volumeButton.update();
	}
	
	public void draw(Graphics g) {
		
		//sound buttons
		musicButton.draw(g);
		sfxButton.draw(g);
		
		//volume button or slider
		volumeButton.draw(g);
	}
	
	public void mouseDragged(MouseEvent e) { // must change code at input mouse event
		if(volumeButton.isMousePressed()) {
			float valueBefore = volumeButton.getFloatValue();
			volumeButton.changeX(e.getX());
			float valueAfter = volumeButton.getFloatValue();
			if(valueBefore != valueAfter)
				game.getauAudioPlayer().setVolume(valueAfter);
		}
	}
	
	public void mousePressed(MouseEvent e) {
		if(isIn(e, musicButton))
			musicButton.setMousePressed(true);
		else if(isIn(e, sfxButton))
			sfxButton.setMousePressed(true);
		else if(isIn(e, volumeButton))
			volumeButton.setMousePressed(true);
		
	}

	public void mouseReleased(MouseEvent e) {
		if(isIn(e, musicButton)) {
			if(musicButton.isMousePressed()) {
				musicButton.setMuted(!musicButton.isMuted()); // default value of boolean is false
				game.getauAudioPlayer().toggleSongMute();
			}
		}
			
		else if(isIn(e, sfxButton)) {
			if(sfxButton.isMousePressed()) {
				sfxButton.setMuted(!sfxButton.isMuted());
				game.getauAudioPlayer().toggleEffectMute();
			}
		}
			
			
		musicButton.resetBools();
		sfxButton.resetBools();
		volumeButton.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		musicButton.setMouseOver(false);
		sfxButton.setMouseOver(false);
		volumeButton.setMouseOver(false);
		
		
		if(isIn(e, musicButton))
			musicButton.setMouseOver(true);
		else if(isIn(e, sfxButton))
			sfxButton.setMouseOver(true);
		else if(isIn(e, volumeButton))
			volumeButton.setMouseOver(true);
		
	}
	
	
	public boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(),e.getY());
	}
	
}
