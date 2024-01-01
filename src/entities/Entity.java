package entities;

import java.awt.Color;
import java.awt.Graphics;
import static utilz.Constants.Directions.UP;
import static utilz.Constants.Directions.DOWN;
import static utilz.Constants.Directions.LEFT;
import java.awt.geom.Rectangle2D;
import static utilz.HelpMethods.CanMoveHere;

import Main.Game;

public abstract class Entity {
	protected float x, y; // only class extend can use
	protected int width, height;
	protected Rectangle2D.Float hitbox;
	protected int aniTick, aniIndex;
	protected int state;
	protected float airSpeed;
	protected boolean inAir = false;
	protected int pushBackDir;
	protected float pushDrawOffset;
	protected int pushBackOffsetDir = UP;
	
	// health property of player
	protected int maxHealth;
	protected int currentHealth;
	
	protected Rectangle2D.Float attackBox;
	protected float walkSpeed;
	
	public Entity(float x, float y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	protected void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}
	
	protected void drawHitbox(Graphics g, int xLvlOffset) {
		// For debugging the hit box
		g.setColor(Color.green);
		g.drawRect((int) (hitbox.x - xLvlOffset), (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}
	 
	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
	}
	
	protected void updatePushBackDrawOffset() {
		float speed = 0.5f;
		float limit = -30f;

		if (pushBackOffsetDir == UP) {
			pushDrawOffset -= speed;
			if (pushDrawOffset <= limit)
				pushBackOffsetDir = DOWN;
		} else {
			pushDrawOffset += speed;
			if (pushDrawOffset >= 0)
				pushDrawOffset = 0;
		}
	}
	
	
	protected void pushBack(int pushBackDir, int[][] lvData, float speedMulti) {
		float xSpeed = 0;
		if (pushBackDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.x + xSpeed * speedMulti, hitbox.y, hitbox.width, hitbox.height, lvData))
			hitbox.x += xSpeed * speedMulti;
		
	}
	
	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public int getState() {
		return state;
	}

	public int getAniIndex() {
		return aniIndex;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}
	
	protected void newState(int enemyState) {
		this.state = enemyState;
		 aniTick = 0;
		 aniIndex = 0;
	}

	public float getPushDrawOffset() {
		return pushDrawOffset;
	}

	public int getPushBackOffsetDir() {
		return pushBackOffsetDir;
	}
	
}
