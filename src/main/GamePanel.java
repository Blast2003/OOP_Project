package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import static main.Game.GAME_WIDTH;
import static main.Game.GAME_HEIGHT;

public class GamePanel extends JPanel {
    
    private MouseInputs mouseinput;
    private Game game;
    public GamePanel(Game game){
       
        mouseinput= new MouseInputs(this);
        this.game=game;
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseinput);
        addMouseMotionListener(mouseinput);
    }
    private void setPanelSize(){
        Dimension size= new Dimension( GAME_WIDTH, GAME_HEIGHT); 
        setPreferredSize(size); 
    }
  
    public void updateGame() {
       
    }
    public void paintComponent (Graphics g){
        super.paintComponent(g);
        game.render(g);

    }
    public Game getGame(){
        return game;
    }
} 

    
