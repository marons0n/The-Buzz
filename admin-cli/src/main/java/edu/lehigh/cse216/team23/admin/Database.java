package edu.lehigh.cse216.team23.admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Database class contains all logic for connecting to and interacting with the PostgreSQL database.
 */
public class Database {

    public static record RowData(int mId, String mSubject, String mMessage, int mVotes) {}
    public static record UserRowData(int uId, String uName, String uEmail, String uGender_identity,String uSexual_orientation) {}
    public static record CommentRowData(int commentId, int userId, int postId, String message) {}
    public static record VoteRowData(int voteId, int userId, int postId, int updown) {}



    private Connection mConnection;

    // SQL Queries for ideas table
    private static final String SQL_CREATE_TABLE_IDEA = 
            "CREATE TABLE IF NOT EXISTS ideas_tbl (" + 
            " id SERIAL PRIMARY KEY," + 
            " subject VARCHAR(50) NOT NULL," +
            " message VARCHAR(500) NOT NULL," +
            " votes INT DEFAULT 0 NOT NULL)"+
            "user_id INT NOT NULL," + 
            " FOREIGN KEY (user_id) REFERENCES users_tbl(user_id)," ;
    

            ;
    
    private static final String SQL_DROP_TABLE_IDEAS = "DROP TABLE IF EXISTS ideas_tbl";

    private static final String SQL_INSERT_ONE_IDEAS = 
            "INSERT INTO ideas_tbl (subject, message, votes) VALUES (?, ?, 0)";

    private static final String SQL_UPDATE_ONE_IDEAS = 
            "UPDATE ideas_tbl SET message = ? WHERE id = ?";

    private static final String SQL_DELETE_ONE = "DELETE FROM ideas_tbl WHERE id = ?";

    private static final String SQL_SELECT_ALL_IDEAS = "SELECT id, subject, message, votes FROM ideas_tbl";

    private static final String SQL_SELECT_ONE_IDEAS = 
            "SELECT * FROM ideas_tbl WHERE id = ?";

    // Prepared Statements
    private PreparedStatement mCreateTable;
    private PreparedStatement mDropTable;
    private PreparedStatement mInsertOne;
    private PreparedStatement mUpdateOne;
    private PreparedStatement mDeleteOne;
    private PreparedStatement mSelectAll;
    private PreparedStatement mSelectOne;

