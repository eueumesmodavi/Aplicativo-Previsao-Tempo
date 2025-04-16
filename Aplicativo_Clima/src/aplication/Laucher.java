package aplication;

import javax.swing.SwingUtilities;


public class Laucher {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				
				new ClimaTempoGUI().setVisible(true);
				
			}
		});
		
		
	}

}
