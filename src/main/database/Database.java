package database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.util.HashMap;

// Object representation of data. parsed in from a csv.
public class Database {

    public HashMap<String, ArrayList<String[]>> DataMap;

    public Database () {
        DataMap = new HashMap<String, ArrayList<String[]>>();
        
        DataMap.put("employees", new ArrayList<String[]>());
        DataMap.put("warehouses", new ArrayList<String[]>());
        DataMap.put("drones", new ArrayList<String[]>());
        DataMap.put("equipments", new ArrayList<String[]>());
        DataMap.put("purchaseorders", new ArrayList<String[]>());
        DataMap.put("users", new ArrayList<String[]>());
        DataMap.put("damages", new ArrayList<String[]>());
        DataMap.put("rentals", new ArrayList<String[]>());
        DataMap.put("reviews", new ArrayList<String[]>());
    }
    

    // This is expecting each line of the csv file to say relation,att1,att2,att3,etc.
    public void ParseFromFile(File file) {
        try (Scanner fileData = new Scanner(file)) { 
            while (fileData.hasNextLine()) {
                String nextLine = fileData.nextLine();
                if (nextLine.strip().length() > 0) {
                    String[] values = nextLine.split(",");
                    DataMap.get(values[0]).add(Arrays.copyOfRange(values, 1, values.length));
                }
            }
            fileData.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        
    }

}
