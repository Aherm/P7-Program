package modelLayer;

public class EvaluationModel {
	String evalMethod;
	int foldNum, TP, TN, FP, FN;
	double precision, recall, tpRate, fpRate;

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
		this.fpRate = calcuateFPRate(FP, TN);
	}

	public void printEvaluation() {
		System.out.println("---------EVALUATION BEGUN---------");
		System.out.println("Evaluation method used: " + evalMethod);
		System.out.println("Fold #:    " + this.foldNum);
		System.out.println("Precision: " + this.precision);
		System.out.println("Recall:    " + this.recall);
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
	
	private double calculatePrecision(double TP, double FP) {
		return TP / (TP + FP);
	}
	
	private double calculateRecall(double TP, double FN) {
		return TP / (TP + FN);
	}
	
	private double calculateTPRate(double TP, double FN) {
		return calculateRecall(TP, FN);
	}
	
	private double calcuateFPRate(double FP, double TN) {
		return FP / (FP + TN);
	}
}
