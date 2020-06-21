package View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

import java.io.*;
import java.util.Properties;
import java.util.Set;

public class Configurations {
    private static Properties prop;

    private static void initProp() {
        try (InputStream input = new FileInputStream("resources/configs.properties")) {
            if (input == null)
                return;
            prop = new Properties();
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String name) {
        if (prop==null)
            initProp();
        String value = prop.getProperty(name);
        return value;
    }

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

    public static Set<String> getAllProperties(){
        if (prop==null)
            initProp();
        return prop.stringPropertyNames();
    }

    public static void main(String[] args) {
        System.out.println(getProperty("insert"));
        setProperty("insert2", "switched2");
        System.out.println(getProperty("insert"));
    }
}
