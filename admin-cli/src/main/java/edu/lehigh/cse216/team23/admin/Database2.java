// package edu.lehigh.cse216.team23.admin;

// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.util.ArrayList;
// import java.util.List;

// public class Database2 {

//     // Database connection object
//     private static Connection connection;

//     // SQL Queries for each table type (idea, user, comment, vote)
    
//     // Idea Table SQL Queries
//     private static final String SQL_CREATE_TABLE_IDEA =
//             "CREATE TABLE IF NOT EXISTS ideas_tbl (" +
//             " id SERIAL PRIMARY KEY," +
//             " subject VARCHAR(50) NOT NULL," +
//             " message VARCHAR(500) NOT NULL," +
//             " votes INT DEFAULT 0 NOT NULL)";
    
//     private static final String SQL_DROP_TABLE_IDEAS = "DROP TABLE IF EXISTS ideas_tbl";
    
//     private static final String SQL_INSERT_ONE_IDEA = 
//             "INSERT INTO ideas_tbl (subject, message, votes) VALUES (?, ?, 0)";
    
//     private static final String SQL_UPDATE_ONE_IDEA = 
//             "UPDATE ideas_tbl SET message = ? WHERE id = ?";
    
//     private static final String SQL_DELETE_ONE_IDEA = "DELETE FROM ideas_tbl WHERE id = ?";
    
//     private static final String SQL_SELECT_ALL_IDEAS = "SELECT id, subject, message, votes FROM ideas_tbl";
    
//     private static final String SQL_SELECT_ONE_IDEA = "SELECT * FROM ideas_tbl WHERE id = ?";

//     // User Table SQL Queries
//     private static final String SQL_CREATE_TABLE_USER =
//             "CREATE TABLE IF NOT EXISTS users_tbl (" +
//             " id SERIAL PRIMARY KEY," +
//             " name VARCHAR(50) NOT NULL," +
//             " email VARCHAR(100) NOT NULL," +
//             " gender_identity VARCHAR(50)," +
//             " sexual_orientation VARCHAR(50))";
    
//     private static final String SQL_DROP_TABLE_USER = "DROP TABLE IF EXISTS users_tbl";
    
//     private static final String SQL_INSERT_ONE_USER = 
//             "INSERT INTO users_tbl (name, email, gender_identity, sexual_orientation) VALUES (?, ?, ?, ?)";
    
//     private static final String SQL_UPDATE_ONE_USER = 
//             "UPDATE users_tbl SET name = ?, email = ?, gender_identity = ?, sexual_orientation = ? WHERE id = ?";
    
//     private static final String SQL_DELETE_ONE_USER = "DELETE FROM users_tbl WHERE id = ?";
    
//     private static final String SQL_SELECT_ALL_USERS = "SELECT id, name, email, gender_identity, sexual_orientation FROM users_tbl";
    
//     private static final String SQL_SELECT_ONE_USER = "SELECT * FROM users_tbl WHERE id = ?";

//     // Comment Table SQL Queries
//     private static final String SQL_CREATE_TABLE_COMMENT =
//             "CREATE TABLE IF NOT EXISTS comments_tbl (" +
//             " id SERIAL PRIMARY KEY," +
//             " user_id INT NOT NULL," +
//             " post_id INT NOT NULL," +
//             " message VARCHAR(500) NOT NULL)";
    
//     private static final String SQL_DROP_TABLE_COMMENT = "DROP TABLE IF EXISTS comments_tbl";
    
//     private static final String SQL_INSERT_ONE_COMMENT = 
//             "INSERT INTO comments_tbl (user_id, post_id, message) VALUES (?, ?, ?)";
    
//     private static final String SQL_UPDATE_ONE_COMMENT = 
//             "UPDATE comments_tbl SET message = ? WHERE id = ?";
    
//     private static final String SQL_DELETE_ONE_COMMENT = "DELETE FROM comments_tbl WHERE id = ?";
    
//     private static final String SQL_SELECT_ALL_COMMENTS = "SELECT id, user_id, post_id, message FROM comments_tbl";
    
//     private static final String SQL_SELECT_ONE_COMMENT = "SELECT * FROM comments_tbl WHERE id = ?";

//     // Vote Table SQL Queries
//     private static final String SQL_CREATE_TABLE_VOTE =
//             "CREATE TABLE IF NOT EXISTS votes_tbl (" +
//             " id SERIAL PRIMARY KEY," +
//             " user_id INT NOT NULL," +
//             " post_id INT NOT NULL," +
//             " updown INT NOT NULL)";
    
//     private static final String SQL_DROP_TABLE_VOTE = "DROP TABLE IF EXISTS votes_tbl";
    
//     private static final String SQL_INSERT_ONE_VOTE = 
//             "INSERT INTO votes_tbl (user_id, post_id, updown) VALUES (?, ?, ?)";
    
//     private static final String SQL_UPDATE_ONE_VOTE = 
//             "UPDATE votes_tbl SET updown = ? WHERE id = ?";
    
//     private static final String SQL_DELETE_ONE_VOTE = "DELETE FROM votes_tbl WHERE id = ?";
    
//     private static final String SQL_SELECT_ALL_VOTES = "SELECT id, user_id, post_id, updown FROM votes_tbl";
    
//     private static final String SQL_SELECT_ONE_VOTE = "SELECT * FROM votes_tbl WHERE id = ?";

//     // RowData class to represent any row
//     public static abstract class RowData {}

