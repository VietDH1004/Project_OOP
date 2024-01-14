package Entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import Main.Game;

public abstract class Entity {
	
	protected float x, y;
	protected int width, height;
	protected Rectangle2D.Float hitBox;
	protected int aniTick, aniIndex;
	protected int state;
	protected float airSpeed;
	protected boolean inAir = false;
	protected int maxHp;
	protected int currentHp;
	protected Rectangle2D.Float atkBox;
	protected float runSpeed = 1.0f * Game.SCALE;
	
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	protected void drawHitbox(Graphics g, int xLvlOffset) {
		g.setColor(Color.BLACK);
		g.drawRect((int)hitBox.x - xLvlOffset, (int) hitBox.y, (int)hitBox.width,(int) hitBox.height);
	}

	protected void drawAtkBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int)atkBox.x - xLvlOffset, (int)atkBox.y, (int)atkBox.width, (int)atkBox.height);
	}
	
	protected void initHitbox(int width, int height) {
		hitBox = new Rectangle2D.Float(x, y,(int)(width * Game.SCALE), (int)(height * Game.SCALE));
	}
	
	public Rectangle2D.Float getHitbox() {
		return hitBox;
	}
	
	public int getEnemyState() {
		return state;
	}
	
	public int getAniIndex() {
		return aniIndex;
	}
	
	protected void newState(int state) {
		this.state = state;
		aniTick = 0;
		aniIndex = 0;
	}
}
