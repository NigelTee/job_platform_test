package materialtest.example.user.materialtest.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SavedJobsDB{

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "SavedJobsDB";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    /*
     * CHANGE 1:
     */
    // TODO: Setup your fields here:
    public static final String KEY_JOBNAME = "jobaname";
    public static final String KEY_COMPANYNAME = "companyname";
    public static final String KEY_QUALIFICATION = "qualification";
    public static final String KEY_DAYS = "days";
    public static final String KEY_TIME = "time";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_SALARY = "salary";
    public static final String KEY_DEADLINE = "deadline";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_REQUIREMENTS = "requirements";
    public static final String KEY_BENEFITS = "benefits";

    // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
    public static final int COL_JOBNAME = 1;
    public static final int COL_COMPANYNAME = 2;
    public static final int COL_QUALIFICATION = 3;
    public static final int COL_DAYS = 4;
    public static final int COL_TIME = 5;
    public static final int COL_LOCATION = 6;
    public static final int COL_SALARY = 7;
    public static final int COL_DEADLINE = 8;
    public static final int COL_DESCRIPTION = 9;
    public static final int COL_REQUIREMENTS = 10;
    public static final int COL_BENEFITS = 11;

    public static final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_JOBNAME, KEY_COMPANYNAME, KEY_QUALIFICATION, KEY_DAYS, KEY_TIME, KEY_LOCATION, KEY_SALARY, KEY_DEADLINE, KEY_DESCRIPTION, KEY_REQUIREMENTS, KEY_BENEFITS};

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_NAME = "MySavedJobsDb";
    public static final String DATABASE_TABLE = "mainTable";
    // Track DB version if a new version of your app changes the format.
    public static final int DATABASE_VERSION = 4;

    private static final String DATABASE_CREATE_SQL =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "

			/*
             * CHANGE 2:
			 */
                    // TODO: Place your fields here!
                    // + KEY_{...} + " {type} not null"
                    //	- Key is the column name you created above.
                    //	- {type} is one of: text, integer, real, blob
                    //		(http://www.sqlite.org/datatype3.html)
                    //  - "not null" means it is a required field (must be given a value).
                    // NOTE: All must be comma separated (end of line!) Last one must have NO comma!!
                    + KEY_JOBNAME + " text not null, "
                    + KEY_COMPANYNAME + " text not null, "
                    + KEY_QUALIFICATION + " text not null, "
                    + KEY_DAYS + " text not null, "
                    + KEY_TIME + " text not null, "
                    + KEY_LOCATION + " text not null, "
                    + KEY_SALARY + " text not null, "
                    + KEY_DEADLINE + " text not null, "
                    + KEY_DESCRIPTION + " text not null, "
                    + KEY_REQUIREMENTS + " text not null, "
                    + KEY_BENEFITS + " text not null "

                    // Rest  of creation:
                    + ");";

    private DatabaseHelper myDBHelper_SJ;
    private SQLiteDatabase db2;

    /////////////////////////////////////////////////////////////////////
    //	Public methods:
    /////////////////////////////////////////////////////////////////////

    public SavedJobsDB(Context ctx) {
        Context context = ctx;
        myDBHelper_SJ = new DatabaseHelper(context);
    }

    // Open the database connection.
    public SavedJobsDB open() {
        db2 = myDBHelper_SJ.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper_SJ.close();
    }

    // Add a new set of values to the database.
    public long insertRow(String jobname,String companyname, String qualification, String days, String time, String location, String salary, String deadline, String description, String requirements, String benefits) {
		/*
		 * CHANGE 3:
		 */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_JOBNAME, jobname);
        initialValues.put(KEY_COMPANYNAME, companyname);
        initialValues.put(KEY_QUALIFICATION, qualification);
        initialValues.put(KEY_DAYS, days);
        initialValues.put(KEY_TIME, time);
        initialValues.put(KEY_LOCATION, location);
        initialValues.put(KEY_SALARY, salary);
        initialValues.put(KEY_DEADLINE, deadline);
        initialValues.put(KEY_DESCRIPTION, description);
        initialValues.put(KEY_REQUIREMENTS, requirements);
        initialValues.put(KEY_BENEFITS, benefits);

        // Insert it into the database.
        return db2.insert(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db2.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows() {
        Cursor c = db2.query(true, DATABASE_TABLE, ALL_KEYS,
                null, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db2.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    /////////////////////////////////////////////////////////////////////
    //	Private Helper Classes:
    /////////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }

}