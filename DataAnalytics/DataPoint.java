package csproject1;

public class DataPoint {

	private double f1;
	private double f2;
	private String label;
	private Boolean isTest;

	public DataPoint(double in1, double in2, String labelIn, Boolean testIn) {
		this.f1 = in1;
		this.f2 = in2;
		this.label = labelIn;
		this.isTest = testIn;
	}

	public DataPoint() {
		this(0.0, 0.0, "", false);
	}

	public void setF1(double in1) {
		this.f1 = in1;
	}

	public void setF2(double in2) {
		this.f2 = in2;
	}

	public void setLabel(String labelIn) {
		this.label = labelIn;
	}

	public void setIsTest(boolean test) {
		this.isTest = test;
	}

	public double getF1() {
		return this.f1;
	}

	public double getF2() {
		return this.f2;
	}

	public String getLabel() {
		return this.label;
	}

	public boolean getIsTest() {
		return this.isTest;
	}

}