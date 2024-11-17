package edu.lehigh.cse216.team23.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Map;
import java.util.ArrayList;

/**
* Manages database operations for the ideas_tbl in a PostgreSQL database.
* Provides methods for creating, reading, updating, and deleting rows.
* Connections are established using environment variables.
*
* Use static getDatabase() methods to obtain instances of this class.
*/
public class Database {
   /**
    *  Represents a row in the ideas_tbl with id, votes count, and message content.
    * @param mId the id of the idea
    * @param mVotes the number of votes on the idea (cannot be less than 0)
    * @param mMessage the content of the idea
    * @param mUserId the user id of the idea
    * */
   public static record RowDataIdeas(int mId, int mVotes, String mMessage, String mUserId) {
   }

   /**
    * Represents a row in the comments_tbl with comment_id, user_id, post_id, and message content.
    * @param mCommentId the id of the comment
    * @param mUserId the user id of the comment
    * @param mPostId the post id of the comment
    * @param mMessage the content of the comment
    */
   public static record RowDataComments(int mCommentId, String mUserId, int mPostId, String mMessage) {
   }

    /**
     * Represents a row in the users_tbl with user_id, email, and name.
     * @param mUserId the user id of the user
     * @param mEmail the email of the user
     * @param mName the name of the user
     */
   public static record RowDataUsers(String mUserId, String mName, String mEmail) {
   }




   private Connection mConnection;


   /**
    * Uses the presence of environment variables to invoke the correct
    * overloaded `getDatabase` method
    *
    * @return a valid Database object with active connection on success, null
    *         otherwise
    */
   static Database getDatabase() {
       // get the Postgres configuration from environment variables;
       // you could name them almost anything
       Map<String, String> env = System.getenv();
       String ip = env.get("POSTGRES_IP");
       String port = env.get("POSTGRES_PORT");
       String user = env.get("POSTGRES_USER");
       String pass = env.get("POSTGRES_PASS");
       String dbname = env.get("POSTGRES_DBNAME");
       String dbUri = env.get("DATABASE_URI");


       System.out.printf("Using the following environment variables:%n%s%n", "-".repeat(45));
       System.out.printf("POSTGRES_IP=%s%n", ip);
       System.out.printf("POSTGRES_PORT=%s%n", port);
       System.out.printf("POSTGRES_USER=%s%n", user);
       System.out.printf("POSTGRES_PASS=%s%n", "omitted");
       System.out.printf("POSTGRES_DBNAME=%s%n", dbname);
       System.out.printf("DATABASE_URI=%s%n", dbUri);


       if (dbUri != null && dbUri.length() > 0) {
           return Database.getDatabase(dbUri);
       } else if (ip != null && port != null && dbname != null && user != null && pass != null) {
           return Database.getDatabase(ip, port, dbname, user, pass);
       }
       // else insufficient information to connect
       System.err.println("Insufficient information to connect to database.");
       return null;
   }


   /**
    * Uses dbUri to create a connection to a database, then stores it in the
    * returned Database object
    *
    * @param dbUri the connection string for the database
    * @return null upon connection failure, otherwise a valid Database with open
    *         connection
    */
   static Database getDatabase(String dbUri) {
       if (dbUri != null && dbUri.length() > 0) {
           Database db = new Database();


           // DATABASE_URI appears to be set
           System.out.println("Attempting to use provided DATABASE_URI env var.");
           try {
               db.mConnection = DriverManager.getConnection(dbUri);
               if (db.mConnection == null) {
                   System.err.println("Error: DriverManager.getConnection() returned a null object");
               } else {
                   System.out.println(" ... successfully connected");
               }
           } catch (SQLException e) {
               System.err.println("Error: DriverManager.getConnection() threw a SQLException");
               e.printStackTrace();
           }
           return db;
       }
       return null;
   }

