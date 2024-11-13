package com.example.santeconnect.Activity.Modules.Dossier;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.santeconnect.Activity.Entities.Dossier;

import java.util.ArrayList;

public class DossierDatabaseHelper extends SQLiteOpenHelper {

    // Nom et version de la base de données
    private static final String DATABASE_NAME = "dossierMedical.db";
    private static final int DATABASE_VERSION = 1;

    // Table et colonnes
    private static final String TABLE_DOSSIERS = "dossiers";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOM = "nom";
    private static final String COLUMN_PRENOM = "prenom";
    private static final String COLUMN_DATE_NAISSANCE = "date_naissance";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_ADRESSE = "adresse";
    private static final String COLUMN_NUMERO_TELEPHONE = "numero_telephone";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_IMG = "img";
    private static final String COLUMN_TYPE_SANGUIN = "type_sanguin";
    private static final String COLUMN_ANTECEDENTS = "antecedents_medicaux";
    private static final String COLUMN_ALLERGIES = "allergies";
    private static final String COLUMN_CONTACT_URGENCE = "personne_contact_urgence";
    private static final String COLUMN_RELATION_URGENCE = "relation_contact_urgence";
    private static final String COLUMN_NUMERO_URGENCE = "numero_contact_urgence";

    public DossierDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_DOSSIERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NOM + " TEXT," +
                COLUMN_PRENOM + " TEXT," +
                COLUMN_DATE_NAISSANCE + " TEXT," +
                COLUMN_GENRE + " TEXT," +
                COLUMN_ADRESSE + " TEXT," +
                COLUMN_NUMERO_TELEPHONE + " TEXT," +
                COLUMN_EMAIL + " TEXT," +
                COLUMN_IMG + " BLOB," +
                COLUMN_TYPE_SANGUIN + " TEXT," +
                COLUMN_ANTECEDENTS + " TEXT," +
                COLUMN_ALLERGIES + " TEXT," +
                COLUMN_CONTACT_URGENCE + " TEXT," +
                COLUMN_RELATION_URGENCE + " TEXT," +
                COLUMN_NUMERO_URGENCE + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOSSIERS);
        onCreate(db);
    }
    // Méthode pour ajouter un dossier
    public long addDossier(Dossier dossier) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM, dossier.getNom());
        values.put(COLUMN_PRENOM, dossier.getPrenom());
        values.put(COLUMN_DATE_NAISSANCE, dossier.getDateNaissance());
        values.put(COLUMN_GENRE, dossier.getGenre());
        values.put(COLUMN_ADRESSE, dossier.getAdresse());
        values.put(COLUMN_NUMERO_TELEPHONE, dossier.getNumeroTelephone());
        values.put(COLUMN_EMAIL, dossier.getEmail());
        values.put(COLUMN_IMG, dossier.getImg());
        values.put(COLUMN_TYPE_SANGUIN, dossier.getTypeSanguin());
        values.put(COLUMN_ANTECEDENTS, dossier.getAntecedentsMedicaux());
        values.put(COLUMN_ALLERGIES, dossier.getAllergies());
        values.put(COLUMN_CONTACT_URGENCE, dossier.getPersonneContactUrgence());
        values.put(COLUMN_RELATION_URGENCE, dossier.getRelationContactUrgence());
        values.put(COLUMN_NUMERO_URGENCE, dossier.getNumeroContactUrgence());

        // Insertion dans la base de données
        long id = db.insert(TABLE_DOSSIERS, null, values);
        db.close();
        return id;
    }
    // Méthode pour récupérer un dossier par son ID
