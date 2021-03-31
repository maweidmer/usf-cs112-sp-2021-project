package csproject1;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Main {

	private static void initAndShowGUI() {

		DummyPredictor tester = new DummyPredictor();

		tester.readData("test.txt");
		
		String accuracy = "";

		accuracy = accuracy + String.format("%.2f", tester.getAccuracy(tester.data)) + "% correct.";
		
		String precision = "";
		
		precision = precision + String.format("%.2f", tester.getPrecision(tester.data))  + "% off target.";

		// A JFrame is a window.
		JFrame myFrame = new JFrame();

		myFrame.setTitle("DummyPredictor Stats");
		myFrame.setLocation(500, 500);

		// The content pane is the meat of the window -- the window minus
		// any menu bars, title bar, close/minimize/maximize buttons, etc.
		Container contentPane = myFrame.getContentPane();

		// We need to set how we want our content pane to lay out the
		// objects we add to it. For now, we'll use a FlowLayout, which
		// places objects left-to-right in a line. (See the javadocs.)
		contentPane.setLayout(new GridLayout(2, 2));

		contentPane.add(new Label("Accuracy: "));
		contentPane.add(new Label(accuracy));
		contentPane.add(new Label("Precision: "));
		contentPane.add(new Label(precision));

		// pack() and setVisible() need to be called to realize and display
		// the window. If you're using an older JDK, you may need to use
		// show() instead of setVisible(true).
		myFrame.pack();
		myFrame.setVisible(true);

	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				initAndShowGUI();
			}
		});

	}

}
