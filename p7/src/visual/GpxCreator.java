package visual;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import modelLayer.*;

public class GpxCreator {
	private static XMLOutputFactory factory = XMLOutputFactory.newInstance();
	private static XMLStreamWriter writer;
	
	public GpxCreator() {
	}

	public static void createGpxFile(TweetStorage storage, String name, String outPutPath){
		try {
			File gpxFile;

			if(System.getProperty("os.name").equals("Linux")) {
				gpxFile = new File(outPutPath +"/" + name + ".gpx");
			}
			else {
				gpxFile = new File(outPutPath +"\\" + name + ".gpx"); 
			}

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
	
	public static void createClusterGpsFiles(List<Cluster> clusters) {
		
	}

	private static void createWPT(Tweet tweet){

		try{
			writer.writeStartElement("wpt");
			writer.writeAttribute("lat", Double.toString(tweet.getLat()));
			writer.writeAttribute("lon", Double.toString(tweet.getLon()));
			writer.writeEndElement();
		}
		catch(XMLStreamException e){
			e.printStackTrace();
		}	
	}
}