//     public static class IdeaRow extends RowData {
//         int id;
//         String subject;
//         String message;
//         int votes;

//         public IdeaRow(int id, String subject, String message, int votes) {
//             this.id = id;
//             this.subject = subject;
//             this.message = message;
//             this.votes = votes;
//         }
        
//         // Getters and Setters
//     }

//     public static class UserRow extends RowData {
//         int id;
//         String name;
//         String email;
//         String gender_identity;
//         String sexual_orientation;

//         public UserRow(int id, String name, String email, String gender_identity, String sexual_orientation) {
//             this.id = id;
//             this.name = name;
//             this.email = email;
//             this.gender_identity = gender_identity;
//             this.sexual_orientation = sexual_orientation;
//         }
        
//         // Getters and Setters
//     }

//     public static class CommentRow extends RowData {
//         int id;
//         int userId;
//         int postId;
//         String message;

//         public CommentRow(int id, int userId, int postId, String message) {
//             this.id = id;
//             this.userId = userId;
//             this.postId = postId;
//             this.message = message;
//         }
        
//         // Getters and Setters
//     }

//     public static class VoteRow extends RowData {
//         int id;
//         int userId;
//         int postId;
//         int updown;

//         public VoteRow(int id, int userId, int postId, int updown) {
//             this.id = id;
//             this.userId = userId;
//             this.postId = postId;
//             this.updown = updown;
//         }
        
//         // Getters and Setters
//     }

//     private Connection mConnection;

//     // Method to create a table
//     public static void createTable(String tableType) {
//         String sql = "";
        
//             if (tableType.equals("idea")) {
//                 sql = SQL_CREATE_TABLE_IDEA;
//             } else if (tableType.equals("user")) {
//                 sql = SQL_CREATE_TABLE_USER;
//             } else if (tableType.equals("comment")) {
//                 sql = SQL_CREATE_TABLE_COMMENT;
//             } else if (tableType.equals("vote")) {
//                 sql = SQL_CREATE_TABLE_VOTE;
//             }
//     }

//     // Method to insert a row
//     public static int insertRow(String tableType, RowData row) {
//         String sql = "";
//         int result = 0;
//         try (Connection conn = getConnection()) {
//             if (tableType.equals("idea")) {
//                 IdeaRow idea = (IdeaRow) row;
//                 sql = SQL_INSERT_ONE_IDEA;
//                 try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                     stmt.setString(1, idea.subject);
//                     stmt.setString(2, idea.message);
//                     result = stmt.executeUpdate();
//                 }
//             } else if (tableType.equals("user")) {
//                 UserRow user = (UserRow) row;
//                 sql = SQL_INSERT_ONE_USER;
//                 try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                     stmt.setString(1, user.name);
//                     stmt.setString(2, user.email);
//                     stmt.setString(3, user.gender_identity);
//                     stmt.setString(4, user.sexual_orientation);
//                     result = stmt.executeUpdate();
//                 }
//             } else if (tableType.equals("comment")) {
//                 CommentRow comment = (CommentRow) row;
//                 sql = SQL_INSERT_ONE_COMMENT;
//                 try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                     stmt.setInt(1, comment.userId);
//                     stmt.setInt(2, comment.postId);
//                     stmt.setString(3, comment.message);
//                     result = stmt.executeUpdate();
//                 }
//             } else if (tableType.equals("vote")) {
//                 VoteRow vote = (VoteRow) row;
//                 sql = SQL_INSERT_ONE_VOTE;
//                 try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                     stmt.setInt(1, vote.userId);
//                     stmt.setInt(2, vote.postId);
//                     stmt.setInt(3, vote.updown);
//                     result = stmt.executeUpdate();
//                 }
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return result;
//     }

//     // Method to select a row by ID
//     public static RowData selectRowById(String tableType, int id) {
//         String sql = "";
//         RowData row = null;
//         try (Connection conn = getConnection()) {
//             if (tableType.equals("idea")) {
//                 sql = SQL_SELECT_ONE_IDEA;
//                 try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                     stmt.setInt(1, id);
//                     ResultSet rs = stmt.executeQuery();
//                     if (rs.next()) {
//                         row = new IdeaRow(rs.getInt("id"), rs.getString("subject"), rs.getString("message"), rs.getInt("votes"));
//                     }
//                 }
//             } else if (tableType.equals("user")) {
//                 sql = SQL_SELECT_ONE_USER;
//                 try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                     stmt.setInt(1, id);
//                     ResultSet rs = stmt.executeQuery();
//                     if (rs.next()) {
//                         row = new UserRow(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
//                                 rs.getString("gender_identity"), rs.getString("sexual_orientation"));
//                     }
//                 }
//             } else if (tableType.equals("comment")) {
//                 sql = SQL_SELECT_ONE_COMMENT;
//                 try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                     stmt.setInt(1, id);
//                     ResultSet rs = stmt.executeQuery();
//                     if (rs.next()) {
//                         row = new CommentRow(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("post_id"), rs.getString("message"));
//                     }
//                 }
//             } else if (tableType.equals("vote")) {
//                 sql = SQL_SELECT_ONE_VOTE;
//                 try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                     stmt.setInt(1, id);
//                     ResultSet rs = stmt.executeQuery();
//                     if (rs.next()) {
//                         row = new VoteRow(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("post_id"), rs.getInt("updown"));
//                     }
//                 }
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return row;
//     }
// }
