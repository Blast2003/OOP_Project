package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;


import Main.Game;
import Objects.ObjectManager;
import entities.EnemyManager;
import entities.player;
import levels.LevelManager;
import ui.GameOverOVerLay;
import ui.LevelCompletedOverLay;
import ui.PauseOverlay;
import utilz.LoadSave;
import static utilz.Constants.Environment.*;

public class Playing extends State implements Statemethods{
	
	private player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private PauseOverlay pauseOverlay;
	private GameOverOVerLay gameOverOVerLay;
	private LevelCompletedOverLay levelCompletedOverLay;
	private boolean paused = false ;
	
	
	// variable to make the background move when character move
	// offset = the part that we can see after move
	private int xLvOffset; 
	private int leftBorder = (int) (0.25 *Game.GAME_WIDTH);
	private int rightBorder = (int) (0.75 *Game.GAME_WIDTH);
	private int maxLvOffsetX; 
	
	private BufferedImage backgroundImg, bigCloud, smallCloud;
	private int [] smallCloudPos;
	private Random rnd = new Random();
	
	private boolean gameOver; //false
	private boolean lvCompleted;
	private boolean playerDying;
	
	
	public Playing(Game game) {
		super(game);
		initClasses();

		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
		bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
		smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
		smallCloudPos = new int[8]; // the length  of cloud's sprite
		for (int i = 0; i < smallCloudPos.length; i++)
			smallCloudPos[i] = (int) (90 * Game.SCALE) + rnd.nextInt((int) (100 * Game.SCALE));

		calcLvOffset();
		loadStartLevel();
	}
	
	public void loadNextLevel() {
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		resetAll();
	}

	private void loadStartLevel() {
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObject(levelManager.getCurrentLevel());
	}

	private void calcLvOffset() {
		maxLvOffsetX = levelManager.getCurrentLevel().getLvOffset();
		
	}

	// important method
	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);

		player = new player(200, 200, (int) (60 * Game.SCALE), (int) (64 * Game.SCALE), this);
		player.loadLvData(levelManager.getCurrentLevel().getLevelData());
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

		pauseOverlay = new PauseOverlay(this);
		gameOverOVerLay = new GameOverOVerLay(this);
		levelCompletedOverLay = new LevelCompletedOverLay(this);
	}
	

	@Override
	public void update() {
		if (paused) {
			pauseOverlay.update();
		} else if (lvCompleted) {
			levelCompletedOverLay.update();
		} else if (gameOver) {
			gameOverOVerLay.update();
		} else if (playerDying) {
			player.update();
		} else {
			levelManager.update();
			objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			player.update();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData());
			checkCloseToBorder();
		}
	}

	private void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLvOffset;

		if (diff > rightBorder)
			xLvOffset += diff - rightBorder;
		else if (diff < leftBorder)
			xLvOffset += diff - leftBorder;

		xLvOffset = Math.max(Math.min(xLvOffset, maxLvOffsetX), 0);
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

		drawClouds(g);

		levelManager.draw(g, xLvOffset);
		objectManager.draw(g, xLvOffset);
		enemyManager.draw(g, xLvOffset);
		player.render(g, xLvOffset);

		if (paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		} else if (gameOver)
			gameOverOVerLay.draw(g);
		else if (lvCompleted)
			levelCompletedOverLay.draw(g);
	}

	
	private void drawClouds(Graphics g) {
		// big cloud
		for(int i = 0; i < 3; i++)
		g.drawImage(bigCloud, i *BIG_CLOUD_WIDTH - (int)(xLvOffset*0.3), (int)(204*Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
		
		// small cloud
		for(int i = 0; i < smallCloudPos.length; i++)
		g.drawImage(smallCloud, SMALL_CLOUD_WIDTH *i*4 - (int)(xLvOffset*0.7), smallCloudPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
		
	}
	
	public void resetAll() {
		gameOver = false;
		paused = false;
		lvCompleted = false;
		playerDying = false;
		
		player.resetAll();
		enemyManager.resetAllEnemies();	
		objectManager.resetAllObjects();
	}
	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	public void checkObjectHit(Rectangle2D.Float attackBox) {
		objectManager.checkObjectHit(attackBox);
	}
	
	public void checkPlayerHit(Rectangle2D.Float attackBox) {
		enemyManager.checkPlayerHit(attackBox);
	}

	public void checkPotionTouched(Rectangle2D.Float hitbox) {
		objectManager.checkObjectTouched(hitbox);
	}
	
	public void checkSpikesTouched(player player) {
		objectManager.checkSpikeTouched(player);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(!gameOver) {
			if(e.getButton() == MouseEvent.BUTTON1)  // left mouse click
				player.setAttack(true);
			else if (e.getButton() == MouseEvent.BUTTON3) // right mouse click
				player.powerAttack();
		}
	}
		
	@Override
	public void mousePressed(MouseEvent e) {
		if (!gameOver) {
			if(paused)
				pauseOverlay.mousePressed(e);
			else if(lvCompleted)
				levelCompletedOverLay.mousePressed(e);
		} else 
			gameOverOVerLay.mousePressed(e);
			
		
	}

	
	public void mouseDragged(MouseEvent e) {
		if(!gameOver)
			if(paused)
				pauseOverlay.mouseDragged(e);
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mouseReleased(e);
			else if(lvCompleted)
				levelCompletedOverLay.mouseReleased(e);
		} else 
			gameOverOVerLay.mouseReleased(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(!gameOver) {
			if(paused)
				pauseOverlay.mouseMoved(e);
			else if(lvCompleted)
				levelCompletedOverLay.mouseMoved(e);
		} else 
			gameOverOVerLay.mouseMoved(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!gameOver && !lvCompleted )
			switch(e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_D:
				player.setRight(true);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(true);
				break;
			case KeyEvent.VK_ESCAPE:
				paused = !paused;
				break;	
			}
	}
		

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver && !lvCompleted)
			switch(e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(false);
				break;
			}
		
	}
	
	
	// create a new method under or above the override method
	
	public void setLevelCompleted(boolean levelCompleted) {
		this.lvCompleted = levelCompleted;
		if(levelCompleted)
			game.getauAudioPlayer().lvCompleted();
		
	}
	
	public void setMaxLvOffset(int lvOffset) {
		this.maxLvOffsetX = lvOffset;
	}
	
	public void unpauseGame() {
		paused = false;
	}
	
	public void windowFocusLost() {
		player.resetDirBooleans();
	}
	
	public player getPlayer() {
		return player;
	}

	public EnemyManager getEnemyManager() {
		return enemyManager;
	}
	
	public ObjectManager getObjectManager() {
		return objectManager;
	}
	
	public LevelManager getlevelManager() {
		return levelManager;
	}

	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;
		
	}
	
}
