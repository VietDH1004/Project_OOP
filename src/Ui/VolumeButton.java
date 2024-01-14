package Ui;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Utilities.LoadSave;
import static Utilities.Constants.UI.VolumeButtons.*;

public class VolumeButton extends PauseButton{

	private BufferedImage[] Imgs;
	private BufferedImage Slider;
	private int index = 0;
	private boolean MouseOver, MousePressed;
	private int buttonX, minX, maxX;
	private float floatValue = 0f;
	
	public VolumeButton(int x, int y, int width, int height) {
		super(x + width / 2, y, VOLUME_WIDTH, height);
		bounds.x -= VOLUME_WIDTH / 2;
		buttonX = x + width / 2;
		this.x = x;
		this.width = width;
		minX = x + VOLUME_WIDTH / 2;
		maxX = x + width - VOLUME_WIDTH / 2;
		loadImgs();
	}

	private void loadImgs() {
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
		Imgs = new BufferedImage[3];
		for( int i = 0; i < Imgs.length; i++)
			Imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
		
		Slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
	}

	public void update() {
		index = 0;
		if(MouseOver)
			index = 1;
		if(MousePressed)
			index = 2;
	}
	
	public void draw(Graphics g) {
		g.drawImage(Slider, x, y, width, height, null);
		g.drawImage(Imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
	}
	
	public void changeX(int x) {
		if(x < minX)
			buttonX = minX;
		else if(x > maxX)
			buttonX = maxX;
		else 
			buttonX = x;
		updateFloatValue();
		bounds.x = buttonX - VOLUME_WIDTH / 2;
	}
	
	private void updateFloatValue() {
		float range = maxX - minX; 
		float value = buttonX - minX;
		floatValue = value / range;
		
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
	
	public float getFloatValue() {
		return floatValue;
	}
}
