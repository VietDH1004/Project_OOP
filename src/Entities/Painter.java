package Entities;

import static Utilities.Constants.EnemyConstants.*;
import GameStates.Playing;

public class Painter extends Enemy {
	
	public Painter(float x, float y) {
		super(x, y, PAINTER_WIDTH, PAINTER_HEIGHT, PAINTER);
		initHitbox(14, 25);
		initAtkBox(44, 25, 15);
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
