package naiveBayes;

public class Document {
    private long ID;
    private String matchedToken;
    private String text;
    private boolean geotagged;
    private String classLabel;

    public Document() {
    }

    public Document(int ID, String matchedToken, String text, boolean geotagged, String classLabel) {
        this.ID = ID;
        this.matchedToken = matchedToken;
        this.text = text;
        this.geotagged = geotagged;
        this.classLabel = classLabel;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getMatchedToken() {
        return matchedToken;
    }

    public void setMatchedToken(String matchedToken) {
        this.matchedToken = matchedToken;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isGeotagged() {
        return geotagged;
    }

    public void setGeotagged(boolean geotagged) {
        this.geotagged = geotagged;
    }

    public String getClassLabel() {
        return classLabel;
    }

    public void setClassLabel(String classLabel) {
        this.classLabel = classLabel;
    }
}
