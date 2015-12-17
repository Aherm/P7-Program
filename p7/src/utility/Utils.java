package utility;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
				switch (counter) {
				case 0:
					document.setMatchedToken(token);
					break;
				case 1:
					document.setID(Long.parseLong(token));
					break;
				case 2:
					document.setText(token);
					break;
				case 3:
					document.setClassLabel(token);
					break;
				}
				counter++;
			}
			counter = 0;
			initializedData.add(document);
		}
		dataSet = convertDataToTweet(initializedData);
		System.out.println(dataSet.size() + "," + initializedData.size());
		return dataSet;
	}
	
	public static TweetStorage getDataFromFileWithGeo(String filePath) {
		ArrayList<String> data = Data.fetchDataFromFile(filePath);
		ArrayList<Document> initializedData = new ArrayList<Document>();
		TweetStorage dataSet = new TweetStorage();
		for (String line : data) {
			int counter = 0;
			Document document = new Document();

			for (String token : line.split(",")) {
				switch (counter) {
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
		List<Long> tweetIDs = new ArrayList<Long>();
		for (Document d : initializedData) {
			if (!tweetIDs.contains(d.getID())) {
				tweetIDs.add(d.getID());
				Tweet t = new Tweet(d.getID(), 1, 1, 1, d.getText(), new Date());
				t.setExpectedClassLabel(d.getClassLabel());
				Preprocessor.processTweet(t);
				dataSet.add(t);
			}
		}
		return dataSet;
	}

	private static String removeDoubleQuotes(String s) {
		String returnString = "";
		if (s.startsWith("\"")) {
			returnString = s.substring(1, s.length() - 1);
		} else {
			returnString = s;
		}
		return returnString;
	}

	private static long parseTweetID(String s) {
		String number = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) != 'E') {
				number += s.charAt(i);
			} else if (s.charAt(i) == 'E') {
				break;
			}
		}

		double realNumber = Double.parseDouble(number);
		realNumber = realNumber * (Math.pow(10, 17));
		long returnNum = (long) realNumber;
		return returnNum;
	}

	public static void printPositivesAndNegatives(String filePath) {
		ArrayList<String> data = Data.fetchDataFromFile(filePath);
		double numPositives = 0;
		double numNegatives = 0;
		double totalNum = 0;
		double percentagePositives = 0;
		double percentageNegatives = 0;
		List<String> tweetIDs = new ArrayList<String>();
		for (String line : data) {
			int counter = 0;
			
			for (String token : line.split(",")) {
				
				switch (counter) {
				case 0:
					System.out.println(token);
					break;
				case 1:
					break;
				case 2:
					break;
				case 3:
					// Classlabel
					if (Integer.parseInt(token) == 10) {
						System.out.println("W T F M8, CLASSLABEL WAS 10");
					}
					if (Integer.parseInt(token) == 11) {
						System.out.println("W T F M8, CLASSLABEL WAS 11");
					}
					if (Integer.parseInt(token) == 0) {
						numNegatives++;
					}
					if (Integer.parseInt(token) == 1) {
						numPositives++;
					}
					break;
				case 4:
					
					break;
				}
				counter++;
			}
			counter = 0;
		}
		
		totalNum = numPositives + numNegatives;
		percentagePositives = numPositives / totalNum * 100;
		percentageNegatives = numNegatives / totalNum * 100;
		System.out.println("Number of positives: " + numPositives + " (" + percentagePositives + ")");
		System.out.println("Number of negatives: " + numNegatives + " (" + percentageNegatives + ")");
		System.out.println("Total instances:     " + totalNum);
	}

	// shit code please ignore

	public static void doNotuse(String filepath, String filepath2) {
		ArrayList<String> listofSubset = Data.fetchDataFromFile(filepath);
		ArrayList<String> listofEntireset = Data.fetchDataFromFile(filepath2);

		ArrayList<String> restaurantsSeen = new ArrayList<String>();
		ArrayList<String> allrestaurants = new ArrayList<String>();

		System.out.println("starting seen");
		for (String line : listofSubset) {
			String[] split = line.split(",");
			if (!restaurantsSeen.contains(split[0]))
				restaurantsSeen.add(split[0]);
		}
		System.out.println("starting all");
		for (String line : listofEntireset) {
			String[] split = line.split(";");
			if (!allrestaurants.contains(split[0]))
				allrestaurants.add(split[0]);
		}

		System.out.println("starting remove");
		for (String line : restaurantsSeen) {
			if (allrestaurants.contains(line))
				allrestaurants.remove(line);
		}

		Collections.shuffle(allrestaurants);
		ArrayList<String> chosenRestaurants = new ArrayList<String>();

		for (int i = 0; i < 2000; i++) {
			chosenRestaurants.add(allrestaurants.get(i));
		}
		int totallines = 0;
		System.out.println("starting last thing");
		StringBuilder builder = new StringBuilder();
		for (String restaurant : chosenRestaurants) {
			int counter = 0;
			for (String line : listofEntireset) {
				String[] split = line.split(";");
				if (restaurant.equals(split[0]) && counter < 10) {
					builder.append(line + "\n");
					counter++;
					totallines++;
				}
			}
		}

		System.out.println(totallines);
		try {
			PrintWriter print = new PrintWriter("tester.csv");
			print.write(builder.toString());
			print.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
