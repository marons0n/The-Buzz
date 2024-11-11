package edu.lehigh.cse216.team23.backend;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


import java.util.Map;
import java.util.ArrayList;
import java.util.List;


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
    * */
   public static record RowDataIdeas(int mId, int mVotes, String mMessage) {
   }

   public static record RowDataComments(int mCommentId, int mUserId, int mPostId, String mText) {
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


   private PreparedStatement mCreateTable;
   private static final String SQL_CREATE_TABLE = "CREATE TABLE ideas_tbl (" +
           " id SERIAL PRIMARY KEY," +
           " votes INT NOT NULL," +
           " message TEXT NOT NULL)";


   /**
    * safely performs mCreateTable
    * mConnection.prepareStatement(SQL_CREATE_TABLE);
    */
   private boolean init_mCreateTable() {
       try {
           mCreateTable = mConnection.prepareStatement(SQL_CREATE_TABLE);
       } catch (SQLException e) {
           System.err.println("Error creating prepared statemetn: mCreateTable");
           System.err.println("Using SQL: " + SQL_CREATE_TABLE);
           e.printStackTrace();
           return false;
       }


       return true;
   }


   /**
    * Create ideas_tbl. If it already exists, this will print an error
    */
   void createTable() {
       if (mCreateTable == null)
           init_mCreateTable(); // lazy init
       try {
           System.out.println("Database operation: createTable()");
           mCreateTable.execute();
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }


   private PreparedStatement mDropTable;
   private static final String SQL_DROP_TABLE_IDEAS_TBL = "DROP TABLE ideas_tbl";


   private boolean init_mDropTable() {
       // return true on success, false otherwise
       try {
           mDropTable = mConnection.prepareStatement(SQL_DROP_TABLE_IDEAS_TBL);
       } catch (SQLException e) {
           System.err.println("Error creating prepared statement: mDropTable");
           System.err.println("Using SQL: " + SQL_DROP_TABLE_IDEAS_TBL);
           e.printStackTrace();
           return false;
       }
       return true;
   }


   /**
    * Remove ideas_tbl table from the database. If it does not exist, this will
    * print
    * an error.
    */
   void dropTable() {
       if (mDropTable == null) // not yet initialized, do lazy init
           init_mDropTable(); // lazy init
       try {
           System.out.println("Database operation: dropTable()");
           mDropTable.execute();
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }

//insert row to ideas table
   private PreparedStatement mInsertOne;
   /** the SQL for mInsertOne */
   private static final String SQL_INSERT_ONE_IDEAS_TBL = "INSERT INTO ideas_tbl (votes, message) VALUES (?, ?)";


   /**
    * safely performs mInsertOne = mConnection.prepareStatement("INSERT INTO
    * ideas_tbl VALUES (default, ?, ?)");
    */
   private boolean init_mInsertOne() {
       // return true on success, false otherwise
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


//insert comment
   private PreparedStatement mInsertComment;
   /** the SQL for mInsertOne */
   private static final String SQL_INSERT_ONE_COMMENT = "INSERT INTO comment (userid, postid, text) VALUES (?, ?, ?) RETURNING commentid";

    /**
    * safely performs mInsertOne = mConnection.prepareStatement("INSERT INTO
    * comment VALUES (default, ?, ?)");
    */
    private boolean init_mInsertComment() {
        // return true on success, false otherwise
        try {
            mInsertOne = mConnection.prepareStatement(SQL_INSERT_ONE_COMMENT);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertOne");
            System.err.println("Using SQL: " + SQL_INSERT_ONE_COMMENT);
            e.printStackTrace();
            this.disconnect(); // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

   /**
    * Insert row into comment table in the database
    *
    * @param userId The user id of the comment
    * @param postId The post id of the comment
    * @param text The text of the comment
    * @return The number of rows that were inserted
    */
    int insertComment(int userId, int postId, String text) {
        if (mInsertComment == null) // not yet initialized, do lazy init
            init_mInsertComment(); // lazy init
        int count = 0;
        try {
            System.out.println("Database operation: insertComment(String, int)");
            mInsertComment.setInt(1, userId);
            mInsertComment.setInt(2, postId);
            mInsertComment.setString(3, text);
            count += mInsertComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }




   private PreparedStatement mUpdateOne;
   /** the SQL for mUpdateOne */
   private static final String SQL_UPDATE_ONE_IDEAS_TBL = "UPDATE ideas_tbl" +
           " SET message = ?" +
           " WHERE id = ?";


   private PreparedStatement mUpdateOne_2arg;
   /** the SQL for mUpdateOne */
   private static final String SQL_UPDATE_ONE_IDEAS_TBL_2ARG = "UPDATE ideas_tbl" +
           " SET votes = ?, message = ?" +
           " WHERE id = ?";


   /**
    * safely performs mUpdateOne = mConnection.prepareStatement("UPDATE ideas_tbl
    * SET message = ? WHERE id = ?");
    */
   private boolean init_mUpdateOne() {
       // return true on success, false otherwise
       try {
           mUpdateOne = mConnection.prepareStatement(SQL_UPDATE_ONE_IDEAS_TBL);
       } catch (SQLException e) {
           System.err.println("Error creating prepared statement: mUpdateOne");
           System.err.println("Using SQL: " + SQL_UPDATE_ONE_IDEAS_TBL);
           e.printStackTrace();
           this.disconnect(); // @TODO is disconnecting on exception what we want?
           return false;
       }
       return true;
   }


   /**
    * safely performs mUpdateOne = mConnection.prepareStatement("UPDATE ideas SET
    * message = ? WHERE id = ?");
    */
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
          
           // if (votes == 1) {
           //     mUpdateOne_2arg.setInt(1, (selectOne(id).mVotes + 1)); // adds 1 vote
           // } else if (votes == -1 && selectOne(id).mVotes > 0) {
           //     mUpdateOne_2arg.setInt(1, (selectOne(id).mVotes - 1)); // removes 1 vote
           // } // if anything else than 1 or -1 is passed nothing is going to happen


           mUpdateOne_2arg.setString(2, selectOne(id).mMessage); // just keeps the message the same because we only
                                                                 // wanna update votes
           mUpdateOne_2arg.setInt(3, id);
           res = mUpdateOne_2arg.executeUpdate();
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return res;
   }


   public int upvoteRow(int id) {
       RowDataIdeas data = selectOne(id);
       System.out.println("id: " + id);
       System.out.println("data: " + data);
       if (data == null) {
           return -1;
       }
       System.out.println("votes: " + data.mVotes);
       int newVotes = data.mVotes + 1;
       return updateOne(id, newVotes);
   }


   public int downvoteRow(int id) {
       RowDataIdeas data = selectOne(id);
       if (data == null) {
           return -1;
       }
       int newVotes = data.mVotes - 1;
       return updateOne(id, newVotes);
   }


   private PreparedStatement mDeleteOne;
   /** the SQL for mDeleteOne */
   private static final String SQL_DELETE_ONE = "DELETE FROM ideas_tbl" +
           " WHERE id = ?";


   /**
    * safely performs mDeleteOne = mConnection.prepareStatement(SQL_DELETE_ONE);
    */
   private boolean init_mDeleteOne() {
       // return true on success, false otherwise
       try {
           mDeleteOne = mConnection.prepareStatement(SQL_DELETE_ONE);
       } catch (SQLException e) {
           System.err.println("Error creating prepared statement: mDeleteOne");
           System.err.println("Using SQL: " + SQL_DELETE_ONE);
           e.printStackTrace();
           this.disconnect(); // @TODO is disconnecting on exception what we want?
           return false;
       }
       return true;
   }


   /**
    * Delete a row by ID
    *
    * @param id The id of the row to delete
    * @return The number of rows that were deleted. -1 indicates an error.
    */
   int deleteRow(int id) {
       if (mDeleteOne == null) // not yet initialized, do lazy init
           init_mDeleteOne(); // lazy init
       int res = -1;
       try {
           System.out.println("Database operation: deleteRow(int)");
           mDeleteOne.setInt(1, id);
           res = mDeleteOne.executeUpdate();
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return res;
   }


//get all ideas
   private PreparedStatement mSelectAll;
   private static final String SQL_SELECT_ALL_IDEAS_TBL = "SELECT id, votes, message" +
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
               RowDataIdeas data = new RowDataIdeas(id, votes, message);
               res.add(data);
           }
           rs.close();
           return res;
       } catch (SQLException e) {
           e.printStackTrace();
           return null;
       }
   }

//get comments from id
    private PreparedStatement mSelectAllComments;
    private static final String SQL_SELECT_ALL_COMMENTS = "SELECT commentid, userid, postid, text" +
           " FROM comment WHERE postid = ?;";

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

    ArrayList<RowDataComments> selectAllComments(int id) {
        if (mSelectAllComments == null)
            init_mSelectAllComments();
        ArrayList<RowDataComments> res = new ArrayList<RowDataComments>();
        try {
            System.out.println("Database operation: selectAllComments()");
            mSelectAllComments.setInt(1, id);
            ResultSet rs = mSelectAllComments.executeQuery();
            while (rs.next()) {
                int commentid = rs.getInt("commentid");
                int userid = rs.getInt("userid");
                int postid = rs.getInt("postid");
                String text = rs.getString("text");
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




//get one idea
   private PreparedStatement mSelectOne;
   /** the SQL for mSelectOne */
   private static final String SQL_SELECT_ONE_IDEAS_TBL = "SELECT *" +
           " FROM ideas_tbl" +
           " WHERE id=? ;";


   /**
    * safely performs mSelectOne = mConnection.prepareStatement("SELECT * from
    * ideas_tbl WHERE id=?");
    */
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
               data = new RowDataIdeas(id, votes, message);
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

   public int insertUser(String userId, String email, String name) {
        //check if user id already exists in database
        try {
            PreparedStatement stmt = mConnection.prepareStatement("SELECT * FROM \"user\" WHERE userid = ?");
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return 0; // Return 0 if the user already exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = "INSERT INTO \"user\" (userid, email, name) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = mConnection.prepareStatement(query)) {
            stmt.setString(1, userId);
            stmt.setString(2, email);
            stmt.setString(3, name);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return 1; // Return 1 if the insertion is successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if the insertion fails
        }


    public int insertRow(String userId, String message) {
        String query = "INSERT INTO ideas_tbl (userid, message) VALUES (?, ?) RETURNING id";
        try (PreparedStatement stmt = mConnection.prepareStatement(query)) {
            stmt.setString(1, userId);
            stmt.setString(2, message);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1); // Return the new row ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if the insertion fails
    }

    public int insertVote(String userId, int postId, String type) {
        String insertVoteQuery = "INSERT INTO vote (userId, postId, votetype) VALUES (?, ?, ?) RETURNING voteid";
        String updateVotesQuery = "UPDATE ideas_tbl SET votes = votes + ? WHERE id = ?";
        int voteChange = type.equals("upvote") ? 1 : -1;

        try {
            mConnection.setAutoCommit(false); // Start transaction

            try (PreparedStatement insertVoteStmt = mConnection.prepareStatement(insertVoteQuery);
                 PreparedStatement updateVotesStmt = mConnection.prepareStatement(updateVotesQuery)) {

                // Insert the vote
                insertVoteStmt.setString(1, userId);
                insertVoteStmt.setInt(2, postId);
                insertVoteStmt.setString(3, type);
                ResultSet rs = insertVoteStmt.executeQuery();
                if (rs.next()) {
                    int voteId = rs.getInt(1);

                    // Update the votes count in the ideas table
                    updateVotesStmt.setInt(1, voteChange);
                    updateVotesStmt.setInt(2, postId);
                    updateVotesStmt.executeUpdate();

                    mConnection.commit(); // Commit transaction
                    return voteId; // Return the new vote ID
                }
            } catch (SQLException e) {
                mConnection.rollback(); // Rollback transaction on error
                e.printStackTrace();
            } finally {
                mConnection.setAutoCommit(true); // Restore auto-commit mode
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if the insertion fails
    }

    public int insertComment(String userId, int postId, String text) {
        String insertCommentQuery = "INSERT INTO comment (userId, postId, text) VALUES (?, ?, ?) RETURNING commentid";

        try {
            mConnection.setAutoCommit(false); // Start transaction

            try (PreparedStatement insertCommentStmt = mConnection.prepareStatement(insertCommentQuery)) {

                // Insert the comment
                insertCommentStmt.setString(1, userId);
                insertCommentStmt.setInt(2, postId);
                insertCommentStmt.setString(3, text);
                ResultSet rs = insertCommentStmt.executeQuery();
                if (rs.next()) {
                    int commentId = rs.getInt(1);

                    mConnection.commit(); // Commit transaction
                    return commentId; // Return the new comment ID
                }
            } catch (SQLException e) {
                mConnection.rollback(); // Rollback transaction on error
                e.printStackTrace();
            } finally {
                mConnection.setAutoCommit(true); // Restore auto-commit mode
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if the insertion fails
    }
}


