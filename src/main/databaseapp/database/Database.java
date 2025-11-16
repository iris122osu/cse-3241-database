package databaseapp.database;

public class Database {

    public static final String[] TableNames = {"Damages","DronePOs","Drones",
        "DroneSpecifications","EmployeePhone","Employees","Equipment",
        "EquipmentPOs","EquipmentSpecifications","PurchaseOrders",
        "Rentals","Reviews","UserDistance","UserPhone","UserRents",
        "Users","WarehouseLocation","Warehouses"};


    /// Ensures no user input is used for a table
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
