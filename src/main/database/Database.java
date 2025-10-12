package database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.util.HashMap;

// Object representation of 
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
    

    // This is expecting each line to say relation,att1,att2,att3,etc.
    public void ParseFromFile(File file) {
        try (Scanner fileData = new Scanner(file)) { 
            while (fileData.hasNextLine()) {
                String nextLine = fileData.nextLine();
                // System.out.println(nextLine);
                if (nextLine.strip().length() > 0) {
                    String[] values = nextLine.split(",");
                    // System.out.println(values[0]);
                    DataMap.get(values[0]).add(Arrays.copyOfRange(values, 1, values.length - 1));
                }
            }
            // System.out.println(Employees.get(0)[2]);
            fileData.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        
    }

}
