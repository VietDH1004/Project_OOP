package Ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import GameStates.Gamestate;
import Utilities.LoadSave;
import static Utilities.Constants.UI.Buttons.*;

public class MenuButton {
	private int xPos, yPos, rowIndex, index;
	private int xOffsetCenter = BUTTON_WIDTH / 2; 
	private Gamestate state;
	private BufferedImage[] Imgs;
	private boolean MouseOver, MousePressed;
	private Rectangle Bounds;
	
	public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.state = state;
		loadImgs();
		initBounds();
	}
	private void initBounds() {
		Bounds = new Rectangle(xPos - xOffsetCenter, yPos, BUTTON_WIDTH, BUTTON_HEIGHT);
	}
	private void loadImgs() {
		Imgs = new BufferedImage[3];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTON);
		for(int i = 0; i < Imgs.length; i++)
			Imgs[i] = temp.getSubimage(i * BUTTON_WIDTH_DEFAULT, rowIndex * BUTTON_HEIGHT_DEFAULT, BUTTON_WIDTH_DEFAULT, BUTTON_HEIGHT_DEFAULT);
	}
	
	public void draw(Graphics g) {
		g.drawImage(Imgs[index], xPos - xOffsetCenter, yPos, BUTTON_WIDTH, BUTTON_HEIGHT, null);
	}
	
	public void update() {
		index = 0;
		if(MouseOver)
			index = 1;
		if(MousePressed)
			index = 2;
	}
	public boolean isMouseOver() {
		return MouseOver;
	}
	public void setMouseOver(boolean mouseOver) {
		this.MouseOver = mouseOver;
	}
	public boolean isMousePressed() {
		return MousePressed;
	}
	public void setMousePressed(boolean mousePressed) {
		this.MousePressed = mousePressed;
	}
	
	public Rectangle getBounds() {
		return Bounds;
	}
	
	public void applyGamestate() {
		Gamestate.state = state;
	}
	
	public void resetBools() {
		MouseOver = false;
		MousePressed = false;
	}
	
	public Gamestate getState() {
		return state;
	}
	
}