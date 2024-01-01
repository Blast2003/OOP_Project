package utilz;

import Main.Game;

public class Constants {
	
	public static final float GRAVITY = 0.04f * Game.SCALE;
	public static final int ANI_SPEED = 25;
	
	public static class Projectiles{
		public static final int CANNON_BALL_DEFAULT_WIDTH = 15;
		public static final int CANNON_BALL_DEFAULT_HEIGHT = 15;
		
		public static final int CANNON_BALL_WIDTH =  (int) (CANNON_BALL_DEFAULT_WIDTH *Game.SCALE) ;
		public static final int CANNON_BALL_HEIGHT = (int) (CANNON_BALL_DEFAULT_HEIGHT *Game.SCALE);
		public static final float SPEED = 0.75f * Game.SCALE;
	}
	
	public static class ObjectConstants {

		public static final int RED_POTION = 0;
		public static final int BLUE_POTION = 1;
		public static final int BARREL = 2;
		public static final int BOX = 3;
		public static final int SPIKE = 4;
		public static final int CANNON_LEFT = 5;
		public static final int CANNON_RIGHT = 6;
		public static final int TREE = 7;

		public static final int RED_POTION_VALUE = 15;
		public static final int BLUE_POTION_VALUE = 15;

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
		
		public static final int TREE_WIDTH_DEFAUTL = 128;
		public static final int TREE_HEIGHT_DEFAUTL = 96;
		public static final int TREE_WIDTH = (int) (TREE_WIDTH_DEFAUTL *Game.SCALE);
		public static final int TREE_HEIGHT = (int) (TREE_HEIGHT_DEFAUTL *Game.SCALE);
		
		public static final int CANNON_WIDTH_DEFAULT = 40;
		public static final int CANNON_HEIGHT_DEFAULT = 26;
		public static final int CANNON_WIDTH = (int) (Game.SCALE * CANNON_WIDTH_DEFAULT);
		public static final int CANNON_HEIGHT = (int) (Game.SCALE * CANNON_HEIGHT_DEFAULT);
	
		public static int GetSpriteAmount(int object_type) {
			switch (object_type) {
			case RED_POTION, BLUE_POTION:
				return 7;
			case BARREL, BOX:
				return 8;
			case CANNON_LEFT, CANNON_RIGHT:
				return 7;
			}
			return 1;
		}
		
		public static int GetTreeOffSetX() {
			return (Game.TILES_SIZE / 2) - (TREE_WIDTH/2);
		}
		
		public static int GetTreeOffSetY() {
			return -TREE_HEIGHT + Game.TILES_SIZE*2;
		}
	}
	
	public static class EnemyConstants{
		public static final int GOBLIN_WORKER = 0; // Constant for  Enemy type
		public static final int GOBLIN_ASSASSIN= 1;
		public static final int DRAGON = 2;
		
		public static final int STAND = 0;
		public static final int RUNNING = 1;
		public static final int ATTACK = 2;
		public static final int HURT = 3;
		public static final int DEAD = 4;
		
		public static final int GOBLIN_WORKER_WIDTH_DEFAULT = 64;
		public static final int GOBLIN_WORKER_HEIGHT_DEFAULT = 64;
		public static final int GOBLIN_WORKER_WIDTH = (int) (GOBLIN_WORKER_WIDTH_DEFAULT  * Game.SCALE);
		public static final int GOBLIN_WORKER_HEIGHT = (int) (GOBLIN_WORKER_HEIGHT_DEFAULT  * Game.SCALE);
		public static final int GOBLIN_WORKER_DRAWOFFSET_X = (int)(22*Game.SCALE);
		public static final int GOBLIN_WORKER_DRAWOFFSET_Y = (int)(24*Game.SCALE);
		
		public static final int GOBLIN_ASSASSIN_WIDTH_DEFAULT = 86;
		public static final int GOBLIN_ASSASSIN_HEIGHT_DEFAULT = 64;
		public static final int GOBLIN_ASSASSIN_WIDTH = (int) (GOBLIN_ASSASSIN_WIDTH_DEFAULT * Game.SCALE);
		public static final int GOBLIN_ASSASSIN_HEIGHT = (int) (GOBLIN_ASSASSIN_HEIGHT_DEFAULT * Game.SCALE);
		public static final int GOBLIN_ASSASSIN_DRAWOFFSET_X = (int) (30 * Game.SCALE);
		public static final int GOBLIN_ASSASSIN_DRAWOFFSET_Y = (int) (40 * Game.SCALE);

