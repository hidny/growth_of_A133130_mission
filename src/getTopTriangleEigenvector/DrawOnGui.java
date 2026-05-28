package getTopTriangleEigenvector;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//From youtube Java GUI Tutorial - Make a GUI in 13 Minutes #99
//From https://introcs.cs.princeton.edu/java/15inout/GUI.java.html
public class DrawOnGui implements ActionListener {

	private JFrame frame;
	private JLabel label;
	private JPanel contentPane;
	private JPanel panelDrawing;
	private JButton button;
	private JButton button2;
	private int count = 0;
	
	public DrawOnGui() {
		frame = new JFrame();
		frame.setSize(600, 800);
		
		button = new JButton("Click me");
		button.addActionListener(this);
		button2 = new JButton("Click me 2");
		button2.addActionListener(this);
		
		label = new JLabel("Number of clicks: " + count);
		
		contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(5, 5));
		contentPane.add(button);
		contentPane.add(button2);
		contentPane.add(label);
		
		panelDrawing = new GPanel();
		panelDrawing.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		//panel.setLayout(new GridLayout(0, 1));
		panelDrawing.setLayout(null);
		//panel.add(button);
		//panel.add(label);
		
		//TODO: please paint grid or whatever.

		frame.add(contentPane, BorderLayout.NORTH);
		frame.add(panelDrawing, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Visualization on Matrix");
		//frame.pack();
		frame.setVisible(true);
		
	}
	
	private class GPanel extends JPanel {
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			//g.drawRect(10,10, 190, 190);
			g.drawRect(10,10, 522, 522);
			g.setColor(Color.BLUE);
			g.fillOval(50, 50, 100, 100);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("?Hello world");
		
		new DrawOnGui();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		count++;
		label.setText("Number of clicks: " + count);
	}

}
