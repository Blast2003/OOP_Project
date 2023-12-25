package gamestates;

import java.awt.event.MouseEvent;

import Main.Game;
import audio.AudioPlayer;
import ui.MenuButton;

public class State {
	
	protected Game game;
	
	//constructor
	public State(Game game) {
		this.game = game;
	}
	
	public boolean isIn(MouseEvent e, MenuButton mb) {
		return mb.getBounds().contains(e.getX(), e.getY());
	}
	
	public Game getGame() {
		return game;
	}
	
	public void setGameState(Gamestate state) {
		switch (state) {
		case MENU -> game.getauAudioPlayer().playSong(AudioPlayer.MENU_1); // lambda  {parameter -> body content}
		case PLAYING -> game.getauAudioPlayer().setLevelSong(game.getPlaying().getlevelManager().getLevelIndex());
		}
		
		Gamestate.state = state;
	}
}
