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
        System.out.println("Options: display this list again");
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

    private static String edit(String table, String id, int index, String replacement) {
        ArrayList<String[]> data = database.DataMap.get(table);
        String out = "";
        int i = 0;
        while (out.isEmpty() && i < data.size()) {
            if (data.get(i)[0].equals(id)) {
                out = "Replaced " + data.get(i)[index] + " with " + replacement + ".";
                data.get(i)[index] = replacement;
            }
            i++;
        }

        if (out.isEmpty()) {
            out = "Record for id: "+ id + " not found.";
        }

        return out;
    }

    private static String add(String table, String[] newRecord){
        ArrayList<String[]> data = database.DataMap.get(table);

        if(data == null) {
            return "Error: table not found";
        }

        for (String[] record : data) {
            if(record[0].equalsIgnoreCase(newRecord[0])){
                return "record already exists";
            }
        }

        data.add(newRecord);
        return "Record Added!";
    }

    public static void main(String[] args) {

        Scanner userIn = new Scanner(System.in);

        // get data from database file
        database = new Database();
        database.ParseFromFile(new File("src\\main\\database\\data.csv"));
        System.out.println("Data Loaded!");
        printOptions();
        String choice = "";

        while (!choice.equals("exit")) {
            choice = userIn.next().toLowerCase().strip();

            switch (choice) {
                case "add":
                    System.out.println("\n Enter the table to add to ("+ TableNames +"):");
                    String addTable = userIn.next().toLowerCase().strip();

                    System.out.println("How many fields does the record have? ");
                    int numFields = userIn.nextInt();
                    userIn.nextLine();

                    String[] newRecord = new String[numFields];
                    for(int i = 0; i < numFields; i++){
                        System.out.println("Enter field " + i + ":");
                        newRecord[i] = userIn.nextLine().strip();
                    }

                    System.out.println(add(addTable, newRecord));
                    break;
                case "options":
                    printOptions();
                    break;
                case "edit":
                    System.out.println("\nEnter the table to search ("+TableNames+"):");
                    String editName = userIn.next().toLowerCase().strip();
                    System.out.println("Enter the id of the record to edit (beginning with 0):");
                    String editID = userIn.next().toLowerCase().strip(); 
                    String editSearchReturn = search(editName, editID);
                    System.out.println(editSearchReturn);
                    if (editSearchReturn.contains("not found.")) {
                        break;
                    }

                    System.out.println("Enter the index of the item to change ("+TableNames+"):");
                    int editIndex = userIn.nextInt(); 
                    System.out.println("Enter the new value ("+TableNames+"):");
                    String editReplacement = userIn.next().toLowerCase().strip();

                    System.out.println(edit(editName, editID, editIndex, editReplacement));
                    
                    
                    break;
                case "delete":
                    System.out.println("\nEnter the table to search ("+TableNames+"):");
                    String deleteName = userIn.next().toLowerCase().strip();
                    System.out.println("Enter the id to delete:");
                    String deleteID = userIn.next().toLowerCase().strip(); 
                    System.out.println(delete(deleteName, deleteID));
                    break;
                case "search":
                    System.out.println("\nEnter the table to search ("+TableNames+"):");
                    String searchName = userIn.next().toLowerCase().strip();
                    System.out.println("Enter the id to search:");
                    String searchID = userIn.next().toLowerCase().strip(); 
                    System.out.println(search(searchName, searchID));
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
