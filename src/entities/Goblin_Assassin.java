package entities;

import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.CanMoveHere;
import static utilz.HelpMethods.IsFloor;
import gamestates.Playing;

public class Goblin_Assassin extends Enemy {

	
	public Goblin_Assassin(float x, float y) {
		super(x, y, GOBLIN_ASSASSIN_WIDTH, GOBLIN_ASSASSIN_HEIGHT, GOBLIN_ASSASSIN);
		initHitbox(19, 20);
		initAttackBox(30, 20, 30);
	}
	
	public void update(int[][] lvlData, Playing playing) {
		updateBehavior(lvlData, playing);
		updateAnimationTick();
		updateAttackBoxFlip();
	}

	private void updateBehavior(int[][] lvData, Playing playing) {
		if (firstUpdate)
			firstUpdateCheck(lvData);
		
		if(inAir)
			updateInAir(lvData);
		else {
			switch(state) {
			case STAND:
//				preRoll = true;
//				if (tickAfterRollInSTAND >= 120) {
					if (IsFloor(hitbox, lvData))
						newState(RUNNING);
					else
						inAir = true;
//					tickAfterRollInSTAND = 0;
//					tickSinceLastDmgToPlayer = 60;
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
				else if(aniIndex >= 3 && !attackChecked ) {
					checkEnemyHit(attackBox, playing.getPlayer());	
				}
				attackMove(lvData, playing);
				break;
				
			case HURT:
				if (aniIndex <= GetSpriteAmount(enemyType, state) - 3)
					pushBack(pushBackDir, lvData, 2f);
				updatePushBackDrawOffset();
				break;
			}
		}	
	}
	

	protected void attackMove(int[][] lvData, Playing playing) {
		float xSpeed = 0;

		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.x + xSpeed * 2, hitbox.y, hitbox.width, hitbox.height, lvData))
			if (IsFloor(hitbox, xSpeed * 2, lvData)) {
				hitbox.x += xSpeed * 2;
				return;
			}
		newState(STAND);
	}
	
	
}
