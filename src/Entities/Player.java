package Entities;

import static Utilities.Constants.PlayerConstants.*;
import static Utilities.Constants.*;	
import static Utilities.HelpMethods.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import Audio.SoundPlayer;
import GameStates.Playing;
import Main.Game;
import Utilities.LoadSave;


public class Player extends Entity {
	//Values
	private BufferedImage[][] animations;
	private boolean moving = false, attacking = false;
	private boolean left, right, jump;
	private int[][] lvlData;
	private float xDrawOffSet = 15 * Game.SCALE;
	private float yDrawOffSet = 6 * Game.SCALE;
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private int flipX = 0;
	private int flipW = 1;
	private boolean atkChecked;
	private Playing playing;
	//Status
	private BufferedImage statBarImg;
		//Hp
	private int statBarWidth = (int)(192 * Game.SCALE);
	private int statBarHeight = (int)(58 * Game.SCALE);
	private int statBarX = (int)(10 * Game.SCALE);
	private int statBarY = (int)(10 * Game.SCALE);
	private int hpBarWidth = (int)(150 * Game.SCALE);
	private int hpBarHeight = (int)(4 * Game.SCALE);
	private int hpBarXStart = (int)(34 * Game.SCALE);
	private int hpBarYStart = (int)(14 * Game.SCALE);
	private int hpWidth = hpBarWidth;
		//Mp
	private int energyBarWidth = (int)(104 * Game.SCALE);
	private int energyBarHeight = (int)(2 * Game.SCALE);
	private int energyBarXStart = (int)(44 * Game.SCALE);
	private int energyBarYStart = (int)(34 * Game.SCALE);
	private int energyWidth = energyBarWidth;
	private int energyMaxValue = 300;
	private int energyValue = energyMaxValue;
	//Special Atk
	private int yTile = 0;
	private boolean spAttackActive;
	private int spAttackTick;
	private int spGrowSpeed = 30;
	private int spGrowTick;
	
	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		this.state = IDLE;
		this.maxHp = 100;
		this.currentHp = maxHp;
		this.runSpeed = 1f * Game.SCALE;
		loadAnimations();
		initHitbox(20,20);
		initAtkBox();
		
	}
	
	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitBox.x = x;
		hitBox.y = y;
	}
	
	private void initAtkBox() {
		atkBox = new Rectangle2D.Float(x, y, (int)(15 * Game.SCALE), (int)(20 * Game.SCALE));
		resetAttackBox();
	}

	public void update() {
		updateHpBar();
		updatepowerBar();
		
		if(currentHp <= 0) {
			
			if(state != DEFEAT) {
				state = DEFEAT;
				aniTick = 0;
				aniIndex = 0;
				playing.setPlayerLosingHp(true);
				playing.getGame().getSoundPlayer().playEffect(SoundPlayer.DIE);
				
				if (!IsEntityOnFloor(hitBox, lvlData)) {
					inAir = true;
					airSpeed = 0;
				}
			} else if(aniIndex == GetSpriteAmount(DEFEAT) - 1 && aniTick >= ANISPEED - 1) {
				playing.setGameOver(true);
				playing.getGame().getSoundPlayer().stopSong();
				playing.getGame().getSoundPlayer().playEffect(SoundPlayer.GAMEOVER);
			} else
				updateAnimationTick();
			if (inAir)
				if (CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
					hitBox.y += airSpeed;
					airSpeed += GRAVITY;
				} else
					inAir = false;

			return;
		}
		
		updateAtkBox();
		
		updatePos();
		if(moving)
			checkPotionTake();
			checkTrapStep();
			yTile = (int)(hitBox.y / Game.TILES_SIZE);
			if(spAttackActive) {
				spAttackTick++;
				if(spAttackTick >= 35) {
					spAttackTick = 0;
					spAttackActive = false;
				}
			}
			
		if(attacking || spAttackActive)
			checkAtk();
		
		updateAnimationTick();
		setAnimation();
		
	}
	
	 private void checkTrapStep() {
		playing.checkTrapStep(this);
		
	}

	private void checkPotionTake() {
		playing.checkPotionTake(hitBox);
		
	}

	private void checkAtk() {
		if(atkChecked || aniIndex != 1)
			return;
		atkChecked = true;
		
		if(spAttackActive)
			atkChecked = false;
		
		playing.checkEnemyHit(atkBox);
		playing.checkObjectHit(atkBox);
		playing.getGame().getSoundPlayer().playAttackSound();
		
	}

	private void setAtkBoxRight() {
		atkBox.x = hitBox.x + hitBox.width + (int) (Game.SCALE * 3);
	}

	private void setAtkBoxLeft() {
		atkBox.x = hitBox.x - hitBox.width + (int) (Game.SCALE * 5);
	}

	private void updateAtkBox() {
		if (right && left) {
			if (flipW == 1) {
				setAtkBoxRight();
			} else {
				setAtkBoxLeft();
			}

		} else if (right || (spAttackActive && flipW == 1))
			setAtkBoxRight();
		else if (left || (spAttackActive && flipW == -1))
			setAtkBoxLeft();

		atkBox.y = hitBox.y + (Game.SCALE);
	}

	private void updateHpBar() {
		hpWidth = (int)((currentHp / (float)(maxHp)) * hpBarWidth);
	}
	
	private void updatepowerBar() {
		energyWidth = (int)((energyValue / (float) energyMaxValue) * energyBarWidth);
		spGrowTick++;
		if(spGrowTick >= spGrowSpeed) {
			spGrowTick = 0;
			changeEnergy(1);
		}
			
	}

	public void render(Graphics g, int lvlOffset) {
		 g.drawImage(animations[state][aniIndex], (int) (hitBox.x - xDrawOffSet) - lvlOffset + flipX, (int) (hitBox.y - yDrawOffSet), width * flipW, height, null);
		 drawHitbox(g, lvlOffset);
		 drawAtkBox(g, lvlOffset);
		 drawUI(g);
		}

	private void drawUI(Graphics g) {
		//background ui
		g.drawImage(statBarImg, statBarX, statBarY, statBarWidth, statBarHeight, null);
		//hp bar
		g.setColor(Color.red);
		g.fillRect(hpBarXStart + statBarX, hpBarYStart + statBarY, hpWidth, hpBarHeight);
		//power bar
		g.setColor(Color.yellow);
		g.fillRect(energyBarXStart + statBarX, energyBarYStart + statBarY, energyWidth, energyBarHeight);
	}

	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANISPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(state)) {
				aniIndex = 0;
				attacking = false;
				atkChecked = false;
				if (state == HIT) {
					newState(IDLE);
					airSpeed = 0f;
					if (!IsGround(hitBox, 0, lvlData))
						inAir = true;
				}
			}
		}
	}

	private void setAnimation() {
		
		int startAni = state;
		
		if(state == HIT)
			return;
		
		if (moving)
			state = RUNNING;
		else
			state = IDLE;
		
		if(inAir) {
			if(airSpeed < 0)
				state = JUMP;
			else
				state = FALLING;
		}
		
		if(spAttackActive) {
			state = ATTACK;
			aniIndex = 1;
			aniTick = 0;
			return;
		}
		
		if(attacking) {
			state = ATTACK;
			
		}
		
		if (startAni != state)
			resetAniTick();
		
		}
	private void resetAniTick() {
			aniTick = 0;
			aniIndex = 0;
	}

	private void updatePos() {
		
		moving = false;
		
		if(jump)
			Jump();
		
		if(!inAir)
			if(!spAttackActive)
				if((!left && !right) || (right && left))
					return;
	
		float xSpeed = 0;
		
		if(left && !right) {
			xSpeed -= runSpeed;
			flipX = width;
			flipW = -1;
		}
		
		if (right && !left) {
			xSpeed += runSpeed;
			flipX = 0;
			flipW = 1;
		}
		
		if(spAttackActive) {
			if((!left && !right) || (left && right)){
					if(flipW == -1)
						xSpeed = -runSpeed;
					else 
						xSpeed = runSpeed;
				}
			
			xSpeed *= 2;
		}
		
		if(!inAir) {
			if(!IsEntityOnFloor(hitBox, lvlData))
				inAir = true;
		}

		if(inAir && !spAttackActive) {
			
			if(CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, lvlData)) {
				hitBox.y += airSpeed;
				airSpeed += GRAVITY;
				updateXPos(xSpeed);
			} else { 
				hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, airSpeed);
				if(airSpeed > 0) {
					resetInAir();
				} else {
					airSpeed = fallSpeedAfterCollision;
				}
				updateXPos(xSpeed);
			}
		} else {
			updateXPos(xSpeed);
		}
		moving = true;
	}
	
	private void Jump() {
		if(inAir)
			return;
		playing.getGame().getSoundPlayer().playEffect(SoundPlayer.JUMP);
		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		if(CanMoveHere(hitBox.x + xSpeed,hitBox.y , hitBox.width, hitBox.height, lvlData)) {
		hitBox.x += xSpeed;
		} else {
			hitBox.x = GetEntityXPosNextToWall(hitBox, xSpeed);
			if(spAttackActive) {
				spAttackActive = false;
				spAttackTick = 0;
			}
		}
	}
	
	public void changeHp(int value) {
		if (value < 0) {
			if (state == HIT)
				return;
			else
				newState(HIT);
		}
		currentHp += value;
		currentHp = Math.max(Math.min(currentHp, maxHp), 0);
	}
	
	public void instakill() {
		currentHp = 0;
	}
	
	public void changeEnergy(int value) {
		energyValue += value;
		energyValue = Math.max(Math.min(energyValue, energyMaxValue), 0);
	}
	
	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
		animations = new BufferedImage[7][5];
		for (int j = 0; j < animations.length; j++)
			for (int i = 0; i < animations[j].length; i++)
				animations[j][i] = img.getSubimage(i * 56, j * 32, 56, 32);
		
		statBarImg = LoadSave.GetSpriteAtlas(LoadSave.STAT_BAR);
}

	
	public void loadLvlData(int [][] lvlData) {
		this.lvlData = lvlData;
		if(!IsEntityOnFloor(hitBox, lvlData))
			inAir = true;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
	}
	
	public void setAttacking(boolean attacking){
		this.attacking = attacking;
	}
	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}
	
	public void setJump(Boolean jump) {
		this.jump = jump;
	}
	
	public int getYTile() {
		return yTile;
	}
	
	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		airSpeed = 0f;
		state = IDLE;
		currentHp = maxHp;
		spAttackActive = false;
		spAttackTick = 0;
		energyValue = energyMaxValue;
		
		hitBox.x = x;
		hitBox.y = y;
		
		resetAttackBox();
		
		if(!IsEntityOnFloor(hitBox, lvlData))
			inAir = true; 
	}

	public void spAttack() {
		if(spAttackActive)
			return;
		if(energyValue >= 60) {
			spAttackActive = true;
			changeEnergy(-100);
		}
	}

	private void resetAttackBox() {
		if (flipW == 1)
			setAtkBoxRight();
		else
			setAtkBoxLeft();
	}
}