package entities;

import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.Directions.UP;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import Main.Game;
import Objects.Projectile;
import audio.AudioPlayer;
import gamestates.Playing;
import utilz.LoadSave;

public class player extends Entity {
	
	
	
	private BufferedImage[][] animations;
	private boolean moving = false, attacking = false;
	private boolean left, right, jump;
	private int[][] lvData;
	private float xDrawOffset = 21 * Game.SCALE;
	private float yDrawOffset = 30 * Game.SCALE;
	
	//Jumping and gravity
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	
	//Status bar UI
	private BufferedImage statusBarImg;
	
	// status property
	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);

	// health property
	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);
	private int healthWidth = healthBarWidth;
	
	// power property
	private int powerBarWidth = (int) (104 * Game.SCALE);
	private int powerBarHeight = (int) (2 * Game.SCALE);
	private int powerBarXStart = (int) (44 * Game.SCALE);
	private int powerBarYStart = (int) (34 * Game.SCALE);
	private int powerWidth = powerBarWidth;
	private int powerMaxValue = 200;
	private int powerValue = powerMaxValue;
	
	// change side of character
	private int flipX = 0;
	private int flipW = 1;
	
	private boolean attackChecked;
	private Playing playing;
	
	private int tileY = 0;
	
	private boolean powerAttackActive;
	private int powerAttackTick;
	private int powerGrowSpeed = 15;
	private int powerGrowTick;
	
	
	public player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		this.state = STAND;
		this.maxHealth = 150;
		this.currentHealth = maxHealth;
		this.walkSpeed = Game.SCALE * 1.0f;
		loadAnimations();
		initHitbox(15, 25);
		initAttackBox();
	}
	
	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (35 * Game.SCALE), (int) (17 * Game.SCALE));
		resetAttackBox();
	}

	public void update() {
		updateHealthBar();
		updatePowerBar();
		
		if(currentHealth <= 0) {
			if(state != DEAD) {
				state = DEAD;
				aniTick = 0;
				aniIndex = 0;
				playing.setPlayerDying(true);
				playing.getGame().getauAudioPlayer().playEffect(AudioPlayer.DIE);
				
				// Check if player died in air
				if (!IsEntityOnFloor(hitbox, lvData)) {
					inAir = true;
					airSpeed = 0;
				}
				
			} else if(aniIndex == GetSpriteAmount(DEAD) -1 && aniTick >= ANI_SPEED -1) {
				playing.setGameOver(true);
				playing.getGame().getauAudioPlayer().stopSong();
				playing.getGame().getauAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
			} else {
				
				updateAnimationTick();
				
				// Fall if in air
				if (inAir)
					if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvData)) {
						hitbox.y += airSpeed;
						airSpeed += GRAVITY;
					} else
						inAir = false;
			}
				

			return;
		}
			
		
		updateAttackBox();
		if (state == HURT) {
			if (aniIndex <= GetSpriteAmount(state) - 3)
				pushBack(pushBackDir, lvData, 1.25f);
			updatePushBackDrawOffset();
		} else
			updatePos();
		
		
		if(moving) {
			checkPotionTouched();
			checkSpikesTouched();
			checkInsideWater();
			tileY = (int) (hitbox.y / Game.TILES_SIZE);
			if(powerAttackActive) {
				powerAttackTick ++;
				if(powerAttackTick >= 50) {
					powerAttackTick = 0;
					powerAttackActive = false;
				}
			}
		}
		
		if(attacking || powerAttackActive)
			checkAttack();
		
		updateAnimationTick();
		setAnimation();
		
	}
	


	private void checkInsideWater() {
		if(IsEntityInWater(hitbox, playing.getlevelManager().getCurrentLevel().getLevelData()))
			currentHealth = 0;
	}

	private void checkSpikesTouched() {
		playing.checkSpikesTouched(this);
		
	}

	private void checkPotionTouched() {
		playing.checkPotionTouched(hitbox);
		
	}

	private void checkAttack() {
		if(attackChecked || aniIndex != 1)
			return; // false
		attackChecked = true;
		
		if(powerAttackActive)
			attackChecked = false;
		
		playing.checkPlayerHit(attackBox);
		playing.checkObjectHit(attackBox);
		playing.getGame().getauAudioPlayer().playAttackSound();
	}

	private void updateAttackBox() {
		if(right && left) {
			if(flipW ==1) {
				attackBox.x = hitbox.x + hitbox.width - (int) (Game.SCALE*5);
			} else {
				attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE*10);
			}
					
		} else if(right || (powerAttackActive && flipW == 1)) {
			attackBox.x = hitbox.x + hitbox.width - (int) (Game.SCALE*5);
		} else if(left || (powerAttackActive && flipW == -1)) {
			attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE*10);
		}
		attackBox.y = hitbox.y + (Game.SCALE * 10);
	}

	private void updateHealthBar() {
		healthWidth = (int)((currentHealth /  (float) maxHealth)* healthBarWidth); // make heal decrease ex : 50/100 * bar width	
	}
	
	private void updatePowerBar() {
		powerWidth = (int) ((powerValue / (float) powerMaxValue) *powerBarWidth);
		
		powerGrowTick ++;
		if(powerGrowTick >= powerGrowSpeed) {
			powerGrowTick = 0;
			changePower(1);
		}
	}
	

	public void render(Graphics g, int lvOffset) {
		// make the small HURT box 	
		g.drawImage(animations[state][aniIndex], 
				(int) (hitbox.x - xDrawOffset) - lvOffset + flipX, 
				(int) (hitbox.y - yDrawOffset - 2), 
				width * flipW, height, null); // width = negative => change the direction of charac
		
	//	drawHitbox(g, lvOffset);
		
	//	drawAttackBox(g,lvOffset);
		drawUI(g);
	}

	private void drawUI(Graphics g) {
		// Status bar
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.red);
		
		// Health bar
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight); // + off set
		
		// Power bar
		g.setColor(Color.blue);
		g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
	}

	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(state)) {
				aniIndex = 0;
				attacking = false;
				attackChecked = false;
				if (state == HURT) {
					newState(STAND);
					airSpeed = 0f;
					if (!IsFloor(hitbox, 0, lvData))
						inAir = true;
				}
			}
		}
	}
	
	private void setAnimation() {
		
		int startAni = state;
		
		if (state == HURT)
			return;
		
		if(moving)
			state = RUNNING;
		else 
			state = STAND;
		
		if(inAir) {
			if(airSpeed < 0)
				state = JUMP;
			else 
				state = FALLING;
		}
		
		if(powerAttackActive) {
			state = ATTACK_1;
			aniIndex = 1;
			aniTick =  0;
			return;
		}
		
		if(attacking) { // make one click to get attacking
			state = ATTACK_1;
			if(startAni != ATTACK_1) {
				aniIndex  = 1;
				aniTick = 0;
				return;
			}
		}
		
		if(startAni != state)
			resetAniTick();
	}
	

	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
	
		moving = false;
		
		if(jump)
			jump();
