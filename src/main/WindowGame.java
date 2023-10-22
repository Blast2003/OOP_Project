package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

public class WindowGame {
	private JFrame jframe;
	public WindowGame (GamePanel  gamePanel) {
		
		jframe = new JFrame();
		
		
		
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jframe.add(gamePanel);
		
		jframe.setLocationRelativeTo(null);
		
		jframe.setResizable(false); // it dont resize terrible
		
		jframe.pack(); // make window enough to fit panel
		
		jframe.setVisible(true);
		
		jframe.addWindowFocusListener(new WindowFocusListener() {
			
			@Override // print out sthing when close window
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowFocusLost();
			}
			@Override
			public void windowGainedFocus(WindowEvent e) {
				
			}
		});
	}

}
