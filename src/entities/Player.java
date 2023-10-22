package entities;

// import static utilz.Constants.Direction.DOWN;
// import static utilz.Constants.Direction.LEFT;
// import static utilz.Constants.Direction.RIGHT;
// import static utilz.Constants.Direction.UP;
// import static utilz.Constants.PlayerConstants.GetSpriteAmount;
// import static utilz.Constants.PlayerConstants.IDLE;
// import static utilz.Constants.PlayerConstants.RUNNING;

// import java.awt.Graphics;
// import java.awt.image.BufferedImage;
// import java.io.FileInputStream;
// import java.io.IOException;
// import java.io.InputStream;

// import javax.imageio.ImageIO;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.Buffer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import static utilz.Constants.PlayerConstants.*;

public class Player extends Entity {
	// private static final int ATTACK_1 = 0;
	// private static final int ATTACK_1 = 0;
	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 15 ; // decrease for faster animation
	private int playerAction = RUNNING ;
	private boolean moving = false , attacking = false;
	private boolean left, up, right, down;
	private float playerSpeed = 2.0f;

	public Player(float x, float y) {
		super(x, y);
		loadAnimations();
		// TODO Auto-generated constructor stub
	}

	public void update() {
		updatePos(); 
		updateAnimationTick();
		setAnimation();
	}
	
	public void render(Graphics g) {
		g.drawImage(animations[playerAction][aniIndex], (int)x, (int)y,64*3,64*3, null);

		 
	}

	// create animation for character
	private void updateAnimationTick() {
		aniTick++;
		if(aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex >= GetSpriteAmount(playerAction)) {
				aniIndex = 0;
				attacking =false ;
			}
				
		}
	}
	
	private void setAnimation() {
		int starAni = playerAction; 
		
		if (moving)
			playerAction = RUNNING;
		else
			playerAction = IDLE;
		
		if (attacking)
			playerAction = ATTACK_1;
		
		if (starAni != playerAction)
			resetAniTick();
	}
	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		 moving = false; 
		
		if(left && !right ) {
			x -= playerSpeed;
			moving = true; 
		} else if (right && !left) {
			x += playerSpeed;
			moving = true; 
		}
		if (up && !down) {
			y -= playerSpeed; 
			moving = true; 
		} else if (down && !up) {
			y += playerSpeed;
			moving = true; 
		}
	
	}
	private void loadAnimations() {
		// InputStream is = getClass().getResourceAsStream("/Adventurer Sprite sheet.png"); // import IMG
	
		
		try {
			// BufferedImage img = ImageIO.read(is);
			BufferedImage img= ImageIO.read(new FileInputStream("res/adventurer_Sprite_Sheet.png"));
			
			animations = new BufferedImage[13][13];
			
			for(int j =0 ; j<animations.length; j++)
				for(int i = 0; i < animations[j].length; i++)
				animations[j][i]= img.getSubimage(i*64,j*64, 64, 64);
		
		}	catch (IOException e ) {
			e.printStackTrace();
			
		} finally {
			try {
				// .close();
				new FileInputStream("res/adventurer_Sprite_Sheet.png").close();
			} catch(IOException e ) {
				e.printStackTrace();
			}
		}		
	}
	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}
	
	public void setAttacking(boolean attacking) {
		this.attacking = attacking ;
	}
	
	
	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}
	
	
}