//		if(!left && !right && !inAir)
//			return;
		if(!inAir)
			if(!powerAttackActive) // false
				if((!left && !right) || (right && left))
					return;
			
		float xSpeed = 0;
		
		if(left && !right) {
			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1;
		}
			
		if(right && !left) {
			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1;
		}
		
		if(powerAttackActive) {
			if((!left && !right) || (left && right)) {
				if(flipW == -1)
					xSpeed = -walkSpeed;
				else 
					xSpeed = walkSpeed;
			}
			
			xSpeed *= 3;
		}
		
		if(!inAir)
			if(!IsEntityOnFloor(hitbox, lvData)) 
				inAir = true;
		
		if(inAir && !powerAttackActive) {
			if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvData)) {
				hitbox.y += airSpeed; 
				airSpeed += GRAVITY;
				updateXPos(xSpeed);
			} else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if(airSpeed > 0)
					resetInAir();
				else 
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}
			
		}else 
			updateXPos(xSpeed);
		moving = true;
	}	
		
		
	private void jump() {
		if(inAir)
			return;
		playing.getGame().getauAudioPlayer().playEffect(AudioPlayer.JUMP);
		inAir = true;
		airSpeed = jumpSpeed;
		
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;	
	}

	private void updateXPos(float xSpeed) {
		if(CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvData)) 
			hitbox.x += xSpeed;
		else { // have a space at the top left or top right corner
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
			if(powerAttackActive) {
				powerAttackActive = false;
				powerAttackTick = 0;
			}
		}
	}
		

	public void changeHealth(int value) {
		if(value < 0)
			if(state == HURT) {
				aniIndex  = 0;
				return;	
			} else 
				newState(HURT);
		
		currentHealth += value;
		
		if(currentHealth <= 0) {
			currentHealth = 0; // Game Over
		} else if(currentHealth >= maxHealth) 
			currentHealth = maxHealth;
		// 4 above code = currentHealth = Math.max(Math.min(maxHealth, currentHealth),0)
	}
	
	public void changeHealth(int value, Enemy e) {
		if (state == HURT)
			return;
		changeHealth(value);
		pushBackOffsetDir = UP;
		pushDrawOffset = 0;

		if (e.getHitbox().x < hitbox.x)
			pushBackDir = RIGHT;
		else
			pushBackDir = LEFT;
	}
	
	public void changeHealthObject(int value, Projectile p) {
		if (state == HURT)
			return;
		changeHealth(value);
		pushBackOffsetDir = UP;
		pushDrawOffset = 0;

		if (p.getHitbox().x < hitbox.x)
			pushBackDir = RIGHT;
		else
			pushBackDir = LEFT;
	}
	
	public void kill() {
		currentHealth = 0;
	}
	
	public void changePower(int value) {
		powerValue += value;
		if(powerValue >= powerMaxValue)
			powerValue = powerMaxValue;
		else if(powerValue <= 0)
			powerValue = 0;
	}
	
	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
		animations = new BufferedImage[16][13];
		for (int j = 0; j < animations.length; j++)
			for (int i = 0; i < animations[j].length; i++)
				animations[j][i] = img.getSubimage(i * 64, j * 64, 64, 64);

		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
	}
	
	public void loadLvData(int[][] lvData) {
		this.lvData = lvData;
		if (!IsEntityOnFloor(hitbox, lvData))
			inAir = true;
	}
	
	public void resetDirBooleans() {
		left = false;
		right = false;
	}
	
	public void setAttack(boolean attacking) {
		this.attacking = attacking;
	}
	
	// use setter and getter if using key event (use boolean is optimal)
	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}
	
	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public void resetAll() {
		resetDirBooleans(); 
		inAir = false;
		attacking = false;
		moving = false;
		airSpeed = 0f;
		state = STAND;
		currentHealth = maxHealth;
		powerAttackActive = false;
		powerAttackTick = 0;
		powerValue = powerMaxValue;
		
		hitbox.x = x;
		hitbox.y = y;
		resetAttackBox();
		
		if(!IsEntityOnFloor(hitbox, lvData))
			inAir = true;
	}
	
	private void resetAttackBox() {
		if(flipW == 1) {
			attackBox.x = hitbox.x + hitbox.width - (int) (Game.SCALE*5);
		} else {
			attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE*5);
		}
	}
	
	public int getTileY() {
		return tileY;
	}

	public void powerAttack() {
		if(powerAttackActive)
			return; // false
		if(powerValue >= 50) {
			powerAttackActive = true;
			changePower(-50);
		}
	}

}
