package jagoclient;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class MyResourceBundle extends ResourceBundle {
    public MyResourceBundle (Reader reader) {
        BufferedReader bufReader = new BufferedReader(reader);
        lookup = new Hashtable();
        try {
            while(bufReader.ready()) {
                String line = bufReader.readLine();
                if (line.startsWith("#")) {
                    continue;
                }
                int idx = line.indexOf("=");
                if (idx > 0) {
                    String key = line.substring(0, idx).trim();
                    String value = line.substring(idx+1);
                    lookup.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Implements java.util.ResourceBundle.handleGetObject; inherits javadoc specification.
    public Object handleGetObject(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return lookup.get(key);
    }

    /**
     * Implementation of ResourceBundle.getKeys.
     */
    public Enumeration getKeys() {
        return lookup.keys();
    }

    // ==================privates====================

    private Hashtable lookup;
}
