package fileCreation;

import modelLayer.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class GpxCreator {
	private static XMLOutputFactory factory = XMLOutputFactory.newInstance();
	private static XMLStreamWriter writer;

	public GpxCreator() {}

	public static void createGpxFile(TweetStorage storage, String name, String outPutPath) {
		try {
			File gpxFile;

			gpxFile = new File(outPutPath +"/" + name + ".gpx");

			if(!gpxFile.exists()) {
				gpxFile.createNewFile(); 
			}

			writer = factory.createXMLStreamWriter(new FileWriter(gpxFile.getAbsoluteFile()));

			writer.writeStartDocument();
			writer.writeStartElement("gpx");
			writer.writeAttribute("version", "1.0");
			for(Tweet t : storage) {
				createWPT(t);
			}
			writer.writeEndElement();
			writer.writeEndDocument();
			writer.flush();
			writer.close();
		}
		catch (XMLStreamException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createClusterGpsFiles(ClusterStorage clusters) {
		TweetStorage centers = new TweetStorage();
		String path = "./gpxFiles";

		for (int i = 0; i < clusters.size(); i++) {
			Cluster c = clusters.get(i);
			String name = "Cluster" + i;
			centers.add(c.getCenter());
			createGpxFile(c.getTweets(), name, path);
		}

		createGpxFile(centers, "centers", path);
	}

	private static void createWPT(Tweet tweet) {
		try{
			if(tweet.isGeotagged()){		
				writer.writeStartElement("wpt");
				writer.writeAttribute("lat", Double.toString(tweet.getLat()));
				writer.writeAttribute("lon", Double.toString(tweet.getLon()));
				writer.writeEndElement();
			}
		}
		catch(XMLStreamException e) {
			e.printStackTrace();
		}	
	}
}