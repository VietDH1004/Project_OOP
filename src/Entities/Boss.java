package Entities;

import static Utilities.Constants.EnemyConstants.*;
import GameStates.Playing;

public class Boss extends Enemy {
	
	public Boss(float x, float y) {
		super(x, y, BOSS_WIDTH, BOSS_HEIGHT, BOSS);
		initHitbox(26, 30);
		initAtkBox(56, 30, 15);
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
				
				if(aniIndex == 5 && !atkChecked)
					checkPlayerHit(atkBox, playing.getPlayer());
					
				break;
			case HIT:
				
				break;
			}
		}
	}	
}
