package Objects;


import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Main.Game;
import entities.player;
import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.ObjectConstants.*;
import static utilz.Constants.Projectiles.*;
import static utilz.HelpMethods.CanCannonSeePlayer;
import static utilz.HelpMethods.isProjectileHittingLevel;

public class ObjectManager {

	private Playing playing;
	private BufferedImage[][] potionImgs, containerImgs;
	private BufferedImage[] cannonImgs;
	private BufferedImage spikeImg, cannonBallImg;
	private BufferedImage[] treeImgs; 
	private ArrayList<Potion> potions;
	private ArrayList<GameContainer> containers;
	private ArrayList<Cannon> cannons;
	private ArrayList<Projectile> projectiles = new ArrayList<>();
	private Level currentLevel;
	
	public ObjectManager (Playing playing) {
		this.playing = playing;
		currentLevel = playing.getlevelManager().getCurrentLevel();
		loadImgs();
	}
	
	public void checkObjectTouched(Rectangle2D.Float hitbox) {
		for(Potion p : potions)
			if(p.isActive()) {
				if(hitbox.intersects(p.getHitbox())) {
					p.setActive(false);
					applyEffectToPlayer(p);
				}
			}
	}
	
	//use for enemy
/*	public void checkSpikeTouched(Enemy enemy) {
		for(Spike s : currentLevel.getSpikes())
			if(s.getHitbox().intersects(enemy.getHitbox()))
				enemy.hurt(200);
	}
*/	
	// use for player
	public void checkSpikeTouched(player player) {
		for(Spike s : currentLevel.getSpikes())
			if(s.getHitbox().intersects(player.getHitbox()))
				player.kill();
	}
	
	public void applyEffectToPlayer(Potion p) {
		if(p.getObjType() == RED_POTION)
			playing.getPlayer().changeHealth(RED_POTION_VALUE);
		else 
			playing.getPlayer().changePower(BLUE_POTION_VALUE);
	}
	
