package com.example.santeconnect.Activity.Modules.Avis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.santeconnect.Activity.Entities.Reclamation;
import com.example.santeconnect.R;
import com.example.santeconnect.database.Appdatabase;

public class ModifierReclamationActivity extends AppCompatActivity {

    private EditText editTextDescription;
    private CheckBox cbDoctor, cbService, cbAppointment, cbOther;
    private EditText etOtherReclamation;
    private Button btnModifier;
    private Appdatabase db;
    private Reclamation reclamation; // Réclamation à modifier

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_reclamation);

        editTextDescription = findViewById(R.id.et_description);
        cbDoctor = findViewById(R.id.cb_doctor);
        cbService = findViewById(R.id.cb_service);
        cbAppointment = findViewById(R.id.cb_appointment);
        cbOther = findViewById(R.id.cb_other);
        etOtherReclamation = findViewById(R.id.et_other_reclamation);
        btnModifier = findViewById(R.id.btn_submit);

        db = Appdatabase.getInstance(this);
        int reclamationId = getIntent().getIntExtra("RECLAMATION_ID", -1);

        // Récupération de la réclamation
        new Thread(() -> {
            reclamation = db.reclamationDAO().getReclamationById(reclamationId);
            runOnUiThread(() -> {
                if (reclamation != null) {
                    editTextDescription.setText(reclamation.getDescription());
                    // Initialiser les cases à cocher selon le type de réclamation
                    initializeCheckboxes(reclamation.getType());
                } else {
                    Toast.makeText(this, "Réclamation introuvable", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }).start();

        btnModifier.setOnClickListener(view -> updateReclamation());
    }

    private void initializeCheckboxes(String type) {
        cbDoctor.setChecked(type.contains("docteur"));
        cbService.setChecked(type.contains("service"));
        cbAppointment.setChecked(type.contains("rendez-vous"));
        cbOther.setChecked(type.contains("autre"));
        etOtherReclamation.setVisibility(cbOther.isChecked() ? View.VISIBLE : View.GONE);
    }

    private void updateReclamation() {
        String newDescription = editTextDescription.getText().toString().trim();
        StringBuilder newType = new StringBuilder();

        if (cbDoctor.isChecked()) newType.append("Réclamation contre un docteur, ");
        if (cbService.isChecked()) newType.append("Réclamation sur le service médical, ");
        if (cbAppointment.isChecked()) newType.append("Problème de rendez-vous, ");
        if (cbOther.isChecked()) {
            newType.append("Autre: ").append(etOtherReclamation.getText().toString().trim()).append(", ");
        }

        if (newDescription.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer une description", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mise à jour de la réclamation
        reclamation.setDescription(newDescription);
        reclamation.setType(newType.toString());

        new Thread(() -> {
            db.reclamationDAO().updateReclamation(reclamation);
            runOnUiThread(() -> {
                Toast.makeText(this, "Réclamation modifiée avec succès", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ModifierReclamationActivity.this, SuiviReclamationActivity.class));
                finish();
            });
        }).start();
    }
}
