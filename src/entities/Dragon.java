package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.IsFloor;

import gamestates.Playing; 
public class Dragon extends Enemy {

	public Dragon(float x, float y) {
		super(x, y, DRAGON_WIDTH, DRAGON_HEIGHT, DRAGON);
		initHitbox(150, 30);
		initAttackBox(80, 25, 70);
	}
	
	public void update(int [][] lvData, Playing playing) {
		updateBehavior(lvData, playing);
		updateAnimationTick();
		updateAttackBoxFlip(); 
		
	}

	private void updateBehavior(int[][] lvData, Playing playing) {
		if(firstUpdate) 
			firstUpdateCheck(lvData);
		
		if(inAir) {
			updateInAir(lvData);
				
		} else{
			switch (state) {
			case STAND: 
				if(IsFloor(hitbox, lvData))
					newState(RUNNING);
				else 
					inAir = true;
				break;
				
			case RUNNING:
				if(canSeePlayer(lvData, playing.getPlayer())) {
					turnTowardsPlayer(playing.getPlayer());
					if(isPlayerCloseForAttack(playing.getPlayer()))
						newState(ATTACK);
				}
				move(lvData);
				break;
				
			case ATTACK:
				if(aniIndex == 0)
					attackChecked = false;
				else if(aniIndex == 5 && !attackChecked ) {
						checkEnemyHit(attackBox, playing.getPlayer());
				}
				break;
			
			case HURT:
				if (aniIndex <= GetSpriteAmount(enemyType, state) - 1)
					pushBack(pushBackDir, lvData, 2f);
				updatePushBackDrawOffset();
				break;
			}
		}
		
	}
	

}
