package Main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import Input.KeyboardInput;
import Input.MouseInput;

import static Main.Game.GAME_HEIGHT;
import static Main.Game.GAME_WIDTH;


public class GamePanel extends JPanel {
	
	private Game game;
	
	
	//constructor
	public GamePanel(Game game) {
		
		this.game = game;
		
		// can use form of Keyboard to add for mouseInput
		setPanelSize();
		addKeyListener(new KeyboardInput(this)); 
		//add Keyboard Input into Game Panel
		addMouseListener(new MouseInput(this));
		addMouseMotionListener(new MouseInput(this));
	}
	

	

	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT); // parent layout
		setPreferredSize(size);
		System.out.println("Size: "+GAME_WIDTH + " : "+GAME_HEIGHT);
	}


	
	public void updateGame() {
		
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
		
	}
	
	public Game getGame() {
		return game;
	}


	
	
}
