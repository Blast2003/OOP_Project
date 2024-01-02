package Objects;
import static utilz.Constants.ObjectConstants.*;

import Main.Game;
 
public class GameContainer extends GameObject{

	public GameContainer(int x, int y, int objType) {
		super(x, y, objType);
		createHitbox();
	}

	private void createHitbox() {
		if (objType == BOX) {
			initHitbox(25, 18);
			
			xDrawOffset = (int)(7 * Game.SCALE);
			yDrawOffset = (int)(12 * Game.SCALE);
			
		} else {
			initHitbox(23, 25);
			
			xDrawOffset = (int)(8 * Game.SCALE);
			yDrawOffset = (int)(5 * Game.SCALE);
		}
		
		hitbox.y += yDrawOffset + (2 * Game.SCALE); // get box in the ground
		hitbox.x += xDrawOffset /2; // get box in the center of tiles
	}
	
	public void update() {
		if (doAnimation)
			updateAnimationTick();
	}

}
