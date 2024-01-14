package Objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Entities.Enemy;
import Entities.Player;
import GameStates.Playing;
import Level.Level;
import Main.Game;
import Utilities.LoadSave;
import static Utilities.Constants.ObjectConstants.*;
import static Utilities.HelpMethods.*;
import static Utilities.Constants.Projectiles.*;


public class ObjectManager {

	private Playing playing;
	private BufferedImage[][] potionImgs, containerImgs;
	private BufferedImage[] shooterImgs;
	private BufferedImage stepTrapImg, shooterBulletImg;
	private ArrayList<Potion> potions;
	private ArrayList<GameContainer> containers;
	private ArrayList<Projectile> projectiles = new ArrayList<>();
	private Level currentLevel;
	
	public ObjectManager(Playing playing) {
		this.playing = playing;
		currentLevel = playing.getLevelManager().getCurrentLevel();
		loadImgs();
	}
	
	public void checkObjectTouched(Rectangle2D.Float hitBox) {
		for (Potion p : potions)
			if (p.isActive()) {
				if (hitBox.intersects(p.getHitBox())) {
					p.setActive(false);
					applyEffectToPlayer(p);
				}
			}
	}
	
	public void checkTrapStepped(Player p) {
		for(StepTrap s : currentLevel.getStepTraps())
			if(s.getHitBox().intersects(p.getHitbox()))
				p.instakill();
	}
	
	public void checkFallTrap(Enemy e) {
		for(StepTrap s : currentLevel.getStepTraps())
			if(s.getHitBox().intersects(e.getHitbox()))
				e.hurt(1000);
	}
	
	public void applyEffectToPlayer(Potion p) {
		if (p.getObjectType() == RED_POTION)
			playing.getPlayer().changeHp(RED_POTION_VALUE);
		else
			playing.getPlayer().changeEnergy(BLUE_POTION_VALUE);
	}
	
	public void checkObjectHit(Rectangle2D.Float atkbox) {
		for (GameContainer c : containers)
			if (c.isActive() && !c.doAnimate) {
				if (c.getHitBox().intersects(atkbox)) {
					c.setAnimation(true);
					int type = 0;
					if (c.getObjectType() == BARREL)
						type = 1;
					potions.add(new Potion((int) (c.getHitBox().x + c.getHitBox().width / 2),
							(int) (c.getHitBox().y - c.getHitBox().height / 3), type));
					return;
				}
			}
	}

	
	public void loadObjects(Level newLevel) {
		currentLevel = newLevel;
		potions = new ArrayList<>(newLevel.getPotions());
		containers = new ArrayList<>(newLevel.getContainers());
		projectiles.clear();
	}
	

	private void loadImgs() {
		BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
		potionImgs = new BufferedImage[2][7];
		
		for(int j = 0; j < potionImgs.length; j++)
			for(int i = 0; i < potionImgs[j].length; i++)
				potionImgs[j][i] = potionSprite.getSubimage(i*12, j*16, 12, 16);
		
		BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
		containerImgs = new BufferedImage[2][8];
		
		for(int j = 0; j < containerImgs.length; j++)
			for(int i = 0; i < containerImgs[j].length; i++)
				containerImgs[j][i] = containerSprite.getSubimage(i*40, j*30, 40, 30);
		
		stepTrapImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);
		
