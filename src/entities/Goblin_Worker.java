package entities;

import static utilz.Constants.EnemyConstants.*;
import gamestates.Playing;
import static utilz.HelpMethods.IsFloor;


public class Goblin_Worker extends Enemy{

	
	public Goblin_Worker(float x, float y) { // call super and add new variable like overriding
		super(x, y, GOBLIN_WORKER_WIDTH, GOBLIN_WORKER_HEIGHT, GOBLIN_WORKER);
		initHitbox(27, 30);
		initAttackBox(30, 25, 30);
	}

	public void update(int [][] lvData, Playing playing ) {
		updateBehavior(lvData, playing);
		updateAnimationTick();
		updateAttackBoxFlip();
	}

	public void updateBehavior(int [][] lvData, Playing playing) {
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
				
				else if(aniIndex == 4 && !attackChecked ) {
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
