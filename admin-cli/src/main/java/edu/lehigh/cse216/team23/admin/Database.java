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

    public static record RowData(int mId, String mSubject, String mMessage) {}

    private Connection mConnection;

    // SQL Queries
    private static final String SQL_CREATE_TABLE = 
            "CREATE TABLE IF NOT EXISTS ideas_tbl (" + 
            " id SERIAL PRIMARY KEY," + 
            " subject VARCHAR(50) NOT NULL," +
            " message VARCHAR(500) NOT NULL)";
    
    private static final String SQL_DROP_TABLE_IDEAS = "DROP TABLE IF EXISTS ideas_tbl";

    private static final String SQL_INSERT_ONE_IDEAS = 
            "INSERT INTO ideas_tbl (subject, message) VALUES (?, ?)";

    private static final String SQL_UPDATE_ONE_IDEAS = 
            "UPDATE ideas_tbl SET message = ? WHERE id = ?";

    private static final String SQL_DELETE_ONE = "DELETE FROM ideas_tbl WHERE id = ?";

    private static final String SQL_SELECT_ALL_IDEAS = "SELECT id, subject FROM ideas_tbl";

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
            mCreateTable = mConnection.prepareStatement(SQL_CREATE_TABLE);
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
                res.add(new RowData(id, subject, null));
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
                data = new RowData(id, subject, message);
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

    private Database() {}

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
