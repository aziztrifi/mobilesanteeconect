package com.example.santeconnect.Activity.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.santeconnect.Activity.Entities.Dossier;
import com.example.santeconnect.Activity.Modules.Dossier.DossierDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DossierDAO {
    private DossierDatabaseHelper dbHelper;

    public DossierDAO(Context context) {
        dbHelper = new DossierDatabaseHelper(context);
    }

    public long insertDossier(Dossier dossier) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nom", dossier.getNom());
        values.put("prenom", dossier.getPrenom());
        values.put("dateNaissance", dossier.getDateNaissance());
        // Ajouter les autres attributs de manière similaire
        return db.insert("dossiers", null, values);
    }

    // Méthodes pour récupérer, mettre à jour et supprimer un dossier
}
