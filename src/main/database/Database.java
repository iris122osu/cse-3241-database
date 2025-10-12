package database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.io.File;

// Object representation of 
public class Database {

    // The outer array list is for each row, each inner array is a row tuple
    // Size variables describe how many columns are in a tuple (don't know if needed)
    public ArrayList<String[]> Employees;
    public final int EmployeeSize = 6;
    public ArrayList<String[]> Warehouses;
    public final int WarehouseSize = 8;
    public ArrayList<String[]> Drones;
    public final int DroneSize = 12;
    public ArrayList<String[]> Equipment;
    public final int EquipmentSize = 14;
    public ArrayList<String[]> PurchaseOrders;
    public final int PurchaseOrderSize = 8;
    public ArrayList<String[]> Users;
    public final int UserSize = 6;
    public ArrayList<String[]> Damages;
    public final int DamageSize = 6;
    public ArrayList<String[]> Rentals;
    public final int RentalSize = 6;
    public ArrayList<String[]> Reviews;
    public final int ReviewSize = 5;

    public Database () {
        Employees = new ArrayList<String[]>();
        Warehouses = new ArrayList<String[]>();
        Drones = new ArrayList<String[]>();
        Equipment = new ArrayList<String[]>();
        PurchaseOrders = new ArrayList<String[]>();
        Users = new ArrayList<String[]>();
        Damages = new ArrayList<String[]>();
        Rentals = new ArrayList<String[]>();
        Reviews = new ArrayList<String[]>();
    }
    

    // This is expecting each line to say relation,att1,att2,att3,etc.
    public void ParseFromFile(File file) {
        try (Scanner fileData = new Scanner(file)) { 
            while (fileData.hasNextLine()) {
                String nextLine = fileData.nextLine();
                // System.out.println(nextLine);
                if (nextLine.strip().length() > 0) {
                    String[] values = nextLine.split(",");

                    switch (values[0]) {
                        case "employee":
                            Employees.add(Arrays.copyOfRange(values, 1, values.length - 1));
                            break;
                        case "warehouse":
                            Warehouses.add(Arrays.copyOfRange(values, 1, values.length - 1));
                            break;
                        case "drone":
                            Drones.add(Arrays.copyOfRange(values, 1, values.length - 1));
                            break;
                        case "equipment":
                            Equipment.add(Arrays.copyOfRange(values, 1, values.length - 1));
                            break;
                        case "purchaseorder":
                            PurchaseOrders.add(Arrays.copyOfRange(values, 1, values.length - 1));
                            break;
                        case "user":
                            Users.add(Arrays.copyOfRange(values, 1, values.length - 1));
                            break;
                        case "damage":
                            Damages.add(Arrays.copyOfRange(values, 1, values.length - 1));
                            break;
                        case "rental":
                            Rentals.add(Arrays.copyOfRange(values, 1, values.length - 1));
                            break;
                        case "review":
                            Reviews.add(Arrays.copyOfRange(values, 1, values.length - 1));
                            break;
                        default:
                            throw new AssertionError();
                    }
                }
            }
            // System.out.println(Employees.get(0)[2]);
            fileData.close();
        } catch (Exception e) {
            System.err.println(e);
        }
        
    }

}
