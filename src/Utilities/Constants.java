package Utilities;

import Main.Game;

public class Constants {
	
	public static final float GRAVITY = 0.04f * Game.SCALE;
	public static final int ANISPEED = 25;
	
	
	public static class Environment{
		public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
		public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
		public static final int BIG_CLOUD_WIDTH = (int)(BIG_CLOUD_WIDTH_DEFAULT * Game.SCALE);
		public static final int BIG_CLOUD_HEIGHT = (int)(BIG_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
		public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
		public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
		public static final int SMALL_CLOUD_WIDTH = (int)(SMALL_CLOUD_WIDTH_DEFAULT * Game.SCALE);
		public static final int SMALL_CLOUD_HEIGHT = (int)(SMALL_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
	}
	
	public static class UI{
		public static class Buttons{
			public static final int BUTTON_WIDTH_DEFAULT = 140;
			public static final int BUTTON_HEIGHT_DEFAULT = 56;
			public static final int BUTTON_WIDTH = (int)(BUTTON_WIDTH_DEFAULT * Game.SCALE);
			public static final int BUTTON_HEIGHT = (int)(BUTTON_HEIGHT_DEFAULT * Game.SCALE);
		}
		public static class PauseButton{
			public static final int SOUND_SIZE_DEFAULT = 42;
			public static final int SOUND_SIZE = (int)(SOUND_SIZE_DEFAULT * Game.SCALE);
		}
		
		public static class UrmButtons{
			public static final int URM_DEFAULT_SIZE = 56;
			public static final int URM_SIZE = (int)(URM_DEFAULT_SIZE * Game.SCALE);
		}
		
		public static class VolumeButtons{
			public static final int VOLUME_DEFAULT_WIDTH = 28;
			public static final int VOLUME_DEFAULT_HEIGHT = 44;
			public static final int SLIDER_DEFAULT_WIDTH = 215;
			
			public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH * Game.SCALE);
			public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT * Game.SCALE);
			public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * Game.SCALE);
		}
		
	}
	
	public static class Directions {
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}

	public static class PlayerConstants {
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int JUMP = 2;
		public static final int FALLING = 3;
		public static final int HIT = 4;
		public static final int ATTACK = 5;
		public static final int DEFEAT = 6;
		
		
		public static int GetSpriteAmount(int player_action) {
			switch (player_action) {
			case RUNNING:
			case IDLE:
				return 4;
			case JUMP:
			case FALLING:
				return 2;
			case HIT:
				return 5;
			case ATTACK:
				return 4;
			case DEFEAT:
				return 4;
			default:
				return 1;
			}
		}
	}
	
	public static class EnemyConstants {
		public static final int PAINTER = 0;
		public static final int SKUNK = 1;
		public static final int BOSS = 2;
		
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int ATTACK = 2;
		public static final int HIT = 3;
		public static final int DEFEAT = 4;
		
		public static final int PAINTER_WIDTH_DEFAULT = 32;
		public static final int PAINTER_HEIGHT_DEFAULT = 40;
		public static final int PAINTER_WIDTH = (int)(PAINTER_WIDTH_DEFAULT * Game.SCALE);
		public static final int PAINTER_HEIGHT = (int)(PAINTER_HEIGHT_DEFAULT * Game.SCALE);
		public static final int PAINTER_DRAWOFFSET_X = (int)(10 * Game.SCALE);
		public static final int PAINTER_DRAWOFFSET_Y = (int)(6 * Game.SCALE);
		
		public static final int SKUNK_WIDTH_DEFAULT = 40;
		public static final int SKUNK_HEIGHT_DEFAULT = 40;
		public static final int SKUNK_WIDTH = (int)(SKUNK_WIDTH_DEFAULT * Game.SCALE);
		public static final int SKUNK_HEIGHT = (int)(SKUNK_HEIGHT_DEFAULT * Game.SCALE);
		public static final int SKUNK_DRAWOFFSET_X = (int)(4 * Game.SCALE);
		public static final int SKUNK_DRAWOFFSET_Y = (int)(8 * Game.SCALE);
		
		public static final int BOSS_WIDTH_DEFAULT = 40;
		public static final int BOSS_HEIGHT_DEFAULT = 48;
		public static final int BOSS_WIDTH = (int)(BOSS_WIDTH_DEFAULT * Game.SCALE);
		public static final int BOSS_HEIGHT = (int)(BOSS_HEIGHT_DEFAULT * Game.SCALE);
		public static final int BOSS_DRAWOFFSET_X = (int)(8 * Game.SCALE);
		public static final int BOSS_DRAWOFFSET_Y = (int)(13* Game.SCALE);
		
		public static int GetSpriteAmount(int enemy_type, int enemy_state) {
			
			switch (enemy_type) {
			case PAINTER:
				switch (enemy_state) {
				case IDLE:
					return 4;
				case RUNNING:
					return 4;
				case ATTACK:
					return 7;
				case HIT:
					return 4;
				case DEFEAT:
					return 4;
				}
			case SKUNK:
				switch (enemy_state) {
				case IDLE:
					return 7;
				case RUNNING:
					return 4;
				case ATTACK:
					return 9;
				case HIT:
					return 4;
				case DEFEAT:
					return 4;
				}
			case BOSS:
				switch (enemy_state) {
				case IDLE:
					return 8;
				case RUNNING:
					return 6;
				case ATTACK:
					return 7;
				case HIT:
					return 4;
				case DEFEAT:
					return 4;
				}
			}
			return 0;
			
		}
		
		public static int GetMaxHp(int enemy_type) {
			switch (enemy_type) {
			case PAINTER:
				return 20;
			case SKUNK:
				return 30;
			case BOSS:
				return 100;
				
			default:
				return 1;
			}
		}
		
		public static int GetEnemyDamage(int enemy_type){
			switch (enemy_type) {
			case PAINTER:
				return 5;
			case SKUNK:
				return 10;
			case BOSS:
				return 20;
			default:
				return 0;
			}
		}
	}
	
	public static class Projectiles{
		public static final int SHOOTER_BALL_DEFAULT_WIDTH = 15;
		public static final int SHOOTER_BALL_DEFAULT_HEIGHT = 15;
		
		public static final int SHOOTER_BALL_WIDTH = (int)(Game.SCALE * SHOOTER_BALL_DEFAULT_WIDTH);
		public static final int SHOOTER_BALL_HEIGHT = (int)(Game.SCALE * SHOOTER_BALL_DEFAULT_HEIGHT);
		public static final float SPEED = 1f * Game.SCALE;
	}
	
	
	
	public static class ObjectConstants {

		public static final int RED_POTION = 0;
		public static final int BLUE_POTION = 1;
		public static final int BARREL = 2;
		public static final int BOX = 3;
		public static final int STEPTRAP = 4;
		public static final int SHOOTER_LEFT = 5;
		public static final int SHOOTER_RIGHT = 6;

		public static final int RED_POTION_VALUE = 15;
		public static final int BLUE_POTION_VALUE = 10;

		public static final int CONTAINER_WIDTH_DEFAULT = 40;
		public static final int CONTAINER_HEIGHT_DEFAULT = 30;
		public static final int CONTAINER_WIDTH = (int) (Game.SCALE * CONTAINER_WIDTH_DEFAULT);
		public static final int CONTAINER_HEIGHT = (int) (Game.SCALE * CONTAINER_HEIGHT_DEFAULT);

		public static final int POTION_WIDTH_DEFAULT = 12;
		public static final int POTION_HEIGHT_DEFAULT = 16;
		public static final int POTION_WIDTH = (int) (Game.SCALE * POTION_WIDTH_DEFAULT);
		public static final int POTION_HEIGHT = (int) (Game.SCALE * POTION_HEIGHT_DEFAULT);

		
		public static final int SPIKE_WIDTH_DEFAULT = 32;
		public static final int SPIKE_HEIGHT_DEFAULT = 32;
		public static final int SPIKE_WIDTH = (int) (Game.SCALE * SPIKE_WIDTH_DEFAULT);
		public static final int SPIKE_HEIGHT = (int) (Game.SCALE * SPIKE_HEIGHT_DEFAULT);
		
		public static final int SHOOTER_WIDTH_DEFAULT = 40;
		public static final int SHOOTER_HEIGHT_DEFAULT = 26;
		public static final int SHOOTER_WIDTH = (int) (SHOOTER_WIDTH_DEFAULT * Game.SCALE);
		public static final int SHOOTER_HEIGHT = (int) (SHOOTER_HEIGHT_DEFAULT * Game.SCALE);

		
		public static int GetSpriteAmount(int object_type) {
			switch (object_type) {
			case RED_POTION, BLUE_POTION:
				return 7;
			case BARREL, BOX:
				return 8;
			case SHOOTER_LEFT, SHOOTER_RIGHT:
				return 7;
			}
			return 1;
		}
	}

}