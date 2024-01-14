package Objects;

import static Utilities.Constants.ANISPEED;
import static Utilities.Constants.ObjectConstants.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import Main.Game;

public class GameObject {
	protected int x, y, objectType;
	protected Rectangle2D.Float hitBox;
	protected boolean doAnimate, active = true;
	protected int aniTick, aniIndex;
	protected int xDrawOffset, yDrawOffset;
	
	public GameObject(int x, int y, int objType) {
		this.x = x;
		this.y = y;
		this.objectType = objType;
		
	}
	
	protected void updateAnimationTick() {
		aniTick++;
		if(aniTick >= ANISPEED) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(objectType)) {
				aniIndex = 0;
				if(objectType == BARREL || objectType == BOX) {
					doAnimate = false;
					active = false;
				} else if (objectType == SHOOTER_LEFT || objectType == SHOOTER_RIGHT)
					doAnimate = false;
			}
		}
	}
	
	public void resetObject() {
		aniIndex = 0;
		aniTick = 0;
		active = true;

		if(objectType == BARREL || objectType == BOX || objectType == SHOOTER_LEFT || objectType == SHOOTER_RIGHT) 
			doAnimate = false;
		else
			doAnimate = true;
	}
	
	protected void initHitbox(int width, int height) {
		hitBox = new Rectangle2D.Float(x, y,(int)(width * Game.SCALE), (int)(height * Game.SCALE));
	}
	
	public void drawHitbox(Graphics g, int xLvlOffset) {
		g.setColor(Color.pink);
		g.drawRect((int)hitBox.x - xLvlOffset, (int) hitBox.y, (int)hitBox.width,(int) hitBox.height);
	}

	public int getObjectType() {
		return objectType;
	}
	
	public Rectangle2D.Float getHitBox() {
		return hitBox;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setAnimation(boolean doAnimate) {
		this.doAnimate = doAnimate;
	}

	public int getxDrawOffset() {
		return xDrawOffset;
	}
	
	public int getyDrawOffset() {
		return yDrawOffset;
	}
	
	public int getAniIndex() {
		return aniIndex;
	}

	public int getAniTick() {
		return aniTick;
	}
}
