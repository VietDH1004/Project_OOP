package Utilities;


import java.awt.geom.Rectangle2D;
import Main.Game;
import Objects.Projectile;


public class HelpMethods {
	
	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
		if(!IsSolid(x, y, lvlData))
			if(!IsSolid(x + width, y + height, lvlData))
				if(!IsSolid(x + width, y, lvlData))
					if(!IsSolid(x, y + height, lvlData))
						return true;
		return false;
	}
	
	private static boolean IsSolid(float x, float y, int[][] lvlData) {
		int maxWidth = lvlData[0].length * Game.TILES_SIZE;
		if(x < 0 || x >= maxWidth)
			return true;
		if(y < 0 || y >= Game.GAME_HEIGHT)
			return true;
		
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;
		
		return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
	}
	
	public static boolean IsProjectileHittingWall(Projectile p, int[][] lvlData) {
		return IsSolid(p.gethitbox().x + p.gethitbox().width / 2, p.gethitbox().y + p.gethitbox().height / 2, lvlData);
		
	}
	
	public static boolean IsTileSolid(int Xtile, int Ytile, int[][] lvlData) {
		int value = lvlData[Ytile][Xtile];
		if(value >= 48 || value < 0 || value != 11)
			return true;
		return false;
	}
	
	public static float GetEntityXPosNextToWall(Rectangle2D.Float Hitbox, float xSpeed) {
		
		int currentTile = (int)(Hitbox.x / Game.TILES_SIZE);
		if( xSpeed > 0) {
			//Right
			int tileXPos = currentTile * Game.TILES_SIZE;
			int xOffset = (int)(Game.TILES_SIZE - Hitbox.width);
			return tileXPos + xOffset -1;
		} else {
			//Left
			return currentTile * Game.TILES_SIZE;
		}
	}
	
	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float Hitbox, float airSpeed) {
		int currentTile = (int)(Hitbox.y / Game.TILES_SIZE);
		if(airSpeed > 0) {
			//fall
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int)(Game.TILES_SIZE - Hitbox.height);
			return tileYPos + yOffset - 1;
		} else {
			//jump
			return currentTile * Game.TILES_SIZE;
		}
	}
	
	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		// pixel check
		if(!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)) 
			if(!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
				return false;
		return true;
	}
	
	
	
	public static boolean IsGround(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		if(xSpeed > 0)
			return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
		else
			return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
	}
	
	
	public static boolean ShooterSeePlayer(int[][] lvlData, Rectangle2D.Float hitboxfirst, Rectangle2D.Float hitboxsecond, int yTile) {
		int firstXTile =(int)(hitboxfirst.x / Game.TILES_SIZE);
		int secondXTile =(int)(hitboxsecond.x / Game.TILES_SIZE);
		
		if(firstXTile > secondXTile) 
			return TilesClear(secondXTile, firstXTile, yTile, lvlData);
		 else 
			return TilesClear(firstXTile, secondXTile, yTile, lvlData);
	}
	
	public static boolean TilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
		for(int i = 0; i < xEnd - xStart; i++) 
			if(IsTileSolid(xStart + i, y, lvlData))
				return false;
			return true;
	}
	
	public static boolean TilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
		if(TilesClear(xStart, xEnd, y, lvlData))
			for(int i = 0; i < xEnd - xStart; i++) {
				if(!IsTileSolid(xStart + i, y + 1, lvlData))
					return false;
		}
		return true;
	}
	
	
	public static boolean SightClear(int[][] lvlData, Rectangle2D.Float enemyBox, Rectangle2D.Float playerBox, int yTile) {
		int firstXTile = (int) (enemyBox.x / Game.TILES_SIZE);

		int secondXTile;
		if (IsSolid(playerBox.x, playerBox.y + playerBox.height + 1, lvlData))
			secondXTile = (int) (playerBox.x / Game.TILES_SIZE);
		else
			secondXTile = (int) ((playerBox.x + playerBox.width) / Game.TILES_SIZE);

		if (firstXTile > secondXTile)
			return TilesWalkable(secondXTile, firstXTile, yTile, lvlData);
		else
			return TilesWalkable(firstXTile, secondXTile, yTile, lvlData);
	}
	
}
