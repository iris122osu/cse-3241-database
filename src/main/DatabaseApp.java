import java.io.File;
import java.util.Scanner;
import database.Database;


public final class DatabaseApp {
    
    private static Database database; 

    // no argument constructor
    private DatabaseApp() {
    }

    private static void printOptions () {
        System.out.println("Add: add a record");
        System.out.println("Edit: Edit a record");
        System.out.println("Search: search for a record");
        System.out.println("Rent: register a rental");
        System.out.println("Return: register a return");
        System.out.println("Deliver: schedule a delivery");
        System.out.println("Pickup: schedule a pickup");
    }

    public static Scanner scanner;
    public static void main(String[] args) {
        Scanner userIn = new Scanner(System.in);

        database = new Database();

        database.ParseFromFile(new File("src\\main\\database\\data.txt"));
        System.out.println("Data Loaded!");
        String choice = "";
        // while (!choice.equals("exit")) {
        //     printOptions();

        // }
        


        userIn.close();


    }

}