    /**
     * Creates the `ideas_tbl` table in the database if it doesn't exist.
     */
    void createTable() {
        if (mCreateTable == null) init_mCreateTable();
        try {
            System.out.println("Database operation: createTable()");
            mCreateTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean init_mCreateTable() {
        try {
            mCreateTable = mConnection.prepareStatement(SQL_CREATE_TABLE_IDEA);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mCreateTable");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    /**
     * Drops the `ideas_tbl` table from the database.
     */
    void dropTable() {
        if (mDropTable == null) init_mDropTable();
        try {
            System.out.println("Database operation: dropTable()");
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean init_mDropTable() {
        try {
            mDropTable = mConnection.prepareStatement(SQL_DROP_TABLE_IDEAS);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDropTable");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    /**
     * Inserts a row into the `ideas_tbl` table.
     */
    int insertRow(String subject, String message) {
        if (mInsertOne == null) init_mInsertOne();
        int count = 0;
        try {
            System.out.println("Database operation: insertRow()");
            mInsertOne.setString(1, subject);
            mInsertOne.setString(2, message);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private boolean init_mInsertOne() {
        try {
            mInsertOne = mConnection.prepareStatement(SQL_INSERT_ONE_IDEAS);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertOne");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    /**
     * Updates a row in the `ideas_tbl` table by ID.
     */
    int updateOne(int id, String message) {
        if (mUpdateOne == null) init_mUpdateOne();
        int res = -1;
        try {
            System.out.println("Database operation: updateOne()");
            mUpdateOne.setString(1, message);
            mUpdateOne.setInt(2, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private boolean init_mUpdateOne() {
        try {
            mUpdateOne = mConnection.prepareStatement(SQL_UPDATE_ONE_IDEAS);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateOne");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    /**
     * Deletes a row from the `ideas_tbl` table by ID.
     */
    int deleteRow(int id) {
        if (mDeleteOne == null) init_mDeleteOne();
        int res = -1;
        try {
            System.out.println("Database operation: deleteRow()");
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private boolean init_mDeleteOne() {
        try {
            mDeleteOne = mConnection.prepareStatement(SQL_DELETE_ONE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDeleteOne");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    /**
     * Selects all rows from the `ideas_tbl` table.
     */
    ArrayList<RowData> selectAll() {
        if (mSelectAll == null) init_mSelectAll();
        ArrayList<RowData> res = new ArrayList<>();
        try {
            System.out.println("Database operation: selectAll()");
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String subject = rs.getString("subject");
                String message = rs.getString("message");
                int votes = rs.getInt("votes");
                res.add(new RowData(id, subject, message, votes));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private boolean init_mSelectAll() {
        try {
            mSelectAll = mConnection.prepareStatement(SQL_SELECT_ALL_IDEAS);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAll");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    /**
     * Selects a single row from the `ideas_tbl` table by ID.
     */
    RowData selectOne(int id) {
        if (mSelectOne == null) init_mSelectOne();
        RowData data = null;
        try {
            System.out.println("Database operation: selectOne()");
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                String subject = rs.getString("subject");
                String message = rs.getString("message");
                int votes = rs.getInt("votes");
                data = new RowData(id, subject, message, votes);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private boolean init_mSelectOne() {
        try {
            mSelectOne = mConnection.prepareStatement(SQL_SELECT_ONE_IDEAS);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectOne");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }


    //Users table

    private static final String SQL_CREATE_TABLE_USER =
        "CREATE TABLE IF NOT EXISTS users_tbl (" +
        " user_id SERIAL PRIMARY KEY," +
        " name VARCHAR(50) NOT NULL," +
        " email VARCHAR(50) NOT NULL UNIQUE," +
        " gender_identity VARCHAR(50)," +
        " sexual_orientation VARCHAR(50))";

    private static final String SQL_INSERT_ONE_USER =
            "INSERT INTO users_tbl (name, email, gender_identity, sexual_orientation) VALUES (?, ?, ?, ?)";

    private static final String SQL_DROP_TABLE_USER = "DROP TABLE IF EXISTS users_tbl";

    private static final String SQL_UPDATE_ONE_USER = 
            "UPDATE users_tbl SET message = ? WHERE id = ?";

    private static final String SQL_SELECT_ALL_USER =
            "SELECT user_id, name, email, gender_identity, sexual_orientation FROM users_tbl";

    private static final String SQL_SELECT_ONE_USER =
            "SELECT * FROM users_tbl WHERE user_id = ?";

    private static final String SQL_DELETE_ONE_USER = "DELETE FROM users_tbl WHERE id = ?";


    // Prepared Statements
    private PreparedStatement mCreateUserTable;
    private PreparedStatement mDropUserTable;
    private PreparedStatement mInsertOneUser;
    private PreparedStatement mUpdateOneUser;
    private PreparedStatement mDeleteOneUser;
    private PreparedStatement mSelectAllUser;
    private PreparedStatement mSelectOneUser;

    /**
     * Creates the `ideas_tbl` table in the database if it doesn't exist.
     */
    void createUserTable() {
        if (mCreateUserTable == null) init_mCreateUserTable();
        try {
            System.out.println("Database operation: createUserTable()");
            mCreateUserTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean init_mCreateUserTable() {
        try {
            mCreateUserTable = mConnection.prepareStatement(SQL_CREATE_TABLE_USER);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mCreateTable");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    /**
     * Drops the `ideas_tbl` table from the database.
     */
    void dropUserTable() {
        if (mDropUserTable == null) init_mDropUserTable();
        try {
            System.out.println("Database operation: dropUserTable()");
            mDropUserTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean init_mDropUserTable() {
        try {
            mDropUserTable = mConnection.prepareStatement(SQL_DROP_TABLE_USER);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDropUserTable");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    /**
     * Inserts a row into the `user_tbl` table.
     */
    int insertUserRow(String name, String  email, String gender_identity, String orientation) {
        if (mInsertOneUser == null) init_mInsertOneUser();
        int count = 0;
        try {
            System.out.println("Database operation: insertUserRow()");
            mInsertOneUser.setString(1, name);
            mInsertOneUser.setString(2, email);
            mInsertOneUser.setString(3, gender_identity);
            mInsertOneUser.setString(4, orientation);
            count += mInsertOneUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private boolean init_mInsertOneUser() {
        try {
            mInsertOneUser = mConnection.prepareStatement(SQL_INSERT_ONE_USER);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertOneUser");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    /**
     * Updates a row in the `ideas_tbl` table by ID.
     */
    int updateOneUser(int id, String name, String email) {
        if (mUpdateOneUser == null) init_mUpdateOneUser();
        int res = -1;
        try {
            System.out.println("Database operation: updateOneUser()");
            mUpdateOneUser.setString(1, name);
            mUpdateOneUser.setString(2, email);
            mUpdateOneUser.setInt(3, id);
            res = mUpdateOneUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }  
        return res;
    }

    private boolean init_mUpdateOneUser() {
        try {
            mUpdateOneUser = mConnection.prepareStatement(SQL_UPDATE_ONE_USER);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateOneUser");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    /**
     * Deletes a row from the `ideas_tbl` table by ID.
     */
    int deleteUserRow(int id) {
        if (mDeleteOneUser == null) init_mDeleteOneUser();
        int res = -1;
        try {
            System.out.println("Database operation: deleteUserRow()");
            mDeleteOneUser.setInt(1, id);
            res = mDeleteOneUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private boolean init_mDeleteOneUser() {
        try {
            mDeleteOneUser = mConnection.prepareStatement(SQL_DELETE_ONE_USER);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDeleteOneUser");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    /**
     * Selects all rows from the `ideas_tbl` table.
     */
    ArrayList<UserRowData> selectAllUser() {
        if (mSelectAllUser == null) init_mSelectAllUser();
        ArrayList<UserRowData> res = new ArrayList<>();
        try {
            System.out.println("Database operation: selectAllUser()");
            ResultSet rs = mSelectAllUser.executeQuery();
            while (rs.next()) {
                int userId = rs.getInt("uId");
                String name = rs.getString("uName");
                String email = rs.getString("uEmail");
                String gender = rs.getString("uGender_identity");
                String orientation = rs.getString("uSexual_orientation");
                res.add(new UserRowData(userId, name, email, gender, orientation));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private boolean init_mSelectAllUser() {
        try {
            mSelectAllUser = mConnection.prepareStatement(SQL_SELECT_ALL_USER);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAllUser");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    /**
     * Selects a single row from the `ideas_tbl` table by ID.
     */
    UserRowData selectOneUser(int id) {
        if (mSelectOneUser == null) init_mSelectOneUser();
        UserRowData data = null;
        try {
            System.out.println("Database operation: selectOneUser()");
            mSelectOneUser.setInt(1, id);
            ResultSet rs = mSelectOneUser.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String gender = rs.getString("gender_identity");
                String orientation = rs.getString("sexual_orientation");
                data = new UserRowData(id, name, email, gender, orientation);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private boolean init_mSelectOneUser() {
        try {
            mSelectOneUser = mConnection.prepareStatement(SQL_SELECT_ONE_USER);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectOneUser");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }


    //Comments

    // SQL Queries for Comment table
    private static final String SQL_CREATE_TABLE_COMMENT = 
    "CREATE TABLE IF NOT EXISTS comments_tbl (" + 
    " comment_id SERIAL PRIMARY KEY," + 
    " user_id INT NOT NULL," + 
    " post_id INT NOT NULL," + 
    " message TEXT NOT NULL," +
    " FOREIGN KEY (user_id) REFERENCES users_tbl(user_id)," + 
    " FOREIGN KEY (post_id) REFERENCES ideas_tbl(id))";

    private static final String SQL_DROP_TABLE_COMMENTS = "DROP TABLE IF EXISTS comments_tbl";

    private static final String SQL_INSERT_ONE_COMMENT = 
    "INSERT INTO comments_tbl (user_id, post_id, message) VALUES (?, ?, ?)";

    private static final String SQL_UPDATE_ONE_COMMENT = 
    "UPDATE comments_tbl SET message = ? WHERE comment_id = ?";

    private static final String SQL_DELETE_ONE_COMMENT = "DELETE FROM comments_tbl WHERE comment_id = ?";

    private static final String SQL_SELECT_ALL_COMMENTS = 
    "SELECT comment_id, user_id, post_id, message FROM comments_tbl";

    private static final String SQL_SELECT_ONE_COMMENT = 
    "SELECT * FROM comments_tbl WHERE comment_id = ?";

    // Prepared Statements for Comment Table
    private PreparedStatement mCreateCommentTable;
    private PreparedStatement mDropCommentTable;
    private PreparedStatement mInsertOneComment;
    private PreparedStatement mUpdateOneComment;
    private PreparedStatement mDeleteOneComment;
    private PreparedStatement mSelectAllComments;
    private PreparedStatement mSelectOneComment;

    // Creates the `comments_tbl` table in the database if it doesn't exist
    void createCommentTable() {
        if (mCreateCommentTable == null) init_mCreateCommentTable();
        try {
            System.out.println("Database operation: createCommentTable()");
            mCreateCommentTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean init_mCreateCommentTable() {
        try {
            mCreateCommentTable = mConnection.prepareStatement(SQL_CREATE_TABLE_COMMENT);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mCreateCommentTable");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    // Drops the `comments_tbl` table from the database
    void dropCommentTable() {
        if (mDropCommentTable == null) init_mDropCommentTable();
        try {
            System.out.println("Database operation: dropCommentTable()");
            mDropCommentTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean init_mDropCommentTable() {
        try {
            mDropCommentTable = mConnection.prepareStatement(SQL_DROP_TABLE_COMMENTS);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDropCommentTable");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    // Inserts a new comment into the `comments_tbl` table
    int insertComment(int userId, int postId, String message) {
        if (mInsertOneComment == null) init_mInsertOneComment();
        int count = 0;
        try {
            System.out.println("Database operation: insertComment()");
            mInsertOneComment.setInt(1, userId);
            mInsertOneComment.setInt(2, postId);
            mInsertOneComment.setString(3, message);
            count += mInsertOneComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private boolean init_mInsertOneComment() {
        try {
            mInsertOneComment = mConnection.prepareStatement(SQL_INSERT_ONE_COMMENT);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertOneComment");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    // Updates a comment in the `comments_tbl` table by comment ID
    int updateComment(int commentId, String message) {
        if (mUpdateOneComment == null) init_mUpdateOneComment();
        int res = -1;
        try {
            System.out.println("Database operation: updateComment()");
            mUpdateOneComment.setString(1, message);
            mUpdateOneComment.setInt(2, commentId);
            res = mUpdateOneComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private boolean init_mUpdateOneComment() {
        try {
            mUpdateOneComment = mConnection.prepareStatement(SQL_UPDATE_ONE_COMMENT);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateOneComment");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    // Deletes a comment from the `comments_tbl` table by comment ID
    int deleteComment(int commentId) {
        if (mDeleteOneComment == null) init_mDeleteOneComment();
        int res = -1;
        try {
            System.out.println("Database operation: deleteComment()");
            mDeleteOneComment.setInt(1, commentId);
            res = mDeleteOneComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private boolean init_mDeleteOneComment() {
        try {
            mDeleteOneComment = mConnection.prepareStatement(SQL_DELETE_ONE_COMMENT);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDeleteOneComment");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    // Selects all comments from the `comments_tbl` table
    ArrayList<CommentRowData> selectAllComments() {
        if (mSelectAllComments == null) init_mSelectAllComments();
        ArrayList<CommentRowData> res = new ArrayList<>();
        try {
            System.out.println("Database operation: selectAllComments()");
            ResultSet rs = mSelectAllComments.executeQuery();
            while (rs.next()) {
                int commentId = rs.getInt("comment_id");
                int userId = rs.getInt("user_id");
                int postId = rs.getInt("post_id");
                String message = rs.getString("message");
                res.add(new CommentRowData(commentId, userId, postId, message));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private boolean init_mSelectAllComments() {
        try {
            mSelectAllComments = mConnection.prepareStatement(SQL_SELECT_ALL_COMMENTS);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAllComments");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    // Selects a single comment from the `comments_tbl` table by comment ID
    CommentRowData selectOneComment(int commentId) {
        if (mSelectOneComment == null) init_mSelectOneComment();
        CommentRowData data = null;
        try {
            System.out.println("Database operation: selectOneComment()");
            mSelectOneComment.setInt(1, commentId);
            ResultSet rs = mSelectOneComment.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                int postId = rs.getInt("post_id");
                String message = rs.getString("message");
                data = new CommentRowData(commentId, userId, postId, message);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private boolean init_mSelectOneComment() {
        try {
            mSelectOneComment = mConnection.prepareStatement(SQL_SELECT_ONE_COMMENT);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectOneComment");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }




    //vote queries

    // SQL Queries for Vote table
    private static final String SQL_CREATE_TABLE_VOTE = 
    "CREATE TABLE IF NOT EXISTS votes_tbl (" + 
    " vote_id SERIAL PRIMARY KEY," + 
    " user_id INT NOT NULL," + 
    " post_id INT NOT NULL," + 
    " updown INT NOT NULL CHECK (updown = 1 OR updown = -1), " + // Ensures value is either 1 or -1
    " FOREIGN KEY (user_id) REFERENCES users_tbl(user_id)," + 
    " FOREIGN KEY (post_id) REFERENCES ideas_tbl(id))";

    private static final String SQL_DROP_TABLE_VOTES = "DROP TABLE IF EXISTS votes_tbl";

    private static final String SQL_INSERT_ONE_VOTE = 
    "INSERT INTO votes_tbl (user_id, post_id, updown) VALUES (?, ?, ?)";

    private static final String SQL_UPDATE_ONE_VOTE = 
    "UPDATE votes_tbl SET updown = ? WHERE vote_id = ?";

    private static final String SQL_DELETE_ONE_VOTE = "DELETE FROM votes_tbl WHERE vote_id = ?";

    private static final String SQL_SELECT_ALL_VOTES = 
    "SELECT vote_id, user_id, post_id, updown FROM votes_tbl";

    private static final String SQL_SELECT_ONE_VOTE = 
    "SELECT * FROM votes_tbl WHERE vote_id = ?";

    // Prepared Statements for Vote Table
    private PreparedStatement mCreateVoteTable;
    private PreparedStatement mDropVoteTable;
    private PreparedStatement mInsertOneVote;
    private PreparedStatement mUpdateOneVote;
    private PreparedStatement mDeleteOneVote;
    private PreparedStatement mSelectAllVotes;
    private PreparedStatement mSelectOneVote;

    // Creates the `votes_tbl` table in the database if it doesn't exist
    void createVoteTable() {
        if (mCreateVoteTable == null) init_mCreateVoteTable();
        try {
            System.out.println("Database operation: createVoteTable()");
            mCreateVoteTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean init_mCreateVoteTable() {
        try {
            mCreateVoteTable = mConnection.prepareStatement(SQL_CREATE_TABLE_VOTE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mCreateVoteTable");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    // Drops the `votes_tbl` table from the database
    void dropVoteTable() {
        if (mDropVoteTable == null) init_mDropVoteTable();
        try {
            System.out.println("Database operation: dropVoteTable()");
            mDropVoteTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean init_mDropVoteTable() {
        try {
            mDropVoteTable = mConnection.prepareStatement(SQL_DROP_TABLE_VOTES);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDropVoteTable");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    // Inserts a new vote into the `votes_tbl` table
    int insertVote(int userId, int postId, int updown) {
        if (mInsertOneVote == null) init_mInsertOneVote();
        int count = 0;
        try {
            System.out.println("Database operation: insertVote()");
            mInsertOneVote.setInt(1, userId);
            mInsertOneVote.setInt(2, postId);
            mInsertOneVote.setInt(3, updown);
            count += mInsertOneVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private boolean init_mInsertOneVote() {
        try {
            mInsertOneVote = mConnection.prepareStatement(SQL_INSERT_ONE_VOTE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertOneVote");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    // Updates a vote in the `votes_tbl` table by vote ID
    int updateVote(int voteId, int updown) {
        if (mUpdateOneVote == null) init_mUpdateOneVote();
        int res = -1;
        try {
            System.out.println("Database operation: updateVote()");
            mUpdateOneVote.setInt(1, updown);
            mUpdateOneVote.setInt(2, voteId);
            res = mUpdateOneVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private boolean init_mUpdateOneVote() {
        try {
            mUpdateOneVote = mConnection.prepareStatement(SQL_UPDATE_ONE_VOTE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateOneVote");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    // Deletes a vote from the `votes_tbl` table by vote ID
    int deleteVote(int voteId) {
        if (mDeleteOneVote == null) init_mDeleteOneVote();
        int res = -1;
        try {
            System.out.println("Database operation: deleteVote()");
            mDeleteOneVote.setInt(1, voteId);
            res = mDeleteOneVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private boolean init_mDeleteOneVote() {
        try {
            mDeleteOneVote = mConnection.prepareStatement(SQL_DELETE_ONE_VOTE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDeleteOneVote");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    // Selects all votes from the `votes_tbl` table
    ArrayList<VoteRowData> selectAllVotes() {
        if (mSelectAllVotes == null) init_mSelectAllVotes();
        ArrayList<VoteRowData> res = new ArrayList<>();
        try {
            System.out.println("Database operation: selectAllVotes()");
            ResultSet rs = mSelectAllVotes.executeQuery();
            while (rs.next()) {
                int voteId = rs.getInt("vote_id");
                int userId = rs.getInt("user_id");
                int postId = rs.getInt("post_id");
                int updown = rs.getInt("updown");
                res.add(new VoteRowData(voteId, userId, postId, updown));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private boolean init_mSelectAllVotes() {
        try {
            mSelectAllVotes = mConnection.prepareStatement(SQL_SELECT_ALL_VOTES);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAllVotes");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }

    // Selects a single vote from the `votes_tbl` table by vote ID
    VoteRowData selectOneVote(int voteId) {
        if (mSelectOneVote == null) init_mSelectOneVote();
        VoteRowData data = null;
        try {
            System.out.println("Database operation: selectOneVote()");
            mSelectOneVote.setInt(1, voteId);
            ResultSet rs = mSelectOneVote.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                int postId = rs.getInt("post_id");
                int updown = rs.getInt("updown");
                data = new VoteRowData(voteId, userId, postId, updown);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private boolean init_mSelectOneVote() {
        try {
            mSelectOneVote = mConnection.prepareStatement(SQL_SELECT_ONE_VOTE);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectOneVote");
            e.printStackTrace();
            disconnect();
            return false;
        }
        return true;
    }







    // Connection and Utility Functions

    boolean disconnect() {
        if (mConnection != null) {
            try {
                mConnection.close();
            } catch (SQLException e) {
                System.err.println("Error: close() threw a SQLException");
                e.printStackTrace();
                mConnection = null;
                return false;
            }
            mConnection = null;
            return true;
        }
        return false;
    }

    // private Database() {}

    static Database getDatabase(String dbUri) {
        if (dbUri != null && dbUri.length() > 0) {
            Database db = new Database();
            try {
                db.mConnection = DriverManager.getConnection(dbUri);
                if (db.mConnection == null) return null;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
            return db;
        }
        return null;
    }

    static Database getDatabase() {
        Map<String, String> env = System.getenv();
        String dbUri = env.get("DATABASE_URI");

        if (dbUri != null && dbUri.length() > 0) {
            return Database.getDatabase(dbUri);
        }
        return null;
    }
}
