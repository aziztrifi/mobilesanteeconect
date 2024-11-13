package com.example.santeconnect.Activity.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name
    private static final String DATABASE_NAME = "SanteConnect+";
    private static final int DATABASE_VERSION = 5; // Incremented version for the new changes

    // User attributes
    private static final String TABLE_NAME_USER = "users";
    private static final String COL_ID = "ID";
    public static final String COLUMN_ID = "id";
    private static final String COL_NAME = "NAME";
    private static final String COL_EMAIL = "EMAIL";
    private static final String COL_ROLE = "ROLE";
    private static final String COL_PASSWORD = "PASSWORD";
    private static final String COL_RESET_CODE = "RESET_CODE";
    private static final String COL_RESET_EXPIRY = "RESET_EXPIRY";

    // Rendez-vous attributes
    public static final String TABLE_RENDEZ_VOUS = "rendez_vous";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_URGENT = "urgent";
    public static final String COLUMN_USER_PATIENT_ID = "idUserPatient";  // New column for patient ID
    public static final String COLUMN_USER_DOCTOR_ID = "idUserDoctor";   // New column for doctor ID

    // SQL statements for creating tables
    private static final String CREATE_USER_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USER + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_NAME + " TEXT, " +
                    COL_EMAIL + " TEXT UNIQUE, " +
                    COL_PASSWORD + " TEXT, " +
                    COL_ROLE + " TEXT, " +
                    COL_RESET_CODE + " TEXT, " +
                    COL_RESET_EXPIRY + " INTEGER);";

    private static final String CREATE_RENDEZ_VOUS_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_RENDEZ_VOUS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_TIME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_STATUS + " TEXT, " +
                    COLUMN_URGENT + " INTEGER, " +
                    COLUMN_USER_PATIENT_ID + " INTEGER, " +
                    COLUMN_USER_DOCTOR_ID + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_USER_PATIENT_ID + ") REFERENCES " + TABLE_NAME_USER + "(" + COL_ID + "), " +
                    "FOREIGN KEY(" + COLUMN_USER_DOCTOR_ID + ") REFERENCES " + TABLE_NAME_USER + "(" + COL_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_RENDEZ_VOUS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Check the old version and apply upgrades
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME_USER + " ADD COLUMN " + COL_ROLE + " TEXT");
        }

        if (oldVersion < 5) {
            db.execSQL("ALTER TABLE " + TABLE_RENDEZ_VOUS + " ADD COLUMN " + COLUMN_USER_PATIENT_ID + " INTEGER");
            db.execSQL("ALTER TABLE " + TABLE_RENDEZ_VOUS + " ADD COLUMN " + COLUMN_USER_DOCTOR_ID + " INTEGER");

            // Add foreign key constraints if possible (SQLite requires re-creating the table to add constraints)
            // Note: Foreign keys can only be added during table creation in SQLite, so the constraint will not be added in upgrade.
        }
    }
}
