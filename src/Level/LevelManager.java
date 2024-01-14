package Level;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import Main.Game;
import Utilities.LoadSave;

public class LevelManager {
	
	private Game game;
	private BufferedImage[] levelSprite;
	private ArrayList<Level> levels;
	private int lvlIndex = 0;
	
	public LevelManager(Game game){
		this.game = game;
		importOutsideSprites();
		levels = new ArrayList<>();
		buildAllLvs();
	}
	
	public void loadNextLevel() {
		Level newLevel = levels.get(lvlIndex);
		game.getPlaying().getEnemyManager().loadEnemies(newLevel);
		game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());	
		game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
		game.getPlaying().getObjectManager().loadObjects(newLevel);
	}
	
	private void buildAllLvs() {
		BufferedImage[] allLvls = LoadSave.GetLevels();
		for(BufferedImage img : allLvls)
			levels.add(new Level(img));
		
	}

	private void importOutsideSprites() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		levelSprite = new BufferedImage[48];
		for (int j = 0; j < 4; j++)
			for (int i = 0; i < 12 ; i++) {
				int index = j * 12 + i;
				levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
				
			}
	}

	public void draw(Graphics g, int lvlOffset) {
		
		for(int j = 0; j < Game.TILES_IN_HEIGHT; j++)
			for(int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
				int index = levels.get(lvlIndex).getSpriteIndex(i, j);
				int x = Game.TILES_SIZE * i - lvlOffset;
				int y = Game.TILES_SIZE * j;
				g.drawImage(levelSprite[index], x, y, Game.TILES_SIZE, Game.TILES_SIZE , null);
			}
		
	}
	
	public void update() {
		
	}
	
	public Level getCurrentLevel() {
		return levels.get(lvlIndex);
	}
	
	public int getLvlAmount() {
		return levels.size();
	}
	
	public int getLvlIndex() {
		return lvlIndex;
	}
	
	public void setLvlIndex(int lvlIndex) {
		this.lvlIndex = lvlIndex;
	}
}
