package csproject1;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class DummyPredictor extends Predictor {
	
	public ArrayList<DataPoint> data;

	public ArrayList<DataPoint> readData(String filename) {

		ArrayList<DataPoint> data = new ArrayList<DataPoint>(20);

		try {
			Scanner fileData = new Scanner(new File(filename));
			
			while (fileData.hasNextLine()) {
				Scanner line = new Scanner(fileData.nextLine());
				DataPoint point = new DataPoint();
				point.setF1(line.nextDouble());
				point.setF2(line.nextDouble());
				point.setLabel(line.next());
				point.setIsTest(line.nextBoolean());
				data.add(point);
			}

			fileData.close();
			
		} catch (FileNotFoundException ex) {
			System.out.println("File Not Found");
		}
		
		this.data = data;
		return data;

	}

	public String test(DataPoint data) {
		
		if (Math.abs(data.getF1() - data.getF2()) <= 2.0) {
			return "Small";
		} else {
			return "Big";
		}

	}

	public double getAccuracy(ArrayList<DataPoint> data) {
		
		double total = 15;
		double right = 0;
		
		for (DataPoint point : data) {
			if ((point.getIsTest() == true) && test(point).equals(point.getLabel())) {
				right++;
			}
		}
		
		return (right / total) * 100;

	}

	public double getPrecision(ArrayList<DataPoint> data) {
		
		return 100 - getAccuracy(data);

	}

}
