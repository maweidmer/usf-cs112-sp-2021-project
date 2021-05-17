package csproject3;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Label;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

public class Graph extends JPanel {

	private static final long serialVersionUID = 1L;
	private int labelPadding = 40;
	private Color lineColor = new Color(255, 255, 254);

	// TODO: Add point colors for each type of data point
	private Color truePos = new Color(0, 0, 255); // blue
	private Color falsePos = new Color(0, 255, 255); // cyan
	private Color trueNeg = new Color(255, 255, 0); // red
	private Color falseNeg = new Color(255, 0, 0); // yellow

	private Color gridColor = new Color(200, 200, 200, 200);
	private static final Stroke GRAPH_STROKE = new BasicStroke(2f);

	// TODO: Change point width as needed
	private static int pointWidth = 8;

	// Number of grids and the padding width
	private int numXGridLines = 6;
	private int numYGridLines = 6;
	private int padding = 40;

	private static ArrayList<DataPoint> data;

	// TODO: Add a private KNNPredictor variable
	private static KNNPredictor predictor;
	private static String accuracy;
	private static String precision;
	private static Graph mainPanel;

	/**
	 * Constructor method
	 */
	public Graph(int K, String fileName) {
		// Generate random data point

		predictor = new KNNPredictor(K);

		this.data = predictor.readData(fileName);
		accuracy = String.format("%.2f", predictor.getAccuracy(data)) + "%";
		precision = String.format("%.2f", predictor.getPrecision(data)) + "%";

		// TODO: Remove the above logic where random data is generated
		// TODO: instantiate the KNNPredictor variable
		// TODO: Run readData using input filename to split the data to test and
		// training
		// TODO: Set this.data as the output of readData
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		double minF1 = getMinF1Data();
		double maxF1 = getMaxF1Data();
		double minF2 = getMinF2Data();
		double maxF2 = getMaxF2Data();

		g2.setColor(Color.WHITE);
		g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding,
				getHeight() - 2 * padding - labelPadding);
		g2.setColor(Color.BLUE);

		double yGridRatio = (maxF2 - minF2) / numYGridLines;
		for (int i = 0; i < numYGridLines + 1; i++) {
			int x0 = padding + labelPadding;
			int x1 = pointWidth + padding + labelPadding;
			int y0 = getHeight()
					- ((i * (getHeight() - padding * 2 - labelPadding)) / numYGridLines + padding + labelPadding);
			int y1 = y0;
			if (data.size() > 0) {
				g2.setColor(gridColor);
				g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
				g2.setColor(Color.BLACK);
				String yLabel = String.format("%.2f", (minF2 + (i * yGridRatio)));
				FontMetrics metrics = g2.getFontMetrics();
				int labelWidth = metrics.stringWidth(yLabel);
				g2.drawString(yLabel, x0 - labelWidth - 6, y0 + (metrics.getHeight() / 2) - 3);
			}
			g2.drawLine(x0, y0, x1, y1);
		}

		double xGridRatio = (maxF1 - minF1) / numXGridLines;
		for (int i = 0; i < numXGridLines + 1; i++) {
			int y0 = getHeight() - padding - labelPadding;
			int y1 = y0 - pointWidth;
			int x0 = i * (getWidth() - padding * 2 - labelPadding) / (numXGridLines) + padding + labelPadding;
			int x1 = x0;
			if (data.size() > 0) {
				g2.setColor(gridColor);
				g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
				g2.setColor(Color.BLACK);
				String xLabel = String.format("%.2f", (minF1 + (i * xGridRatio)));
				FontMetrics metrics = g2.getFontMetrics();
				int labelWidth = metrics.stringWidth(xLabel);
				g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
			}
			g2.drawLine(x0, y0, x1, y1);
		}

