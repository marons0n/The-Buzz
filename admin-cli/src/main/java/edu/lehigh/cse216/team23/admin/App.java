package edu.lehigh.cse216.team23.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * App is the entry point for the Admin CLI that allows users to interact with the database.
 * Users can create, delete, query, insert, and update rows in a PostgreSQL database through
 * the command-line interface.
 */
public class App {

    public static void main(String[] argv) {
        // Display the menu at startup
        menu();
        // Start the CLI loop to process user commands
        mainCliLoop(argv);
    }

    /**
     * Entry point for the admin CLI program. Establishes a connection to the database and
     * runs the CLI loop, which processes user commands.
     *
     * @param argv Command-line options, ignored in this implementation.
     */
    public static void mainCliLoop(String[] argv) {
        // Get a connection to the database
        Database db = Database.getDatabase();
        if (db == null) {
            System.err.println("Unable to connect to the database, exiting.");
            System.exit(1);
        }

        // Start the command-line interface
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Prompt for user action
            char action = prompt(in);
            switch (action) {
                case '?':
                    menu();
                    break;
                case 'q':
                    db.disconnect();
                    return;
                case 'T':
                    db.createTable();
                    break;
                case 'D':
                    db.dropTable();
                    break;
                case '1':
                    queryRowById(db, in);
                    break;
                case '*':
                    queryAllRows(db);
                    break;
                case '+':
                    insertRow(db, in);
                    break;
                case '~':
                    updateRow(db, in);
                    break;
                case '-':
                    deleteRow(db, in);
                    break;
                default:
                    System.out.println("Unknown command: " + action);
            }
        }
    }

    /**
     * Display the CLI menu options.
     */
    static void menu() {
        System.out.println("Admin CLI - Main Menu");
        System.out.println("  [T] Create table");
        System.out.println("  [D] Drop table");
        System.out.println("  [1] Query a specific row by ID");
        System.out.println("  [*] Query all rows");
        System.out.println("  [+] Insert a new row");
        System.out.println("  [~] Update a row");
        System.out.println("  [-] Delete a row");
        System.out.println("  [q] Quit the program");
        System.out.println("  [?] Show this menu");
    }

    /**
     * Prompt the user for a menu option.
     *
     * @param in BufferedReader for reading user input.
     * @return The character corresponding to the chosen menu option.
     */
    static char prompt(BufferedReader in) {
        String actions = "TD1*-+~q?";
        while (true) {
            System.out.print("[" + actions + "] :> ");
            try {
                String action = in.readLine();
                if (action.length() == 1 && actions.contains(action)) {
                    return action.charAt(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Invalid command. Please try again.");
        }
    }

    /**
     * Query a specific row by ID.
     *
     * @param db The database object.
     * @param in BufferedReader for reading user input.
     */
    static void queryRowById(Database db, BufferedReader in) {
        int id = getInt(in, "Enter the row ID");
        if (id == -1) return;
        Database.RowData row = db.selectOne(id);
        if (row != null) {
            System.out.println("Row [" + row.mId() + "]: " + row.mSubject() + " --> " + row.mMessage() + " | Likes: " + row.mLikes());
        } else {
            System.out.println("Row not found.");
        }
    }

    /**
     * Query all rows from the table.
     *
     * @param db The database object.
     */
    static void queryAllRows(Database db) {
        ArrayList<Database.RowData> rows = db.selectAll();
        if (rows != null) {
            System.out.println("All rows in the database:");
            for (Database.RowData row : rows) {
                System.out.println("Row [" + row.mId() + "]: " + row.mSubject() + " --> " + row.mMessage() + " | Likes: " + row.mLikes());
            }
        } else {
            System.out.println("No data found.");
        }
    }

    /**
     * Insert a new row into the table. Likes are automatically initialized to 0.
     *
     * @param db The database object.
     * @param in BufferedReader for reading user input.
     */
    static void insertRow(Database db, BufferedReader in) {
        String subject = getString(in, "Enter the subject");
        String message = getString(in, "Enter the message");
        if (!subject.isEmpty() && !message.isEmpty()) {
            int result = db.insertRow(subject, message);
            System.out.println(result + " row(s) inserted.");
        } else {
            System.out.println("Invalid input. Please try again.");
        }
    }

    /**
     * Update a row in the table.
     *
     * @param db The database object.
     * @param in BufferedReader for reading user input.
     */
    static void updateRow(Database db, BufferedReader in) {
        int id = getInt(in, "Enter the row ID");
        if (id == -1) return;
        String newMessage = getString(in, "Enter the new message");
        if (!newMessage.isEmpty()) {
            int result = db.updateOne(id, newMessage);
            System.out.println(result + " row(s) updated.");
        } else {
            System.out.println("Invalid input. Please try again.");
        }
    }

    /**
     * Delete a row from the table.
     *
     * @param db The database object.
     * @param in BufferedReader for reading user input.
     */
    static void deleteRow(Database db, BufferedReader in) {
        int id = getInt(in, "Enter the row ID");
        if (id == -1) return;
        int result = db.deleteRow(id);
        System.out.println(result + " row(s) deleted.");
    }

    /**
     * Get an integer input from the user.
     *
     * @param in BufferedReader for reading user input.
     * @param prompt The message to display when asking for input.
     * @return The integer input from the user, or -1 on error.
     */
    static int getInt(BufferedReader in, String prompt) {
        try {
            System.out.print(prompt + " :> ");
            return Integer.parseInt(in.readLine());
        } catch (IOException | NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid integer.");
            return -1;
        }
    }

    /**
     * Get a string input from the user.
     *
     * @param in BufferedReader for reading user input.
     * @param prompt The message to display when asking for input.
     * @return The string input from the user.
     */
    static String getString(BufferedReader in, String prompt) {
        try {
            System.out.print(prompt + " : ");
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
