package streaming;

import modelLayer.Tweet;
import twitter4j.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.sql.Connection;

public class OurStatusListener implements StatusListener {
    HashMap<String, Tweet> tweets = new HashMap<String, Tweet>();

    public void onStatus(Status status) {
        GeoLocation geo = status.getGeoLocation();

        if (geo != null) {
        	List<String> matchedKeywords = containsKeywords(status.getText());
            if (matchedKeywords.size() > 0) {
                System.out.println("\n" + status.getUser().getScreenName() + " wrote: ");

                tweets.put(java.lang.Long.toString(status.getId()),
                        new Tweet(status.getId(), status.getUser().getId(), status.getInReplyToUserId(), status.getCurrentUserRetweetId(),
                        status.getText(), status.getCreatedAt(), status.getGeoLocation().getLatitude(), status.getGeoLocation().getLongitude(), matchedKeywords));

                System.out.println(status.getText());

                if (status.getPlace() != null) {
                    if (status.getPlace().getFullName().equals("New York, NY")) {
                        System.out.println(status.getText());
                        System.out.println("Country: " + status.getPlace().getCountry());
                        System.out.println("Place: " + status.getPlace().getFullName());
                    }
                }

                double latitude = geo.getLatitude();
                double longitude = geo.getLongitude();

                //System.out.println("Place boundingbox " + status.getPlace().getBoundingBoxCoordinates());
                System.out.println("location:");
                System.out.println("latitude: " + latitude + " , " + "longitude: " + longitude);
            }
            else {
                System.out.println("Tweet: " + status.getId() + " removed");
            }
        }
    }

    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        //System.out.println("User: " + statusDeletionNotice.getUserId() + " deleted");
    }

    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
    }

    public void onException(Exception ex) {
        ex.printStackTrace();
    }

    public void onScrubGeo(long x, long y) {
    }

    public void onStallWarning(StallWarning warning) {
    }


    private List<String> containsKeywords(String tweetText) {
        tweetText = tweetText.toLowerCase();
        List<String> matchedKeys = new ArrayList<String>();
        List<String> keywords = new ArrayList<String>();
        keywords.add("food");
        keywords.add("poison");
        keywords.add("restaurant");
        keywords.add("sick");
        keywords.add("soup");
        keywords.add("drink");
        keywords.add("bed");
        keywords.add("hungry");
        keywords.add("soda");
        keywords.add("chinese food");
        keywords.add("chipotle");
        keywords.add("mcdonald");
        keywords.add("mc donald");
        keywords.add("burgerking");
        keywords.add("burger king");
        keywords.add("stomach pain");
        keywords.add("diarrhea");
        keywords.add("the shits");

        //btc temp
        keywords.add("the");
        keywords.add("a");

        for (String keyword : keywords) {
            if (tweetText.contains(keyword))
                matchedKeys.add(keyword);
        }
        return matchedKeys;
    }

    public HashMap<String, Tweet> getTweets(){
        return tweets;
    }

}
