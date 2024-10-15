package com.example.santeconnect.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.santeconnect.R;

public class SuiviReclamationActivity extends AppCompatActivity {

    private TextView tvTitle, tvReclamationDescription1, tvStatus1, tvReclamationDescription2, tvStatus2, tvReclamationDescription3, tvStatus3;
    private Button btnPasserReclamation; // Déclaration du bouton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate le layout XML activity_suivi_reclamation
        setContentView(R.layout.activity_suivi_reclamation);

        // Initialise les TextView
        tvTitle = findViewById(R.id.tv_title);
        tvReclamationDescription1 = findViewById(R.id.tv_reclamation_description_1);
        tvStatus1 = findViewById(R.id.tv_status_1);
        tvReclamationDescription2 = findViewById(R.id.tv_reclamation_description_2);
        tvStatus2 = findViewById(R.id.tv_status_2);
        tvReclamationDescription3 = findViewById(R.id.tv_reclamation_description_3);
        tvStatus3 = findViewById(R.id.tv_status_3);

        // Initialise le bouton pour passer à la soumission d'une réclamation
        btnPasserReclamation = findViewById(R.id.btn_passer_reclamation);

        // Exemple : mettre à jour le texte des réclamations et statuts si nécessaire
        tvReclamationDescription1.setText("Réclamation 1: Non traité");
        tvStatus1.setText("Statut: Non Traité");

        tvReclamationDescription2.setText("Réclamation 2: En cours de traitement");
        tvStatus2.setText("Statut: En cours");

        tvReclamationDescription3.setText("Réclamation 3: Traité");
        tvStatus3.setText("Statut: Traité");

        // Configurer le clic sur le bouton pour passer à l'interface de soumission de réclamation
        btnPasserReclamation.setOnClickListener(view -> {
            Intent intent = new Intent(SuiviReclamationActivity.this, ReclamationActivity.class);
            startActivity(intent);
        });
    }
}
