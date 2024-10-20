package com.example.santeconnect.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DatabaseHelper extends SQLiteOpenHelper {



    private static final String DATABASE_NAME = "SanteConnect";
    private static final String TABLE_NAME = "users";
    private static final String COL_ID = "ID";
    private static final String COL_NAME = "NAME";
    private static final String COL_EMAIL = "EMAIL";
    private static final String COL_PASSWORD = "PASSWORD";
    private static final String AES = "AES";
    private static final byte[] encryptionKey = "1234567890123456".getBytes();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PASSWORD + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Encryption method
    private String encrypt(String data) throws Exception {
        SecretKeySpec key = new SecretKeySpec(encryptionKey, AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }
    // Decryption method
    private String decrypt(String data) throws Exception {
        SecretKeySpec key = new SecretKeySpec(encryptionKey, AES);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedData = Base64.decode(data, Base64.DEFAULT);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData);
    }


    public boolean checkUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_ID},
                COL_EMAIL + "=?", new String[]{email},
                null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean insertUser(String name, String email, String password) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_NAME, name);
            contentValues.put(COL_EMAIL, email);
            contentValues.put(COL_PASSWORD, encrypt(password)); // Encrypt password

            long result = db.insert(TABLE_NAME, null, contentValues);
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_PASSWORD},
                COL_EMAIL + "=?", new String[]{email},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                // Decrypt stored password and compare with input password
                String storedPassword = decrypt(cursor.getString(0));
                cursor.close();
                return storedPassword.equals(password);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        if (cursor != null) cursor.close();
        return false;
    }

}
