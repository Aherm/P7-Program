package streaming;

import twitter4j.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OurStatusListener implements StatusListener {

    public void onStatus(Status status) {
        GeoLocation geo = status.getGeoLocation();

        if (geo != null) {
            if (containsKeywords(status.getText())) {
                System.out.println("\n" + status.getUser().getScreenName() + " wrote: ");
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



        /*
        HashtagEntity[] array = status.getHashtagEntities();
		if(array.length != 0)
            System.out.println(array[0].getText());
		*/
    }

    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
        System.out.println("User: " + statusDeletionNotice.getUserId() + " deleted");
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


    private boolean containsKeywords(String tweetText) {
        tweetText = tweetText.toLowerCase();

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

        for (String keyword : keywords) {
            if (tweetText.contains(keyword))
                return true;
        }
        return false;
    }

}