   /**
    * Uses the provided ip, port, dbname, user, and pass to create a connection to a
    * database, then stores it in the returned Database object
    *
    * @param ip    the IP address of the database
    * @param port  the port of the database
    * @param dbname the name of the database
    * @param user  the username for the database
    * @param pass  the password for the database
    * @return null upon connection failure, otherwise a valid Database with open
    *         connection
    */
   static Database getDatabase(String ip, String port, String dbname, String user, String pass) {
       if (ip != null && port != null && dbname != null && user != null && pass != null) {
           // POSTGRES_* variables appear to be set
           System.out.println("Attempting to use provided POSTGRES_{IP, PORT, USER, PASS, DBNAME} env var.");


           System.out.println("Connecting to " + ip + ":" + port);
           try {
               Database db = new Database();


               // Open a connection, fail if we cannot get one
               db.mConnection = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port + "/" + dbname,
                       user, pass);
               if (db.mConnection == null) {
                   System.out.println("\n\tError: DriverManager.getConnection() returned a null object");
                   return null;
               } else {
                   System.out.println(" ... successfully connected");
               }


               return db;
           } catch (SQLException e) {
               System.out.println("\n\tError: DriverManager.getConnection() threw a SQLException");
               e.printStackTrace();
               return null;
           }
       } else { // insufficient information to connect
           System.err.println("Insufficient information to connect. Bye.");
           return null;
       }
   }


   /**
    * The Database constructor is private: we only create Database objects
    * through one or more static getDatabase() methods.
    */
   private Database() {}


