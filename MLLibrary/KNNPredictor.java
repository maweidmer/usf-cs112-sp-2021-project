package csproject3;

import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Random;

public class KNNPredictor extends Predictor {

	private int k;
	private int passNumSurv;
	private int passNumDec;
	private ArrayList<DataPoint> passengers;

	KNNPredictor(int kVal) {
		this.k = kVal;
		this.passNumSurv = 0;
		this.passNumDec = 0;
	}

	public ArrayList<DataPoint> readData(String filename) {

		ArrayList<DataPoint> passengers = new ArrayList<DataPoint>();
		
		int numPassTotal = 0;

		Random rand = new Random();

		try (Scanner scanner = new Scanner(new File(filename))) {

			// Skip column labels
			scanner.nextLine();

			while (scanner.hasNextLine()) {
				ArrayList<String> record = getRecordFromLine(scanner.nextLine());

				// TODO: Select the columns from the records and create a DataPoint object

				// Get last two entries (age, fare)

				double age;
				double fare;

				try {
					age = Double.valueOf(record.get(record.size() - 2));
					fare = Double.valueOf(record.get(record.size() - 1));
				} catch (NumberFormatException a) {
					continue;
				}

				String label = record.get(1);

				double randNum = rand.nextDouble();

				DataPoint passenger = new DataPoint();
				passenger.setF1(age);
				passenger.setF2(fare);
				passenger.setLabel(label);

				if (randNum < 0.9) {

					passenger.setIsTest(false);

					if (label.equals("0")) {
						this.passNumDec++;
					} else if (label.equals("1")) {
						this.passNumSurv++;
					}

				} else {
					passenger.setIsTest(true);
				}

				// TODO: Store the DataPoint object in a collection

				passengers.add(passenger);
				numPassTotal++;

			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}

		System.out.println(this.passNumSurv);
		System.out.println(this.passNumDec);
		System.out.println(numPassTotal);

		this.passengers = passengers;
		return passengers;

	}

	public String test(DataPoint data) {
		
		int numTraining = this.passNumDec + this.passNumSurv;

		Double[][] arr = new Double[numTraining][2];
		
		int arrInd = 0;

		if (data.getIsTest() == true) {
			
			for (int i = 0; i < this.passengers.size(); i++) {
				
				DataPoint trainPoint = this.passengers.get(i);
				
				if (trainPoint.getIsTest() == false) {
					
					Double distance = this.getDistance(data, trainPoint);
					Double label = Double.valueOf(trainPoint.getLabel());
					
					arr[arrInd][0] = distance;
					arr[arrInd][1] = label;
					arrInd++;
					
				}
				
			}

			java.util.Arrays.sort(arr, new java.util.Comparator<Double[]>() {
				public int compare(Double[] a, Double[] b) {
					return a[0].compareTo(b[0]);
				}
			});

			int kSurv = 0;
			int kDec = 0;
			
			for (int j = 0; j < this.k; j++) {
				if (arr[j][1] == 1.0) {
					kSurv++;
				} else {
					kDec++;
				}
			}

			if (kSurv > kDec) {
				return "1";
			} else {
				return "0";
			}
			
		} else {
			return "Not a test point";
		}

	}

	public double getAccuracy(ArrayList<DataPoint> data) {
		
		double truePositive = 0;
		double falsePositive = 0;
		double trueNegative = 0;
		double falseNegative = 0;
		
		String compLabel;
		String realLabel;
		
		for (DataPoint passenger: data) {
			
			compLabel = this.test(passenger);
			realLabel = passenger.getLabel();
			
			if (compLabel.equals("1") && realLabel.equals("1")) {
				truePositive++;
			} else if (compLabel.equals("1") && realLabel.equals("0")) {
				falsePositive++;
			} else if (compLabel.equals("0") && realLabel.equals("1")) {
				falseNegative++;
			} else if (compLabel.equals("0") && realLabel.equals("0")) {
				trueNegative++;
			}
		}
		
		return ((truePositive + trueNegative) / (truePositive + trueNegative + falsePositive + falseNegative)) * 100;
	}

	public double getPrecision(ArrayList<DataPoint> data) {
		
		double truePositive = 0;
		double falsePositive = 0;
		double trueNegative = 0;
		double falseNegative = 0;
		
		String compLabel;
		String realLabel;
		
		for (DataPoint passenger: data) {
			
			compLabel = this.test(passenger);
			realLabel = passenger.getLabel();
			
			if (compLabel.equals("1") && realLabel.equals("1")) {
				truePositive++;
			} else if (compLabel.equals("1") && realLabel.equals("0")) {
				falsePositive++;
			} else if (compLabel.equals("0") && realLabel.equals("1")) {
				falseNegative++;
			} else if (compLabel.equals("0") && realLabel.equals("0")) {
				trueNegative++;
			}
		}
		
		return (truePositive / (truePositive + falseNegative)) * 100;
	}

	// --------- Helper functions --------------

	// Helper function to split the line by commas and
	// return the values as a List of String
	private ArrayList<String> getRecordFromLine(String line) {
		ArrayList<String> values = new ArrayList<String>();

		try (Scanner rowScanner = new Scanner(line)) {
			rowScanner.useDelimiter(",");
			while (rowScanner.hasNext()) {
				values.add(rowScanner.next());
			}
		}
		return values;
	}

	private double getDistance(DataPoint p1, DataPoint p2) {

		double x1 = p1.getF1();
		double x2 = p2.getF1();

		double y1 = p1.getF2();
		double y2 = p2.getF2();

		double xDifSqr = Math.pow(x2 - x1, 2);
		double yDifSqr = Math.pow(y2 - y1, 2);

		double distance = Math.sqrt(xDifSqr + yDifSqr);
		return distance;

	}

}