package pkg;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.commons.io.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Class for obtaining geolocation information of an IP address. The class uses the
 * <a href="http://freegeoip.net">freegeoip.net</a> service.
 */
public class GeoLocator {

    /**
     * URI of the geolocation service.
     */
    public static final String GEOLOCATOR_SERVICE_URI = "http://freegeoip.net/json/";

    private static final ObjectMapper mapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    /**
     * Creates a {@code GeoLocator} object.
     */


    public GeoLocator() {
    }

    /**
     * Returns geolocation information about the JVM running the application.
     *
     * @return an object wrapping the geolocation information returned
     * @throws IOException if any I/O error occurs
     */
    public GeoLocation getGeoLocation() throws IOException {
        return getGeoLocation((String) null);
    }

    /**
     * Returns geolocation information about the IP address or host name specified.
     * If the argument is <code>null</code>, the method returns geolocation information
     * about the JVM running the application.
     *
     * @param ipAddrOrHost the IP address or host name, may be {@code null}
     * @return an object wrapping the geolocation information returned
     * @throws IOException if any I/O error occurs
     */
    public GeoLocation getGeoLocation(String ipAddrOrHost) throws IOException {
        URL url = new URL(
                ipAddrOrHost == null ? GEOLOCATOR_SERVICE_URI : GEOLOCATOR_SERVICE_URI + URLEncoder.encode(ipAddrOrHost, "UTF-8")
        );
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(conn.getResponseMessage());
        }
        String s = IOUtils.toString(conn.getInputStream(), "UTF-8");
        return mapper.readValue(s, GeoLocation.class);
    }

    public static void main(String[] args) throws IOException {
        try {
            String arg = args.length > 0 ? args[0] : null;
            System.out.println(new GeoLocator().getGeoLocation(arg));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
