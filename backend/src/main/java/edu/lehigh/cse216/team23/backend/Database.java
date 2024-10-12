package edu.lehigh.cse216.team23.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Map;
import java.util.ArrayList;

public class Database {
    public static record RowData (int mId, int mLikes, String mMessage) {}

    private Connection mConnection;

    /**
     * Uses the presence of environment variables to invoke the correct 
     * overloaded `getDatabase` method
     * @return a valid Database object with active connection on success, null otherwise
     */
    static Database getDatabase(){
        // get the Postgres configuration from environment variables; 
        // you could name them almost anything
        Map<String, String> env = System.getenv();
        String ip     = env.get("POSTGRES_IP");
        String port   = env.get("POSTGRES_PORT");
        String user   = env.get("POSTGRES_USER");
        String pass   = env.get("POSTGRES_PASS");
        String dbname = env.get("POSTGRES_DBNAME");
        String dbUri  = env.get("DATABASE_URI");

        System.out.printf("Using the following environment variables:%n%s%n", "-".repeat(45)); 
        System.out.printf("POSTGRES_IP=%s%n", ip);
        System.out.printf("POSTGRES_PORT=%s%n", port);
        System.out.printf("POSTGRES_USER=%s%n", user);
        System.out.printf("POSTGRES_PASS=%s%n", "omitted");
        System.out.printf("POSTGRES_DBNAME=%s%n", dbname );
        System.out.printf("DATABASE_URI=%s%n", dbUri );

        if( dbUri != null && dbUri.length() > 0 ){
            return Database.getDatabase(dbUri);
        }else if( ip != null && port != null && dbname != null && user != null && pass != null ){
            return Database.getDatabase(ip, port, dbname, user, pass);
        }
        //else insufficient information to connect
        System.err.println( "Insufficient information to connect to database." );
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
                db.mConnection = DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + port + "/" + dbname, user, pass);
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
    private Database() {

    }

    private PreparedStatement mCreateTable;
    private static final String SQL_CREATE_TABLE= 
            "CREATE TABLE ideas_tbl (" +
            " id SERIAL PRIMARY KEY," +
            " likes INT NOT NULL," +
            " message VARCHAR(500) NOT NULL)";

