package csproject3;

import java.util.ArrayList;

public abstract class Predictor {

	public abstract ArrayList<DataPoint> readData(String filename);

	public abstract String test(DataPoint data);

	public abstract double getAccuracy(ArrayList<DataPoint> data);

	public abstract double getPrecision(ArrayList<DataPoint> data);

}
