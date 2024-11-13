package com.example.dossiermedical;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class ViewDossierFragment extends Fragment {

    private TextView nomTextView, prenomTextView, dateNaissanceTextView, telephoneTextView, emailTextView, typeSanguinTextView, adresseTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_dossier, container, false);

        // Initialiser les vues
        nomTextView = rootView.findViewById(R.id.nomTextView);

        prenomTextView = rootView.findViewById(R.id.prenomTextView);
        dateNaissanceTextView = rootView.findViewById(R.id.dateNaissanceTextView);
        telephoneTextView = rootView.findViewById(R.id.telephoneTextView);
        emailTextView = rootView.findViewById(R.id.emailTextView);
        typeSanguinTextView = rootView.findViewById(R.id.typeSanguinTextView);
        adresseTextView = rootView.findViewById(R.id.adresseTextView);

        // Charger le dossier à afficher
        Dossier dossier = getArguments().getParcelable("dossier");
        if (dossier != null) {
            nomTextView.setText(dossier.getNom());
            prenomTextView.setText(dossier.getPrenom());
            dateNaissanceTextView.setText(dossier.getDateNaissance());
            telephoneTextView.setText(dossier.getNumeroTelephone());
            emailTextView.setText(dossier.getEmail());
            typeSanguinTextView.setText(dossier.getTypeSanguin());
            adresseTextView.setText(dossier.getAdresse());
        }

        return rootView;
    }
}
