package Objects;

import Main.Game;

public class Shooter extends GameObject {
	
	private int yTile;

	public Shooter(int x, int y, int objType) {
		super(x, y, objType);
		yTile = y / Game.TILES_SIZE;
		initHitbox(40, 26);
		hitBox.x -= (int)(4 * Game.SCALE);
		hitBox.y += (int)(6 * Game.SCALE);
	}
	
	public void update() {
		if(doAnimate)
			updateAnimationTick();
	}
	
	public int getYTile() {
		return yTile;
	}
}
