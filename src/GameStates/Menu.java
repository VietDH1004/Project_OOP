package GameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import Main.Game;
import Ui.MenuButton;
import Utilities.LoadSave;

public class Menu extends State implements StateMethods {
	
	private MenuButton[] buttons = new MenuButton[3];
	private BufferedImage BackgroundImg, Startbackground;
	private int menuX, menuY, menuWidth, menuHeight;

	public Menu(Game game) {
		super(game);
		loadButtons();
		loadBackground();
		Startbackground = LoadSave.GetSpriteAtlas(LoadSave.START_MENU_BACKGROUND);
	}

	private void loadBackground() {
		BackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
		menuWidth = (int) (BackgroundImg.getWidth() * Game.SCALE);
		menuHeight = (int) (BackgroundImg.getHeight() * Game.SCALE);
		menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
		menuY = (int)(60 * Game.SCALE);
		
	}

	private void loadButtons() {
		buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (165 * Game.SCALE), 0, Gamestate.PLAYING);
		buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (235 * Game.SCALE), 1, Gamestate.OPTIONS);
		buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (305 * Game.SCALE), 2, Gamestate.QUIT);
		
	}

	@Override
	public void update() {
		for(MenuButton mb: buttons)
			mb.update();
		
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(Startbackground,0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(BackgroundImg, menuX, menuY, menuWidth, menuHeight, null);
		
		for(MenuButton mb: buttons)
			mb.draw(g);
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		for(MenuButton mb: buttons) {
			if(IsIn(e, mb))
				mb.setMousePressed(true);
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for(MenuButton mb: buttons) {
			if(IsIn(e, mb)) {
				if(mb.isMousePressed())
					mb.applyGamestate();
				if(mb.getState() == Gamestate.PLAYING)
					game.getSoundPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
				break;
			}
		}
		resetButtons();
	}
	

	private void resetButtons() {
		for(MenuButton mb: buttons)
			mb.resetBools();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for(MenuButton mb: buttons)
			mb.setMouseOver(false);
		
		for(MenuButton mb: buttons)
			if(IsIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
			Gamestate.state = Gamestate.PLAYING;
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}
}
