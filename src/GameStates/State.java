package GameStates;

import java.awt.event.MouseEvent;

import Audio.SoundPlayer;
import Main.Game;
import Ui.MenuButton;

public class State {

	protected Game game;
	
	public State(Game game) {
		this.game = game;	
	}
	
	public boolean IsIn(MouseEvent e, MenuButton mb) {
		return mb.getBounds().contains(e.getX(), e.getY());
	}
	
	public Game getGame() {
		return game;
	}
	
	public void setGameState(Gamestate state) {
		switch(state) {
			case MENU -> game.getSoundPlayer().playSong(SoundPlayer.MENU_1);		
			case PLAYING -> game.getSoundPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
		}
		
		Gamestate.state = state;
	}
	
	
	
	
	
	
}