		public static final int DRAGON_WIDTH_DEFAULT = 384;
		public static final int DRAGON_HEIGHT_DEFAULT = 192;	
		public static final int DRAGON_WIDTH = (int) (DRAGON_WIDTH_DEFAULT * Game.SCALE);
		public static final int DRAGON_HEIGHT = (int) (DRAGON_HEIGHT_DEFAULT * Game.SCALE);
		public static final int DRAGON_DRAWOFFSET_X = (int) (110 * Game.SCALE);
		public static final int DRAGON_DRAWOFFSET_Y = (int) (160 * Game.SCALE);
	
		
		public static int GetSpriteAmount (int enemy_type, int enemy_state) {
			switch(enemy_state) {
						
				case STAND: {
					if(enemy_type == GOBLIN_WORKER)
						return 7;
					else if (enemy_type == DRAGON)
						return 8;
					else if(enemy_type == GOBLIN_ASSASSIN)
						return 4;
				}
				case RUNNING:{
					if(enemy_type == GOBLIN_WORKER)
						return 8;
					else if (enemy_type == DRAGON)
						return 8;
					else if(enemy_type == GOBLIN_ASSASSIN)
						return 8;
				}
				case ATTACK:{
					if(enemy_type == GOBLIN_ASSASSIN)
						return 8;
					else if(enemy_type == GOBLIN_WORKER)
						return 5;
					else
						return 8;
				}
				case HURT:
					if(enemy_type == GOBLIN_WORKER)
						return 3;
					return 4;
				case DEAD:{
					if(enemy_type == GOBLIN_WORKER)
						return 4;
					else if(enemy_type == DRAGON)
						return 8;
					else if(enemy_type == GOBLIN_ASSASSIN)
						return 7;
				}		
			}
			return 0;
		}
		
		public static int GetMaxHealth(int enemy_type) {
			switch(enemy_type) {
			case GOBLIN_WORKER:
				return 10;
			case GOBLIN_ASSASSIN:
				return 10;
			case DRAGON:
				return 30;
				default: 
					return 2;
			}
		}
		
		public static int GetEnemyDmg(int enemy_type) {
			switch(enemy_type) {
			case GOBLIN_WORKER:
				return 15;
			case GOBLIN_ASSASSIN:
				return 20;
			case DRAGON:
				return 20;
			default: 
				return 0;
			}
		}
	}
	
	public static class Environment{
		public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
		public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
		
		public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
		public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
		
		public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * Game.SCALE);
		public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * Game.SCALE);

		public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * Game.SCALE);
		public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
		
	}

	public static class UI{
		
		public static class Buttons{
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
			public static final int B_WIDTH = (int)(B_WIDTH_DEFAULT * Game.SCALE);
			public static final int B_HEIGHT= (int)(B_HEIGHT_DEFAULT * Game.SCALE);
		}
		
		public static class PauseButtons{
			public static final int SOUND_SIZE_DEFAULT = 42;
			public static final int SOUND_SIZE = (int)(SOUND_SIZE_DEFAULT * Game.SCALE );
			
		}
		
		public static class URMButton{
			public static final int URM_DEFAULT_SIZE = 56;
			public static final int URM_SIZE = (int)(URM_DEFAULT_SIZE * Game.SCALE);
		}
		
		public static class VolumeButtons{
			public static final int VOLUME_DEFAULT_WIDTH = 28;
			public static final int VOLUME_DEFAULT_HEIGHT = 44;
			public static final int SLIDER_DEFAULT_WIDTH  = 215;
			
			
			public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
			public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
			public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);
		}
	}
	
	public static class Directions{
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;	
	}
	
	public static class PlayerConstants{
		public static final int STAND = 0;
		public static final int RUNNING = 1;
		public static final int  JUMP = 5;
		public static final int ATTACK_1= 3;
		public static final int ATTACK_2= 2;
		public static final int POWER_ATTACK = 4 ;
		public static final int FALLING= 6 ;
		public static final int HURT = 7;
		public static final int DEAD = 8;
		
		public static int GetSpriteAmount(int player_action) {
			switch(player_action) {
			case DEAD: // return column
				return 7;
			case RUNNING: 
				return 8;
			case STAND:
				return 13;
			case HURT:
				return 4;
			case JUMP:
				return 5;
			case ATTACK_1, ATTACK_2,POWER_ATTACK:
				return 10;
			case FALLING:
				default:
					return 1;
			}
		}
		
		
		
	}
	
}
