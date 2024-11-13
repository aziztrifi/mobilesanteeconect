package com.example.santeconnect.Activity.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.santeconnect.Activity.Entities.Reclamation;

import java.util.List;

@Dao
public interface ReclamationDAO {
    @Insert
    void createReclamation(Reclamation reclamation);
    @Query("SELECT * FROM reclamation")
    List<Reclamation> getAllReclamations();
    @Delete
    void deleteReclamation(Reclamation reclamation);
    @Update
    void updateReclamation(Reclamation reclamation);
    @Query("SELECT * FROM reclamation WHERE id = :id")
    Reclamation getReclamationById(int id);

}
