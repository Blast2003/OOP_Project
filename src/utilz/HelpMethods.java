package utilz;

import static utilz.Constants.ObjectConstants.*;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


import Main.Game;
import Objects.Cannon;
import Objects.GameContainer;
import Objects.Potion;
import Objects.Projectile;
import Objects.Spike;
import Objects.Tree;

//Check collision

public class HelpMethods {

	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvData) {
		if (!IsSolid(x, y, lvData))
			if (!IsSolid(x + width, y + height, lvData))
				if (!IsSolid(x + width, y, lvData))
					if (!IsSolid(x, y + height, lvData))
						return true;
		return false;
	}
	
	private static boolean IsSolid(float x, float y, int[][] lvData) {
		int maxWidth = lvData[0].length * Game.TILES_SIZE;
		if (x < 0 || x >= maxWidth)
			return true;
		if (y < 0 || y >= Game.GAME_HEIGHT)
			return true;
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;

		return IsTileSolid((int) xIndex, (int) yIndex, lvData);
	}	
	
	public static boolean isProjectileHittingLevel(Projectile p, int [][] lvData) {
		return IsSolid (p.getHitbox().x + p.getHitbox().width/2, p.getHitbox().y + p.getHitbox().height/2 , lvData);
	}
	
	public static boolean IsEntityInWater(Rectangle2D.Float hitbox, int[][] lvlData) {
		// only touched top water.
		if (GetTileValue(hitbox.x, hitbox.y + hitbox.height, lvlData) != 48)
			if (GetTileValue(hitbox.x + hitbox.width, hitbox.y + hitbox.height, lvlData) != 48)
				return false;
		return true;
	}
	
	private static int GetTileValue(float xPos, float yPos, int[][] lvData) {
		int xCord = (int) (xPos / Game.TILES_SIZE);
		int yCord = (int) (yPos / Game.TILES_SIZE);
		return lvData[yCord][xCord];
	}

	public static boolean IsTileSolid(int xTile, int yTile, int[][] lvData) {
		int value = lvData[yTile][xTile];

		switch (value) {
		case 11, 48, 49:
			return false;
		default:
			return true;
		}

	}
	
	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
		if (xSpeed > 0) {
			// Right
			int tileXPos = currentTile * Game.TILES_SIZE;
			int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
			return tileXPos + xOffset - 1;
		} else
			// Left
			return currentTile * Game.TILES_SIZE;
	}
	
	
	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
		if (airSpeed > 0) {
			// get the space between hit box and tile 
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
			return tileYPos + yOffset -1;
		} else
			// Jumping
			return currentTile * Game.TILES_SIZE;

	}
	
	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvData) {
		//check the pixel below bottom left and bottom right
		if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvData))
			if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvData))
				return false;
		return true;
		
	}
	
	public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvData) {
		if (xSpeed > 0)
			return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvData);
		else
			return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvData);
	}

	public static boolean IsFloor(Rectangle2D.Float hitbox, int[][] lvData) {
		if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvData))
			if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvData))
				return false;
		return true;
	}
	
	public static boolean CanCannonSeePlayer(int [][] lvData, Rectangle2D.Float firstHitbox ,Rectangle2D.Float secondHitbox, int yTile) {
		int firstXTile = (int) (firstHitbox.x/Game.TILES_SIZE); // player
		int secondXTile = (int) (secondHitbox.x/Game.TILES_SIZE); // cannon
		
		//check move and if having solid => false : can't move
		if(firstXTile > secondXTile) 
			return IsAllTilesClear(secondXTile, firstXTile, yTile, lvData);
		else 
			return IsAllTilesClear(firstXTile, secondXTile, yTile, lvData);
	}
	
	public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvData) {
		for (int i = 0; i < xEnd - xStart; i++)
			if (IsTileSolid(xStart + i, y, lvData))
				return false;
		return true;
	}
	
	public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvData) {
		if (IsAllTilesClear(xStart, xEnd, y, lvData))
			for (int i = 0; i < xEnd - xStart; i++) {
				if (!IsTileSolid(xStart + i, y + 1, lvData))
					return false;
			}
		return true;
	}
	
	public static boolean IsSightClear(int [][] lvData, Rectangle2D.Float enemyBox ,Rectangle2D.Float playerBox, int yTile) {
		
		int firstXTile = (int) (enemyBox.x/Game.TILES_SIZE); 
		
		int secondXTile;
		if(IsSolid(playerBox.x, playerBox.y + playerBox.height, lvData))
			secondXTile =  (int) (playerBox.x/Game.TILES_SIZE); 
		else
			secondXTile = (int) ((playerBox.x + playerBox.width) / Game.TILES_SIZE);
		
		//check move and if having solid => false : can't move
		if(firstXTile > secondXTile) 
			return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvData);
		else 
			return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvData);
	}
	
	// help player born (spawn) at the determine side (direction)
	public static Point GetPlayerSpawn(BufferedImage img) {
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == 100)
					return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
			}
		return new Point(1 * Game.TILES_SIZE, 1 * Game.TILES_SIZE);
	}
	
	
	
	public static ArrayList<Potion> getPotions(BufferedImage img) {
		ArrayList<Potion> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == RED_POTION || value == BLUE_POTION)
					list.add(new Potion(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
			}
		return list;
	}
	
	public static ArrayList<GameContainer> getContainers(BufferedImage img) {
		ArrayList<GameContainer> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == BARREL|| value == BOX)
					list.add(new GameContainer(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
			}
		return list;
	}

	public static ArrayList<Tree> GetTrees(BufferedImage img) {
		ArrayList<Tree> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == TREE)
					list.add(new Tree(i * Game.TILES_SIZE, j * Game.TILES_SIZE, TREE));
			}
		return list;
	}
	
	public static ArrayList<Spike> GetSpikes(BufferedImage img) {
		ArrayList<Spike> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == SPIKE)
					list.add(new Spike(i * Game.TILES_SIZE, j * Game.TILES_SIZE, SPIKE));
			}
		return list;
	}
	
	public static ArrayList<Cannon> GetCanons(BufferedImage img) {
		ArrayList<Cannon> list = new ArrayList<>();
		
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getBlue();
				if (value == CANNON_LEFT || value == CANNON_RIGHT)
					list.add(new Cannon(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
			}
		return list;
	}
}
