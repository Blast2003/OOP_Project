package entities;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.*;
import static utilz.HelpMethods.*;


import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.*;
import Main.Game;

public abstract class Enemy extends Entity {

	protected int enemyType;
	protected boolean firstUpdate = true;
	protected int walkDir = LEFT; 
	protected int tileY;
	protected float attackDistance = Game.TILES_SIZE;
	protected boolean active = true;
	protected boolean attackChecked;
	protected int attackBoxOffSetX;
	
	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		
		maxHealth = GetMaxHealth(enemyType);
		currentHealth = maxHealth;
		walkSpeed = Game.SCALE * 0.35f;
	}
	
	// use in assassin
	protected void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffSetX;
		attackBox.y = hitbox.y; 
	}
	
	
	// only use in shark
	protected void updateAttackBoxFlip() { 
		if(walkDir == RIGHT)
			attackBox.x = hitbox.x + hitbox.width -15;
		else
			attackBox.x = hitbox.x - attackBoxOffSetX;
		
		attackBox.y = hitbox.y; 
	}
	
	
	protected void initAttackBox(int w, int h, int attackBoxOffSetX) {
		attackBox = new Rectangle2D.Float(x, y, (int)(w*Game.SCALE), (int) (h*Game.SCALE)); // 30 + 22 + 30 = 82
		this.attackBoxOffSetX = (int) (Game.SCALE * attackBoxOffSetX);
	}
	
	
	
	protected void firstUpdateCheck(int [][] lvData) {
		if(!IsEntityOnFloor(hitbox, lvData)) 
			inAir = true;
		firstUpdate = false;
	}
	
	protected void updateInAir(int [][] lvData) {
		if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvData)) {
			hitbox.y += airSpeed;
			airSpeed += GRAVITY;
		} else {
			inAir = false;
			hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
			tileY = (int)(hitbox.y /Game.TILES_SIZE);
		}
	}
	
	protected void move(int [][] lvData) {
		float xSpeed = 0;
		
		if (firstUpdate) 
			firstUpdateCheck(lvData);

		if (inAir) 
			updateInAir(lvData);
		else {
			switch (state) {
			case STAND:
				state = RUNNING;
				break;
			case RUNNING:

				if (walkDir == LEFT)
					xSpeed = -walkSpeed;
				else
					xSpeed = walkSpeed;

				if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvData))
					if (IsFloor(hitbox, xSpeed, lvData)) {
						hitbox.x += xSpeed;
						return;
					}

				changeWalkDir();

				break;
			}
		}
	}
	
	protected void turnTowardsPlayer(player player) {
		if(player.hitbox.x > hitbox.x)
			walkDir = RIGHT;
		else 
			walkDir = LEFT;
	}

	protected boolean canSeePlayer(int [][] lvData, player player) {
		int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
		// fix bug player locates at the top position with enemy
		if(playerTileY == tileY)
			if (isPlayerInRange(player)) {
				if (IsSightClear(lvData, hitbox, player.hitbox, tileY)) // HURT box = enemy HURT box
					return true;
			}	
		return false;
	}
	
	protected boolean isPlayerInRange(player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		return absValue <=  attackDistance * 7;
	}
	
	protected boolean isPlayerCloseForAttack(player player) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		switch (enemyType) {
		case GOBLIN_WORKER ->{
			return absValue <= attackDistance;
		}
		case DRAGON ->{
			if(walkDir == RIGHT)
				return absValue <= attackDistance * 6;
			return absValue <= attackDistance * 2;
		}
		case GOBLIN_ASSASSIN ->{
			if(walkDir == RIGHT)
				return absValue <= attackDistance * 3;
			return absValue <= attackDistance * 2;
		}
		}
		return false;
	}
	
	public void hurt(int amount) {
		currentHealth -= amount;
		if(currentHealth <= 0)
			newState(DEAD);
		else 
			newState(HURT);
	}
	
	protected void checkEnemyHit(Rectangle2D.Float attackBox, player player) {
		if(attackBox.intersects(player.hitbox))
			player.changeHealth(-GetEnemyDmg(enemyType),this);
		
		attackChecked = true;
	}
	
	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(enemyType, state)) {
				if (enemyType == GOBLIN_WORKER || enemyType == DRAGON) {
					aniIndex = 0;
					switch (state) {
					case ATTACK, HURT -> state = STAND;
					case DEAD -> active = false;
					}
				} else if (enemyType == GOBLIN_ASSASSIN) {
					if (state == ATTACK)
						aniIndex = 3;
					else {
						aniIndex = 0;
						if(state == HURT) {
							state = STAND;
						} else if(state == DEAD)
							active = false;
					}
				}
			}
		}
	}			
	
	
	
	protected void changeWalkDir() {
		if(walkDir == LEFT)
			walkDir = RIGHT;
		else 
			walkDir = LEFT;
		
	}
	
	public void resetEnemy() {
		aniIndex = 0;
		aniTick =0;
		hitbox.x = x; 
		hitbox.y = y;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(STAND);
		active = true;
		airSpeed = 0;
	}
	
	public int flipX() {
		if(walkDir == RIGHT)
			return width;
		else 
			return 0;
	}
	
	public int flipW() {
		if(walkDir == RIGHT)
			return -1;
		else {
			return 1;
		}
	}
	
	public boolean isActive() { // getter
		return active;
	}
	
	public float getPushDrawOffset() {
		return pushDrawOffset;
	}
	
}
