package Model;

import java.io.*;
import java.util.Properties;
import java.util.Set;

/**
 * A  class used to control all the properties in the config.properties file.
 */
public class Configurations {
    /**
     * Singleton class - one instance only of Properties
     */
    private static Properties prop;

    // private constructor
    private Configurations(){}

    private static void initProp() { //initialize the Class
        try (InputStream input = new FileInputStream("resources/configs.properties")) {
            if (input == null)
                return;
            prop = new Properties();
            prop.load(input); // open the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get a requested property value
     * @param name - property key
     * @return property value
     */
    public static String getProperty(String name) {
        if (prop==null)
            initProp();
        String value = prop.getProperty(name);
        return value;
    }

    /**
     * Update or set a new property into the file
     * @param key - new property key to put
     * @param value - new property value
     */
    public static void setProperty(String key, String value){
        if (prop==null)
            initProp();
        try (OutputStream output = new FileOutputStream("resources/configs.properties")) {

            prop.setProperty(key,value);
            prop.store(output, null);

            System.out.println("Properties updated successfully!\n"+prop);

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    /**
     * Return a Set including all property keys
     * @return
     */
    public static Set<String> getAllProperties(){
        if (prop==null)
            initProp();
        return prop.stringPropertyNames();
    }
}
