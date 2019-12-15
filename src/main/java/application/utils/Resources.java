package application.utils;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * @author Limbo
 * @since 19/12/15 12:26
 */
public class Resources {

    /**
     * @param path example "conf\\mvc.xml"
     */
    public static InputStream getResourceAsStream(String path) {
        return Resources.class.getClassLoader().getResourceAsStream(path);
    }

    /**
     * @param path example "conf\\mvc.xml"
     */
    public static Element getRootElement(String path) {
        try {
            return new SAXReader().read(getResourceAsStream(path)).getRootElement();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
