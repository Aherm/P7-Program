package utility;

import modelLayer.Box;
import modelLayer.OurLocation;

public class Distance {
	// Semi-axes of WGS-84 geoidal reference
	private static final double WGS84_a = 6378137.0; // Major semiaxis [m]
	private static final double WGS84_b = 6356752.3; // Minor semiaxis [m]

	// 'halfSideInKm' is the half length of the bounding box you want in kilometers.
	public static Box getBoundingBox(OurLocation point, double halfSide) {            
	    // Bounding box surrounding the point at given coordinates,
	    // assuming local approximation of Earth surface as a sphere
	    // of radius given by WGS84
	    double lat = deg2rad(point.getLat());
	    double lon = deg2rad(point.getLon());

	    // Radius of Earth at given latitude
	    double radius = WGS84EarthRadius(lat);
	    // Radius of the parallel at given latitude
	    double pradius = radius * Math.cos(lat);

	    double latMin = lat - halfSide / radius;
	    double latMax = lat + halfSide / radius;
	    double lonMin = lon - halfSide / pradius;
	    double lonMax = lon + halfSide / pradius;

	    return new Box(rad2deg(lonMin), rad2deg(lonMax), rad2deg(latMin), rad2deg(latMax));        
	}

	public static double getDist(OurLocation loc1, OurLocation loc2) {
		double R = 6371;	//Earths radius
		double deltaLat = deg2rad(loc2.getLat() - loc1.getLat());
		double deltaLon = deg2rad(loc2.getLon() - loc1.getLon());
		double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
				Math.cos(deg2rad(loc1.getLat())) * Math.cos(deg2rad(loc2.getLat())) *
				Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return (R * c) * 1000;
	}
	
	// degrees to radians
	private static double deg2rad(double degrees)
	{
	    return Math.PI * degrees / 180.0;
	}

	// radians to degrees
	private static double rad2deg(double radians)
	{
	    return 180.0 * radians / Math.PI;
	}

	// Earth radius at a given latitude, according to the WGS-84 ellipsoid [m]
	private static double WGS84EarthRadius(double lat)
	{
	    // http://en.wikipedia.org/wiki/Earth_radius
	    double An = WGS84_a * WGS84_a * Math.cos(lat);
	    double Bn = WGS84_b * WGS84_b * Math.sin(lat);
	    double Ad = WGS84_a * Math.cos(lat);
	    double Bd = WGS84_b * Math.sin(lat);
	    return Math.sqrt((An*An + Bn*Bn) / (Ad*Ad + Bd*Bd));
	}
}
