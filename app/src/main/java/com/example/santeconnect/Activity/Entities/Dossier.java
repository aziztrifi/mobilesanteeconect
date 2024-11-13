package com.example.santeconnect.Activity.Entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Dossier implements Parcelable{
    private long id;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String genre;
    private String adresse;
    private String numeroTelephone;
    private String email;
    private byte[] img;
    private String typeSanguin;
    private String antecedentsMedicaux;
    private String allergies;
    private String personneContactUrgence;
    private String relationContactUrgence;
    private String numeroContactUrgence;

    // Constructeur par défaut
    public Dossier() {}

    public Dossier(long id, String nom, String prenom, String telephone, String adresse, String email, String dateNaissance, String typeSanguin, byte[] img) {
    }

    // Getters et Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getTypeSanguin() {
        return typeSanguin;
    }

    public void setTypeSanguin(String typeSanguin) {
        this.typeSanguin = typeSanguin;
    }

    public String getAntecedentsMedicaux() {
        return antecedentsMedicaux;
    }

    public void setAntecedentsMedicaux(String antecedentsMedicaux) {
        this.antecedentsMedicaux = antecedentsMedicaux;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getPersonneContactUrgence() {
        return personneContactUrgence;
    }

    public void setPersonneContactUrgence(String personneContactUrgence) {
        this.personneContactUrgence = personneContactUrgence;
    }

    public String getRelationContactUrgence() {
        return relationContactUrgence;
    }

    public void setRelationContactUrgence(String relationContactUrgence) {
        this.relationContactUrgence = relationContactUrgence;
    }

    public String getNumeroContactUrgence() {
        return numeroContactUrgence;
    }

    public void setNumeroContactUrgence(String numeroContactUrgence) {
        this.numeroContactUrgence = numeroContactUrgence;
    }
    // Implémentation de la méthode Parcelable
    protected Dossier(Parcel in) {
        id = in.readLong();
        nom = in.readString();
        prenom = in.readString();
        dateNaissance = in.readString();
        genre = in.readString();
        adresse = in.readString();
        numeroTelephone = in.readString();
        email = in.readString();
        img = in.createByteArray();
        typeSanguin = in.readString();
        antecedentsMedicaux = in.readString();
        allergies = in.readString();
        personneContactUrgence = in.readString();
        relationContactUrgence = in.readString();
        numeroContactUrgence = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeString(dateNaissance);
        dest.writeString(genre);
        dest.writeString(adresse);
        dest.writeString(numeroTelephone);
        dest.writeString(email);
        dest.writeByteArray(img);
        dest.writeString(typeSanguin);
        dest.writeString(antecedentsMedicaux);
        dest.writeString(allergies);
        dest.writeString(personneContactUrgence);
        dest.writeString(relationContactUrgence);
        dest.writeString(numeroContactUrgence);
    }

    public static final Creator<Dossier> CREATOR = new Creator<Dossier>() {
        @Override
        public Dossier createFromParcel(Parcel in) {
            return new Dossier(in);
        }

        @Override
        public Dossier[] newArray(int size) {
            return new Dossier[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }
}


