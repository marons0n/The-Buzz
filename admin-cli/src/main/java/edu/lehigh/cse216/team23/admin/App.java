package edu.lehigh.cse216.team23.admin;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class App {
    //private static Database db;
    private static Scanner scanner;
    


    public static void main(String[] args) {
        Database db = Database.getDatabase();
        if (db == null) {
            System.err.println("Unable to connect to the database, exiting.");
            System.exit(1);
        }
        scanner = new Scanner(System.in);

        System.out.println("Welcome to the Admin CLI");
        showMenu();
        System.out.println(db);

        String choice;
        do {
            System.out.print("Enter your choice: ");
            choice = scanner.nextLine().toLowerCase();
            processChoice(choice, db);
        } while (!choice.equals("y"));

        scanner.close();
        db.disconnect();
    }

    private static void showMenu() {
        System.out.println("\nAdmin CLI - Main Menu");
        System.out.println("\nIdeas:");
        System.out.println("  [a] Show all ideas");
        System.out.println("  [b] Show a single idea by ID");
        System.out.println("  [c] Insert new idea");
        System.out.println("  [d] Update an idea");
        System.out.println("  [e] Delete an idea");
        System.out.println("\nUsers:");
        System.out.println("  [g] Find all users");
        System.out.println("  [h] Find a user by user ID");
        System.out.println("  [i] Insert a new user");
        System.out.println("  [j] Update a user");
        System.out.println("  [k] Delete a user");
        System.out.println("\nComments:");
        System.out.println("  [m] Query all comments");
        System.out.println("  [o] Insert new comment");
        System.out.println("  [q] Delete comment");
        System.out.println("\nVotes:");
        System.out.println("  [u] Insert a new vote");
        System.out.println("  [v] Delete a vote");
        System.out.println("\nGeneral:");
        System.out.println("  [w] Create tables");
        System.out.println("  [x] Drop tables");
        System.out.println("  [y] Quit");
        System.out.println("  [z] Show this menu");
    }

    private static void processChoice(String choice, Database db) {
        switch (choice) {
            case "a":
                showAllIdeas(db);
                break;
            case "b":
                showSingleIdea(db);
                break;
            case "c":
                insertNewIdea(db);
                break;
            case "d":
                updateIdea(db);
                break;
            case "e":
                deleteIdea(db);
                break;
            case "g":
                findAllUsers(db);
                break;
            case "h":
                findUserById(db);
                break;
            case "i":
                insertNewUser(db);
                break;
            case "j":
                updateUser(db);
                break;
            case "k":
                deleteUser(db);
                break;
            case "m":
                queryAllComments(db);
                break;
            case "o":
                insertNewComment(db);
                break;
            case "q":
                deleteComment(db);
                break;
            case "u":
                insertNewVote(db);
                break;
            case "v":
                deleteVote(db);
                break;
            case "w":
                createTables(db);
                break;
            case "x":
                dropTables(db);
                break;
            case "y":
                System.out.println("Exiting...");
                break;
            case "z":
                showMenu();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    //IDEA ITEMS

    public static void showAllIdeas(Database db) {
        ArrayList<Database.RowData> ideas = db.selectAll();
        if (ideas != null && !ideas.isEmpty()) {
            System.out.println("All ideas:");
            for (Database.RowData idea : ideas) {
                System.out.println("ID: " + idea.mId() + ", Message: " + idea.mMessage() + 
                                   ", Votes: " + idea.mVotes() + ", User ID: " + idea.userId());
            }
        } else {
            System.out.println("No ideas found.");
        }
    }

    private static void showSingleIdea(Database db) {
        System.out.print("Enter idea ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        Database.RowData idea = db.selectOne(id);
        if (idea != null) {
            System.out.println("Idea: " + idea.mMessage() + ", Votes: " + idea.mVotes() + ", User ID: " + idea.userId());
        } else {
            System.out.println("Idea not found.");
        }
    }

    public static void insertNewIdea(Database db) {
        System.out.print("Enter idea message: ");
        String message = scanner.nextLine();
        System.out.print("Enter user ID: ");
        
        int userId = Integer.parseInt(scanner.nextLine());
        if (/*db.userExists(userId)*/true) {
            int result = db.insertRow(message, userId);
            System.out.println(result > 0 ? "Idea inserted successfully." : "Failed to insert idea.");
        } else {
            System.out.println("User ID does not exist. Please try again.");
            
        }
    }

    private static void updateIdea(Database db) {
        System.out.print("Enter idea ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        Database.RowData idea = db.selectOne(id);
        if (idea != null) {
            System.out.println("Current message: " + idea.mMessage());
            System.out.print("Enter new message (press enter to keep the current message): ");
            String newMessage = scanner.nextLine();
            
            System.out.println(idea.displayed() == 1 ? "Current idea is visible" : "Current idea is invisible");
            System.out.print("Make it visible (type: 1) or make it invisible (type: 0): ");
            int newVisible = scanner.nextInt();

            System.out.println(idea.file_displayed() == 1 ? "Current file is visible" : "Current file is invisible");
            System.out.print("Make it visible (type: 1) or make it invisible (type: 0): ");
            int newFileVisible = scanner.nextInt();

            System.out.println(idea.link_displayed() == 1 ? "Current link is visible" : "Current link is invisible");
            System.out.print("Make it visible (type: 1) or make it invisible (type: 0): ");
            int newLinkVisible = scanner.nextInt();
            
            int result = 0;
            if(newMessage.trim().isEmpty()){
                result = db.updateOne(id, idea.mMessage(), newVisible, newFileVisible, newLinkVisible);
            }else if (!(newVisible == 1 || newVisible == 0)){
                System.out.println("You didnt enter a 0 or 1. Try again.");
            }else{
                result = db.updateOne(id, newMessage, newVisible, newFileVisible, newLinkVisible);
            }

            
            System.out.println(result > 0 ? "Idea updated successfully." : "Failed to update idea.");
        } else {
            System.out.println("Idea not found.");
        }
    }

    private static void deleteIdea(Database db) {
        System.out.print("Enter idea ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        int result = db.deleteRow(id);
        System.out.println(result > 0 ? "Idea deleted successfully." : "Failed to delete idea.");
    }

    //USER ITEMS

    public static void findAllUsers(Database db) {
        ArrayList<Database.UserRowData> users = db.selectAllUser();
        if (users != null && !users.isEmpty()) {
            System.out.println("All users:");
            for (Database.UserRowData user : users) {
                System.out.println("ID: " + user.uId() + ", Name: " + user.uName() + 
                                   ", Email: " + user.uEmail() + ", Gender: " + user.uGender_identity() + 
                                   ", Orientation: " + user.uSexual_orientation());
            }
        } else {
            System.out.println("No users found.");
        }
    }

    private static void findUserById(Database db) {
        System.out.print("Enter user ID: ");
        String id = scanner.nextLine();
        Database.UserRowData user = db.selectOneUser(id);
        if (user != null) {
            System.out.println("User: " + user.uName() + ", Email: " + user.uEmail());
        } else {
            System.out.println("User not found.");
        }
    }

    private static void insertNewUser(Database db) {
        System.out.print("Enter user name: ");
        String name = scanner.nextLine();
        System.out.print("Enter user email: ");
        String email = scanner.nextLine();
        System.out.print("Enter gender identity: ");
        String genderIdentity = scanner.nextLine();
        System.out.print("Enter sexual orientation: ");
        String sexualOrientation = scanner.nextLine();
        int result = db.insertUserRow(name, email, genderIdentity, sexualOrientation);
        System.out.println(result > 0 ? "User inserted successfully." : "Failed to insert user.");
    }

    private static void updateUser(Database db) {
        System.out.print("Enter user ID to update: ");
        String id = scanner.nextLine();
        Database.UserRowData user = db.selectOneUser(id);
        if (user != null) {
            System.out.println("Current user details:");
            System.out.println("Name: " + user.uName() + ", Email: " + user.uEmail() + ", Restricted: " + user.uRestricted());
            
            System.out.print("Enter new name (press enter to keep current): ");
            String newName = scanner.nextLine();
            if (newName.isEmpty()) newName = user.uName();
            
            System.out.print("Enter new email (press enter to keep current): ");
            String newEmail = scanner.nextLine();
            if (newEmail.isEmpty()) newEmail = user.uEmail();

            System.out.print("Enter 1 or 0 to unrestrict or restrict a user: ");
            int newRestricted = scanner.nextInt();
            
            int result = db.updateOneUser(id, newName, newEmail, newRestricted);
            System.out.println(result > 0 ? "User updated successfully." : "Failed to update user.");
        } else {
            System.out.println("User not found.");
        }
    }

    private static void deleteUser(Database db) {
        System.out.print("Enter user ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        int result = db.deleteUserRow(id);
        System.out.println(result > 0 ? "User deleted successfully." : "Failed to delete user.");
    }

    public static void queryAllComments(Database db) {
        ArrayList<Database.CommentRowData> comments = db.selectAllComments();
        for (Database.CommentRowData comment : comments) {
            System.out.println("Comment ID: " + comment.commentId() + ", User ID: " + comment.userId() + 
                               ", Post ID: " + comment.postId() + ", Message: " + comment.message());
        }
    }

    private static void insertNewComment(Database db) {
        System.out.print("Enter user ID: ");
        int userId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter post ID: ");
        int postId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter comment message: ");
        String message = scanner.nextLine();
        int result = db.insertComment(userId, postId, message);
        System.out.println(result > 0 ? "Comment inserted successfully." : "Failed to insert comment.");
    }

    private static void deleteComment(Database db) {
        System.out.print("Enter comment ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        int result = db.deleteComment(id);
        System.out.println(result > 0 ? "Comment deleted successfully." : "Failed to delete comment.");
    }

    private static void insertNewVote(Database db) {
        System.out.print("Enter user ID: ");
        int userId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter post ID: ");
        int postId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter vote (1 for up, -1 for down): ");
        int updown = Integer.parseInt(scanner.nextLine());
        // Assuming there's a method to insert a vote in the Database class
        // int result = db.insertVote(userId, postId, updown);
        // System.out.println(result > 0 ? "Vote inserted successfully." : "Failed to insert vote.");
        System.out.println("Vote insertion not implemented in the Database class.");
    }

    private static void deleteVote(Database db) {
        System.out.print("Enter vote ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        // Assuming there's a method to delete a vote in the Database class
        // int result = db.deleteVote(id);
        // System.out.println(result > 0 ? "Vote deleted successfully." : "Failed to delete vote.");
        System.out.println("Vote deletion not implemented in the Database class.");
    }

    private static void createTables(Database db) {
        db.createTable();
        db.createUserTable();
        db.createCommentTable();
        // Assuming there's a method to create the votes table
        // db.createVoteTable();
        System.out.println("Tables created successfully.");
    }

    private static void dropTables(Database db) {
        db.dropTable();
        db.dropUserTable();
        db.dropCommentTable();
        // Assuming there's a method to drop the votes table
        // db.dropVoteTable();
        System.out.println("Tables dropped successfully.");
    }
}