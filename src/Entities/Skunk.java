package Entities;

import static Utilities.Constants.EnemyConstants.*;
import GameStates.Playing;

public class Skunk extends Enemy {

	public Skunk(float x, float y) {
		super(x, y, SKUNK_WIDTH, SKUNK_HEIGHT, SKUNK);
		initHitbox(30, 26);
		initAtkBox(54, 26, 12);
	}
	
	public void update(int[][] lvlData, Playing playing) {
		updateBehave(lvlData, playing);
		updateAnimationTick();
		updateAtkBox();
		
	} 
	
	private void updateBehave(int[][] lvlData, Playing playing) {
		if(f_update)
			firstUpdCheck(lvlData);
		
		if(inAir) 
			updateInAir(lvlData);
		 else {
			switch (state) {
			case IDLE:
				newState(RUNNING);
				break;
			case RUNNING:
				if(playerInSight(lvlData, playing.getPlayer())) {
					turnToPlayer(playing.getPlayer());
					if(playerClose(playing.getPlayer()))
						newState(ATTACK);
				}
				moving(lvlData);
				break;
			case ATTACK:
				if(aniIndex == 0)
					atkChecked = false;
				
				if(aniIndex == 4 && !atkChecked)
					checkPlayerHit(atkBox, playing.getPlayer());
					
				break;
			case HIT:
				
				break;
			}
		}
	}	
}
