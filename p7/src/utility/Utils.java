package utility;

import java.util.ArrayList;
import java.util.Date;

import businessLogicLayer.Preprocessor;
import modelLayer.Tweet;
import modelLayer.TweetStorage;
import naiveBayes.Data;
import naiveBayes.Document;

public class Utils {

    public static boolean isStringInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    
    public static TweetStorage getDataFromFile(String filePath) {
    	ArrayList<String> data = Data.fetchDataFromFile(filePath);
        ArrayList<Document> initializedData = new ArrayList<Document>();
        TweetStorage dataSet = new TweetStorage();
        for (String line : data) {
            int counter = 0;
            Document document = new Document();

            for (String token : line.split(",")) {
                switch (counter){
                    case 0:
                    	String processedWord = removeDoubleQuotes(token);
                        document.setMatchedToken(processedWord);
                        break;
                    case 1:
                    	long processedID = parseTweetID(token);
                        document.setID(processedID);
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
        dataSet = convertDataToTweet(initializedData);
        return dataSet;
    }
    
    private static TweetStorage convertDataToTweet(ArrayList<Document> initializedData) {
    	TweetStorage dataSet = new TweetStorage();
    	for(Document d : initializedData) {
        	Tweet t = new Tweet(d.getID(), 1, 1, 1, d.getText(), new Date());
        	t.setExpectedClassLabel(d.getClassLabel());
        	Preprocessor.processTweet(t);
        	dataSet.add(t);
        }
    	return dataSet;
    }
    
    private static String removeDoubleQuotes(String s) {
		String returnString = "";
		if(s.startsWith("\"")) {
			returnString = s.substring(1, s.length() - 1);
		} else {
			returnString = s;
		}
		return returnString;
	}
	
	private static long parseTweetID(String s) {
		String number = "";
		for (int i = 0; i < s.length(); i++) {
			if(s.charAt(i) != 'E') {
				number += s.charAt(i);
			} else if(s.charAt(i) == 'E') {
				break;
			}
		}
		
		double realNumber = Double.parseDouble(number);
		realNumber = realNumber * (Math.pow(10, 17));
		long returnNum = (long)realNumber;
		return returnNum;
	}
}
