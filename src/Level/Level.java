package Level;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static Utilities.Constants.EnemyConstants.*;
import static Utilities.Constants.ObjectConstants.*;
import Entities.*;
import Main.Game;
import Objects.GameContainer;
import Objects.Potion;
import Objects.Shooter;
import Objects.StepTrap;



public class Level {
	
	private int[][] lvlData;
	private BufferedImage img;
	
	private ArrayList<Painter> painters = new ArrayList<>();
	private ArrayList<Skunk> skunks = new ArrayList<>();
	private ArrayList<Boss> bosses = new ArrayList<>();
	private ArrayList<Potion> potions = new ArrayList<>();
	private ArrayList<StepTrap> steptraps = new ArrayList<>();
	private ArrayList<GameContainer> containers = new ArrayList<>();
	private ArrayList<Shooter> shooters = new ArrayList<>();
	
	
	private int lvlTilesWide;
	private int maxTilesOffset;
	private int maxLvlOffsetX;
	private Point playerSpawn;
	
	
	public Level(BufferedImage img) {
		this.img = img;
		lvlData = new int[img.getHeight()][img.getWidth()];
		loadLevel();
		calLvlOffsets();
	}
	
	private void loadLevel() {
		
		for (int y = 0; y < img.getHeight(); y++)
			for (int x = 0; x < img.getWidth(); x++) {
				Color c = new Color(img.getRGB(x, y));
				
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();
				
				loadLevelData(red, x, y);
				loadEntities(green, x, y);
				loadObjects(blue, x, y);
			}
	}
	
	private void loadLevelData(int redValue, int x, int y) {
		if (redValue >= 50)
			lvlData[y][x] = 0;
		else
			lvlData[y][x] = redValue;
	}
	
	private void loadEntities(int greenValue, int x, int y) {
		switch (greenValue) {
		case PAINTER -> painters.add(new Painter(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
		case SKUNK -> skunks.add(new Skunk(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
		case BOSS -> bosses.add(new Boss(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
		case 100 -> playerSpawn = new Point(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
		}
	}
	
	private void loadObjects(int blueValue, int x, int y) {
		switch (blueValue) {
		case RED_POTION, BLUE_POTION -> potions.add(new Potion(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
		case BOX, BARREL -> containers.add(new GameContainer(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
		case STEPTRAP -> steptraps.add(new StepTrap(x * Game.TILES_SIZE, y * Game.TILES_SIZE, STEPTRAP));
		case SHOOTER_LEFT, SHOOTER_RIGHT -> shooters.add(new Shooter(x * Game.TILES_SIZE, y * Game.TILES_SIZE, blueValue));
		}
	}

	private void calLvlOffsets() {
		lvlTilesWide = img.getWidth();
		maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
		maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
		
	}


	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}
	
	public int[][] getLevelData(){
		return lvlData;
	}
	
	public int getLvlOffset() {
		return maxLvlOffsetX;
	}
	
	public ArrayList<Painter> getPainters(){
		return painters;
	}
	
	public ArrayList<Skunk> getSkunks(){
		return skunks;
	}
	
	public ArrayList<Boss> getBosses(){
		return bosses;
	}
	
	public ArrayList<Potion> getPotions(){
		return potions;
	}
	
	public ArrayList<GameContainer> getContainers(){
		return containers;
	}

	public ArrayList<StepTrap> getStepTraps(){
		return steptraps;
	}
	
	public ArrayList<Shooter> getShooters(){
		return shooters;
	}
	
	public Point getPlayerSpawn() {
		return playerSpawn;
	}
	
	
}
