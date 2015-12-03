package modelLayer;

public class EvaluationModel {
	int foldNum;
	double precision, recall, tpRate, fpRate;

	public EvaluationModel(int foldNum, double precision, double recall, double tpRate, double fpRate) {
		this.foldNum = foldNum;
		this.precision = precision;
		this.recall = recall;
		this.tpRate = tpRate;
		this.fpRate = fpRate;
	}

	public void printEvaluation() {
		System.out.println("---------EVALUATION BEGUN---------");
		System.out.println("Fold #:    " + this.foldNum);
		System.out.println("Precision: " + this.precision);
		System.out.println("Recall:    " + this.recall);
		System.out.println("TP Rate:   " + this.tpRate);
		System.out.println("FP Rate:   " + this.fpRate);
		System.out.println("---------EVALUATION ENDED---------");
	}

	public int getFoldNum() {
		return this.foldNum;
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
}
