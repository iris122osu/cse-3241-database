package databaseapp;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import databaseapp.database.*;
import databaseapp.utility.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;


public final class DatabaseApp {
    
    private static Database database; 

    // so user knows their options when choosing a table to search, delete, etc.
    private static String TableNames = "";

    private DatabaseApp() {
    }

    private static void getTableNames() {
        for (int i = 0; i < Database.TableNames.length; i++) {
            TableNames = TableNames + ", " + Database.TableNames[i];
        }

        TableNames = TableNames.substring(2, TableNames.length() - 1); 
    }

    private static void printResultSet(ResultSet rs) {
        try {
            String columns = "";
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                columns = columns + rsmd.getColumnName(i) + "\t";
            }
            System.out.println(columns);
            while (rs.next()) {
                String row = "";
                for (int i = 1; i <= columnCount; i++) {
                    row = row + rs.getString(i) + "\t";
                }
                System.out.println(row);
            }
            
        } catch (SQLException e) {
            System.err.println(e);
        }
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
        System.out.println("Edit: edit a record");
        System.out.println("Delete: delete a record");
        System.out.println("Search: search for a record");
        System.out.println("Reports: get a report");
        System.out.println("Rent: register a rental");
        System.out.println("Return: register a return");
        System.out.println("Deliver: schedule a delivery");
        System.out.println("Pickup: schedule a pickup");
        System.out.println("Options: display this list again");
        System.out.println("Exit: quit the program");
    }

    // Returns a ResultSet, or null if user exits
    private static ResultSet search(Connection conn, Scanner userIn) {
        String addTable = getTableFromUser(userIn);
        if (addTable.isBlank()) {
            return null;
        }
        String[] columns = SQL.getPrimaryKeys(conn, addTable);

        String[] values = new String[columns.length];

        for(int i = 0; i < columns.length; i++){
            System.out.println("Enter " + columns[i] + ":");
            values[i] = Utility.getStandardInput(userIn);
        }
        
        return SQL.search(conn, addTable, columns, values);
    }

    private static String edit(Connection conn, Scanner userIn) {

        
        System.out.println("\nEnter the table to search ("+TableNames+"):");
        String editName = userIn.next().toLowerCase().strip();
        System.out.println("Enter the id of the record to edit (beginning with 0):");
        String editID = userIn.next().toLowerCase().strip(); 

        return "";
    }

    private static void add(Connection conn, Scanner userIn) {
        String addTable = getTableFromUser(userIn);
        if (addTable.isBlank()) {
            return;
        }
        ArrayList<String> columns = SQL.getColumns(conn, addTable);
        String[] newRecord = new String[columns.size()];
        for(int i = 0; i < columns.size(); i++){
            System.out.println("Enter " + columns.get(i) + ":");
            newRecord[i] = Utility.getStandardInput(userIn);
        }
        System.out.println(SQL.add(conn, addTable, newRecord));
    }

    private static void delete(Connection conn, Scanner userIn) {
        String deleteTable = getTableFromUser(userIn);
        if (deleteTable.isBlank()) {
            return;
        }
        String[] columns = SQL.getPrimaryKeys(conn, deleteTable);
        String[] values = new String[columns.length];
        for(int i = 0; i < columns.length; i++){
            System.out.println("Enter " + columns[i] + ":");
            values[i] = Utility.getStandardInput(userIn);
        }
        System.out.println(SQL.delete(conn, deleteTable, columns, values));
    }

    private static String getTableFromUser(Scanner userIn) {
        String tableName = "";
        do {
            System.out.println("\nEnter the table's name or 'exit' ("+ TableNames +"):");
            String in = Utility.getStandardInput(userIn);
            if (in.toLowerCase().equals("exit")){
                return "";
            }
            tableName = Database.getTable(in) ;
        } while  (tableName.isBlank());
        return tableName;
    }


    public static void main(String[] args) {
        getTableNames();

        Scanner userIn = new Scanner(System.in);

        Connection conn = initializeDB();
        String choice = "";

        do {
            
            printOptions(); 
            choice = userIn.next().toLowerCase().strip();

            switch (choice) {
                case "add" -> add(conn, userIn);
                case "options" -> printOptions();
                case "edit" -> edit(conn, userIn);
                case "delete" -> delete(conn, userIn);
                case "search" -> {
                    ResultSet rs = search(conn, userIn);
                    printResultSet(rs);
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
                case "reports" -> {}
                default -> {
                }
            }

        } while (!choice.equals("exit"));
        


        userIn.close();


    }

}
