package csproject2;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Label;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.Scanner;

public class Driver {

	private static void initAndShowGUI() {

		Scanner input = new Scanner(System.in);
		
		System.out.print("Enter an odd k value: ");
		int kIn = input.nextInt();
		
		input.close();
		
		if (kIn > 1045 || kIn < 0 || kIn % 2 == 0) {
			System.out.println("Invalid k value. Using default value of 25");
			kIn = 25;
		}
		
		KNNPredictor predictor = new KNNPredictor(kIn);

		ArrayList<DataPoint> passengers = predictor.readData("titanic.csv");
		
		String accuracy = "";

		accuracy = accuracy + String.format("%.2f", predictor.getAccuracy(passengers)) + "%";
		
		String precision = "";
		
		precision = precision + String.format("%.2f", predictor.getPrecision(passengers))  + "%";

		// A JFrame is a window.
		JFrame myFrame = new JFrame();

		myFrame.setTitle("Titanic Predictor Statistics");
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