//INSERT ROW INTO DATABASE--------------------------------------------------------------------------------------------
    private PreparedStatement mInsertOne;
    private static final String SQL_INSERT_ONE_IDEAS_TBL = "INSERT INTO ideas_tbl (user_id, message) VALUES (?, ?) RETURNING id";

    private boolean init_mInsertOne() {
        try {
            mInsertOne = mConnection.prepareStatement(SQL_INSERT_ONE_IDEAS_TBL);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertOne");
            System.err.println("Using SQL: " + SQL_INSERT_ONE_IDEAS_TBL);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }
    /**
     * Insert a new row into the ideas_tbl table
     *
     * @param userId  The user id of the idea
     * @param message The content of the idea
     * @return The id of the new row, or -1 if the insertion fails
     */
    int insertRow(String userId, String message) {
        if (mInsertOne == null) // not yet initialized, do lazy init
            init_mInsertOne(); // lazy init
        int res = -1;
        try {
            System.out.println("Database operation: insertOne(String userId, String message)");
            mInsertOne.setString(1, userId);
            mInsertOne.setString(2, message);
            ResultSet rs = mInsertOne.executeQuery();
            if (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

//SELECT ONE ROW FROM DATABASE--------------------------------------------------------------------------------------------
    private PreparedStatement mSelectOne;
    private static final String SQL_SELECT_ONE_IDEAS_TBL = "SELECT *" +
            " FROM ideas_tbl" +
            " WHERE id=? ;";
 
    private boolean init_mSelectOne() {
        // return true on success, false otherwise
        try {
            mSelectOne = mConnection.prepareStatement(SQL_SELECT_ONE_IDEAS_TBL);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectOne");
            System.err.println("Using SQL: " + SQL_SELECT_ONE_IDEAS_TBL);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }
 
    /**
     * Get all data for a specific row, by ID
     *
     * @param id The id of the row being requested
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowDataIdeas selectOne(int row_id) {
        if (mSelectOne == null) // not yet initialized, do lazy init
            init_mSelectOne(); // lazy init
        RowDataIdeas data = null;
        try {
            System.out.println("Database operation: selectOne(int)");
            mSelectOne.setInt(1, row_id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                int votes = rs.getInt("votes");
                String message = rs.getString("message");
                 String userId = rs.getString("user_id");
                data = new RowDataIdeas(id, votes, message, userId);
            }
            rs.close(); // remember to close the result set
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }


//UPDATE ONE ROW IN DATABASE--------------------------------------------------------------------------------------------
   private PreparedStatement mUpdateOne_2arg;
   private static final String SQL_UPDATE_ONE_IDEAS_TBL_2ARG = "UPDATE ideas_tbl" +
           " SET votes = ?, message = ?" +
           " WHERE id = ?";

   private boolean init_mUpdateOne_2arg() {
       // return true on success, false otherwise
       try {
           mUpdateOne_2arg = mConnection.prepareStatement(SQL_UPDATE_ONE_IDEAS_TBL_2ARG);
       } catch (SQLException e) {
           System.err.println("Error creating prepared statement: mUpdateOne");
           System.err.println("Using SQL: " + SQL_UPDATE_ONE_IDEAS_TBL_2ARG);
           e.printStackTrace();
           this.disconnect(); // @TODO is disconnecting on exception what we want?
           return false;
       }
       return true;
   }

   /**
    * Update the votes and message for a row in the database
    *
    * @param id      The id of the row to update
    * @param votes   The new message votes
    * @param message The new message contents
    * @return The number of rows that were updated. -1 indicates an error.
    */
   int updateOne(int id, int votes/* , String message */) {
       if (mUpdateOne_2arg == null) // not yet initialized, do lazy init
           init_mUpdateOne_2arg(); // lazy init
       int res = -1;
       try {
           System.out.println("Database operation: updateOne(int id, int votes, String message)");
           mUpdateOne_2arg.setInt(1, votes);

           mUpdateOne_2arg.setString(2, selectOne(id).mMessage); // just keeps the message the same because we only
                                                                 // wanna update votes
           mUpdateOne_2arg.setInt(3, id);
           res = mUpdateOne_2arg.executeUpdate();
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return res;
   }

//INSERT VOTE INTO DATABASE--------------------------------------------------------------------------------------------
    private PreparedStatement mInsertVote;
    private static final String SQL_INSERT_VOTE = "INSERT INTO votes_tbl (user_id, post_id, updown) VALUES (?, ?, ?) RETURNING vote_id";
    
    private boolean init_mInsertVote() {
        try {
            mInsertVote = mConnection.prepareStatement(SQL_INSERT_VOTE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertVote");
            System.err.println("Using SQL: " + SQL_INSERT_VOTE);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Insert a vote into the database
     *
     * @param userId The user id of the vote
     * @param postId The post id of the vote
     * @param updown The updown of the vote
     * @return The number of rows that were inserted. -1 indicates an error.
     */
    int insertVote(String userId, int postId, int updown) {
        if (mInsertVote == null) // not yet initialized, do lazy init
            init_mInsertVote(); // lazy init
        int res = -1;
        try {
            System.out.println("Database operation: insertVote(String userId, int postId, int updown)");
            mInsertVote.setString(1, userId);
            mInsertVote.setInt(2, postId);
            mInsertVote.setInt(3, updown);
            ResultSet rs = mInsertVote.executeQuery();
            if (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

//GET ALL IDEAS FROM DATABASE--------------------------------------------------------------------------------------------
   private PreparedStatement mSelectAll;
   private static final String SQL_SELECT_ALL_IDEAS_TBL = "SELECT id, votes, message, user_id" +
           " FROM ideas_tbl;";

   private boolean init_mSelectAll() {
       try {
           mSelectAll = mConnection.prepareStatement(SQL_SELECT_ALL_IDEAS_TBL);
       } catch (SQLException e) {
           System.err.println("Error creating prepared statement: mSelectAll");
           System.err.println("Using SQL: " + SQL_SELECT_ALL_IDEAS_TBL);
           e.printStackTrace();
           this.disconnect(); // @TODO is disconnecting on exception what we want?
           return false;
       }
       return true;
   }

    /**
     * Get all data from the database
     *
     * @return The data for all rows in the database
     */
   ArrayList<RowDataIdeas> selectAll() {
       if (mSelectAll == null)
           init_mSelectAll();
       ArrayList<RowDataIdeas> res = new ArrayList<RowDataIdeas>();
       try {
           System.out.println("Database operation: selectAll()");
           ResultSet rs = mSelectAll.executeQuery();
           while (rs.next()) {
               int id = rs.getInt("id");
               int votes = rs.getInt("votes");
               String message = rs.getString("message");
               String userId = rs.getString("user_id");
               RowDataIdeas data = new RowDataIdeas(id, votes, message, userId);
               res.add(data);
           }
           rs.close();
           return res;
       } catch (SQLException e) {
           e.printStackTrace();
           return null;
       }
   }

//GET ALL COMMENTS FROM POST FROM DATABASE--------------------------------------------------------------------------------------------
    private PreparedStatement mSelectAllComments;
    private static final String SQL_SELECT_ALL_COMMENTS = "SELECT comment_id, user_id, post_id, message" +
           " FROM comments_tbl WHERE post_id = ?;";

    private boolean init_mSelectAllComments() {
        try {
            mSelectAllComments = mConnection.prepareStatement(SQL_SELECT_ALL_COMMENTS);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAllComments");
            System.err.println("Using SQL: " + SQL_SELECT_ALL_COMMENTS);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Get all comments on idea from the database
     *
     * @return The data for all comments in the database
     */
    ArrayList<RowDataComments> selectAllComments(int id) {
        if (mSelectAllComments == null)
            init_mSelectAllComments();
        ArrayList<RowDataComments> res = new ArrayList<RowDataComments>();
        try {
            System.out.println("Database operation: selectAllComments()");
            mSelectAllComments.setInt(1, id);
            ResultSet rs = mSelectAllComments.executeQuery();
            while (rs.next()) {
                int commentid = rs.getInt("comment_id");
                String userid = rs.getString("user_id");
                int postid = rs.getInt("post_id");
                String text = rs.getString("message");
                RowDataComments data = new RowDataComments(commentid, userid, postid, text);
                res.add(data);
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        
    }

//INSERT COMMENT INTO DATABASE--------------------------------------------------------------------------------------------
    private PreparedStatement mInsertComment;
    private static final String SQL_INSERT_COMMENT = "INSERT INTO comments_tbl (user_id, post_id, message) VALUES (?, ?, ?) RETURNING comment_id";

    private boolean init_mInsertComment() {
        try {
            mInsertComment = mConnection.prepareStatement(SQL_INSERT_COMMENT);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertComment");
            System.err.println("Using SQL: " + SQL_INSERT_COMMENT);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Insert a comment into the database
     *
     * @param userId The user id of the comment
     * @param postId The post id of the comment
     * @param message The content of the comment
     * @return The number of rows that were inserted. -1 indicates an error.
     */
    int insertComment(String userId, int postId, String message) {
        if (mInsertComment == null) // not yet initialized, do lazy init
            init_mInsertComment(); // lazy init
        int res = -1;
        try {
            System.out.println("Database operation: insertComment(String userId, int postId, String message)");
            mInsertComment.setString(1, userId);
            mInsertComment.setInt(2, postId);
            mInsertComment.setString(3, message);
            ResultSet rs = mInsertComment.executeQuery();
            if (rs.next()) {
                res = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;     
    }

//SELECT ALL USERS FROM DATABASE--------------------------------------------------------------------------------------------
    private PreparedStatement mSelectAllUsers;
    private static final String SQL_SELECT_ALL_USERS = "SELECT user_id, email, name" +
           " FROM users_tbl;";

    private boolean init_mSelectAllUsers() {
        try {
            mSelectAllUsers = mConnection.prepareStatement(SQL_SELECT_ALL_USERS);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAllUsers");
            System.err.println("Using SQL: " + SQL_SELECT_ALL_USERS);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Get all users from the database
     *
     * @return The data for all users in the database
     */
    ArrayList<RowDataUsers> selectAllUsers() {
        if (mSelectAllUsers == null)
            init_mSelectAllUsers();
        ArrayList<RowDataUsers> res = new ArrayList<RowDataUsers>();
        try {
            System.out.println("Database operation: selectAllUsers()");
            ResultSet rs = mSelectAllUsers.executeQuery();
            while (rs.next()) {
                String userid = rs.getString("user_id");
                String email = rs.getString("email");
                String name = rs.getString("name");
                RowDataUsers data = new RowDataUsers(userid, email, name);
                res.add(data);
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        
    }

//INSERT USER INTO DATABASE--------------------------------------------------------------------------------------------
    private PreparedStatement mInsertUser;
    private static final String SQL_INSERT_USER = "INSERT INTO users_tbl (user_id, name, email) VALUES (?, ?, ?)";

    private boolean init_mInsertUser() {
        try {
            mInsertUser = mConnection.prepareStatement(SQL_INSERT_USER);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertUser");
            System.err.println("Using SQL: " + SQL_INSERT_USER);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Insert a user into the database
     *
     * @param userId The user id of the user
     * @param email The email of the user
     * @param name The name of the user
     * @return The number of rows that were inserted. -1 indicates an error.
     */
    int insertUser(String userId, String name, String email) {
        if (mInsertUser == null) // not yet initialized, do lazy init
            init_mInsertUser(); // lazy init
        int res = -1;
        try {
            System.out.println("Database operation: insertUser(String userId, String name, String email)");
            mInsertUser.setString(1, userId);
            mInsertUser.setString(2, name);
            mInsertUser.setString(3, email);
            res = mInsertUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    //check if user email already exists in database
    public boolean emailExists(String email) {
        String query = "SELECT 1 FROM users_tbl WHERE email = ?";
        try (PreparedStatement stmt = mConnection.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

//GET USER BY GOOGLEID
    private PreparedStatement mSelectUserByGoogleId;
    private static final String SQL_SELECT_USER_BY_GOOGLEID = "SELECT * FROM users_tbl WHERE user_id = ?";

    private boolean init_mSelectUserByGoogleId() {
        try {
            mSelectUserByGoogleId = mConnection.prepareStatement(SQL_SELECT_USER_BY_GOOGLEID);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectUserByGoogleId");
            System.err.println("Using SQL: " + SQL_SELECT_USER_BY_GOOGLEID);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Get a user by their google id
     *
     * @param userId The google id of the user
     * @return The data for the requested user, or null if the ID was invalid
     */
    RowDataUsers selectUserByGoogleId(String userId) {
        if (mSelectUserByGoogleId == null) // not yet initialized, do lazy init
            init_mSelectUserByGoogleId(); // lazy init
        RowDataUsers data = null;
        try {
            System.out.println("Database operation: selectUserByGoogleId(String userId)");
            mSelectUserByGoogleId.setString(1, userId);
            ResultSet rs = mSelectUserByGoogleId.executeQuery();
            if (rs.next()) {
                String userid = rs.getString("user_id");
                String email = rs.getString("email");
                String name = rs.getString("name");
                data = new RowDataUsers(userid, email, name);
            }
            rs.close(); // remember to close the result set
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }



   /**
    * Disconnect from the database
    *
    * @return returns true if disconnect was successful, and false if not
    */
   boolean disconnect() {
       if (mConnection != null) {
           System.out.print("Disconnecting from database.");
           try {
               mConnection.close();
           } catch (SQLException e) {
               System.err.println("\n\tError: close() threw a SQLException");
               e.printStackTrace();
               mConnection = null; // set to null rather than leave broken
               return false;
           }

           System.out.println(" ... connection successfully closed");
           mConnection = null; // connection is gone, so null this out
           return true;
       }

       System.err.println("Unable to close connection: Connection was null");
       return false;
   }
}