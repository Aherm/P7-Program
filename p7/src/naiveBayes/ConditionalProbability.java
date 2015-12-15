package naiveBayes;

public class ConditionalProbability {
    private String token;
    private String cls;
    private double probability;

    public ConditionalProbability(String token, String cls, double probability) {
        this.token = token;
        this.probability = probability;
        this.cls = cls;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public static double findClassDifference(ConditionalProbability positiveCondProb, ConditionalProbability negativeCondProb) throws Exception{
        if (positiveCondProb.getToken().equals(negativeCondProb.getToken()))
            return positiveCondProb.getProbability() - negativeCondProb.getProbability();
        else
            throw new Exception("The probability parameters have to be of the same token");
    }
}