		shooterImgs = new BufferedImage[7];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SHOOTER_ATLAS);
		
		for(int i = 0; i < shooterImgs.length; i++)
			shooterImgs[i] = temp.getSubimage(i * 40, 0, 40, 26);
		
		shooterBulletImg = LoadSave.GetSpriteAtlas(LoadSave.SHOOTER_BALL);
		
	}
	
	public void update(int[][] lvlData, Player player) {
		
		for(Potion p : potions)
			if(p.isActive())
				p.update();
		
		for(GameContainer c : containers)
			if(c.isActive())
				c.update();
		
		updateShooters(lvlData, player);
		updateProjectiles(lvlData, player);
	}
	
	private void updateProjectiles(int[][] lvlData, Player player) {
		for(Projectile p : projectiles)
			if(p.isActive()) {
				p.updatePos();
				if(p.gethitbox().intersects(player.getHitbox())) {
					player.changeHp(-20);
					p.setActive(false);
				} else if (IsProjectileHittingWall(p, lvlData)) 
					p.setActive(false);
			}
	}

	private boolean playerInRange(Shooter s, Player player) {
		int absVal = (int)(Math.abs(player.getHitbox().x - s.getHitBox().x));
		return absVal <= Game.TILES_SIZE * 4;
	}
	
	private boolean playerInfontofShooter(Shooter s, Player player) {
		if(s.getObjectType() == SHOOTER_LEFT) {
			if(s.getHitBox().x > player.getHitbox().x)
				return true;
		} else if(s.getHitBox().x < player.getHitbox().x)
				return true;
		return false;
	}

	
	private void updateShooters(int[][] lvlData, Player player) {
		for(Shooter s : currentLevel.getShooters()) {
			if(!s.doAnimate)
				if(s.getYTile() == player.getYTile())
					if(playerInRange(s, player))
						if(playerInfontofShooter(s, player))
							if(ShooterSeePlayer(lvlData, player.getHitbox(), s.getHitBox(), s.getYTile())) 
								s.setAnimation(true);
													
			s.update();
			if(s.getAniIndex() == 4 && s.getAniTick() == 0)
				shootBullet(s);
		}
	}
	
	private void shootBullet(Shooter s) {
		int dir = 1;
		if(s.getObjectType() == SHOOTER_LEFT)
			dir = -1;
		
		projectiles.add(new Projectile((int)s.getHitBox().x, (int)s.getHitBox().y, dir));
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawPotions(g, xLvlOffset);
		drawContainers(g, xLvlOffset);
		drawStepTraps(g, xLvlOffset);
		drawShooters(g, xLvlOffset);
		drawProjectiles(g, xLvlOffset);
	}

	private void drawProjectiles(Graphics g, int xLvlOffset) {
		for(Projectile p : projectiles)
			if(p.isActive())
				g.drawImage(shooterBulletImg, (int)(p.gethitbox().x - xLvlOffset), (int)(p.gethitbox().y), SHOOTER_BALL_WIDTH, SHOOTER_BALL_HEIGHT, null);
		
	}

	private void drawShooters(Graphics g, int xLvlOffset) {
		for(Shooter s : currentLevel.getShooters()) {
			int x = (int)(s.getHitBox().x - xLvlOffset);
			int width = SHOOTER_WIDTH;
			
			if(s.getObjectType() == SHOOTER_RIGHT) {
				x += width;
				width *= -1;
			}
			g.drawImage(shooterImgs[s.getAniIndex()], x, (int)(s.getHitBox().y), width, SHOOTER_HEIGHT, null);
		}
	}

	private void drawStepTraps(Graphics g, int xLvlOffset) {
		for(StepTrap s : currentLevel.getStepTraps())
			g.drawImage(stepTrapImg,(int)s.getHitBox().x - xLvlOffset, (int)s.getHitBox().y - s.getyDrawOffset(), SPIKE_WIDTH, SPIKE_HEIGHT, null);
		
	}

	private void drawContainers(Graphics g, int xLvlOffset) {
		for(GameContainer c : containers)
			if(c.isActive()) {
				int type = 0;
				if(c.getObjectType() == BARREL) 
					type = 1;
				g.drawImage(containerImgs[type][c.getAniIndex()],
						(int) c.getHitBox().x - c.getxDrawOffset() - xLvlOffset,
						(int) c.getHitBox().y - c.getyDrawOffset(),
						CONTAINER_WIDTH, CONTAINER_HEIGHT, null);
			}
	}

	private void drawPotions(Graphics g, int xLvlOffset) {
		for(Potion p : potions)
			if(p.isActive()) {
				int type = 0;
				if(p.getObjectType() == RED_POTION) 
					type = 1;
				g.drawImage(potionImgs[type][p.getAniIndex()],(int) p.getHitBox().x - p.getxDrawOffset() - xLvlOffset,(int) p.getHitBox().y - p.getyDrawOffset(),POTION_WIDTH, POTION_HEIGHT, null);
			}
	}

	public void resetAllObject() {
		
		loadObjects(playing.getLevelManager().getCurrentLevel());		
		for(Potion p : potions)
			p.resetObject();
		for(GameContainer c : containers)
			c.resetObject();
		for(Shooter s : currentLevel.getShooters())
			s.resetObject();
	}	
}