//  aa
    // Méthode pour mettre à jour un dossier
    public int updateDossier(Dossier dossier) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM, dossier.getNom());
        values.put(COLUMN_PRENOM, dossier.getPrenom());
        values.put(COLUMN_DATE_NAISSANCE, dossier.getDateNaissance());
        values.put(COLUMN_GENRE, dossier.getGenre());
        values.put(COLUMN_ADRESSE, dossier.getAdresse());
        values.put(COLUMN_NUMERO_TELEPHONE, dossier.getNumeroTelephone());
        values.put(COLUMN_EMAIL, dossier.getEmail());
        values.put(COLUMN_IMG, dossier.getImg());
        values.put(COLUMN_TYPE_SANGUIN, dossier.getTypeSanguin());
        values.put(COLUMN_ANTECEDENTS, dossier.getAntecedentsMedicaux());
        values.put(COLUMN_ALLERGIES, dossier.getAllergies());
        values.put(COLUMN_CONTACT_URGENCE, dossier.getPersonneContactUrgence());
        values.put(COLUMN_RELATION_URGENCE, dossier.getRelationContactUrgence());
        values.put(COLUMN_NUMERO_URGENCE, dossier.getNumeroContactUrgence());

        // Mise à jour du dossier dans la base de données
        return db.update(TABLE_DOSSIERS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(dossier.getId())});
    }
    public boolean deleteDossier(int dossierId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("dossiers", "id = ?", new String[]{String.valueOf(dossierId)});
        db.close();
        return result > 0; // Retourne true si la suppression a été effectuée
    }

    // Méthode pour obtenir tous les dossiers
    public ArrayList<Dossier> getAllDossiers() {
        ArrayList<Dossier> dossiers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DOSSIERS, null);

        // Vérification des indices de colonnes avant de les utiliser
        if (cursor != null) {
            int indexId = cursor.getColumnIndex(COLUMN_ID);
            int indexNom = cursor.getColumnIndex(COLUMN_NOM);
            int indexPrenom = cursor.getColumnIndex(COLUMN_PRENOM);
            int indexDateNaissance = cursor.getColumnIndex(COLUMN_DATE_NAISSANCE);
            int indexGenre = cursor.getColumnIndex(COLUMN_GENRE);
            int indexAdresse = cursor.getColumnIndex(COLUMN_ADRESSE);
            int indexNumeroTelephone = cursor.getColumnIndex(COLUMN_NUMERO_TELEPHONE);
            int indexEmail = cursor.getColumnIndex(COLUMN_EMAIL);
            int indexImg = cursor.getColumnIndex(COLUMN_IMG);
            int indexTypeSanguin = cursor.getColumnIndex(COLUMN_TYPE_SANGUIN);
            int indexAntecedents = cursor.getColumnIndex(COLUMN_ANTECEDENTS);
            int indexAllergies = cursor.getColumnIndex(COLUMN_ALLERGIES);
            int indexContactUrgence = cursor.getColumnIndex(COLUMN_CONTACT_URGENCE);
            int indexRelationUrgence = cursor.getColumnIndex(COLUMN_RELATION_URGENCE);
            int indexNumeroUrgence = cursor.getColumnIndex(COLUMN_NUMERO_URGENCE);

            // Vérifiez si tous les indices sont valides
            if (indexId >= 0 && indexNom >= 0 && indexPrenom >= 0 && indexDateNaissance >= 0 &&
                    indexGenre >= 0 && indexAdresse >= 0 && indexNumeroTelephone >= 0 && indexEmail >= 0 &&
                    indexImg >= 0 && indexTypeSanguin >= 0 && indexAntecedents >= 0 && indexAllergies >= 0 &&
                    indexContactUrgence >= 0 && indexRelationUrgence >= 0 && indexNumeroUrgence >= 0) {

                // Si tous les indices sont valides, on continue à récupérer les données
                if (cursor.moveToFirst()) {
                    do {
                        Dossier dossier = new Dossier();
                        dossier.setId(cursor.getLong(indexId));
                        dossier.setNom(cursor.getString(indexNom));
                        dossier.setPrenom(cursor.getString(indexPrenom));
                        dossier.setDateNaissance(cursor.getString(indexDateNaissance));
                        dossier.setGenre(cursor.getString(indexGenre));
                        dossier.setAdresse(cursor.getString(indexAdresse));
                        dossier.setNumeroTelephone(cursor.getString(indexNumeroTelephone));
                        dossier.setEmail(cursor.getString(indexEmail));
                        dossier.setImg(cursor.getBlob(indexImg));
                        dossier.setTypeSanguin(cursor.getString(indexTypeSanguin));
                        dossier.setAntecedentsMedicaux(cursor.getString(indexAntecedents));
                        dossier.setAllergies(cursor.getString(indexAllergies));
                        dossier.setPersonneContactUrgence(cursor.getString(indexContactUrgence));
                        dossier.setRelationContactUrgence(cursor.getString(indexRelationUrgence));
                        dossier.setNumeroContactUrgence(cursor.getString(indexNumeroUrgence));
                        dossiers.add(dossier);
                    } while (cursor.moveToNext());
                }
            } else {
                // Si un ou plusieurs indices sont invalides, affichez un message d'erreur ou gérez le cas
                Log.e("DossierDatabaseHelper", "Une ou plusieurs colonnes n'existent pas dans la base de données.");
            }

            cursor.close();
        }

        db.close();
        return dossiers;
    }
