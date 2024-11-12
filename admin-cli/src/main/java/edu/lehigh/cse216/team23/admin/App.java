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
        System.out.println("  [/] Hide a row");
        System.out.println("  [q] Quit the program");
        System.out.println("  [?] Show this menu");
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
            System.out.println("\nMenu 2. Select this option after your first selection from [TD1*-+~q?]");
            System.out.println("  [I] Idea table");
            System.out.println("  [U] User table");
            System.out.println("  [C] Comment table");
            System.out.println("  [V] Vote table");
            System.out.println("  [M] Return to the main menu\n");
            char action = prompt(in);
            switch (action) {
                case '?':
                    menu();
                    break;
                case 'q':
                    db.disconnect();
                    return;
                case 'T':
                    createTableLoop();
                    break;
                case 'D':
                    dropTableLoop();
                    break;
                case '1':
                    QueryRowLoop();
                    break;
                case '*':
                    QueryAllRowsLoop();
                    break;
                case '+':
                    InsertRowLoop();
                    break;
                case '~':
                    UpdateRowLoop();
                    break;
                case '/':
                    HideRowLoop();
                    break;
                case '-':
                    DeleteRowLoop();
                    break;
                default:
                    System.out.println("Unknown command: " + action);
            }
        }
    }

    public static void createTableLoop(){
        Database db = Database.getDatabase();
        if (db == null) {
            System.err.println("Unable to connect to the database, exiting.");
            System.exit(1);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Prompt for user action
            char action = promptTable(in);
            switch (action) {
                case 'I':
                    db.createTable();
                    break;
                case 'U':
                    db.createUserTable();
                    break;
                case 'C':
                    db.createCommentTable();
                    break;
                case 'V':
                    db.createVoteTable();
                    break;
                case 'M':
                System.out.println("\nMain Menu");
                System.out.println("  [T] Create table");
                System.out.println("  [D] Drop table");
                System.out.println("  [1] Query a specific row by ID");
                System.out.println("  [*] Query all rows");
                System.out.println("  [+] Insert a new row");
                System.out.println("  [~] Update a row");
                System.out.println("  [-] Delete a row");
                System.out.println("  [/] Hide a row");
                System.out.println("  [q] Quit the program");
                System.out.println("  [?] Show this menu");
                return;
            }
        }
    }


    public static void dropTableLoop(){
        Database db = Database.getDatabase();
        if (db == null) {
            System.err.println("Unable to connect to the database, exiting.");
            System.exit(1);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Prompt for user action
            char action = promptTable(in);
            switch (action) {
                case 'I':
                    db.dropTable();
                    break;
                case 'U':
                    db.dropUserTable();
                    break;
                case 'C':
                    db.dropCommentTable();
                    break;
                case 'V':
                    db.dropVoteTable();
                    break;
                case 'M':
                System.out.println("\nMain Menu");
                System.out.println("  [T] Create table");
                System.out.println("  [D] Drop table");
                System.out.println("  [1] Query a specific row by ID");
                System.out.println("  [*] Query all rows");
                System.out.println("  [+] Insert a new row");
                System.out.println("  [~] Update a row");
                System.out.println("  [-] Delete a row");
                System.out.println("  [/] Hide a row");
                System.out.println("  [q] Quit the program");
                System.out.println("  [?] Show this menu");
                    return;
            }
        }
    }


    public static void QueryRowLoop(){
        Database db = Database.getDatabase();
        if (db == null) {
            System.err.println("Unable to connect to the database, exiting.");
            System.exit(1);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Prompt for user action
            char action = promptTable(in);
            switch (action) {
                case 'I':
                    queryRowById(db, in);
                    break;
                case 'U':
                    queryUserRowById(db, in);
                    break;
                case 'C':
                    queryCommentRowById(db, in);
                    break;
                case 'V':
                    queryVoteRowById(db, in);
                    break;
                case 'M':
                System.out.println("\nMain Menu");
                System.out.println("  [T] Create table");
                System.out.println("  [D] Drop table");
                System.out.println("  [1] Query a specific row by ID");
                System.out.println("  [*] Query all rows");
                System.out.println("  [+] Insert a new row");
                System.out.println("  [~] Update a row");
                System.out.println("  [-] Delete a row");
                System.out.println("  [/] Hide a row");
                System.out.println("  [q] Quit the program");
                System.out.println("  [?] Show this menu");
                    return;
            }
        }  
    }

    public static void QueryAllRowsLoop(){
        Database db = Database.getDatabase();
        if (db == null) {
            System.err.println("Unable to connect to the database, exiting.");
            System.exit(1);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Prompt for user action
            char action = promptTable(in);
            switch (action) {
                case 'I':
                    queryAllRows(db);
                    break;
                case 'U':
                    queryAllUserRows(db);
                    break;
                case 'C':
                    queryAllCommentRows(db);
                    break;
                case 'V':
                    queryAllVoteRows(db);
                    break;
                case 'M':
                System.out.println("\nMain Menu");
                System.out.println("  [T] Create table");
                System.out.println("  [D] Drop table");
                System.out.println("  [1] Query a specific row by ID");
                System.out.println("  [*] Query all rows");
                System.out.println("  [+] Insert a new row");
                System.out.println("  [~] Update a row");
                System.out.println("  [-] Delete a row");
                System.out.println("  [/] Hide a row");
                System.out.println("  [q] Quit the program");
                System.out.println("  [?] Show this menu");
                    return;
            }
        }  
    }

    public static void InsertRowLoop(){
        Database db = Database.getDatabase();
        if (db == null) {
            System.err.println("Unable to connect to the database, exiting.");
            System.exit(1);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Prompt for user action
            char action = promptTable(in);
            switch (action) {
                case 'I':
                    insertRow(db, in);
                    break;
                case 'U':
                    insertUserRow(db, in);
                    break;
                case 'C':
                    insertCommentRow(db, in);
                    break;
                case 'V':
                    insertVoteRow(db, in);
                    break;
                case 'M':
                System.out.println("\nMain Menu");
                System.out.println("  [T] Create table");
                System.out.println("  [D] Drop table");
                System.out.println("  [1] Query a specific row by ID");
                System.out.println("  [*] Query all rows");
                System.out.println("  [+] Insert a new row");
                System.out.println("  [~] Update a row");
                System.out.println("  [-] Delete a row");
                System.out.println("  [/] Hide a row");
                System.out.println("  [q] Quit the program");
                System.out.println("  [?] Show this menu");
                    return; //return to main menu
            }
        }  
    }

    public static void UpdateRowLoop(){
        Database db = Database.getDatabase();
        if (db == null) {
            System.err.println("Unable to connect to the database, exiting.");
            System.exit(1);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Prompt for user action
            char action = promptTable(in);
            switch (action) {
                case 'I':
                    updateRow(db, in);
                    break;
                case 'U':
                    updateUserRow(db, in);
                    break;
                case 'C':
                    updateCommentRow(db, in);
                    break;
                case 'V':
                    updateVoteRow(db, in);
                    break;
                case 'M':
                System.out.println("\nMain Menu");
                System.out.println("  [T] Create table");
                System.out.println("  [D] Drop table");
                System.out.println("  [1] Query a specific row by ID");
                System.out.println("  [*] Query all rows");
                System.out.println("  [+] Insert a new row");
                System.out.println("  [~] Update a row");
                System.out.println("  [-] Delete a row");
                System.out.println("  [/] Hide a row");
                System.out.println("  [q] Quit the program");
                System.out.println("  [?] Show this menu");
                    return;
            }
        }  
    }

    public static void HideRowLoop(){
        Database db = Database.getDatabase();
        if (db == null) {
            System.err.println("Unable to connect to the database, exiting.");
            System.exit(1);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Prompt for user action
            char action = promptTable(in);
            switch (action) {
                case 'I':
                    hideRow(db, in);
                    break;
                case 'U':
                    hideUserRow(db, in);
                    break;
                case 'C':
                    System.out.println("Can't hide comments yet! Check back in next week if we ever implement that");
                    break;
                case 'V':
                    System.out.println("Can't hide votes!");
                    break;
                case 'M':
                System.out.println("\nMain Menu");
                System.out.println("  [T] Create table");
                System.out.println("  [D] Drop table");
                System.out.println("  [1] Query a specific row by ID");
                System.out.println("  [*] Query all rows");
                System.out.println("  [+] Insert a new row");
                System.out.println("  [~] Update a row");
                System.out.println("  [-] Delete a row");
                System.out.println("  [/] Hide a row");
                System.out.println("  [q] Quit the program");
                System.out.println("  [?] Show this menu");
                    return;
            }
        }  
    }

    public static void DeleteRowLoop(){
        Database db = Database.getDatabase();
        if (db == null) {
            System.err.println("Unable to connect to the database, exiting.");
            System.exit(1);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Prompt for user action
            char action = promptTable(in);
            switch (action) {
                case 'I':
                    deleteRow(db, in);
                    break;
                case 'U':
                    deleteUserRow(db, in);
                    break;
                case 'C':
                    deleteCommentRow(db, in);
                    break;
                case 'V':
                    deleteVoteRow(db, in);
                    break;
                case 'M':
                System.out.println("\nMain Menu");
                System.out.println("  [T] Create table");
                System.out.println("  [D] Drop table");
                System.out.println("  [1] Query a specific row by ID");
                System.out.println("  [*] Query all rows");
                System.out.println("  [+] Insert a new row");
                System.out.println("  [~] Update a row");
                System.out.println("  [-] Delete a row");
                System.out.println("  [/] Hide a row");
                System.out.println("  [q] Quit the program");
                System.out.println("  [?] Show this menu");
                    return;
                default:
                    System.out.println("Invalid choice. Please choose from IUCV");
            }
        }  
    }


    /**
     * Prompt the user for a menu option.
     *
     * @param in BufferedReader for reading user input.
     * @return The character corresponding to the chosen menu option.
     */
    static char prompt(BufferedReader in) {
        String actions = "TD1*-+~q/?";
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
    static char promptTable(BufferedReader in) {
        String actions = "IUCVM?";
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



  //IDEA queries  
    /**
     * Query a specific IDEA row by ID.
     *
     * @param db The database object.
     * @param in BufferedReader for reading user input.
     */
    static void queryRowById(Database db, BufferedReader in) {
        int id = getInt(in, "Enter the row ID");
        if (id == -1) return;
        Database.RowData row = db.selectOne(id);
        if (row != null) {
            System.out.println("Row [" + row.mId() + "]: "  + " --> " + row.mMessage() + " | Votes: " + row.mVotes());
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
                System.out.println("Row [" + row.mId() + "]: " + " --> " + row.mMessage() + " | Votes: " + row.mVotes());
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
        String message = getString(in, "Enter the message");
        int user_id = getInt(in, "Enter the user ID");
        if (!message.isEmpty()) {
            int result = db.insertRow(message, user_id);
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


    static void hideRow(Database db, BufferedReader in) {
        int user_id = getInt(in, "Enter the id of the post you would like to hide");
        int displayed = getInt(in, "Enter 0 to hide post. Enter 1 to allow post to be displayed");
        if (displayed == 1 || displayed == 0) {
            int result = db.HideOne(user_id, displayed);
            System.out.println(result + " row(s) updated.");
        } else {
            System.out.println("Invalid vote input. Please try again.");
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


//USER queries:
    /**
     * Query a specific IDEA row by ID.
     *
     * @param db The database object.
     * @param in BufferedReader for reading user input.
     */
    static void queryUserRowById(Database db, BufferedReader in) {
        int id = getInt(in, "Enter the row ID");
        if (id == -1) return;
        Database.UserRowData row = db.selectOneUser(id);
        if (row != null) {
            System.out.println("Row [" + row.uId() + "]: " + row.uName() + " Email: " + row.uEmail() + " | Gender: " + row.uGender_identity() + "| Orientation: " + row.uSexual_orientation());
        } else {
            System.out.println("Row not found.");
        }
    }

    /**
     * Query all rows from the table.
     *
     * @param db The database object.
     */
    static void queryAllUserRows(Database db) {
        ArrayList<Database.UserRowData> rows = db.selectAllUser();
        if (rows != null) {
            System.out.println("All rows in the database:");
            for (Database.UserRowData row : rows) {
                System.out.println("Row [" + row.uId() + "]: " + row.uName() + " Email: " + row.uEmail() + " | Gender: " + row.uGender_identity() + "| Orientation: " + row.uSexual_orientation());
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
    static void insertUserRow(Database db, BufferedReader in) {
        String name = getString(in, "Enter the user's name");
        String email = getString(in, "Enter the email");
        String gender = getString(in, "Enter the gender");
        String orientation = getString(in, "Enter the orientation");
        if (!name.isEmpty() && !email.isEmpty()&& !gender.isEmpty()&& !orientation.isEmpty()) {
            int result = db.insertUserRow(name, email, gender, orientation);
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
    static void updateUserRow(Database db, BufferedReader in) {
        int id = getInt(in, "Enter the row ID");
        if (id == -1) return;
        String newName = getString(in, "Enter the new name:");
        String newEmail = getString(in, "Enter the new email:");
        if (!newName.isEmpty() && !newEmail.isEmpty()) {
            int result = db.updateOneUser(id, newName, newEmail);
            System.out.println(result + " row(s) updated.");
        } else {
            System.out.println("Invalid input. Please try again.");
        }
    }

    static void hideUserRow(Database db, BufferedReader in) {
        int user_id = getInt(in, "Enter the id of the user you would like to restrict");
        int displayed = getInt(in, "Enter 0 to restrict user. Enter 1 to allow them to post");
        if (displayed == 1 || displayed == 0) {
            int result = db.HideOneUser(user_id, displayed);
            System.out.println(result + " row(s) updated.");
        } else {
            System.out.println("Invalid vote input. Please try again.");
        }
    }

    /**
     * Delete a row from the table.
     *
     * @param db The database object.
     * @param in BufferedReader for reading user input.
     */
    static void deleteUserRow(Database db, BufferedReader in) {
        int id = getInt(in, "Enter the row ID");
        if (id == -1) return;
        int result = db.deleteUserRow(id);
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


    // COMMENT queries

    /**
     * Query a specific COMMENT row by ID.
     *
     * @param db The database object.
     * @param in BufferedReader for reading user input.
     */
    static void queryCommentRowById(Database db, BufferedReader in) {
        int id = getInt(in, "Enter the row ID");
        if (id == -1) return;
        Database.CommentRowData row = db.selectOneComment(id);
        if (row != null) {
            System.out.println("Row [" + row.commentId() + "]: User [" + row.userId() + "] --> Post [" + row.postId() + "] Message: " + row.message());
        } else {
            System.out.println("Row not found.");
        }
    }

    /**
     * Query all rows from the COMMENT table.
     *
     * @param db The database object.
     */
    static void queryAllCommentRows(Database db) {
        ArrayList<Database.CommentRowData> rows = db.selectAllComments();
        if (rows != null) {
            System.out.println("All comments in the database:");
            for (Database.CommentRowData row : rows) {
                System.out.println("Row [" + row.commentId() + "]: User [" + row.userId() + "] --> Post [" + row.postId() + "] Message: " + row.message());
            }
        } else {
            System.out.println("No data found.");
        }
    }

    /**
     * Insert a new COMMENT row into the table.
     *
     * @param db The database object.
     * @param in BufferedReader for reading user input.
     */
    static void insertCommentRow(Database db, BufferedReader in) {
        int userId = getInt(in, "Enter the user ID");
        int postId = getInt(in, "Enter the post ID");
        String message = getString(in, "Enter the message");
        if (!message.isEmpty()) {
            int result = db.insertComment(userId, postId, message);
            System.out.println(result + " row(s) inserted.");
        } else {
            System.out.println("Invalid input. Please try again.");
        }
    }

    /**
     * Update a COMMENT row in the table.
     *
     * @param db The database object.
     * @param in BufferedReader for reading user input.
     */
    static void updateCommentRow(Database db, BufferedReader in) {
        int id = getInt(in, "Enter the row ID");
        if (id == -1) return;
        String newMessage = getString(in, "Enter the new message");
        if (!newMessage.isEmpty()) {
            int result = db.updateComment(id, newMessage);
            System.out.println(result + " row(s) updated.");
        } else {
            System.out.println("Invalid input. Please try again.");
        }
    }

    /**
     * Delete a COMMENT row from the table.
     *
     * @param db The database object.
     * @param in BufferedReader for reading user input.
     */
    static void deleteCommentRow(Database db, BufferedReader in) {
        int id = getInt(in, "Enter the row ID");
        if (id == -1) return;
        int result = db.deleteComment(id);
        System.out.println(result + " row(s) deleted.");
    }



    // VOTE queries

    /**
     * Query a specific VOTE row by ID.
     *
     * @param db The database object.
     * @param in BufferedReader for reading user input.
     */
    static void queryVoteRowById(Database db, BufferedReader in) {
        int id = getInt(in, "Enter the row ID");
        if (id == -1) return;
        Database.VoteRowData row = db.selectOneVote(id);
        if (row != null) {
            System.out.println("Row [" + row.voteId() + "]: User [" + row.userId() + "] --> Post [" + row.postId() + "] Vote: " + (row.updown() == 1 ? "Upvote" : "Downvote"));
        } else {
            System.out.println("Row not found.");
        }
    }

    /**
     * Query all rows from the VOTE table.
     *
     * @param db The database object.
     */
    static void queryAllVoteRows(Database db) {
        ArrayList<Database.VoteRowData> rows = db.selectAllVotes();
        if (rows != null) {
            System.out.println("All votes in the database:");
            for (Database.VoteRowData row : rows) {
                System.out.println("Row [" + row.voteId() + "]: User [" + row.userId() + "] --> Post [" + row.postId() + "] Vote: " + (row.updown() == 1 ? "Upvote" : "Downvote"));
            }
        } else {
            System.out.println("No data found.");
        }
    }

    /**
     * Insert a new VOTE row into the table.
     *
     * @param db The database object.
     * @param in BufferedReader for reading user input.
     */
    static void insertVoteRow(Database db, BufferedReader in) {
        int userId = getInt(in, "Enter the user ID");
        int postId = getInt(in, "Enter the post ID");
        int updown = getInt(in, "Enter 1 for upvote, -1 for downvote");
        if (updown == 1 || updown == -1) {
            int result = db.insertVote(userId, postId, updown);
            System.out.println(result + " row(s) inserted.");
        } else {
            System.out.println("Invalid vote input. Please try again.");
        }
    }

    /**
     * Update a VOTE row in the table.
     *
     * @param db The database object.
     * @param in BufferedReader for reading user input.
     */
    static void updateVoteRow(Database db, BufferedReader in) {
        int id = getInt(in, "Enter the row ID");
        if (id == -1) return;
        int newVote = getInt(in, "Enter the new vote (1 for upvote, -1 for downvote)");
        if (newVote == 1 || newVote == -1) {
            int result = db.updateVote(id, newVote);
            System.out.println(result + " row(s) updated.");
        } else {
            System.out.println("Invalid input. Please try again.");
        }
    }

    /**
     * Delete a VOTE row from the table.
     *
     * @param db The database object.
     * @param in BufferedReader for reading user input.
     */
    static void deleteVoteRow(Database db, BufferedReader in) {
        int id = getInt(in, "Enter the row ID");
        if (id == -1) return;
        int result = db.deleteVote(id);
        System.out.println(result + " row(s) deleted.");
    }
}