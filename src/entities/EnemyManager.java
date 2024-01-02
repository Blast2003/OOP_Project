package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.Constants.EnemyConstants.*;
import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;

public class EnemyManager {
	
	
	private Playing playing;
	private BufferedImage [][] Goblin_Worker_Arr, Goblin_Assassin_Arr, Dragon_Arr;
	private Level currentLevel;
	
	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}
	
	public void loadEnemies(Level newlevel) {
		this.currentLevel = newlevel;
	}

	public void update(int [][] lvData) {
		boolean isAnyActive = false;
		
		for(Goblin_Worker w : currentLevel.getWorkers())
			if(w. isActive()) {
				w.update(lvData, playing);
				isAnyActive = true;
			}
		
		for(Goblin_Assassin A : currentLevel.getAssassins())
			if(A. isActive()) {
				A.update(lvData, playing);
				isAnyActive = true;
			}
		
		for(Dragon d : currentLevel.getDragons())
			if(d. isActive()) {
				d.update(lvData, playing);
				isAnyActive = true;
			}
		
		if(!isAnyActive)
			playing.setLevelCompleted(true);
	}
	
	public void draw(Graphics g, int XlvOffset) {
		drawWorkers(g, XlvOffset);
		drawAssassin(g, XlvOffset);
		drawDragons(g, XlvOffset);
	}
	
	private void drawWorkers(Graphics g, int XlvOffset) {
		for(Goblin_Worker w : currentLevel.getWorkers()) 
			if(w.isActive()) {
				g.drawImage( Goblin_Worker_Arr[w.getState()][w.getAniIndex()],
						(int) w.getHitbox().x - XlvOffset - GOBLIN_WORKER_DRAWOFFSET_X + w.flipX(),
						(int) w.getHitbox().y - GOBLIN_WORKER_DRAWOFFSET_Y - 7 , 
						GOBLIN_WORKER_WIDTH * w.flipW(), GOBLIN_WORKER_HEIGHT, null);
			
			// draw attack Hit box
			//	w.drawAttackBox(g, XlvOffset);
			//	w.drawHitbox(g, XlvOffset);
			}
	}
	
	
	private void drawAssassin (Graphics g, int XlvOffset) {
		for(Goblin_Assassin A : currentLevel.getAssassins()) 
			if(A.isActive()) {
				g.drawImage( Goblin_Assassin_Arr[A.getState()][A.getAniIndex()],
						(int) A.getHitbox().x - XlvOffset - GOBLIN_ASSASSIN_DRAWOFFSET_X + A.flipX(),
						(int) A.getHitbox().y - GOBLIN_ASSASSIN_DRAWOFFSET_Y  , 
						GOBLIN_ASSASSIN_WIDTH * A.flipW(), GOBLIN_ASSASSIN_HEIGHT, null);
				
			//	A.drawHitbox(g, XlvOffset);
			//	A.drawAttackBox(g, XlvOffset);
			}	
	}
	
	
	private void drawDragons (Graphics g, int XlvOffset) {
		for(Dragon d : currentLevel.getDragons()) 
			if(d.isActive()) {
				g.drawImage( Dragon_Arr[d.getState()][d.getAniIndex()],
						(int) d.getHitbox().x - XlvOffset - DRAGON_DRAWOFFSET_X + d.flipX(),
						(int) d.getHitbox().y - DRAGON_DRAWOFFSET_Y, 
						DRAGON_WIDTH * d.flipW(),DRAGON_HEIGHT, null);
				
			//	d.drawAttackBox(g, XlvOffset);
			//	d.drawHitbox(g, XlvOffset);
			}	
	}
	
	// get the number of dame to player
	public void checkPlayerHit(Rectangle2D.Float attackBox) {
		for(Goblin_Worker w : currentLevel.getWorkers())
			if(w.isActive()) 
				if(w.getState() != DEAD && w.getState() != HURT)
					if(attackBox.intersects(w.getHitbox())) {
						w.hurt(10);
						return;
					}
		
		for(Goblin_Assassin A : currentLevel.getAssassins())
			if(A.isActive())
				if (A.getState() != DEAD && A.getState() != HURT)
					if (attackBox.intersects(A.getHitbox())) {
						A.hurt(20);
						return;
					}
			
		
		for(Dragon d : currentLevel.getDragons())
			if(d.isActive()) 
				if(d.getState() != DEAD && d.getState() != HURT)
					if(attackBox.intersects(d.getHitbox())) {
						d.hurt(20);
						return;
					}
		
	}

	private void loadEnemyImgs() {
		Goblin_Worker_Arr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE), 8, 5, GOBLIN_WORKER_WIDTH_DEFAULT, GOBLIN_WORKER_HEIGHT_DEFAULT);
		Goblin_Assassin_Arr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.STARFISH_SPRITE), 8, 5, GOBLIN_ASSASSIN_WIDTH_DEFAULT, GOBLIN_ASSASSIN_HEIGHT_DEFAULT);
		Dragon_Arr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.SHARK_SPRITE), 8, 5, DRAGON_WIDTH_DEFAULT, DRAGON_HEIGHT_DEFAULT);
	}

	private BufferedImage[][] getImgArr(BufferedImage atlas, int xSize, int ySize, int spriteW, int spriteH) {
		BufferedImage[][] tempArr = new BufferedImage[ySize][xSize];
		for (int j = 0; j < tempArr.length; j++)
			for (int i = 0; i < tempArr[j].length; i++)
				tempArr[j][i] = atlas.getSubimage(i * spriteW, j * spriteH, spriteW, spriteH);
		return tempArr;
	}

	
	public void resetAllEnemies() {
		loadEnemies(playing.getlevelManager().getCurrentLevel());
		loadEnemyImgs();
		
		for( Goblin_Worker w: currentLevel.getWorkers())
			w.resetEnemy();
		
		for(Goblin_Assassin A : currentLevel.getAssassins())
			A.resetEnemy();
		
		for(Dragon d: currentLevel.getDragons())
			d.resetEnemy();
	}
}
