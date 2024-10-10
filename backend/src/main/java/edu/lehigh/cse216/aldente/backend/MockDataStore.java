package edu.lehigh.cse216.aldente.backend;

import java.util.ArrayList;

/**
 * MockDataStore provides access to a set of objects, and makes sure that each has
 * a unique identifier that remains unique even after the object is deleted.
 * 
 * We follow the convention that member fields of a class have names that start
 * with a lowercase 'm' character, and are in camelCase.
 * 
 * NB: The methods of MockDataStore are synchronized, since they will be used from a 
 * web framework and there may be multiple concurrent accesses to the MockDataStore.
 */
public class MockDataStore {
    /**
     * The rows of data in our MockDataStore
     */
    private ArrayList<MockDataRow> mRows;

    /**
     * A counter for keeping track of the next ID to assign to a new row
     */
    private int mCounter;

    /**
     * Construct the MockDataStore by resetting its counter and creating the
     * ArrayList for the rows of data.
     */
    MockDataStore() {
        mCounter = 0;
        mRows = new ArrayList<>();
    }

    /**
     * Add a new row to the MockDataStore
     * 
     * Note: we return -1 on an error.  There are many good ways to handle an 
     * error, to include throwing an exception.  In robust code, returning -1 
     * may not be the most appropriate technique, but it is sufficient for this 
     * tutorial.
     * 
     * @param title The title for this newly added row
     * @param content The content for this row
     * @return the ID of the new row, or -1 if no row was created
     */
    public synchronized int createEntry(String title, String content) {
        if (title == null || content == null)
            return -1;
        // NB: we can safely assume that id is greater than the largest index in 
        //     mRows, and thus we can use the index-based add() method
        int id = mCounter++;
        mRows.add(id, new MockDataRow(id, title, content, null) );
        return id;
    }

    /**
     * Get one complete row from the MockDataStore using its ID to select it
     * Because we are using a record instead of POJO, we do not make a
     * defensive copy of the object before returning (it's immutable).
     * @param id The id of the row to select
     * @return the data in the row, if it exists, or null otherwise
     */
    public synchronized MockDataRow readOne(int id) {
        if (id >= mRows.size())
            return null;
        return mRows.get(id);
    }

    public synchronized MockDataRow updateOne(int id, String title, String content) {
        if(title == null || content == null) {
            System.err.println("ERROR: attempt to updateOne using a null title or content");
            return null;
        }

        if(id >= mRows.size() || id < 0) {
            System.err.println("ERROR:: attempt to updateOne using an id > mRows.size() or id < 0");
            return null;
        }

        MockDataRow rowOrig = mRows.get(id);
        MockDataRow rowUpdated = new MockDataRow(rowOrig.mId(), title, content, rowOrig.mCreated());
        mRows.set(id, rowUpdated);
        return mRows.get(id);
    }

    public synchronized boolean deleteOne(int id) {
        if(id >= mRows.size() || id < 0) {
            System.err.println("ERROR: attempt to deleteOne using an invalid id");
            return false;
        }
        if (mRows.get(id) == null) {
            System.out.println("WARNING: attempt to deleteOne with an id of a row not present in data store");
            return false;
        }

        mRows.set(id, null);
        return true;
    }

    /**
     * Get all of the ids and titles that are present in the MockDataStore
     * Why a MockDataRowLite rather than MockDataRow? To conserve bandwidth.
     * @return An ArrayList with all of the data; everything is immutable
     */
    public synchronized ArrayList<MockDataRowLite> readAll() {
        ArrayList<MockDataRowLite> data = new ArrayList<>();
        // NB: our ArrayList only has ids and titles to conserve data transfer
        for (MockDataRow row : mRows) {
            if (row != null)
                data.add(new MockDataRowLite(row));
        }
        return data;
    }
}