package edu.lehigh.cse216.maa326.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * App is our basic admin app.  For now, all it does is connect to the database
 * and then disconnect
 */
public class App {
    
    public static void main(String[] argv) {
        simpleManualTests(argv);
        mainCliLoop(argv);
    }

    /**
     * Reads arguments from the environment and then uses those
     * arguments to connect to the database. Either DATABASE_URL should be set,
     * or the values of four other variables POSTGRES_{IP, PORT, USER, PASS, DBNAME}.
     */
    public static void simpleManualTests( String[] argv ){
        /* holds connection to the database created from environment variables */
        Database db = Database.getDatabase();

        db.dropTable();
        db.createTable();
        db.insertRow("test subject", "test message");
        db.updateOne(1, "updated test message");

        ArrayList<Database.RowData> list_rd = db.selectAll();
        System.out.println( "Row data:" );
        for( Database.RowData rd : list_rd )
            System.out.println( ">\t" + rd );

        Database.RowData single_rd = db.selectOne(1);
        System.out.println( "Single row: " + single_rd );

        db.deleteRow(1);

        if( db != null )
            db.disconnect();
    }

    /**
     * Entry point for our admin command-line interface program.
     * 
     * Runs a loop that gets a request from the user and processes it
     * using our Database class.
     * 
     * @param argv Command-line options.  Ignored by this program.
     */
    public static void mainCliLoop(String[] argv) {
        // Get a fully-configured connection to the database, or exit immediately
        Database db = Database.getDatabase();
        if (db == null){
            System.err.println( "Unable to make database object, exiting." );
            System.exit(1);
        }

        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Get the user's request, and do it
            //
            // NB: for better testability, each action should be a separate
            //     function call
            char action = prompt(in);
            if (action == '?') {
                menu();
            } else if (action == 'q') {
                break;
            } else if (action == 'T') {
                db.createTable();
            } else if (action == 'D') {
                db.dropTable();
            } else if (action == '1') {
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                Database.RowData res = db.selectOne(id);
                if (res != null) {
                    System.out.println("  [" + res.mId() + "] " + res.mSubject());
                    System.out.println("  --> " + res.mMessage());
                }
            } else if (action == '*') {
                ArrayList<Database.RowData> res = db.selectAll();
                if (res == null)
                    continue;
                System.out.println("  Current Database Contents");
                System.out.println("  -------------------------");
                for (Database.RowData rd : res) {
                    System.out.println("  [" + rd.mId() + "] " + rd.mSubject());
                }
            } else if (action == '-') {
                int id = getInt(in, "Enter the row ID");
                if (id == -1)
                    continue;
                int res = db.deleteRow(id);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows deleted");
            } else if (action == '+') {
                String subject = getString(in, "Enter the subject");
                String message = getString(in, "Enter the message");
                if (subject.equals("") || message.equals(""))
                    continue;
                int res = db.insertRow(subject, message);
                System.out.println(res + " rows added");
            } else if (action == '~') {
                int id = getInt(in, "Enter the row ID :> ");
                if (id == -1)
                    continue;
                String newMessage = getString(in, "Enter the new message");
                int res = db.updateOne(id, newMessage);
                if (res == -1)
                    continue;
                System.out.println("  " + res + " rows updated");
            }
        }
        // Always remember to disconnect from the database when the program 
        // exits
        db.disconnect();
    }

    /**
     * Print the menu for our program
     */
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("  [T] Create tblData");
        System.out.println("  [D] Drop tblData");
        System.out.println("  [1] Query for a specific row");
        System.out.println("  [*] Query for all rows");
        System.out.println("  [-] Delete a row");
        System.out.println("  [+] Insert a new row");
        System.out.println("  [~] Update a row");
        System.out.println("  [q] Quit Program");
        System.out.println("  [?] Help (this message)");
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * 
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in) {
        // The valid actions:
        String actions = "TD1*-+~q?";

        // We repeat until a valid single-character option is selected        
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    /**
     * Ask the user to enter a String message
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided.  May be "".
     */
    static String getString(BufferedReader in, String message) {
        String s;
        try {
            System.out.print(message + " :> ");
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Ask the user to enter an integer
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer that the user provided.  On error, it will be -1
     */
    static int getInt(BufferedReader in, String message) {
        int i = -1;
        try {
            System.out.print(message + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }
}
