package com.example.dossiermedical;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DossierListFragment extends Fragment {

    private RecyclerView recyclerView;
    private DossierAdapter dossierAdapter;
    private DossierDatabaseHelper dbHelper;
    private SearchView searchView;
    private Button buttonAddDossier; // Bouton pour ajouter un dossier

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dossier_list, container, false);

        // Initialiser le RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewDossiers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialiser la SearchView
        searchView = view.findViewById(R.id.searchView);
// Initialiser le bouton pour ajouter un dossier
        buttonAddDossier = view.findViewById(R.id.buttonAddDossier);

        // Configurer le clic sur le bouton pour rediriger vers AddDossierFragment
        buttonAddDossier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remplacer le fragment actuel par AddDossierFragment
                Fragment addDossierFragment = new AddDossierFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, addDossierFragment);
                transaction.addToBackStack(null); // Permet de revenir en arrière
                transaction.commit();
            }
        });
        // Initialiser la base de données et l'adaptateur
        dbHelper = new DossierDatabaseHelper(getContext());
        ArrayList<Dossier> dossiers = dbHelper.getAllDossiers();
        sortDossiersByDateNaissance(dossiers); // Tri initial par date de naissance
        dossierAdapter = new DossierAdapter(dossiers, getContext());

        // Assigner l'adaptateur au RecyclerView
        recyclerView.setAdapter(dossierAdapter);

        // Configure SearchView pour filtrer les dossiers par nom
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterDossiers(newText);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recharger les données à chaque fois que le fragment est affiché
        ArrayList<Dossier> dossiers = dbHelper.getAllDossiers();
        sortDossiersByDateNaissance(dossiers);
        dossierAdapter.updateDossiers(dossiers);
    }

    private void filterDossiers(String nom) {
        ArrayList<Dossier> dossiers = dbHelper.searchDossiersByNom(nom);
        sortDossiersByDateNaissance(dossiers);
        dossierAdapter.updateDossiers(dossiers);
    }

    private void sortDossiersByDateNaissance(ArrayList<Dossier> dossiers) {
        Collections.sort(dossiers, new Comparator<Dossier>() {
            @Override
            public int compare(Dossier d1, Dossier d2) {
                return d1.getDateNaissance().compareTo(d2.getDateNaissance());
            }
        });
    }
}