    /** safely performs mCreateTable = mConnection.prepareStatement(SQL_CREATE_TABLE); */
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
     * Create ideas_tbl.  If it already exists, this will print an error
     */
    void createTable() {
        if(mCreateTable == null)
            init_mCreateTable(); //lazy init
        try {
            System.out.println("Database operation: createTable()");
            mCreateTable.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement mDropTable;
    private static final String SQL_DROP_TABLE_IDEAS_TBL = "DROP TABLE ideas_tbl";

    private boolean init_mDropTable() {
        // return true on success, false otherwise
        try {
            mDropTable = mConnection.prepareStatement( SQL_DROP_TABLE_IDEAS_TBL );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDropTable");
            System.err.println("Using SQL: " + SQL_DROP_TABLE_IDEAS_TBL);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Remove ideas_tbl table from the database.  If it does not exist, this will print
     * an error.
     */
    void dropTable() {
        if( mDropTable == null )  // not yet initialized, do lazy init
            init_mDropTable();    // lazy init
        try {
            System.out.println( "Database operation: dropTable()" );
            mDropTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement mInsertOne;
    /** the SQL for mInsertOne */
    private static final String SQL_INSERT_ONE_IDEAS_TBL = 
            "INSERT INTO ideas_tbl" + 
            " VALUES (default, ?, ?);";

    /** safely performs mInsertOne = mConnection.prepareStatement("INSERT INTO ideas_tbl VALUES (default, ?, ?)"); */
    private boolean init_mInsertOne(){
        // return true on success, false otherwise
        try {
            mInsertOne = mConnection.prepareStatement( SQL_INSERT_ONE_IDEAS_TBL );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mInsertOne");
            System.err.println("Using SQL: " + SQL_INSERT_ONE_IDEAS_TBL);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Insert a row into the database
     * @param likes The lieks for this new row
     * @param message The message body for this new row
     * @return The number of rows that were inserted
     */
    int insertRow(int likes, String message) {
        if( mInsertOne == null )  // not yet initialized, do lazy init
            init_mInsertOne();    // lazy init
        int count = 0;
        try {
            System.out.println( "Database operation: insertRow(String, String)" );
            mInsertOne.setInt(1, likes);
            mInsertOne.setString(2, message);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private PreparedStatement mUpdateOne;
    /** the SQL for mUpdateOne */
    private static final String SQL_UPDATE_ONE_IDEAS_TBL = 
            "UPDATE ideas_tbl" + 
            " SET message = ?" +
            " WHERE id = ?";

    private PreparedStatement mUpdateOne_2arg;
    /** the SQL for mUpdateOne */
    private static final String SQL_UPDATE_ONE_IDEAS_TBL_2ARG = 
            "UPDATE ideas_tbl" + 
            " SET likes = ?, message = ?" + 
            " WHERE id = ?";

    /** safely performs mUpdateOne = mConnection.prepareStatement("UPDATE ideas_tbl SET message = ? WHERE id = ?"); */
    private boolean init_mUpdateOne(){
        // return true on success, false otherwise
        try {
            mUpdateOne = mConnection.prepareStatement( SQL_UPDATE_ONE_IDEAS_TBL );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateOne");
            System.err.println("Using SQL: " + SQL_UPDATE_ONE_IDEAS_TBL);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

     /** safely performs mUpdateOne = mConnection.prepareStatement("UPDATE ideas SET message = ? WHERE id = ?"); */
     private boolean init_mUpdateOne_2arg(){
        // return true on success, false otherwise
        try {
            mUpdateOne_2arg = mConnection.prepareStatement( SQL_UPDATE_ONE_IDEAS_TBL_2ARG );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mUpdateOne");
            System.err.println("Using SQL: " + SQL_UPDATE_ONE_IDEAS_TBL_2ARG);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Update the likes and message for a row in the database
     * @param id The id of the row to update
     * @param likes The new message likes
     * @param message The new message contents
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateOne(int id, int likes/*, String message*/) {
        if( mUpdateOne_2arg == null )  // not yet initialized, do lazy init
            init_mUpdateOne_2arg();    // lazy init
        int res = -1;
        try {
            System.out.println( "Database operation: updateOne(int id, String likes, String message)" );
            if(likes == 1){
                mUpdateOne_2arg.setInt(1, (selectOne(id).mLikes + 1)); //adds 1 like
            }else if(likes == -1){
                mUpdateOne_2arg.setInt(1, (selectOne(id).mLikes + 1)); //removes 1 like
            }//if anything else than 1 or -1 is passed nothing is going to happen
            
            mUpdateOne_2arg.setString(2, selectOne(id).mMessage); //just keeps the message the same because we only wanna update likes
            mUpdateOne_2arg.setInt(3, id);
            res = mUpdateOne_2arg.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private PreparedStatement mDeleteOne;
    /** the SQL for mDeleteOne */
    private static final String SQL_DELETE_ONE = 
            "DELETE FROM ideas_tbl" + 
            " WHERE id = ?";

    /** safely performs mDeleteOne = mConnection.prepareStatement(SQL_DELETE_ONE); */
    private boolean init_mDeleteOne(){
        // return true on success, false otherwise
        try {
            mDeleteOne = mConnection.prepareStatement( SQL_DELETE_ONE );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mDeleteOne");
            System.err.println("Using SQL: " + SQL_DELETE_ONE);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Delete a row by ID
     * @param id The id of the row to delete
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRow(int id) {
        if( mDeleteOne == null )  // not yet initialized, do lazy init
            init_mDeleteOne();    // lazy init
        int res = -1;
        try {
            System.out.println( "Database operation: deleteRow(int)" );
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    private PreparedStatement mSelectAll;
    private static final String SQL_SELECT_ALL_IDEAS_TBL = 
            "SELECT id, likes" +
            " FROM ideas_tbl;";

    private boolean init_mSelectAll() {
        try {
            mSelectAll = mConnection.prepareStatement(SQL_SELECT_ALL_IDEAS_TBL);
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectAll");
            System.err.println("Using SQL: " + SQL_SELECT_ALL_IDEAS_TBL);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    ArrayList<RowData> selectAll() {
        if(mSelectAll == null)
            init_mSelectAll();
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            System.out.println("Database operation: selectAll()");
            ResultSet rs = mSelectAll.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                int likes = rs.getInt("likes");
                RowData data = new RowData(id, likes, null);
                res.add(data);
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private PreparedStatement mSelectOne;
    /** the SQL for mSelectOne */
    private static final String SQL_SELECT_ONE_IDEAS_TBL = 
            "SELECT *" + 
            " FROM ideas_tbl" +
            " WHERE id=? ;";

    /** safely performs mSelectOne = mConnection.prepareStatement("SELECT * from ideas_tbl WHERE id=?"); */
    private boolean init_mSelectOne(){
        // return true on success, false otherwise
        try {
            mSelectOne = mConnection.prepareStatement( SQL_SELECT_ONE_IDEAS_TBL );
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement: mSelectOne");
            System.err.println("Using SQL: " + SQL_SELECT_ONE_IDEAS_TBL);
            e.printStackTrace();
            this.disconnect();  // @TODO is disconnecting on exception what we want?
            return false;
        }
        return true;
    }

    /**
     * Get all data for a specific row, by ID
     * @param id The id of the row being requested
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowData selectOne(int row_id) {
        if( mSelectOne == null )  // not yet initialized, do lazy init
            init_mSelectOne();    // lazy init
        RowData data = null;
        try {
            System.out.println( "Database operation: selectOne(int)" );
            mSelectOne.setInt(1, row_id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                int likes = rs.getInt("likes");
                String message = rs.getString("message");
                data = new RowData(id, likes, message);
            }
            rs.close();  // remember to close the result set
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Disconnect from the database
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

            System.out.println(" ... connection succcessfully closed");
            mConnection = null; // connection is gone, so null this out
            return true;
        }

        System.err.println("Unable to close connection: Connection was null");
        return false;
    }
}
