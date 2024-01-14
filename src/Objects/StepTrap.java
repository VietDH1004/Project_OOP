package Objects;

import Main.Game;

public class StepTrap extends GameObject{

	public StepTrap(int x, int y, int objType) {
		
		super(x, y, objType);
		initHitbox(32, 16);
		xDrawOffset = 0;
		yDrawOffset = (int)(10 * Game.SCALE);
		hitBox.y += yDrawOffset;
		
	}

}
