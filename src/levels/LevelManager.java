package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


import Main.Game;
import gamestates.Gamestate;
import utilz.LoadSave;

public class LevelManager {

	private BufferedImage[] levelSprite;
	private BufferedImage[] waterSprite;
	private Game game;
	private ArrayList<Level> levels;
	private int lvIndex = 0, aniTick, aniIndex;
	
	
	//constructor
	public LevelManager(Game game) {
		this.game = game;
//		levelSprite = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		importOutsideSprites();
		createWater();
		levels = new ArrayList<>();
		buildAllLevels();
	}
	
	private void createWater() {
		waterSprite = new BufferedImage[5];
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.WATER_TOP);
		for (int i = 0; i < 4; i++)
			waterSprite[i] = img.getSubimage(i * 32, 0, 32, 32);
		waterSprite[4] = LoadSave.GetSpriteAtlas(LoadSave.WATER_BOTTOM);
		
	}

	public void loadNextLevel() {
		lvIndex++;
		if(lvIndex >= levels.size()) {
			lvIndex = 0;
			System.out.println("No more level! Game completed!");
			Gamestate.state = Gamestate.MENU; // enum => can use directly name of class
		}
			
		Level newLevel = levels.get(lvIndex);
		game.getPlaying().getEnemyManager().loadEnemies(newLevel);
		game.getPlaying().getPlayer().loadLvData(newLevel.getLevelData());
		game.getPlaying().setMaxLvOffset(newLevel.getLvOffset());    	// make the map moving follow to the movement of player
		game.getPlaying().getObjectManager().loadObject(newLevel);
	}

	private void buildAllLevels() {
		BufferedImage[] allLevels = LoadSave.GetAllLevels();	
		for(BufferedImage img : allLevels)
			levels.add(new Level(img));
	}

	private void importOutsideSprites() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		levelSprite = new BufferedImage[48];
		for (int j = 0; j< 4; j++) {
			for (int i =0; i<12; i++) {
				int index = j*12 + i;
				levelSprite[index] = img.getSubimage(i*32, j*32, 32, 32);
			}
		}
		
	}

	public void draw(Graphics g, int lvOffset) {
		for(int j =0; j < Game.TILES_IN_HEIGHT; j++) {
			for (int i =0; i < levels.get(lvIndex).getLevelData()[0].length; i++ ) {
				int index = levels.get(lvIndex).getSpriteIndex(i, j);
				int x = Game.TILES_SIZE * i - lvOffset;
				int y = Game.TILES_SIZE * j;
				if(index == 48) 
					g.drawImage(waterSprite[aniIndex], x, y,Game.TILES_SIZE ,Game.TILES_SIZE, null);
				else if(index == 49)
					g.drawImage(waterSprite[4], x, y, Game.TILES_SIZE ,Game.TILES_SIZE, null);
				else
					g.drawImage(levelSprite[index],x, y,Game.TILES_SIZE ,Game.TILES_SIZE, null);
			}
		}
		
	}
	
	public void update() {
		updateWaterAnimation();
	}
	
	private void updateWaterAnimation() {
		aniTick ++;
		if(aniTick > 40) {
			aniTick = 0;
			aniIndex++;
			
			if(aniIndex >= 4)
				aniIndex = 0;
		}
	}

	public Level getCurrentLevel() {
		return levels.get(lvIndex);
	}
	
	
	public int getLevelIndex() {
		return lvIndex;
	}

}
