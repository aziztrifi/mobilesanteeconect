package com.example.santeconnect.Activity.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.santeconnect.Activity.Database.DatabaseHelper;
import com.example.santeconnect.Activity.Entities.RendezVou;

import java.util.ArrayList;
import java.util.List;

public class RendezVousDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public RendezVousDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }


    public void insertRendezVou(RendezVou rendezVou) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DATE, rendezVou.getDate());
        values.put(DatabaseHelper.COLUMN_TIME, rendezVou.getTime());
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, rendezVou.getDescription());
        values.put(DatabaseHelper.COLUMN_STATUS, rendezVou.getStatus());
        values.put(DatabaseHelper.COLUMN_URGENT, rendezVou.isUrgent() ? 1 : 0);  // Store boolean as 1 or 0
        database.insert(DatabaseHelper.TABLE_RENDEZ_VOUS, null, values);
    }

    // Update an existing rendez-vous
    public void updateRendezVou(RendezVou rendezVou) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DATE, rendezVou.getDate());
        values.put(DatabaseHelper.COLUMN_TIME, rendezVou.getTime());
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, rendezVou.getDescription());
        values.put(DatabaseHelper.COLUMN_STATUS, rendezVou.getStatus());
        values.put(DatabaseHelper.COLUMN_URGENT, rendezVou.isUrgent() ? 1 : 0);
        database.update(DatabaseHelper.TABLE_RENDEZ_VOUS, values,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(rendezVou.getId())});
    }
    // Update the status of a rendez-vous to "Accepted"
    public void acceptRendezVou(int id) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STATUS, "Accepted");
        database.update(DatabaseHelper.TABLE_RENDEZ_VOUS, values,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Update the status of a rendez-vous to "Rejected"
    public void rejectRendezVou(int id) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_STATUS, "Rejected");
        database.update(DatabaseHelper.TABLE_RENDEZ_VOUS, values,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }


    // Delete a rendez-vous
    public void deleteRendezVou(int id) {
        database.delete(DatabaseHelper.TABLE_RENDEZ_VOUS,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Get all rendez-vous
    public List<RendezVou> getAllRendezVous() {
        List<RendezVou> rendezVousList = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_RENDEZ_VOUS,
                null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                RendezVou rendezVou = new RendezVou();
                rendezVou.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
                rendezVou.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE)));
                rendezVou.setTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIME)));
                rendezVou.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION)));
                rendezVou.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUS)));
                rendezVou.setUrgent(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_URGENT)) == 1);
                rendezVousList.add(rendezVou);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return rendezVousList;
    }

    // Get a single rendez-vous by ID
    public RendezVou getRendezVouById(int id) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_RENDEZ_VOUS,
                null, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            RendezVou rendezVou = new RendezVou();
            rendezVou.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)));
            rendezVou.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE)));
            rendezVou.setTime(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TIME)));
            rendezVou.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION)));
            rendezVou.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUS)));
            rendezVou.setUrgent(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_URGENT)) == 1);
            cursor.close();
            return rendezVou;
        } else {
            return null;
        }
    }
}
