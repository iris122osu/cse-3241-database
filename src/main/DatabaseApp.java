import java.io.File;
import java.util.Scanner;
import database.Database;
import java.util.ArrayList;


public final class DatabaseApp {
    
    private static Database database; 

    // so user knows their options when choosing a table to search, delete, etc.
    private static final String TableNames = "Employees, Warehouses";

    private DatabaseApp() {
    }

    private static void printOptions () {
        System.out.println("");
        System.out.println("Options, case insensitive:");
        System.out.println("Add: add a record");
        System.out.println("Edit: Edit a record");
        System.out.println("Delete: Delete a record");
        System.out.println("Search: search for a record");
        System.out.println("Rent: register a rental");
        System.out.println("Return: register a return");
        System.out.println("Deliver: schedule a delivery");
        System.out.println("Pickup: schedule a pickup");
        System.out.println("Exit: quit the program");
    }

    // Searches a particlar table for a specific id
    private static String search(String table, String id) {
        ArrayList<String[]> data = database.DataMap.get(table);
        String out = "";
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i)[0].equals(id)) {
                out = "Record Found: ";
                for (String s : data.get(i)) { 
                    out = out + s + ", ";
                }
                break;
            }
        }

        if (out.length() == 0) {
            out = "Record for id: "+ id + " not found. ";
        }

        return out;
    }

    private static String delete(String table, String id) {
        ArrayList<String[]> data = database.DataMap.get(table);
        String out = "";
        int i = 0;
        while (out.isEmpty() && i < data.size()) {
            if (data.get(i)[0].equals(id)) {
                data.remove(i);
                out = "Deleted!";
            }
            i++;
        }

        if (out.isEmpty()) {
            out = "Record for id: "+ id + " not found.";
        }

        return out;
    }

    public static void main(String[] args) {

        Scanner userIn = new Scanner(System.in);

        // get data from database file
        database = new Database();
        database.ParseFromFile(new File("src\\main\\database\\data.csv"));
        System.out.println("Data Loaded!");
        String choice = "";

        while (!choice.equals("exit")) {
            printOptions();
            choice = userIn.next().toLowerCase().strip();

            switch (choice) {
                case "add":
                    
                    break;
                case "edit":
                    
                    break;
                case "delete":
                    System.out.println("\nEnter the table to search ("+TableNames+"):");
                    String deleteName = userIn.next().toLowerCase().strip();
                    System.out.println("Enter the id to delete ("+TableNames+"):");
                    String deleteID = userIn.next().toLowerCase().strip(); 
                    System.out.println(delete(deleteName, deleteID));
                    break;
                case "search":
                    System.out.println("\nEnter the table to search ("+TableNames+"):");
                    String searchName = userIn.next().toLowerCase().strip();
                    System.out.println("Enter the id to search ("+TableNames+"):");
                    String searchId = userIn.next().toLowerCase().strip(); 
                    System.out.println(search(searchName, searchId));
                    break;
                case "rent":
                    System.out.println("\nEnter Equipment ID: ");
                    String rentEID = userIn.next().toLowerCase().strip(); 
                    System.out.println("\nEnter User ID: ");
                    String rentUID = userIn.next().toLowerCase().strip();
                    System.out.println("\nRented!");
                    break;
                case "return":
                    System.out.println("\nEnter Equipment ID: ");
                    String returnEID = userIn.next().toLowerCase().strip(); 
                    System.out.println("\nEnter User ID: ");
                    String returnUID = userIn.next().toLowerCase().strip();
                    System.out.println("\nReturned!");
                    break;
                case "deliver":
                    System.out.println("\nEnter Drone ID: ");
                    String deliverDID = userIn.next().toLowerCase().strip();
                    System.out.println("\nEnter Equipment ID: ");
                    String deliverEID = userIn.next().toLowerCase().strip(); 
                    System.out.println("\nEnter User ID: ");
                    String deliverUID = userIn.next().toLowerCase().strip();
                    System.out.println("\nDelivered!");
                    break;
                case "pickup":
                    System.out.println("\nEnter Drone ID: ");
                    String pickupDID = userIn.next().toLowerCase().strip();
                    System.out.println("\nEnter Equipment ID: ");
                    String pickupEID = userIn.next().toLowerCase().strip(); 
                    System.out.println("\nEnter User ID: ");
                    String pickupUID = userIn.next().toLowerCase().strip();
                    System.out.println("\nScheduled for pickup!");
                    break;
                default:
                    break;
            }

        }
        


        userIn.close();


    }

}
