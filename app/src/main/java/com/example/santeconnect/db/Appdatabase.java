package com.example.santeconnect.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.santeconnect.DAO.ReclamationDAO;
import com.example.santeconnect.entities.Reclamation;

@Database(entities = {Reclamation.class}, version = 1, exportSchema = false)
public abstract class Appdatabase extends RoomDatabase {
    private static Appdatabase instance;
    public abstract ReclamationDAO reclamationDAO();

 /*  public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Exemple : Ajouter une nouvelle colonne à une table existante
            database.execSQL("ALTER TABLE reclamation ADD COLUMN status TEXT");
        }
    };*/

    public static Appdatabase getAppDatabase(Context context){
        if (instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),Appdatabase.class,"SanteConnect")
                    //.addMigrations(MIGRATION_1_2)
                    .build();

        }
        return instance;
    }

}
