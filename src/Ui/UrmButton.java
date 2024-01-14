package Ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import Utilities.LoadSave;
import static Utilities.Constants.UI.UrmButtons.*;

public class UrmButton extends PauseButton {
	
	private  BufferedImage[] Imgs;
	private int rowIndex, index;
	private boolean MouseOver, MousePressed;

	public UrmButton(int x, int y, int width, int height, int rowIndex) {
		super(x, y, width, height);
		this.rowIndex = rowIndex;
		loadImgs();
	}
	
	private void loadImgs() {
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
		Imgs = new BufferedImage[3];
		for(int i = 0; i < Imgs.length; i++)
			Imgs[i] = temp.getSubimage(i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
		
	}

	public void update() {
		index = 0;
		if(MouseOver)
			index = 1;
		if(MousePressed)
			index = 2;
	}
	
	public void draw(Graphics g) {
		g.drawImage(Imgs[index], x, y, URM_SIZE, URM_SIZE, null);
	}
	
	public void resetBools() {
		MouseOver = false;
		MousePressed = false;
	}

	public boolean isMouseOver() {
		return MouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		MouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return MousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		MousePressed = mousePressed;
	}
}
