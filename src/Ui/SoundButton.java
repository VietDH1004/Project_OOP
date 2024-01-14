package Ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Utilities.LoadSave;
import static Utilities.Constants.UI.PauseButton.*;

public class SoundButton extends PauseButton{

	
	private BufferedImage[][] soundImgs;
	private boolean MouseOver, MousePressed;
	private boolean Muted;
	private int rowIndex, columnIndex;
	
 	public SoundButton(int x, int y, int width, int height) {
		super(x, y, width, height);
		
		loadSoundImgs();
	}
 	
	private void loadSoundImgs() {
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
		soundImgs = new BufferedImage[2][3];
		for(int j = 0; j < soundImgs.length; j++)
			for(int i = 0; i < soundImgs[j].length; i++)
				soundImgs[j][i] = temp.getSubimage(i * SOUND_SIZE_DEFAULT, j * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
		
	}
	public void update() {
		if(Muted)
			rowIndex = 1;
		else 
			rowIndex = 0;
			
		columnIndex = 0;
		if(MouseOver)
			columnIndex = 1;
		if(MousePressed)
			columnIndex = 2;
		
	}
	
	public void resetBools() {
		MouseOver = false;
		MousePressed = false;
	}
	
	public void draw(Graphics g) {
		g.drawImage(soundImgs[rowIndex][columnIndex], x, y, width, height, null);
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

	public boolean isMuted() {
		return Muted;
	}

	public void setMuted(boolean muted) {
		this.Muted = muted;
	}
	
}
