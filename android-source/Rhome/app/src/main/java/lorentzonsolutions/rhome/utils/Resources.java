package lorentzonsolutions.rhome.utils;

/**
 * Application resources.
 *
 * @author Johan Lorentzon
 *
 */

public class Resources {

    private static Resources instance = new Resources();
    private Resources() {}

    private final String API_KEY_WEB_API = "AIzaSyDA5A7MQiJOWkWPbk2QIK78V0svBK7buW8";

    public static Resources getInstance() {return instance;}
    public String getAPI_KEY_WEB_API() {return API_KEY_WEB_API;}
}
