package naiveBayes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Processing.Stopwords;
import modelLayer.Tweet;
import modelLayer.TweetStorage;

public class Multinomial extends NaiveBayes implements java.io.Serializable {
    ProbabilityModel probabilityModel = null;
    ArrayList<String> C;
    Stopwords stopwords = new Stopwords();

    /**
     * @param C : set of possible classes
     * @param D : the trainingset, aka the document set D, in this case a list of tweet
     * @return Probability object : the model used to later classify new stweets
     */
    @Override
    public void train(ArrayList<String> C, TweetStorage D) {
        this.C = C;
        Map<String, Double> prior = new HashMap<String, Double>();
        Map<ArrayList<String>, Double> condprob = new HashMap<ArrayList<String>, Double>();

        List<String> V = extractVocabulary(D);
        double N = D.size();
        for (String c : C) {
            double totalT_ct = 0;

            double N_c = countTweetsInClass(D, c);
            prior.put(c, N_c / N);
            String text_c = concatenateTextOfAllTweetsInClass(D, c);

            for (String t : V)
                totalT_ct += countTokensInTextInClass(text_c, t);
            //totalT_ct += countTokensInTextInClass(text_c, t) + 1; // this is an alternative to adding the vocab size at the computation

            for (String t : V) {
                double T_ct = countTokensInTextInClass(text_c, t);
                condprob.put(new ArrayList<String>(Arrays.asList(t, c)), (T_ct + 1) / (totalT_ct + V.size()));
            }
        }
        this.probabilityModel = new ProbabilityModel(V, prior, condprob);
    }

    /**
     * @param tweet : is the tweet to classify
     * @return c : the class the provided tweet is set to
     */

    @Override
    public String apply(Tweet tweet) throws Exception {
        if (this.probabilityModel == null)
            throw new Exception("Classifier needs to be trained before evaluation");

        Map<String, Double> score = new HashMap<String, Double>();
        List<String> W = extractTokens(this.probabilityModel.getVocabulary(), tweet);

        for (String c : C) {
            score.put(c, Math.log10(this.probabilityModel.getPriorProbability(c)));
            for (String t : W) {
                if (this.stopwords.contains(t))
                    continue;
                else
                    score.put(c, score.get(c) + Math.log10(this.probabilityModel.getConditionalProbability(t, c)));
            }
        }
        // return the class with the highest probability value
        return classWHighestProbability(score);
    }

    @Override
    public Map<String, Double> applyGetScore(ProbabilityModel probability, Tweet tweet) throws Exception {
        if (this.probabilityModel == null)
            throw new Exception("Classifier needs to be trained before evaluation");

        Map<String, Double> score = new HashMap<String, Double>();
        List<String> W = extractTokens(probability.getVocabulary(), tweet);

        for (String c : C) {
            score.put(c, Math.log10(probability.getPriorProbability(c)));
            for (String t : W) {
                score.put(c, score.get(c) + Math.log10(probability.getConditionalProbability(t, c)));
            }
        }
        return score;
    }

    public String applyProbability(Tweet tweet) throws Exception {
        if (this.probabilityModel == null)
            throw new Exception("Classifier needs to be trained before evaluation");

        Map<String, Double> score = new HashMap<String, Double>();
        List<String> W = extractTokens(this.probabilityModel.getVocabulary(), tweet);

        for (String c : C) {
            score.put(c, this.probabilityModel.getPriorProbability(c));
            for (String t : W) {
                score.put(c, score.get(c) * this.probabilityModel.getConditionalProbability(t, c));
            }
        }
        // return the class with the highest probability value
        return classWHighestProbability(score);
    }


    public Map<String, Double> applyProbabilityGetScore(Tweet tweet) throws Exception {
        if (this.probabilityModel == null)
            throw new Exception("Classifier needs to be trained before evaluation");

        Map<String, Double> score = new HashMap<String, Double>();
        List<String> W = extractTokens(this.probabilityModel.getVocabulary(), tweet);

        for (String c : C) {
            score.put(c, this.probabilityModel.getPriorProbability(c));
            for (String t : W) {
                score.put(c, score.get(c) * this.probabilityModel.getConditionalProbability(t, c));
            }
        }
        // return the class with the highest probability value
        return score;
    }


    protected static String classWHighestProbability(Map<String, Double> score) {
        String c = null;
        for (Map.Entry<String, Double> entry : score.entrySet()) {
            if (c == null)
                c = entry.getKey();
            else if (entry.getValue() > score.get(c))
                c = entry.getKey();
        }
        return c;
    }


    protected double countTweetsInClass(TweetStorage tweets, String c) {
        double counter = 0;
        for (Tweet tweet : tweets) {
            if (tweet.getExpectedClassLabel().equals(c))
                counter++;
        }
        return counter;
    }

    private double countTokensInTextInClass(String text, String token) {
        double counter = 0;
        String[] words = text.split(" ");
        for (String word : words) {
            if (word.equals(token))
                counter++;
        }
        return counter;
    }

    public ProbabilityModel getLearnedProbabilityModel() {
        return probabilityModel;
    }

    public static Multinomial loadClassifier(String filePath) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
            Multinomial learnedClassifier = (Multinomial) ois.readObject();
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

    public Stopwords getStopwords() {
        return stopwords;
    }

    public void setStopwords(Stopwords stopwords) {
        this.stopwords = stopwords;
    }
}
