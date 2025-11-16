package databaseapp.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;
import java.util.HashMap;

// Object representation of data. parsed in from a csv.
public class Database {

    public HashMap<String, ArrayList<String[]>> DataMap;

    public static final String[] TableNames = {"Damages","DronePOs","Drones",
        "DroneSpecifications","EmployeePhone","Employees","Equipment",
        "EquipmentPOs","EquipmentSpecifications","PurchaseOrders",
        "Rentals","Reviews","UserDistance","UserPhone","UserRents",
        "Users","WarehouseLocation","Warehouses"};

    public Database () {


        DataMap = new HashMap<String, ArrayList<String[]>>();
        
        DataMap.put("Employees", new ArrayList<String[]>());
        DataMap.put("Warehouses", new ArrayList<String[]>());
        DataMap.put("Drones", new ArrayList<String[]>());
        DataMap.put("Equipment", new ArrayList<String[]>());
        DataMap.put("PurchaseOrders", new ArrayList<String[]>());
        DataMap.put("Users", new ArrayList<String[]>());
        DataMap.put("Damages", new ArrayList<String[]>());
        DataMap.put("Rentals", new ArrayList<String[]>());
        DataMap.put("Reviews", new ArrayList<String[]>());
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

    public static String getTable(String tableName) {
        int i = 0;
        while (i < TableNames.length) {
            if (TableNames[i].toLowerCase().equals(tableName.toLowerCase())) {
                return TableNames[i];
            }
            i++;
        }
        return "";
    }

}