		// Draw the main axis
		g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
		g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding,
				getHeight() - padding - labelPadding);

		// Draw the points
		paintPoints(g2, minF1, maxF1, minF2, maxF2);
	}

	private void paintPoints(Graphics2D g2, double minF1, double maxF1, double minF2, double maxF2) {
		Stroke oldStroke = g2.getStroke();
		g2.setColor(lineColor);
		g2.setStroke(GRAPH_STROKE);
		double xScale = ((double) getWidth() - (3 * padding) - labelPadding) / (maxF1 - minF1);
		double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (maxF2 - minF2);
		g2.setStroke(oldStroke);
		for (int i = 0; i < data.size(); i++) {

			if (data.get(i).getIsTest()) {
				int x1 = (int) ((data.get(i).getF1() - minF1) * xScale + padding + labelPadding);
				int y1 = (int) ((maxF2 - data.get(i).getF2()) * yScale + padding);
				int x = x1 - pointWidth / 2;
				int y = y1 - pointWidth / 2;
				int ovalW = pointWidth;
				int ovalH = pointWidth;

				// TODO: Depending on the type of data and how it is tested, change color here.
				// You need to test your data here using the model to obtain the test value
				// and compare against the true label.
				// Note that depending on how you implemented "test" method, you may need to
				// modify KNNPredictor to store the output from readData.
				// You can also optimize further to compute accuracy and precision in a single
				// iteration.

				String compLabel;
				String realLabel;

				compLabel = predictor.test(data.get(i));
				realLabel = data.get(i).getLabel();

				if (compLabel.equals("1") && realLabel.equals("1")) {
					g2.setColor(truePos);
				} else if (compLabel.equals("1") && realLabel.equals("0")) {
					g2.setColor(falsePos);
				} else if (compLabel.equals("0") && realLabel.equals("1")) {
					g2.setColor(falseNeg);
				} else if (compLabel.equals("0") && realLabel.equals("0")) {
					g2.setColor(trueNeg);
				}

				g2.fillOval(x, y, ovalW, ovalH);
			}

		}

	}

	/*
	 * @Return the min values
	 */
	private double getMinF1Data() {
		double minData = Double.MAX_VALUE;
		for (DataPoint pt : this.data) {
			minData = Math.min(minData, pt.getF1());
		}
		return minData;
	}

	private double getMinF2Data() {
		double minData = Double.MAX_VALUE;
		for (DataPoint pt : this.data) {
			minData = Math.min(minData, pt.getF2());
		}
		return minData;
	}

	/*
	 * @Return the max values;
	 */
	private double getMaxF1Data() {
		double maxData = Double.MIN_VALUE;
		for (DataPoint pt : this.data) {
			maxData = Math.max(maxData, pt.getF1());
		}
		return maxData;
	}

	private double getMaxF2Data() {
		double maxData = Double.MIN_VALUE;
		for (DataPoint pt : this.data) {
			maxData = Math.max(maxData, pt.getF2());
		}
		return maxData;
	}

	/* Mutator */
	public void setData(ArrayList<DataPoint> data) {
		this.data = data;
		invalidate();
		this.repaint();
	}

	/* Accessor */
	public List<DataPoint> getData() {
		return data;
	}

	/*
	 * Run createAndShowGui in the main method, where we create the frame too and
	 * pack it in the panel
	 */
	private static void createAndShowGui(int K, String fileName) {

		/* Main panel */
		mainPanel = new Graph(K, fileName);

		// Feel free to change the size of the panel
		mainPanel.setPreferredSize(new Dimension(900, 700));

		/* creating the frame */
		JFrame frame = new JFrame("CS 112 Lab Part 4");
		Container contentPane = frame.getContentPane();

		contentPane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 2;
		c.gridwidth = 2;
		contentPane.add(mainPanel, c);

		Label accprec = new Label("<html>Accuracy: " + accuracy + "<br/>Precision: " + precision + "</html>");
		c.gridx = 2;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.ipadx = 40;
		contentPane.add(accprec, c);

		Label sliderLabel = new Label("Choose the majority value");
		c.gridx = 0;
		c.gridy = 3;
		c.ipady = 40;
		c.insets = new Insets(20, 20, 20, 20);
		c.anchor = c.LINE_END;
		contentPane.add(sliderLabel, c);

		JSlider kSlider = new JSlider(2, 25, 5);
		kSlider.setMajorTickSpacing(5);
		kSlider.setMinorTickSpacing(1);
		kSlider.setPaintTicks(true);
		kSlider.setSnapToTicks(true);
		c.gridx = 1;
		c.gridy = 3;
		c.anchor = c.LINE_START;
		c.ipady = 20;
		contentPane.add(kSlider, c);

		JButton runTest = new JButton("Run Test");
		c.gridx = 1;
		c.gridy = 4;
		c.ipady = 10;
		runTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int sliderVal = (kSlider.getValue() * 2) + 1;
				predictor = new KNNPredictor(sliderVal);
				data = predictor.readData(fileName);
				accuracy = String.format("%.2f", predictor.getAccuracy(data)) + "%";
				precision = String.format("%.2f", predictor.getPrecision(data)) + "%";
				accprec.setText("<html>Accuracy: " + accuracy + "<br/>Precision: " + precision + "</html>");
				mainPanel.validate();
				mainPanel.repaint();

			}
		});
		contentPane.add(runTest, c);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	/* The main method runs createAndShowGui */
	public static void main(String[] args) {
		int K = 43; // A value of K selected
		String fileName = "titanic.csv"; // TODO: Change this to titanic.csv
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGui(K, fileName);
			}
		});
	}
}
