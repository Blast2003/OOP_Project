package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import Main.Game;
import gamestates.Gamestate;
import gamestates.Playing;
import utilz.LoadSave;
import static utilz.Constants.UI.URMButton.*;

public class LevelCompletedOverLay {

	private Playing playing;
	private UrmButton menu, next;
	private BufferedImage img;
	private int bgX, bgY, bgW, bgH;
	
	public LevelCompletedOverLay( Playing playing) {
		this.playing = playing;
		initImg();
		initButtons();
	}

	private void initButtons() {
		int menuX = (int) (330 * Game.SCALE);
		int nextX = (int) (445 * Game.SCALE);
		int y = (int) (195 * Game.SCALE);
		next = new UrmButton(nextX, y, URM_SIZE, URM_SIZE, 0); // 0 = row index according to urm_buttons.png
		menu = new UrmButton(menuX, y, URM_SIZE, URM_SIZE, 2); // 2 = row index
		
	}

	private void initImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_IMG);
		bgW = (int) (img.getWidth() * Game.SCALE);
		bgH = (int) (img.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH/2 - bgW/2;
		bgY = (int)(75 * Game.SCALE);
		
	}
	
	public void update() {
		next.update();
		menu.update();
	}
	
	public void draw (Graphics g) {
		
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		
		g.drawImage(img, bgX, bgY, bgW, bgH, null);
		next.draw(g);
		menu.draw(g);
	}
	
	private boolean isIn(UrmButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}
	
	public void mouseMoved(MouseEvent e) {
		next.setMouseOver(false);
		menu.setMouseOver(false);
		
		if(isIn(menu, e))
			menu.setMouseOver(true);
		else if(isIn(next, e))
			next.setMouseOver(true);
	}
	
	public void mouseReleased(MouseEvent e) {
		if(isIn(menu, e)) {
			if(menu.isMousePressed())
			{
				playing.resetAll();
				playing.setGameState(Gamestate.MENU);
			}
		} else if(isIn(next, e)) 
			if(next.isMousePressed()) {
				playing.loadNextLevel();
				playing.getGame().getauAudioPlayer().setLevelSong(playing.getlevelManager().getLevelIndex());
			}
		
		menu.resetBools();
		next.resetBools();
	}
	
	public void mousePressed(MouseEvent e) {
		if(isIn(menu, e))
			menu.setMousePressed(true);
		else if(isIn(next, e))
			next.setMousePressed(true);
	}
	
}
