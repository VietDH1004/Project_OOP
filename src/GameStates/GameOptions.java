package GameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import Main.Game;
import Ui.SoundOptions;
import Ui.PauseButton;
import Ui.UrmButton;
import Utilities.LoadSave;
import static Utilities.Constants.UI.UrmButtons.*;

public class GameOptions extends State implements StateMethods {

	private SoundOptions soundOptions;
	private BufferedImage backgroundImg, optionsBackgroundImg;
	private int bgX, bgY, bgW, bgH;
	private UrmButton menuButton;
	
	public GameOptions(Game game) {
		super(game);
		loadImgs();
		loadButton();
		soundOptions = game.getSoundOptions();
	}

	private void loadButton() {
		
		int menuX = (int)(387 * Game.SCALE);
		int menuY = (int)(325 * Game.SCALE);
		
		menuButton = new UrmButton(menuX, menuY, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, 2);
	}

	private void loadImgs() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.START_MENU_BACKGROUND);
		optionsBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTION_MENU);
		
		bgW = (int) (optionsBackgroundImg.getWidth() * Game.SCALE);
		bgH = (int) (optionsBackgroundImg.getHeight() * Game.SCALE);
		bgX = Game.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (33 * Game.SCALE);
	}

	@Override
	public void update() {
		menuButton.update();
		soundOptions.update();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);
		
		menuButton.draw(g);
		soundOptions.draw(g);
	}

	public void MouseDragged(MouseEvent e) {
		soundOptions.mouseDragged(e);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(isIn(e, menuButton)) {
			menuButton.setMousePressed(true);
		} else {
			soundOptions.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(isIn(e, menuButton)) {
			if(menuButton.isMousePressed()) 
				Gamestate.state = Gamestate.MENU;
		} else 
			soundOptions.mouseReleased(e);
		
		menuButton.resetBools();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		menuButton.setMouseOver(false);
		if(isIn(e, menuButton))
			menuButton.setMouseOver(true);
		else 
			soundOptions.mouseMoved(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			Gamestate.state = Gamestate.MENU;
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}
}
