package naiveBayes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class MultinomialBigDecimal extends NaiveBayes implements java.io.Serializable {
    ProbabilityModelBigDecimal probabilityModel = null;
    ArrayList<String> C;

    /**
     * @param C : set of possible classes
     * @param D : the trainingset, aka the document set D, in this case a list of tweet
     * @return Probability object : the model used to later classify new stweets
     */
    public void trainBigDecimal(ArrayList<String> C, TweetStorage D) {
        this.C = C;
        Map<String, BigDecimal> prior = new HashMap<String, BigDecimal>();
        Map<ArrayList<String>, BigDecimal> condprob = new HashMap<ArrayList<String>, BigDecimal>();

        List<String> V = extractVocabulary(D);
        BigDecimal N = new BigDecimal(D.size());
        for (String c : C) {
            BigDecimal totalT_ct = new BigDecimal(0);

            BigDecimal N_c = countTweetsInClass(D, c);
            prior.put(c, N_c.divide(N, 2, RoundingMode.HALF_UP));
            String text_c = concatenateTextOfAllTweetsInClass(D, c);

            for (String t : V)
                totalT_ct = totalT_ct.add(countTokensInTextInClass(text_c, t));
            //totalT_ct += countTokensInTextInClass(text_c, t) + 1; // this is an alternative to adding the vocab size at the computation

            for (String t : V) {
                BigDecimal T_ct = countTokensInTextInClass(text_c, t);
                condprob.put(new ArrayList<String>(Arrays.asList(t, c)),
                        (T_ct.add(new BigDecimal(1))).divide(totalT_ct.add(new BigDecimal(V.size())), 2, RoundingMode.HALF_UP));
            }
        }
        probabilityModel = new ProbabilityModelBigDecimal(V, prior, condprob);
    }

    /**
     * @param tweet : is the tweet to classify
     * @return c : the class the provided tweet is set to
     */

    public String applyBigDecimal(Tweet tweet) throws Exception {
        if (this.probabilityModel == null)
            throw new Exception("Classifier needs to be trained before evaluation");

        Map<String, BigDecimal> score = new HashMap<String, BigDecimal>();
        List<String> W = extractTokens(this.probabilityModel.getVocabulary(), tweet);

        for (String c : C) {
            score.put(c, this.probabilityModel.getPriorProbability(c));
            for (String t : W) {
                score.put(c, score.get(c).multiply(this.probabilityModel.getConditionalProbability(t, c)));
            }
        }
        return classWHighestProbability(score);
    }

    public Tweet applyGetProbability(Tweet tweet) throws Exception {
        if (this.probabilityModel == null)
            throw new Exception("Classifier needs to be trained before evaluation");

        Map<String, BigDecimal> score = new HashMap<String, BigDecimal>();
        List<String> W = extractTokens(this.probabilityModel.getVocabulary(), tweet);

        for (String c : C) {
            score.put(c, this.probabilityModel.getPriorProbability(c));
            for (String t : W) {
                score.put(c, score.get(c).multiply(this.probabilityModel.getConditionalProbability(t, c)));
            }
        }

        for (Map.Entry<String, BigDecimal> entry : score.entrySet()) {
            if (entry.getKey().equals("1"))
                tweet.setProbabilityTrue(entry.getValue());
        }
        return tweet;
    }

    protected BigDecimal countTweetsInClass(TweetStorage tweets, String c) {
        double counter = 0;
        for (Tweet tweet : tweets) {
            if (tweet.getExpectedClassLabel().equals(c))
                counter++;
        }
        return new BigDecimal(counter);
    }

    protected static String classWHighestProbability(Map<String, BigDecimal> score) {
        String c = null;
        for (Map.Entry<String, BigDecimal> entry : score.entrySet()) {
            BigDecimal value = entry.getValue();
            if (c == null)
                c = entry.getKey();
            else if (value.equals(score.get(c)))
                c = entry.getKey();
        }
        return c;
    }

    private BigDecimal countTokensInTextInClass(String text, String token) {
        double counter = 0;
        String[] words = text.split(" ");
        for (String word : words) {
            if (word.equals(token))
                counter++;
        }
        return new BigDecimal(counter);
    }


    @Override
    public void train(ArrayList<String> C, TweetStorage D) {

    }

    @Override
    public String apply(Tweet tweet) {
        return null;
    }

    @Override
    public Map<String, Double> applyGetScore(ProbabilityModel probability, Tweet tweet) {
        return null;
    }

    public static MultinomialBigDecimal loadClassifier(String filePath) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
            MultinomialBigDecimal learnedClassifier = (MultinomialBigDecimal) ois.readObject();
            ois.close();
            return learnedClassifier;
        } catch (Exception ex) {
            System.out.println(ex);
            return null;
        }
    }

    public boolean saveClassifier(String filePath) throws Exception {
        if (this.probabilityModel == null)
            throw new Exception("Classifier needs to be trained before evaluation");

        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(filePath));

            oos.writeObject(this);
            oos.flush();
            oos.close();
            return true;
        } catch (IOException iex) {
            System.out.println(iex);
            return false;
        }
    }

}
