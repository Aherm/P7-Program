package modelLayer;

public class EvaluationModel {
	String evalMethod;
	int foldNum, TP, TN, FP, FN;
	double precision, recall, tpRate, fpRate, accuracy;

	public EvaluationModel(String evalMethod, int foldNum, int TP, int TN, int FP, int FN) {
		this.evalMethod = evalMethod;
		this.foldNum = foldNum;
		this.TP = TP;
		this.TN = TN;
		this.FP = FP;
		this.FN = FN;
		this.precision = calculatePrecision(TP, FP);
		this.recall = calculateRecall(TP, FN);
		this.tpRate = calculateTPRate(TP, FN);
		this.fpRate = calculateFPRate(FP, TN);
		this.accuracy = calculateAccuracy(TP, TN, FP, FN);
	}

	public void printEvaluation() {
		System.out.println("---------EVALUATION BEGUN---------");
		System.out.println("Evaluation method used: " + evalMethod);
		System.out.println("Fold #:    " + this.foldNum);
		System.out.println("Precision: " + this.precision);
		System.out.println("Recall:    " + this.recall);
		System.out.println("Accuracy:  " + this.accuracy);
		System.out.println("TP Rate:   " + this.tpRate);
		System.out.println("FP Rate:   " + this.fpRate);
		System.out.println("# of TP:   " + this.TP);
		System.out.println("# of TN:   " + this.TN);
		System.out.println("# of FP:   " + this.FP);
		System.out.println("# of FN:   " + this.FN);
		System.out.println("---------EVALUATION ENDED---------");
	}

	public int getFoldNum() {
		return this.foldNum;
	}
	
	public int getTP() {
		return this.TP;
	}
	
	public int getTN() {
		return this.TN;
	}
	
	public int getFP() {
		return this.FP;
	}

	public int getFN() {
		return this.FN;
	}
	
	public double getPrecision() {
		return this.precision;
	}

	public double getRecall() {
		return this.recall;
	}

	public double getTpRate() {
		return this.tpRate;
	}

	public double getFpRate() {
		return this.fpRate;
	}
	
	public double getAccuracy() {
		return this.accuracy;
	}
	
	private double calculatePrecision(double TP, double FP) {
		return TP / (TP + FP);
	}
	
	private double calculateRecall(double TP, double FN) {
		return TP / (TP + FN);
	}
	
	private double calculateTPRate(double TP, double FN) {
		return calculateRecall(TP, FN);
	}
	
	private double calculateFPRate(double FP, double TN) {
		return FP / (FP + TN);
	}
	
	private double calculateAccuracy(double TP, double TN, double FP, double FN) {
		double total = TP + TN + FP + FN;
		double positives = TP + TN;
		return positives / total;
	}
	
}
