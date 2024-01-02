package Main;



import java.awt.Graphics;

import audio.AudioPlayer;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import ui.AudioOptions;


public class Game implements Runnable{
	
	private GamePanel gamePanel;
	private Thread gameThread;
	private final int FPS_SET = 120;
	private final int UPS_SET = 200; // update
	
	private Playing playing;
	private Menu menu;
	private GameOptions gameOptions;
	private AudioOptions audioOptions;
	private AudioPlayer audioPlayer;
	
	public final static int TILES_DEFAULT_SIZE = 32;
	public final static float SCALE = 1.5f;
	public final static int TILES_IN_WIDTH = 26;
	public final static int TILES_IN_HEIGHT = 14;
	public final static int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE); // AREA of GAME BOARD
	public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH  ;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT ;
	
	// constructor
	public Game() {
		initClasses();
		
		gamePanel = new GamePanel(this); // create a gamePanal firstly after adding to gameWindow
		new GameWindow(gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus(); // make game Panel focus on keyListener (and other components)
		gameOptions = new GameOptions(this);
		
		startGameLoop(); // start game loop
	}
	
	private void initClasses() {
		audioOptions = new AudioOptions(this);
		audioPlayer = new AudioPlayer();
		menu = new Menu(this);
		playing = new Playing(this);
		gameOptions = new GameOptions(this);
		
	}

	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start(); // runnable of thread
	}
	
	public void update() {
		switch(Gamestate.state) {
		case MENU -> menu.update();
			
		case PLAYING -> playing.update();
			
		case OPTIONS ->	gameOptions.update();
			
		case QUIT -> System.exit(0); // exit the window 
		
		}
	}
	
	// uncompleted switch
	public void render(Graphics g) { // render = draw 
		
		switch(Gamestate.state) {
		case MENU:
			menu.draw(g);
			break;
		case PLAYING:
			playing.draw(g);
			break;
		case OPTIONS:
			gameOptions.draw(g);
			break;
		default:
			break;
		
		}
		
		
	}
	
	
	@Override
	public void run() { // run game thread
		
		double timePerFrame = 1000000000.0/FPS_SET; // Unit: nano second = 10^-9s
		double timePerUpdate = 1000000000.0/UPS_SET;

		
		long previousTime = System.nanoTime();
		
		
		int frames = 0;
		int updates = 0;
		long lastCheck = System.currentTimeMillis();
		
		double deltaU = 0;
		double deltaF = 0;
		
		//check and control FPS
		while(true) {
			long currentTime = System.nanoTime();
			
			deltaU += (currentTime - previousTime)/timePerUpdate; // more than or equal 1
			deltaF += (currentTime - previousTime)/timePerFrame;
			previousTime = currentTime;
			
			//UPS check moving (ms)
			if(deltaU >= 1) {
				update();
				updates++;
				deltaU --;
			}
			
			// FPS check moving
			if(deltaF >= 1) {
				gamePanel.repaint();
				frames++;
				deltaF --;
			}
			

			
			if(System.currentTimeMillis() - lastCheck >=1000) { //after 1s check the frame, save it and repeat
				lastCheck = System.currentTimeMillis();
				System.out.println("FPS: "+frames+" | UPS: "+updates); // the size of window larger => the FPS larger
				frames = 0;
				updates = 0;
			}
			
		}
		
	}
	
	public void windowFocusLost() {
		if(Gamestate.state == Gamestate.PLAYING)
			playing.getPlayer().resetDirBooleans();
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public Playing getPlaying() {
		return playing;
	}
	
	public GameOptions getGameOptions() {
		return gameOptions;
	}
	
	public AudioOptions getAudioOptions() {
		return audioOptions;
	}
	
	public AudioPlayer getauAudioPlayer() {
		return audioPlayer;
	}
	
	
}
