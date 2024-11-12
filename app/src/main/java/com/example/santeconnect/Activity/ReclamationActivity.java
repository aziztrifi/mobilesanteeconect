package com.example.santeconnect.Activity;

import android.content.DialogInterface;
import android.content.Intent; // Ajout de l'importation Intent
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.santeconnect.R;
import com.example.santeconnect.db.Appdatabase;
import com.example.santeconnect.entities.Reclamation;
import com.google.android.material.snackbar.Snackbar;

public class ReclamationActivity extends AppCompatActivity {

    private EditText etName, etEmail, etDescription, etOtherReclamation;
    private CheckBox cbDoctor, cbService, cbAppointment, cbOther;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamation);





        // Initialisation des vues
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etDescription = findViewById(R.id.et_description);
        etOtherReclamation = findViewById(R.id.et_other_reclamation);
        cbDoctor = findViewById(R.id.cb_doctor);
        cbService = findViewById(R.id.cb_service);
        cbAppointment = findViewById(R.id.cb_appointment);
        cbOther = findViewById(R.id.cb_other);
        btnSubmit = findViewById(R.id.btn_submit);

        // Action pour le bouton Soumettre
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

        // Listener pour la case à cocher "Autre"
        cbOther.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etOtherReclamation.setVisibility(View.VISIBLE);
            } else {
                etOtherReclamation.setVisibility(View.GONE);
                etOtherReclamation.setText(""); // Clear text if unchecked
            }
        });
    }

    private void validateForm() {
        // Récupérer les valeurs des champs
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        // Vérification du nom (min 3 caractères et pas de caractères spéciaux)
        if (name.isEmpty() || name.length() < 3 || !name.matches("^[a-zA-Z\\s]+$")) {
            etName.setError("Le nom doit avoir au moins 3 caractères et ne doit contenir que des lettres.");
            return;
        }

        // Vérification de l'email (doit être un email valide)
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Veuillez entrer une adresse email valide.");
            return;
        }

        // Vérification de la description (min 8 caractères)
        if (description.isEmpty() || description.length() < 8) {
            etDescription.setError("La description doit avoir au moins 8 caractères.");
            return;
        }

        // Vérifier si au moins une case à cocher est sélectionnée
        if (!cbDoctor.isChecked() && !cbService.isChecked() && !cbAppointment.isChecked() && !cbOther.isChecked()) {
            Snackbar.make(findViewById(R.id.btn_submit), "Veuillez sélectionner au moins une option de réclamation.", Snackbar.LENGTH_SHORT).show();
            return;
        }

        // Afficher un dialogue de confirmation
        showConfirmationDialog(name, email, description);
    }

    private void showConfirmationDialog(String name, String email, String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmer la soumission");
        builder.setMessage("Êtes-vous sûr de vouloir soumettre votre réclamation ?");

        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Créer un objet Reclamation
                String type = getSelectedReclamationType();
                Reclamation newReclamation = new Reclamation(0, name, email, description, type, "Non traité");

                // Insérer la réclamation dans la base de données
                new Thread(() -> {
                    Appdatabase db = Appdatabase.getAppDatabase(getApplicationContext());
                    db.reclamationDAO().createReclamation(newReclamation);
                }).start();

                // Réinitialiser le formulaire
                resetForm();

                // Afficher un message de succès
                Snackbar snackbar = Snackbar.make(findViewById(R.id.btn_submit), "Votre réclamation a bien été soumise.", Snackbar.LENGTH_LONG);
                snackbar.show();

                // Démarrer l'activité de suivi des réclamations
                Intent intent = new Intent(ReclamationActivity.this, SuiviReclamationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Annuler et retourner au formulaire
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Méthode pour obtenir le type de réclamation sélectionné
    private String getSelectedReclamationType() {
        if (cbDoctor.isChecked()) {
            return "Docteur";
        } else if (cbService.isChecked()) {
            return "Service";
        } else if (cbAppointment.isChecked()) {
            return "Rendez-vous";
        } else if (cbOther.isChecked()) {
            return etOtherReclamation.getText().toString().trim();
        }
        return "";
    }


    private void resetForm() {
        // Réinitialiser les champs
        etName.setText("");
        etEmail.setText("");
        etDescription.setText("");
        etOtherReclamation.setText("");
        cbDoctor.setChecked(false);
        cbService.setChecked(false);
        cbAppointment.setChecked(false);
        cbOther.setChecked(false);
        etOtherReclamation.setVisibility(View.GONE);
    }
}