	public void checkObjectHit(Rectangle2D.Float attackbox) {
		for(GameContainer gc : containers)
			if(gc.isActive() && !gc.doAnimation) {
				if(gc.getHitbox().intersects(attackbox)) {
					gc.setAnimation(true);
					int type = 0;
					if(gc.getObjType() == BARREL)
						type = 1;
					potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width/2), 
							(int) (gc.getHitbox().y - gc.getHitbox().height/2), type));
					return;
				}
				
				
			}
	}
	
	public void loadObject(Level newLevel) {
		currentLevel = newLevel;
		potions = new ArrayList<>(newLevel.getPotions()); // use for object has many types
		containers = new ArrayList<>(newLevel.getContainers());
		cannons = newLevel .getCannons();
		projectiles.clear(); // project has clear => don' need reset 
	}

	private void loadImgs() {
		//potion
		BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
		potionImgs = new BufferedImage[2][7];
		for(int j = 0; j < potionImgs.length; j++)
			for(int i = 0; i < potionImgs[j].length; i++)
				potionImgs[j][i] = potionSprite.getSubimage(12 *i , 16 *j , 12, 16);
		
		//container
		BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
		containerImgs = new BufferedImage[2][8];
		for(int j = 0; j < containerImgs.length; j++)
			for(int i = 0; i < containerImgs[j].length; i++)
				containerImgs[j][i] = containerSprite.getSubimage(40 *i , 30 *j , 40, 30);	
		
		
		//spike
		spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS); 
		
		//canon 
		cannonImgs = new BufferedImage[7];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);
		for(int i = 0; i < cannonImgs.length ; i++)
			cannonImgs[i] = temp.getSubimage(i * 40, 0, 40, 26); 
		
		//canon ball
		cannonBallImg = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);
		
		//tree
		treeImgs = new BufferedImage[6];
		BufferedImage temp1 = LoadSave.GetSpriteAtlas(LoadSave.TREE_ATLAS);
		for(int i = 0; i < treeImgs.length ; i++)
			treeImgs[i] = temp1.getSubimage(i * 128, 0, 128, 96); 
	}
	
	public void update(int [][] lvData, player player) {
		updateTree();
		for(Potion p : potions) 
			if(p.isActive())
				p.update();
			
		for(GameContainer gc : containers)
			if(gc.isActive())
				gc.update();
		
		updateCannons(lvData, player);
		updateProjectiles(lvData, player);
	}
	
	private void updateTree() {
		for(Tree tree : currentLevel.getTrees())
			tree.update();
	}

	private void updateProjectiles(int[][] lvData, player player) {
		for(Projectile p : projectiles)
			if(p.isActive()) {
				p.updatePos();
				if(p.getHitbox().intersects(player.getHitbox())) {
					player.changeHealth(-25);
					p.setActive(false);
				} else if (isProjectileHittingLevel(p, lvData)) 
					p.setActive(false);
			}
	}

	private boolean isPlayerInRange(Cannon c, player player) {
		int absValue = (int) Math.abs(player.getHitbox().x - c.getHitbox().x);
		return absValue <=  Game.TILES_SIZE * 5;
	}

	private boolean isPlayerInFrontOfCannon(Cannon c, player player) {
			if(c.objType == CANNON_LEFT) {
				if(c.getHitbox().x > player.getHitbox().x)
					return true;
				
			} else if(c.getHitbox().x < player.getHitbox().x)
				return true;
			return false;	
	}
	
	private void updateCannons(int [][] lvData, player player) {
		for (Cannon c : cannons) {
			if(!c.doAnimation)
				if(c.getTileY() == player.getTileY())
					if(isPlayerInRange(c,player))
						if(isPlayerInFrontOfCannon(c, player))
							if(CanCannonSeePlayer(lvData, player.getHitbox(), c.getHitbox(), c.getTileY())) {
								c.setAnimation(true);;
						}	
			c.update();
			if(c.getAniIndex() == 4 && c.getAniTick() == 0)
				shootCannon(c);
		}			
	}


	private void shootCannon(Cannon c) {
		int dir = 1;
		if(c.getObjType() == CANNON_LEFT)
			dir = -1;
		projectiles.add(new Projectile((int)c.getHitbox().x, (int) c.getHitbox().y, dir));
	}

	public void draw(Graphics g, int xLvOffset) {
		drawPotions(g, xLvOffset);
		drawContainer(g, xLvOffset);
		drawTraps(g, xLvOffset);
		drawCannon(g, xLvOffset);
		drawProjectiles(g, xLvOffset);
		drawTrees(g, xLvOffset);
	}

	private void drawProjectiles(Graphics g, int xLvOffset) {
		for(Projectile p : projectiles)
			if(p.isActive())
				g.drawImage(cannonBallImg, (int) (p.getHitbox().x - xLvOffset), 
						(int) (p.getHitbox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
	}

	private void drawCannon(Graphics g, int xLvOffset) {
		for (Cannon c : cannons) {
			int x = (int) (c.getHitbox().x - xLvOffset);
			int width = CANNON_WIDTH;
			
			if(c.getObjType() == CANNON_RIGHT) {
				x += width;
				width*= -1;
			}
			
			g.drawImage(cannonImgs[c.getAniIndex()],
					x, (int)(c.getHitbox().y ),
					width, CANNON_HEIGHT, null);
		}
			
	}

	private void drawTraps(Graphics g, int xLvOffset) {
		for(Spike s : currentLevel.getSpikes())
			g.drawImage(spikeImg,(int) (s.getHitbox().x -xLvOffset), 
					(int)(s.getHitbox().y - s.getyDrawOffset()),
					SPIKE_WIDTH, SPIKE_HEIGHT,
					null);	
	}
	
	private void drawTrees(Graphics g, int xLvOffset) {
		for(Tree tree : currentLevel.getTrees())
			g.drawImage(treeImgs[tree.getAniIndex()],(int) (tree.getX() - xLvOffset + GetTreeOffSetX()), 
					(int)(tree.getY() + GetTreeOffSetY()-48),
					TREE_WIDTH, TREE_HEIGHT,
					null);	
	}
	

	private void drawContainer(Graphics g, int xLvOffset) {
		for(GameContainer gc : containers)
			if (gc.isActive()) {
				int type = 0;
				if (gc.getObjType() == BARREL) // a box contain alcohol
					type = 1;
				
				g.drawImage(containerImgs[type][gc.getAniIndex()],
						 (int)(gc.getHitbox().x - gc.getxDrawOffset() - xLvOffset),
						 (int)(gc.getHitbox().y - gc.getyDrawOffset()),
						 CONTAINER_WIDTH, CONTAINER_HEIGHT,	null);
			}
				
		
	}

	private void drawPotions(Graphics g, int xLvOffset) {
		for(Potion p : potions) {
			if(p.isActive()) {
				int type = 0;
				if(p.getObjType() == RED_POTION)
					type = 1;
				
				g.drawImage(potionImgs[type][p.getAniIndex()],
						 (int)(p.getHitbox().x - p.getxDrawOffset() - xLvOffset),
						 (int)(p.getHitbox().y - p.getyDrawOffset()),
						 POTION_WIDTH, POTION_HEIGHT, null);
				p.drawHitbox(g, xLvOffset);
			}
		
		}
	
	}
	
	public void resetAllObjects() {
		
		loadObject(playing.getlevelManager().getCurrentLevel());
		
		for(Potion p : potions)
			p.reset();
			
		for(GameContainer gc : containers)
			gc.reset();
		
		for(Cannon c : cannons)
			c.reset();	
		
	}

}
