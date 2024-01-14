package Inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import GameStates.Gamestate;
import Main.GamePanel;

public class KeyBoardInputs implements KeyListener {

	private GamePanel gamePanel;

	public KeyBoardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyReleased(e);	
		default:
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (Gamestate.state) {
		case MENU:
			gamePanel.getGame().getMenu().keyPressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyPressed(e);	
			break;
		case OPTIONS:
			gamePanel.getGame().getGameOptions().keyPressed(e);
		default:
			break;
		}
	}
}