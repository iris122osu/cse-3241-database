package databaseapp;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import databaseapp.database.*;
import databaseapp.utility.*;
import java.util.ArrayList;


public final class DatabaseApp {
    
    private static Database database; 

    // so user knows their options when choosing a table to search, delete, etc.
    private static final String TableNames = "Employees, Warehouses";

    private DatabaseApp() {
    }

	
    /**
     * Connects to the database if it exists, creates it if it does not, and returns the connection object.
     * 
     * @param databaseFileName the database file name
     * @return a connection object to the designated database
     */
    public static Connection initializeDB() {
        String url = "jdbc:sqlite:Checkpoint3Files/CSE-3241-DB.db";
        Connection conn = null; 
        try {
            conn = DriverManager.getConnection(url);
            if (conn != null) {
            	// Provides some positive assurance the connection and/or creation was successful.
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The connection to the database was successful.");
            } else {
            	// Provides some feedback in case the connection failed but did not throw an exception.
            	System.out.println("Null Connection");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("There was a problem connecting to the database.");
        }
        return conn;
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

    private static void add(Connection conn, Scanner userIn) {
        {
                    String addTable = "";
                    do {
                        System.out.println("\nEnter the table to add to ("+ TableNames +"):");
                        addTable = Database.getTable(Utility.getStandardInput(userIn)) ;
                    } while  (addTable.isBlank());
                    ArrayList<String> columns = SQL.getColumns(conn, addTable);
                    String[] newRecord = new String[columns.size()];
                    for(int i = 0; i < columns.size(); i++){
                        System.out.println("Enter " + columns.get(i) + ":");
                        newRecord[i] = Utility.getStandardInput(userIn);
                    }
                    System.out.println(SQL.add(conn, addTable, newRecord));
                }
    }


    public static void main(String[] args) {

        Scanner userIn = new Scanner(System.in);

        Connection conn = initializeDB();
        String choice = "";

        do {
            
            printOptions(); 
            choice = userIn.next().toLowerCase().strip();

            switch (choice) {
                case "add" -> add(conn, userIn);
                case "options" -> printOptions();
                case "edit" -> {
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
                }
                case "delete" -> {
                    System.out.println("\nEnter the table to search ("+TableNames+"):");
                    String deleteName = userIn.next().toLowerCase().strip();
                    System.out.println("Enter the id to delete:");
                    String deleteID = userIn.next().toLowerCase().strip(); 
                    System.out.println(delete(deleteName, deleteID));
                }
                case "search" -> {
                    System.out.println("\nEnter the table to search ("+TableNames+"):");
                    String searchName = userIn.next().toLowerCase().strip();
                    System.out.println("Enter the id to search:");
                    String searchID = userIn.next().toLowerCase().strip(); 
                    System.out.println(search(searchName, searchID));
                }
                case "rent" -> {
                    System.out.println("\nEnter Equipment ID: ");
                    String rentEID = userIn.next().toLowerCase().strip(); 
                    System.out.println("\nEnter User ID: ");
                    String rentUID = userIn.next().toLowerCase().strip();
                    System.out.println("\nRented!");
                }
                case "return" -> {
                    System.out.println("\nEnter Equipment ID: ");
                    String returnEID = userIn.next().toLowerCase().strip(); 
                    System.out.println("\nEnter User ID: ");
                    String returnUID = userIn.next().toLowerCase().strip();
                    System.out.println("\nReturned!");
                }
                case "deliver" -> {
                    System.out.println("\nEnter Drone ID: ");
                    String deliverDID = userIn.next().toLowerCase().strip();
                    System.out.println("\nEnter Equipment ID: ");
                    String deliverEID = userIn.next().toLowerCase().strip(); 
                    System.out.println("\nEnter User ID: ");
                    String deliverUID = userIn.next().toLowerCase().strip();
                    System.out.println("\nDelivered!");
                }
                case "pickup" -> {
                    System.out.println("\nEnter Drone ID: ");
                    String pickupDID = userIn.next().toLowerCase().strip();
                    System.out.println("\nEnter Equipment ID: ");
                    String pickupEID = userIn.next().toLowerCase().strip(); 
                    System.out.println("\nEnter User ID: ");
                    String pickupUID = userIn.next().toLowerCase().strip();
                    System.out.println("\nScheduled for pickup!");
                }
                default -> {
                }
            }

        } while (!choice.equals("exit"));
        


        userIn.close();


    }

}
