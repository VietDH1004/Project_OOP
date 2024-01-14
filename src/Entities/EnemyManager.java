package Entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import GameStates.Playing;
import Level.Level;
import Utilities.LoadSave;
import static Utilities.Constants.EnemyConstants.*;

public class EnemyManager {
	
	private Playing playing;
	private BufferedImage[][] painterArray, skunkArray, bossArray;
	private Level currentLvl;

	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}

	public void loadEnemies(Level level) {
		this.currentLvl = level;
	}

	public void update(int[][] lvlData, Player player) {
		boolean isEnemyActive = false;
		
		for(Painter p : currentLvl.getPainters())
			if(p.isActive()) {
				p.update(lvlData, playing);
				isEnemyActive = true;
			}
		for(Skunk s : currentLvl.getSkunks())
			if(s.isActive()) {
				s.update(lvlData, playing);
				isEnemyActive = true;
			}
		for(Boss b : currentLvl.getBosses())
			if(b.isActive()) {
				b.update(lvlData, playing);
				isEnemyActive = true;
			}
		
		if(!isEnemyActive)
			playing.setLvlComplete(true);
	}
	
	public void draw(Graphics g, int xLvlOffset) {
		drawPainter(g, xLvlOffset);
		drawSkunks(g, xLvlOffset);
		drawBoss(g, xLvlOffset);
	}

	private void drawPainter(Graphics g, int xLvlOffset) {
		for(Painter p : currentLvl.getPainters()) 
			if(p.isActive()) {
				g.drawImage(painterArray[p.getEnemyState()][p.getAniIndex()],
					(int)p.getHitbox().x - xLvlOffset - PAINTER_DRAWOFFSET_X + p.flipX(),
					(int)p.getHitbox().y - PAINTER_DRAWOFFSET_Y, PAINTER_WIDTH * p.flipW(), PAINTER_HEIGHT, null);
				p.drawHitbox(g, xLvlOffset);
				p.drawAtkBox(g, xLvlOffset);
		}
	}
	
	private void drawSkunks(Graphics g, int xLvlOffset) {
		for(Skunk s : currentLvl.getSkunks()) 
			if(s.isActive()) {
				g.drawImage(skunkArray[s.getEnemyState()][s.getAniIndex()],
					(int)s.getHitbox().x - xLvlOffset - SKUNK_DRAWOFFSET_X + s.flipX(),
					(int)s.getHitbox().y - SKUNK_DRAWOFFSET_Y, SKUNK_WIDTH * s.flipW(), SKUNK_HEIGHT, null);
				s.drawHitbox(g, xLvlOffset);
				s.drawAtkBox(g, xLvlOffset);
		}
	}
	
	private void drawBoss(Graphics g, int xLvlOffset) {
		for(Boss b : currentLvl.getBosses()) 
			if(b.isActive()) {
				g.drawImage(bossArray[b.getEnemyState()][b.getAniIndex()],
					(int)b.getHitbox().x - xLvlOffset - BOSS_DRAWOFFSET_X + b.flipX(),
					(int)b.getHitbox().y - BOSS_DRAWOFFSET_Y, BOSS_WIDTH * b.flipW(), BOSS_HEIGHT, null);
				b.drawHitbox(g, xLvlOffset);
				b.drawAtkBox(g, xLvlOffset);
		}
	}
	
	public void checkEnemyHit(Rectangle2D.Float atkBox) {
		for(Painter p : currentLvl.getPainters())
			if (p.isActive())
				if (p.getEnemyState() != DEFEAT && p.getEnemyState() != HIT)
					if (atkBox.intersects(p.getHitbox())) {
						p.hurt(10);
						return;
					}

		for(Skunk s : currentLvl.getSkunks())
			if (s.isActive())
				if (s.getEnemyState() != DEFEAT && s.getEnemyState() != HIT)
					if (atkBox.intersects(s.getHitbox())) {
						s.hurt(10);
						return;
					}
		for(Boss b : currentLvl.getBosses())
			if (b.isActive())
				if (b.getEnemyState() != DEFEAT && b.getEnemyState() != HIT)
					if (atkBox.intersects(b.getHitbox())) {
						b.hurt(10);
						return;
					}
	}

	private void loadEnemyImgs() {
		painterArray = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.PAINTER_SPRITE), 7, 5, PAINTER_WIDTH_DEFAULT, PAINTER_HEIGHT_DEFAULT);
		skunkArray = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.SKUNK_SPRITE), 9, 5, SKUNK_WIDTH_DEFAULT, SKUNK_HEIGHT_DEFAULT);
		bossArray = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.BOSS_SPRITE), 8, 5, BOSS_WIDTH_DEFAULT, BOSS_HEIGHT_DEFAULT);
		
	}
	
	private BufferedImage[][] getImgArr(BufferedImage atlas, int xSize, int ySize, int spriteW, int spriteH) {
		BufferedImage[][] tempArr = new BufferedImage[ySize][xSize];
		for (int j = 0; j < tempArr.length; j++)
			for (int i = 0; i < tempArr[j].length; i++)
				tempArr[j][i] = atlas.getSubimage(i * spriteW, j * spriteH, spriteW, spriteH);
		return tempArr;
	}
	
	public void resetEnemies() {
		for(Painter p : currentLvl.getPainters())
			p.resetEnemy();
		for(Skunk s : currentLvl.getSkunks())
			s.resetEnemy();
		for(Boss b : currentLvl.getBosses())
			b.resetEnemy();
	}
}
