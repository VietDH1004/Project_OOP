package Ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import GameStates.Gamestate;
import GameStates.Playing;
import Main.Game;
import Utilities.LoadSave;
import static Utilities.Constants.UI.UrmButtons.*;

public class PauseOverlay {
	
	private Playing playing;
	private BufferedImage BackgroundImg;
	private int bgX, bgY, bgW, bgH;
	private SoundOptions soundOptions;
	private UrmButton menuButton, replayButton, unpauseButton;

	public PauseOverlay(Playing playing) {
		this.playing = playing;
		loadBackground();
		createUrmButtons();	
		soundOptions = playing.getGame().getSoundOptions();
	}
	
	private void createUrmButtons() {
		int menuX = (int)(313 * Game.SCALE);
		int replayX = (int)(387* Game.SCALE);
		int unpauseX = (int)(462 * Game.SCALE);
		int bY = (int)(325 * Game.SCALE);		
		
		 menuButton = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
		 replayButton = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
		 unpauseButton = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);	 
	}

	private void loadBackground() {
		BackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
		bgW = (int) (BackgroundImg.getWidth() * Game.SCALE);
		bgH = (int) (BackgroundImg.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (25 * Game.SCALE);
		
	}
	
	public void update() {
		menuButton.update();
		replayButton.update();
		unpauseButton.update();
		soundOptions.update();
	}	
	
	public void draw(Graphics g) {
		//background
		g.drawImage(BackgroundImg, bgX, bgY, bgW, bgH, null);
		//urm
		menuButton.draw(g);
		replayButton.draw(g);
		unpauseButton.draw(g);
		soundOptions.draw(g);
	}
	
	public void mouseDragged(MouseEvent e) {
		soundOptions.mouseDragged(e);
	}
	
	public void mousePressed(MouseEvent e) {
		if (isIn(e, menuButton))
			menuButton.setMousePressed(true);
		else if (isIn(e, replayButton))
			replayButton.setMousePressed(true);
		else if (isIn(e, unpauseButton))
			unpauseButton.setMousePressed(true);
		else 
			soundOptions.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(e, menuButton)) {
			if(menuButton.isMousePressed()) {
				playing.resetGame();
				playing.setGameState(Gamestate.MENU);
				playing.unPauseGame();
			}		
		} else if (isIn(e, replayButton)) {
			if(replayButton.isMousePressed()) {
				playing.resetGame();
				playing.unPauseGame();
			}	
		} else if (isIn(e, unpauseButton)) {
			if(unpauseButton.isMousePressed())
				playing.unPauseGame();;
		} else {
			soundOptions.mouseReleased(e);
		}
		menuButton.resetBools();
		replayButton.resetBools();
		unpauseButton.resetBools();	
	}
	
	public void mouseMoved(MouseEvent e) {
		menuButton.setMouseOver(false);
		replayButton.setMouseOver(false);
		unpauseButton.setMouseOver(false);
		
		if (isIn(e, menuButton)) 
			menuButton.setMouseOver(true);
		else if (isIn(e, replayButton)) 
			replayButton.setMouseOver(true);
		else if (isIn(e, unpauseButton)) 
			unpauseButton.setMouseOver(true);
		else 
			soundOptions.mouseMoved(e);
	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

}
