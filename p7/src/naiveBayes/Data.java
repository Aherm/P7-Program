package naiveBayes;

import modelLayer.Tweet;
import modelLayer.TweetStorage;
import testLayer.TestNaiveBayes;
import utility.Utils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

public class Data {
    public static String[] readLines(URL url) throws IOException {

        Reader fileReader = new InputStreamReader(url.openStream(), Charset.forName("UTF-8"));
        List<String> lines = new ArrayList<String>();
        ;
        try {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return lines.toArray(new String[lines.size()]);
    }

    public static ArrayList<String> initializeClassLabels() {
        return new ArrayList<String>(Arrays.asList("No", "Yes"));
        //return new ArrayList<String>(Arrays.asList("UK","China","poultry","coffee","elections","sports"));
    }


    public static Map<String, String[]> getTrainingExamples() {
        Map<String, URL> trainingFiles = new HashMap<String, URL>();
        trainingFiles.put("English", TestNaiveBayes.class.getResource("training.language.en.txt"));

        //loading examples in memory
        Map<String, String[]> trainingExamples = new HashMap<String, String[]>();
        try {
            for (Map.Entry<String, URL> entry : trainingFiles.entrySet()) {
                trainingExamples.put(entry.getKey(), Data.readLines(entry.getValue()));
            }
        } catch (IOException iex) {
            System.out.println(iex);
        }
        return trainingExamples;
    }


    public static TweetStorage initializeTrainingSet(ArrayList<String> classLabels) {
        return new TweetStorage(
                new ArrayList<Tweet>(Arrays.asList(
                        new Tweet(2, 2, 3, 4, "Chinese Beijing Chinese", new Date(), -73, 41, classLabels.get(1)),
                        new Tweet(2, 2, 3, 4, "Chinese Chinese Shanghai", new Date(), -73, 41, classLabels.get(1)),
                        new Tweet(2, 2, 3, 4, "Chinese Macao", new Date(), -73, 41, classLabels.get(1)),
                        new Tweet(2, 2, 3, 4, "Tokyo Japan Chinese", new Date(), -73, 41, classLabels.get(0))

                        /*
                        new Tweet(2, 2, 3, 4, "congestion London London", new Date(), -73, 41, classLabels.get(0)),
                        new Tweet(2, 2, 3, 4, "Parliament Big Ben", new Date(), -73, 41, classLabels.get(0)),
                        new Tweet(2, 2, 3, 4, "Windsor the Queen", new Date(), -73, 41, classLabels.get(0)),
                        new Tweet(2, 2, 3, 4, "Olympics Beijing", new Date(), -73, 41, classLabels.get(1)),
                        new Tweet(2, 2, 3, 4, "tourism Great Wall", new Date(), -73, 41, classLabels.get(1)),
                        new Tweet(2, 2, 3, 4, "Mao communist", new Date(), -73, 41, classLabels.get(1)),
                        new Tweet(2, 2, 3, 4, "feed chicken", new Date(), -73, 41, classLabels.get(2)),
                        new Tweet(2, 2, 3, 4, "pate ducks", new Date(), -73, 41, classLabels.get(2)),
                        new Tweet(2, 2, 3, 4, "bird flu turkey", new Date(), -73, 41, classLabels.get(2)),
                        new Tweet(2, 2, 3, 4, "roasting beans", new Date(), -73, 41, classLabels.get(3)),
                        new Tweet(2, 2, 3, 4, "arabica robusta", new Date(), -73, 41, classLabels.get(3)),
                        new Tweet(2, 2, 3, 4, "Kenya harvest", new Date(), -73, 41, classLabels.get(3)),
                        new Tweet(2, 2, 3, 4, "recount votes", new Date(), -73, 41, classLabels.get(4)),
                        new Tweet(2, 2, 3, 4, "seat run-off", new Date(), -73, 41, classLabels.get(4)),
                        new Tweet(2, 2, 3, 4, "TV ads campaign", new Date(), -73, 41, classLabels.get(4)),
                        new Tweet(2, 2, 3, 4, "diamond baseball", new Date(), -73, 41, classLabels.get(5)),
                        new Tweet(2, 2, 3, 4, "forward soccer", new Date(), -73, 41, classLabels.get(5)),
                        new Tweet(2, 2, 3, 4, "team captain", new Date(), -73, 41, classLabels.get(5))
                    */
                ))
        );
    }

    //Note that these collections will not give the expected result of "China" as none of the words in this
    //tweet have ever been seen before
    public static TweetStorage initializeTestSet() {
        TweetStorage trainingSet = new TweetStorage(
                new ArrayList<Tweet>(Arrays.asList(
                        new Tweet(2, 2, 3, 4, "Chinese Chinese Chinese Tokyo Japan", new Date(), -73, 41)
                        //new Tweet(2, 2, 3, 4, "first private Chinese airline Beijing", new Date(), -73, 41)
                ))
        );
        return trainingSet;
    }

    public static ArrayList<String> fetchDataFromFile(String filePath) {
        ArrayList<String> data = new ArrayList<String>();
        File file = new File(filePath);
        
        try {
        	BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        	String line;
            while ((line = reader.readLine()) != null) {
                //String nextToken = input.next();

                //or to process line by line
                data.add(line);
            }
            reader.close();

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return data;
    }

    public static ArrayList<Document> initializeDataFromFile(String filePath){
        ArrayList<String> data = fetchDataFromFile(filePath);
        ArrayList<Document> initializedData = new ArrayList<Document>();

        for (String tweet : data) {
            int counter = 0;
            Document document = new Document();

            for (String token : tweet.split(";")) {
                switch (counter){
                    case 0:
                        document.setMatchedToken(token);
                        break;
                    case 1:
                        if (Utils.isStringInt(token))
                            document.setID(Integer.parseInt(token));
                        break;
                    case 2:
                        document.setText(token);
                        break;
                    case 3:
                        document.setGeotagged(Boolean.parseBoolean(token));
                        break;
                    case 4:
                        document.setClassLabel(token);
                        break;
                }
                counter++;
            }
            counter = 0;
            initializedData.add(document);
        }
        return initializedData;
    }


    //btc temp test
    public static void main(String[] args) {
        String filePath = "resturant_mentions.csv";
        ArrayList<Document> documents = initializeDataFromFile(filePath);
        System.out.println("documents: " + documents.size());
    }
}