///***********


    // Méthode pour récupérer un dossier par son ID
    public Dossier getDossierById(int dossierId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Dossier dossier = null;

        // Requête pour récupérer un dossier spécifique par ID
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DOSSIERS + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(dossierId)});

        if (cursor != null && cursor.moveToFirst()) {
            // Vérification des indices des colonnes avant de les utiliser
            int indexId = cursor.getColumnIndex(COLUMN_ID);
            int indexNom = cursor.getColumnIndex(COLUMN_NOM);
            int indexPrenom = cursor.getColumnIndex(COLUMN_PRENOM);
            int indexDateNaissance = cursor.getColumnIndex(COLUMN_DATE_NAISSANCE);
            int indexGenre = cursor.getColumnIndex(COLUMN_GENRE);
            int indexAdresse = cursor.getColumnIndex(COLUMN_ADRESSE);
            int indexNumeroTelephone = cursor.getColumnIndex(COLUMN_NUMERO_TELEPHONE);
            int indexEmail = cursor.getColumnIndex(COLUMN_EMAIL);
            int indexImg = cursor.getColumnIndex(COLUMN_IMG);
            int indexTypeSanguin = cursor.getColumnIndex(COLUMN_TYPE_SANGUIN);
            int indexAntecedents = cursor.getColumnIndex(COLUMN_ANTECEDENTS);
            int indexAllergies = cursor.getColumnIndex(COLUMN_ALLERGIES);
            int indexContactUrgence = cursor.getColumnIndex(COLUMN_CONTACT_URGENCE);
            int indexRelationUrgence = cursor.getColumnIndex(COLUMN_RELATION_URGENCE);
            int indexNumeroUrgence = cursor.getColumnIndex(COLUMN_NUMERO_URGENCE);

            // Vérifiez si tous les indices sont valides (≥ 0)
            if (indexId >= 0 && indexNom >= 0 && indexPrenom >= 0 && indexDateNaissance >= 0 &&
                    indexGenre >= 0 && indexAdresse >= 0 && indexNumeroTelephone >= 0 && indexEmail >= 0 &&
                    indexImg >= 0 && indexTypeSanguin >= 0 && indexAntecedents >= 0 && indexAllergies >= 0 &&
                    indexContactUrgence >= 0 && indexRelationUrgence >= 0 && indexNumeroUrgence >= 0) {

                // Si tous les indices sont valides, récupérez les données
                dossier = new Dossier();
                dossier.setId(cursor.getLong(indexId));
                dossier.setNom(cursor.getString(indexNom));
                dossier.setPrenom(cursor.getString(indexPrenom));
                dossier.setDateNaissance(cursor.getString(indexDateNaissance));
                dossier.setGenre(cursor.getString(indexGenre));
                dossier.setAdresse(cursor.getString(indexAdresse));
                dossier.setNumeroTelephone(cursor.getString(indexNumeroTelephone));
                dossier.setEmail(cursor.getString(indexEmail));
                dossier.setImg(cursor.getBlob(indexImg));
                dossier.setTypeSanguin(cursor.getString(indexTypeSanguin));
                dossier.setAntecedentsMedicaux(cursor.getString(indexAntecedents));
                dossier.setAllergies(cursor.getString(indexAllergies));
                dossier.setPersonneContactUrgence(cursor.getString(indexContactUrgence));
                dossier.setRelationContactUrgence(cursor.getString(indexRelationUrgence));
                dossier.setNumeroContactUrgence(cursor.getString(indexNumeroUrgence));
            } else {
                // Si un ou plusieurs indices sont invalides, affichez un message d'erreur
                Log.e("DossierDatabaseHelper", "Une ou plusieurs colonnes n'existent pas dans la base de données.");
            }
        }

        cursor.close();
        db.close();

        return dossier;
    }
    public ArrayList<Dossier> searchDossiersByNom(String nom) {
        ArrayList<Dossier> dossiers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DOSSIERS + " WHERE " + COLUMN_NOM + " LIKE ?",
                new String[]{"%" + nom + "%"});

        if (cursor != null) {
            int indexId = cursor.getColumnIndex(COLUMN_ID);
            int indexNom = cursor.getColumnIndex(COLUMN_NOM);
            int indexPrenom = cursor.getColumnIndex(COLUMN_PRENOM);
            int indexDateNaissance = cursor.getColumnIndex(COLUMN_DATE_NAISSANCE);
            int indexGenre = cursor.getColumnIndex(COLUMN_GENRE);
            int indexAdresse = cursor.getColumnIndex(COLUMN_ADRESSE);
            int indexNumeroTelephone = cursor.getColumnIndex(COLUMN_NUMERO_TELEPHONE);
            int indexEmail = cursor.getColumnIndex(COLUMN_EMAIL);
            int indexImg = cursor.getColumnIndex(COLUMN_IMG);
            int indexTypeSanguin = cursor.getColumnIndex(COLUMN_TYPE_SANGUIN);
            int indexAntecedents = cursor.getColumnIndex(COLUMN_ANTECEDENTS);
            int indexAllergies = cursor.getColumnIndex(COLUMN_ALLERGIES);
            int indexContactUrgence = cursor.getColumnIndex(COLUMN_CONTACT_URGENCE);
            int indexRelationUrgence = cursor.getColumnIndex(COLUMN_RELATION_URGENCE);
            int indexNumeroUrgence = cursor.getColumnIndex(COLUMN_NUMERO_URGENCE);

            if (cursor.moveToFirst()) {
                do {
                    Dossier dossier = new Dossier();
                    dossier.setId(cursor.getLong(indexId));
                    dossier.setNom(cursor.getString(indexNom));
                    dossier.setPrenom(cursor.getString(indexPrenom));
                    dossier.setDateNaissance(cursor.getString(indexDateNaissance));
                    dossier.setGenre(cursor.getString(indexGenre));
                    dossier.setAdresse(cursor.getString(indexAdresse));
                    dossier.setNumeroTelephone(cursor.getString(indexNumeroTelephone));
                    dossier.setEmail(cursor.getString(indexEmail));
                    dossier.setImg(cursor.getBlob(indexImg));
                    dossier.setTypeSanguin(cursor.getString(indexTypeSanguin));
                    dossier.setAntecedentsMedicaux(cursor.getString(indexAntecedents));
                    dossier.setAllergies(cursor.getString(indexAllergies));
                    dossier.setPersonneContactUrgence(cursor.getString(indexContactUrgence));
                    dossier.setRelationContactUrgence(cursor.getString(indexRelationUrgence));
                    dossier.setNumeroContactUrgence(cursor.getString(indexNumeroUrgence));
                    dossiers.add(dossier);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return dossiers;
    }

}
