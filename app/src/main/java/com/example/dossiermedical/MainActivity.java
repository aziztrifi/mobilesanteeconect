package com.example.dossiermedical;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DossierAdapter dossierAdapter;
    private DossierDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Charger le fragment initial si c'est le premier lancement
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DossierListFragment())
                    .commit();
        }

        // Configuration du RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DossierDatabaseHelper(this);
        List<Dossier> dossiers = dbHelper.getAllDossiers();
        dossierAdapter = new DossierAdapter(dossiers, this);
        recyclerView.setAdapter(dossierAdapter);

        // Ajouter un dossier avec le FloatingActionButton
        FloatingActionButton addDossierButton = findViewById(R.id.addDossierButton);
        addDossierButton.setOnClickListener(view -> {
            // Remplacer le fragment actuel par le fragment de détail
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ViewDossierFragment())
                    .addToBackStack(null)  // Ajouter la transaction à la pile arrière pour pouvoir revenir
                    .commit();
        });

        // Demande de permission pour accéder au stockage externe si nécessaire
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Mettre à jour la liste des dossiers lorsque l'activité reprend
        dossierAdapter.updateDossiers(dbHelper.getAllDossiers());
    }

    // Méthode pour afficher les détails d'un dossier lorsqu'un élément est cliqué
    public void onDossierItemClick(Dossier dossier) {
        // Remplacer le fragment actuel par un fragment de détails avec les informations du dossier
        Bundle bundle = new Bundle();
        bundle.putParcelable("dossier", dossier);  // Passer le dossier sélectionné au fragment
        ViewDossierFragment detailsFragment = new ViewDossierFragment();
        detailsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, detailsFragment)
                .addToBackStack(null)  // Ajouter à la pile arrière pour permettre un retour
                .commit();
    }
}
