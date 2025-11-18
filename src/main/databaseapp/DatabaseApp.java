package databaseapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import databaseapp.database.*;
import databaseapp.utility.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;


public final class DatabaseApp {
    
    // so user knows their options when choosing a table to search, delete, etc.
    private static String TableNames = "";

    private DatabaseApp() {
    }

    private static void getTableNames() {
        for (String TableName : Database.TableNames) {
            TableNames = TableNames + ", " + TableName;
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
        System.out.println("Report: retrive a report");
        System.out.println("Rent: register a rental");
        System.out.println("Return: register a return");
        System.out.println("Deliver: schedule a delivery");
        System.out.println("Pickup: schedule a pickup");
        System.out.println("Options: display this list again");
        System.out.println("Exit: quit the program");
    }

    private static void printReports () {
        System.out.println("");
        System.out.println("Reports, enter the number next to the option:");
        System.out.println("Checkouts: find the total number of equipment items rented by a single member");
        System.out.println("PopularItem: find the most popular item in the database");
        System.out.println("PopularManufacturer: Find the most frequent equipment manufacturer");
        System.out.println("PopularDrone: Find the most used drone");
        System.out.println("MostCheckedOut: Find the member who has rented out the most items and the total number of items they have rented out");
        System.out.println("Equipment: Get the name of equipment before year by type");
        System.out.println("Options: display this list again");
        System.out.println("Exit: exit back to the main menu");
    }

    private static void rentingCheckouts(Connection conn, Scanner userIn) {
        String userID = "";
        do {
            System.out.println("\nEnter the member's ID or 'exit':");
            userID = Utility.getStandardInput(userIn);
            if (userID.toLowerCase().equals("exit")){
                return;
            } 
        } while (userID.isBlank());
        printResultSet(SQL.rentingCheckouts(conn, userID));
    }

    private static void popularItem(Connection conn) {
        ResultSet rs = SQL.popularItem(conn);
        printResultSet(rs);
    }

    private static void popularManufacturer(Connection conn) {
        ResultSet rs = SQL.popularManufacturer(conn);
        printResultSet(rs);
    }

    private static void mostCheckedOut(Connection conn) {
        ResultSet rs = SQL.mostCheckedOut(conn);
        printResultSet(rs);
    }
    
    private static void equipmentAfterYear(Connection conn, Scanner userIn) {
        System.out.println("Enter the equipment type: ");
        String man = Utility.getStandardInput(userIn);
        System.out.println("Enter the year after the equipment was released: ");
        int year = Utility.getInt(userIn);
        
        ResultSet rs = SQL.equipmentAfterYear(conn, man, year);
        printResultSet(rs);
    }

    // Returns a ResultSet, or null if user exits
    private static ResultSet search(Connection conn, Scanner userIn) {
        String searchTable = getTableFromUser(userIn);
        if (searchTable.isBlank()) {
            return null;
        }
        return search(conn, userIn, searchTable);
    }

    private static ResultSet search(Connection conn, Scanner userIn, String tableName) {
        String[] columns = SQL.getPrimaryKeys(conn, tableName);
        String[] values = new String[columns.length];

        for(int i = 0; i < columns.length; i++){
            System.out.println("Enter " + columns[i] + ":");
            values[i] = Utility.getStandardInput(userIn);
        }
        
        return SQL.search(conn, tableName, columns, values);
    }



    private static void edit(Connection conn, Scanner userIn) {
        String editTable = getTableFromUser(userIn);
        if (editTable.isBlank()) {
            return;
        }
        ResultSet rs = search(conn, userIn, editTable);
        try {
            rs.next(); 
        } catch (SQLException e) {
        }

        System.out.println("Enter new values:");
        
        ArrayList<String> columns = SQL.getColumns(conn, editTable);
        String[] pk = SQL.getPrimaryKeys(conn, editTable);
        String[] pv = new String[pk.length];
        String[] columnValues = new String[columns.size() - pk.length];
        String[] newValues = new String[columns.size() - pk.length];
        int i = 0;
        int j = 0;
        int k = 0;
        while(j < columns.size()){
            if (!Utility.isIn(pk, columns.get(j))) {
                System.out.println("Enter " + columns.get(j) + ":");
                String value = Utility.getStandardInput(userIn);
                if (!value.isBlank()) {
                    newValues[i] = value;
                    columnValues[i] = columns.get(j);
                    i++;
                }
            } else {
                try {
                    pv[k] = rs.getString(columns.get(j));                    
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                }
            }
            j++;
            
        }

        System.out.println(SQL.edit(conn, editTable, columnValues, newValues, pk, pv));
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

    private static void reportMenu(Connection conn, Scanner userIn) {
        String choice;
        printReports(); 

        do {
            
            System.out.print("Report Choice: ");
            choice = userIn.next().toLowerCase().strip();

            switch (choice) {
                case "checkouts" -> rentingCheckouts(conn, userIn);
                case "popularitem" -> {popularItem(conn);}
                case "popularmanufacturer" -> {popularManufacturer(conn);}
                case "populardrone" -> {}
                case "mostcheckedout" -> {mostCheckedOut(conn);}
                case "equipment" -> {equipmentAfterYear(conn, userIn);}
                case "options" -> printReports();
                case "exit" -> {return;}
                default -> {
                    System.out.println("Not recognised, try again:");
                }
            }

        } while (!choice.equals("exit"));
    }


    public static void main(String[] args) {
        getTableNames();

        Connection conn;
        try (Scanner userIn = new Scanner(System.in)) {
            conn = initializeDB();
            String choice;
            printOptions();
            do {
                
                System.out.print("Choice: ");
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
                    case "report" -> {
                        reportMenu(conn, userIn);
                        printOptions();
                    }
                    case "exit" -> {/* catch so it doesn't print default*/ }
                    default -> {
                        System.out.println("Not recognised, try again:");
                    }
                }
                
            } while (!choice.equals("exit"));
        }

        try {
			conn.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

    }

}
