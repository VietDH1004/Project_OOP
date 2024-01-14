package Entities;

import static Utilities.Constants.EnemyConstants.*;
import static Utilities.HelpMethods.*;
import static Utilities.Constants.*;

import java.awt.geom.Rectangle2D;

import GameStates.Playing;

import static Utilities.Constants.Directions.*;

import Main.Game;


public abstract class Enemy extends Entity {
	
	protected int enemyType;
	protected boolean f_update = true;
	protected float walkSpeed = 0.4f * Game.SCALE;
	protected int walkDir = LEFT;
	protected int yTile;
	protected float atkDis = Game.TILES_SIZE;
	protected boolean active = true;
	protected boolean atkChecked;
	protected int atkBoxOffsetX;

	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		maxHp = GetMaxHp(enemyType);
		currentHp = maxHp;
		runSpeed = 0.4f * Game.SCALE;
	}
	
	protected void updateAtkBox() {
		atkBox.x = hitBox.x - atkBoxOffsetX;
		atkBox.y = hitBox.y;
	}

	protected void updateAtkBoxFlip() {
		if (walkDir == RIGHT)
			atkBox.x = hitBox.x + hitBox.width;
		else
			atkBox.x = hitBox.x - atkBoxOffsetX;

		atkBox.y = hitBox.y;
	}

	protected void initAtkBox(int w, int h, int attackBoxOffsetX) {
		atkBox = new Rectangle2D.Float(x, y, (int) (w * Game.SCALE), (int) (h * Game.SCALE));
		this.atkBoxOffsetX = (int) (Game.SCALE * attackBoxOffsetX);
	}
	
	protected void firstUpdCheck(int[][] lvlData) {
		if(!IsEntityOnFloor(hitBox, lvlData))
			inAir = true;
		f_update = false;
	}
	
	protected void inAirChecks(int[][] lvlData, Playing playing) {
		if (state != HIT && state != DEFEAT) {
			updateInAir(lvlData);
			playing.getObjectManager().checkFallTrap(this);
		}
	}
	
	protected void updateInAir(int[][] lvlData) {
		if(CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
			hitBox.y += airSpeed;
			airSpeed += GRAVITY;
		} else {
			inAir = false;
			hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, airSpeed);
			yTile = (int)(hitBox.y / Game.TILES_SIZE);
			}
	}
	
	protected void moving(int[][] lvlData) {
		float xSpeed = 0;
		
		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else 
			xSpeed = walkSpeed;
		
		if(CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, lvlData))
			if(IsGround(hitBox, xSpeed, lvlData)) {
				hitBox.x += xSpeed;
				return;
			}
		changDir();
	}
	
	protected void turnToPlayer(Player player) {
		if(player.hitBox.x > hitBox.x)
			walkDir = RIGHT;
		else 
			walkDir = LEFT;
	}
	
	protected boolean playerInSight(int[][] lvlData, Player player) {
		int playerYTile = (int)(player.getHitbox().y / Game.TILES_SIZE);
		if(playerYTile == yTile)
			if(playerInRange(player)) {
				if(SightClear(lvlData, hitBox, player.hitBox, yTile))
					return true;
			}
		return false;
	}
	
	private boolean playerInRange(Player player) {
		int absVal = (int)(Math.abs(player.hitBox.x - hitBox.x));
		return absVal <= atkDis * 4;
	}
	
	protected boolean playerClose(Player player) {
		int absVal = (int)(Math.abs(player.hitBox.x - hitBox.x));
		switch (enemyType) {
		case PAINTER -> {
			return absVal <= atkDis;
			}
		case SKUNK -> {
			return absVal <= atkDis;
			}
		case BOSS -> {
			return absVal <= atkDis;
			}
		}
		return false;
	}

	public void hurt(int amount) {
		currentHp -= amount;
		if(currentHp <= 0)
			newState(DEFEAT);
		else
			newState(HIT);
	}
	
	protected void checkPlayerHit(Rectangle2D.Float atkBox, Player player) {
		if(atkBox.intersects(player.hitBox))
			player.changeHp(-GetEnemyDamage(enemyType));
		atkChecked = true;
	}
	
	protected void updateAnimationTick() {
		aniTick++;
		if(aniTick >= ANISPEED) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(enemyType, state)) {
				aniIndex = 0;
				
				switch(state) {
				case ATTACK,HIT -> state = IDLE;
				case DEFEAT -> active = false;
				}
			}
		}
	}

	protected void changDir() {
		if(walkDir == LEFT)
			walkDir = RIGHT;
		else 
			walkDir = LEFT;
	}
	
	public void resetEnemy() {
		hitBox.x = x;
		hitBox.y = y;
		f_update = true;
		currentHp = maxHp;
		newState(IDLE);
		active = true;
		airSpeed = 0;
	}
	
	public int flipX() {
		if(walkDir == RIGHT)
			return width;
		else 
			return 0;
	}
	
	public int flipW() {
		if(walkDir == RIGHT)
			return -1;
		else 
			return 1;
		
	}

	
	public boolean isActive() {
		return active;
	}
	
}