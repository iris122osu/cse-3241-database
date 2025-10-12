import java.io.File;
import java.util.Scanner;
import database.Database;
import java.util.ArrayList;


public final class DatabaseApp {
    
    private static Database database; 

    private static final String TableNames = "Employees, Warehouses";

    // no argument constructor
    private DatabaseApp() {
    }

    private static void printOptions () {
        System.out.println("");
        System.out.println("Options, case insensitive:");
        System.out.println("Add: add a record");
        System.out.println("Edit: Edit a record");
        System.out.println("Search: search for a record");
        System.out.println("Rent: register a rental");
        System.out.println("Return: register a return");
        System.out.println("Deliver: schedule a delivery");
        System.out.println("Pickup: schedule a pickup");
        System.out.println("Exit: quit the program");
    }

    private static String search(String type, String id) {
        ArrayList<String[]> data = database.DataMap.get(type);
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
            out = "Record not found";
        }

        return out;
    }

    public static Scanner scanner;
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
                    
                    break;
                case "search":
                    System.out.println("\nEnter the table to search ("+TableNames+"):");
                    String name = userIn.next();
                    System.out.println("Enter the id to search ("+TableNames+"):");
                    String id = userIn.next(); 
                    System.out.println(search(name, id));
                    break;
                case "rent":
                    
                    break;
                case "return":
                    
                    break;
                case "deliver":
                    
                    break;
                case "pickup":
                    
                    break;
                default:
                    break;
            }

        }
        


        userIn.close();


    }

}
