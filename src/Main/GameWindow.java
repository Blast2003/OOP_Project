package Main;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;
// make frame
public class GameWindow {
	
	private JFrame frame;
	//constructor
	public GameWindow(GamePanel gamePanel) {
		frame = new JFrame();
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // make frame close {terminated}
		frame.add(gamePanel);
		
		frame.setResizable(false); // don't want to resize again
		frame.pack();
		frame.setLocationRelativeTo(null); // set the window in the center automatically and must add after frame.pack()
		frame.setVisible(true);
		// focus on key listener
		frame.addWindowFocusListener(new WindowFocusListener() {
			
			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowFocusLost();
				
			}
			
			@Override
			public void windowGainedFocus(WindowEvent e) {
				
				
			}
		});
		
	}
}
