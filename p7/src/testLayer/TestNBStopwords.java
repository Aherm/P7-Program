package testLayer;

import Processing.Stopwords;
import businessLogicLayer.Preprocessor;
import fileCreation.GenericPrint;
import modelLayer.TweetStorage;
import naiveBayes.ConditionalProbability;
import naiveBayes.Multinomial;
import naiveBayes.ProbabilityModel;
import utility.Utils;

import java.util.*;

public class TestNBStopwords {
    static ArrayList<String> classLabels = new ArrayList<String>(Arrays.asList("0", "1"));
    static TweetStorage trainingSet = Utils.getDataFromFile("trainingData\\sickTraining.csv");
    static TweetStorage testSet = Utils.getDataFromFile("trainingData\\sickTest.csv");

    public static void testWithStopwords() {
        Preprocessor.processTweets(trainingSet);

        //remove stopwords, iter 1
        Stopwords stopwords = new Stopwords(Arrays.asList("i", "the", "to", "a", "and", "my"));

        //iter 2
        stopwords.add("on");
        stopwords.add("in");
        stopwords.add("is");

        //iter 3
        stopwords.add("it");
        stopwords.add("that");
        stopwords.add("just");


        Multinomial mlb = new Multinomial();
        mlb.setStopwords(stopwords);
        mlb.train(classLabels, trainingSet);
        try {
            mlb.saveClassifier("./classifiers/sickNaiveBayes.model");
            System.out.println("MLB saved\n");
        } catch (Exception ex) {
            System.out.println(ex);
        }

        Multinomial nb = Multinomial.loadClassifier("./classifiers/sickNaiveBayes.model");
        //nb.setStopwords(stopwords);
        //EvalNB.testSet(testSet, nb);


        ProbabilityModel pm = nb.getLearnedProbabilityModel();
        //pm.setStopwords(stopwords);
        //initialize conditional probabilities
        Map<ArrayList<String>, Double> conditionalProbabilitiesFor0 = new HashMap<ArrayList<String>, Double>();
        Map<ArrayList<String>, Double> conditionalProbabilitiesFor1 = new HashMap<ArrayList<String>, Double>();
        List<ConditionalProbability> conditionalProbabilitiesList0 = new ArrayList<ConditionalProbability>();
        List<ConditionalProbability> conditionalProbabilitiesList1 = new ArrayList<ConditionalProbability>();
        for (String cls : classLabels) {
            if (cls.equals("0"))
                conditionalProbabilitiesFor0 = pm.getConditionalProbabilitiesForClass(cls);
            else if (cls.equals("1"))
                conditionalProbabilitiesFor1 = pm.getConditionalProbabilitiesForClass(cls);
        }

        //put the probabilities in lists instead as well
        getCondProbInList(conditionalProbabilitiesFor0, conditionalProbabilitiesList0);
        getCondProbInList(conditionalProbabilitiesFor1, conditionalProbabilitiesList1);

        List<ConditionalProbability> topKCondProbFor0 = ProbabilityModel.getTopKProbabilities(conditionalProbabilitiesFor0, 10);
        List<ConditionalProbability> topKCondProbFor1 = ProbabilityModel.getTopKProbabilities(conditionalProbabilitiesFor1, 10);
        List<String> results = new ArrayList<String>();
        writeProbabilityResultsToList(topKCondProbFor0, results);
        writeProbabilityResultsToList(topKCondProbFor1, results);

        StringBuilder finalStrToPrint = new StringBuilder();
        for (String res : results) {
            finalStrToPrint.append(res + "\n");
            finalStrToPrint.append("\n");
        }

        //print to file
        GenericPrint.PRINTER("./statistics/topK.txt", finalStrToPrint.toString());

        System.out.println("-------------------------------");
        System.out.println("done with top k");
        System.out.println("-------------------------------");

        findCondProbDiffs(topKCondProbFor0, topKCondProbFor1, conditionalProbabilitiesList0, conditionalProbabilitiesList1);
    }

    private static void findCondProbDiffs(
            List<ConditionalProbability> topKCondProbFor0, List<ConditionalProbability> topKCondProbFor1,
            List<ConditionalProbability> conditionalProbabilitiesList0, List<ConditionalProbability> conditionalProbabilitiesList1){
        try {
            System.out.println("Top k diff for non-sick");
            for (ConditionalProbability condProb0 : topKCondProbFor0) {
                for (ConditionalProbability condProb1 : conditionalProbabilitiesList1) {
                    if (condProb1.getToken().equals(condProb0.getToken())) {
                        double tokenClsDiff = ConditionalProbability.findClassDifference(condProb1, condProb0);
                        System.out.println("token: '" + condProb1.getToken() + "' difference: " + tokenClsDiff);
                        if (tokenClsDiff > 0)
                            System.out.println("For this token sick dominates the classification\n");
                        else
                            System.out.println("For this token non-sick dominates the classification\n");
                    }
                }
            }
            System.out.println("--------------------------------------------------------------------------------------");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }

        try {
            System.out.println("Top k diff for sick");
            for (ConditionalProbability condProbSick : topKCondProbFor1) {
                for (ConditionalProbability condProbNonSick : conditionalProbabilitiesList0) {
                    if (condProbSick.getToken().equals(condProbNonSick.getToken())) {
                        double tokenClsDiff = ConditionalProbability.findClassDifference(condProbSick, condProbNonSick);
                        System.out.println("token: '" + condProbSick.getToken() + "' difference: " + tokenClsDiff);
                        if (tokenClsDiff > 0)
                            System.out.println("For this token sick dominates the classification\n");
                        else
                            System.out.println("For this token non-sick dominates the classification\n");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }
    }

    private static void getCondProbInList(Map<ArrayList<String>, Double> conditionalProbabilities, List<ConditionalProbability> conditionalProbabilitiesList) {
        for (Map.Entry<ArrayList<String>, Double> entry : conditionalProbabilities.entrySet()) {
            ArrayList<String> key = entry.getKey();
            String token = key.get(0);
            String cls = key.get(1);
            conditionalProbabilitiesList.add(new ConditionalProbability(token, cls, entry.getValue()));
        }
    }

    private static void writeProbabilityResultsToList(List<ConditionalProbability> topKCondProb, List<String> results) {
        for (ConditionalProbability condProb : topKCondProb) {
            String strResult = "token: '" + condProb.getToken() + "' class: " + condProb.getCls() + " Probability: " + condProb.getProbability();
            results.add(strResult);
            System.out.println(strResult);
        }
    }

}
