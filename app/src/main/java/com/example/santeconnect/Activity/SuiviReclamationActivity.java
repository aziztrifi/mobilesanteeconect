package com.example.santeconnect.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.santeconnect.R;
import com.example.santeconnect.db.Appdatabase;
import com.example.santeconnect.entities.Reclamation;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SuiviReclamationActivity extends AppCompatActivity {

    private LinearLayout reclamationContainer;
    private Button btnPasserReclamation;
    private Appdatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suivi_reclamation);

        reclamationContainer = findViewById(R.id.reclamation_container); // Container pour ajouter les réclamations dynamiquement
        btnPasserReclamation = findViewById(R.id.btn_passer_reclamation);

        // Récupérer la base de données
        db = Appdatabase.getAppDatabase(this);

        // Utiliser un thread en arrière-plan pour récupérer les réclamations depuis la base de données
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Reclamation> reclamations = db.reclamationDAO().getAllReclamations();

            // Mettre à jour l'interface utilisateur sur le thread principal
            runOnUiThread(() -> {
                // Ajouter dynamiquement les réclamations récupérées
                for (Reclamation reclamation : reclamations) {
                    addReclamationView(reclamation);
                }
            });
        });

        // Configurer le clic sur le bouton pour passer à l'interface de soumission de réclamation
        btnPasserReclamation.setOnClickListener(view -> {
            Intent intent = new Intent(SuiviReclamationActivity.this, ReclamationActivity.class);
            startActivity(intent);
        });
    }

   /* private void addReclamationView(Reclamation reclamation) {
        // Créer dynamiquement un layout pour chaque réclamation
        LinearLayout reclamationLayout = new LinearLayout(this);
        reclamationLayout.setOrientation(LinearLayout.HORIZONTAL);
        reclamationLayout.setPadding(16, 16, 16, 16);

        // Définir la couleur de fond selon le statut
        switch (reclamation.getStatus()) {
            case "Non traité":
                reclamationLayout.setBackgroundResource(R.drawable.reclamation_background_red);
                break;
            case "En cours":
                reclamationLayout.setBackgroundResource(R.drawable.reclamation_background_orange);
                break;
            case "Traité":
                reclamationLayout.setBackgroundResource(R.drawable.reclamation_background_green);
                break;
        }

        // Créer et ajouter les TextViews pour la description et le statut
        TextView descriptionTextView = new TextView(this);
        descriptionTextView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        descriptionTextView.setText(reclamation.getDescription());
        descriptionTextView.setTextColor(getResources().getColor(android.R.color.white));
        reclamationLayout.addView(descriptionTextView);

        TextView statusTextView = new TextView(this);
        statusTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        statusTextView.setText("Statut: " + reclamation.getStatus());
        statusTextView.setTextColor(getResources().getColor(android.R.color.white));
        reclamationLayout.addView(statusTextView);

        // Créer le bouton de suppression
        Button deleteButton = new Button(this);
        deleteButton.setText("Supprimer");
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Ajouter un listener pour gérer la suppression de la réclamation
        deleteButton.setOnClickListener(v -> {
            // Supprimer la réclamation de la base de données
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                db.reclamationDAO().deleteReclamation(reclamation); // Méthode pour supprimer la réclamation de la base de données

                // Mettre à jour l'interface utilisateur sur le thread principal
                runOnUiThread(() -> {
                    // Retirer la réclamation de l'affichage
                    reclamationContainer.removeView(reclamationLayout);
                });
            });
        });

        // Ajouter le bouton de suppression au layout de la réclamation
        reclamationLayout.addView(deleteButton);

        Button btnModifier = new Button(this);
        btnModifier.setText("Modifier");
        btnModifier.setOnClickListener(view -> {
            Intent intent = new Intent(SuiviReclamationActivity.this, ModifierReclamationActivity.class);
            intent.putExtra("RECLAMATION_ID", reclamation.getId()); // Passer l'ID de la réclamation
            startActivity(intent);
        });
        reclamationLayout.addView(btnModifier);


        // Nouveau bouton de traitement
        Button btnTraiter = new Button(this);
        btnTraiter.setText("Traiter");
        btnTraiter.setOnClickListener(view -> {
            reclamation.setStatus("En cours"); // Mettre à jour le statut

            // Exécuter la mise à jour en arrière-plan
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                db.reclamationDAO().updateReclamation(reclamation); // Méthode de mise à jour dans le DAO
                runOnUiThread(() -> {
                    // Mettre à jour l'affichage du statut
                    statusTextView.setText("Statut: " + reclamation.getStatus());
                    reclamationLayout.setBackgroundResource(R.drawable.reclamation_background_orange);
                });
            });
        });
        reclamationLayout.addView(btnTraiter);






        // Ajouter la réclamation au container principal
        reclamationContainer.addView(reclamationLayout);
    }*/

    private void addReclamationView(Reclamation reclamation) {
        // Créer un layout vertical pour chaque réclamation
        LinearLayout reclamationLayout = new LinearLayout(this);
        reclamationLayout.setOrientation(LinearLayout.VERTICAL);
        reclamationLayout.setPadding(16, 16, 16, 16);
        reclamationLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // Définir la couleur de fond selon le statut
        switch (reclamation.getStatus()) {
            case "Non traité":
                reclamationLayout.setBackgroundResource(R.drawable.reclamation_background_red);
                break;
            case "En cours":
                reclamationLayout.setBackgroundResource(R.drawable.reclamation_background_orange);
                break;
            case "Traité":
                reclamationLayout.setBackgroundResource(R.drawable.reclamation_background_green);
                break;
        }

        // Ajouter un TextView pour la description de la réclamation
        TextView descriptionTextView = new TextView(this);
        descriptionTextView.setText("Description: " + reclamation.getDescription());
        descriptionTextView.setTextColor(getResources().getColor(android.R.color.white));
        descriptionTextView.setTextSize(16);
        reclamationLayout.addView(descriptionTextView);

        TextView typeTextView = new TextView(this);
        typeTextView.setText("Reclamation Type: " + reclamation.getType());
        typeTextView.setTextColor(getResources().getColor(android.R.color.white));
        typeTextView.setTextSize(16);
        reclamationLayout.addView(typeTextView);

        // Ajouter un TextView pour le statut de la réclamation
        TextView statusTextView = new TextView(this);
        statusTextView.setText("Statut: " + reclamation.getStatus());
        statusTextView.setTextColor(getResources().getColor(android.R.color.white));
        statusTextView.setTextSize(14);
        reclamationLayout.addView(statusTextView);

        // Ajouter le bouton de traitement
        Button btnTraiter = new Button(this);
        btnTraiter.setText("Traiter");
        btnTraiter.setOnClickListener(view -> {
            reclamation.setStatus("En cours");

            // Exécuter la mise à jour en arrière-plan
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                db.reclamationDAO().updateReclamation(reclamation);
                runOnUiThread(() -> {
                    statusTextView.setText("Statut: " + reclamation.getStatus());
                    reclamationLayout.setBackgroundResource(R.drawable.reclamation_background_orange);
                });
            });
        });
        reclamationLayout.addView(btnTraiter);

        // Ajouter le bouton de modification
        Button btnModifier = new Button(this);
        btnModifier.setText("Modifier");
        btnModifier.setOnClickListener(view -> {
            Intent intent = new Intent(SuiviReclamationActivity.this, ModifierReclamationActivity.class);
            intent.putExtra("RECLAMATION_ID", reclamation.getId()); // Passer l'ID de la réclamation
            startActivity(intent);
        });
        reclamationLayout.addView(btnModifier);

        // Ajouter le bouton de suppression
        Button deleteButton = new Button(this);
        deleteButton.setText("Supprimer");
        deleteButton.setOnClickListener(v -> {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                db.reclamationDAO().deleteReclamation(reclamation); // Méthode pour supprimer la réclamation

                // Mettre à jour l'interface utilisateur sur le thread principal
                runOnUiThread(() -> {
                    reclamationContainer.removeView(reclamationLayout);
                });
            });
        });
        reclamationLayout.addView(deleteButton);

        // Ajouter de l'espacement entre les réclamations pour améliorer la lisibilité
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 16);
        reclamationLayout.setLayoutParams(params);

        // Ajouter la réclamation au container principal
        reclamationContainer.addView(reclamationLayout);
    }



}
