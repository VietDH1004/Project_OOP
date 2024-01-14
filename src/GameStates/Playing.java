package GameStates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import Entities.EnemyManager;
import Entities.Player;
import Level.LevelManager;
import Main.Game;
import Objects.ObjectManager;
import Ui.GameEndOverlay;
import Ui.LvlComOverlay;
import Ui.PauseOverlay;
import Utilities.LoadSave;
import static Utilities.Constants.Environment.*;

public class Playing extends State implements StateMethods{
	
	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private PauseOverlay pauseOverlay;
	private GameEndOverlay gameEndOverlay;
	private LvlComOverlay lvlComOverlay;
	
	private boolean Pausing = false;
	
	private int xLvlOffset;
	private int LeftBorder = (int)(0.2 * Game.GAME_WIDTH);
	private int RightBorder = (int)(0.8 * Game.GAME_WIDTH);
	private int maxLvlOffsetX;
	
	private BufferedImage BackgroundImg, BigCloud, SmallCloud;
	private int[] smallCloudPos;
	private Random RND = new Random();
	
	private boolean gameOver;
	private boolean lvlComplete;
	private boolean playerHploss;
	
	public Playing(Game game) {
		super(game);
		initClasses();
		BackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND);
		BigCloud = LoadSave.GetSpriteAtlas(LoadSave.LARGE_CLOUD);
		SmallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUD);
		smallCloudPos = new int[8];
		for(int i = 0; i < smallCloudPos.length; i++)
			smallCloudPos[i] = (int)(90 * Game.SCALE) + RND.nextInt((int) (100 * Game.SCALE));
		
		calLvlOffset();
		loadStartLvl();	
		}
	
	public void loadNextLvl() {
		levelManager.setLvlIndex(levelManager.getLvlIndex() + 1);
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		resetGame();
	}
	
	private void loadStartLvl() {
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
		
	}

	private void calLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();		
	}

	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);
		
		player = new Player(200, 200, (int) (56 * Game.SCALE), (int) (32 * Game.SCALE), this);
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		
		pauseOverlay = new PauseOverlay(this);
		gameEndOverlay = new GameEndOverlay(this);
		lvlComOverlay = new LvlComOverlay(this);
		
		}
	
	@Override
	public void update() {
		if (Pausing) {
			pauseOverlay.update();
		} else if (lvlComplete) {
			lvlComOverlay.update();
		} else if(gameOver){
			gameEndOverlay.update();
		} else if(playerHploss) {
			player.update();
		} else {
			levelManager.update();
			objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			player.update();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			checkBorder();
		}
	}

	private void checkBorder() {
		int playerX = (int) player.getHitbox().x;
		int dif = playerX - xLvlOffset;
		
		if(dif > RightBorder)
			xLvlOffset += dif - RightBorder;
		else if(dif < LeftBorder)
			xLvlOffset += dif - LeftBorder;
		
		xLvlOffset = Math.max(Math.min(xLvlOffset, maxLvlOffsetX), 0);
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(BackgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		drawClouds(g);
		
		levelManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset);
		objectManager.draw(g, xLvlOffset);
		
		if(Pausing) {
			g.setColor(new Color(0, 0, 0, 100));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		} else if(gameOver)
			gameEndOverlay.draw(g);
		else if (lvlComplete)
			lvlComOverlay.draw(g);
			
	}
	private void drawClouds(Graphics g) {
		
		for(int i = 0; i < 3; i++)
			g.drawImage(BigCloud, 0 + i * BIG_CLOUD_WIDTH - (int)(xLvlOffset * 0.3), (int)(204*Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
		
		for(int i = 0; i < smallCloudPos.length; i++)
		g.drawImage(SmallCloud, SMALL_CLOUD_WIDTH * 4 * i - (int)(xLvlOffset * 0.7), smallCloudPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
			
	}
	
	public void resetGame() {
		gameOver = false;
		Pausing = false;
		lvlComplete = false;
		playerHploss = false;
		
		player.resetAll();
		enemyManager.resetEnemies();
		objectManager.resetAllObject();
		
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	public void checkObjectHit(Rectangle2D.Float atkBox) {
		objectManager.checkObjectHit(atkBox);
		
	}
	
	public void checkEnemyHit(Rectangle2D.Float atkBox) {
		enemyManager.checkEnemyHit(atkBox);
	}

	public void checkPotionTake(Rectangle2D.Float hitBox) {
		objectManager.checkObjectTouched(hitBox);
	}
	
	public void checkTrapStep(Player p) {
		objectManager.checkTrapStepped(p);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(!gameOver && !lvlComplete)
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			player.setLeft(true);
			break;
		case KeyEvent.VK_D:
			player.setRight(true);
			break;
		case KeyEvent.VK_SPACE:
			player.setJump(true);
			break;
		case KeyEvent.VK_ESCAPE:
			Pausing = !Pausing;
			break;
			}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver && !lvlComplete) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			player.setLeft(false);
			break;
		case KeyEvent.VK_D:
			player.setRight(false);
			break;
		case KeyEvent.VK_SPACE:
			player.setJump(false);
			break;
			}
		}
	}
	public void mouseDragged(MouseEvent e) {
		if(!gameOver && !lvlComplete)
			if (Pausing)
				pauseOverlay.mouseDragged(e);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(!gameOver) {
			if(e.getButton() == MouseEvent.BUTTON1) 
				player.setAttacking(true);
			else if(e.getButton() == MouseEvent.BUTTON3)
				player.spAttack();
		}	
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (gameOver)
			gameEndOverlay.mousePressed(e);
		else if (Pausing)
			pauseOverlay.mousePressed(e);
		else if (lvlComplete)
			lvlComOverlay.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (gameOver)
			gameEndOverlay.mouseReleased(e);
		else if (Pausing)
			pauseOverlay.mouseReleased(e);
		else if (lvlComplete)
			lvlComOverlay.mouseReleased(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (gameOver)
			gameEndOverlay.mouseMoved(e);
		else if (Pausing)
			pauseOverlay.mouseMoved(e);
		else if (lvlComplete)
			lvlComOverlay.mouseMoved(e);
	}
	
	public void setLvlComplete(boolean lvlCompleted) {
		if (levelManager.getLvlIndex() + 1 >= levelManager.getLvlAmount()) {
			levelManager.setLvlIndex(0);
			levelManager.loadNextLevel();
			resetGame();
			return;
		}
		this.lvlComplete = lvlCompleted;
	}
	
	public void setMaxLvlOffset(int lvlOffset) {
		this.maxLvlOffsetX = lvlOffset;
	}
	
	public void unPauseGame() {
		Pausing = false;
	}

	public void windowFocusLost() {
		player.resetDirBooleans();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public EnemyManager getEnemyManager() {
		return enemyManager;
	}
	
	public ObjectManager getObjectManager() {
		return objectManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public void setPlayerLosingHp(boolean playerHploss) {
		this.playerHploss = playerHploss;
		
	}
	
}
