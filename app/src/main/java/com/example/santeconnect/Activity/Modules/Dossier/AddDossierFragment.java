package com.example.santeconnect.Activity.Modules.Dossier;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.santeconnect.Activity.Entities.Dossier;
import com.example.santeconnect.R;

import java.util.Calendar;

public class AddDossierFragment extends Fragment {

    private EditText nomEditText, prenomEditText, telephoneEditText, adresseEditText, emailEditText;
    private TextView dateNaissanceTextView;
    private Spinner typeSanguinSpinner;
    private Button selectDateButton, saveButton;
    private ImageView dossierImageView;
    private CheckBox checkBoxHomme, checkBoxFemme;
    private DossierDatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_dossier, container, false);

        // Initialiser les vues
        nomEditText = rootView.findViewById(R.id.nomEditText);
        prenomEditText = rootView.findViewById(R.id.prenomEditText);
        telephoneEditText = rootView.findViewById(R.id.telephoneEditText);
        adresseEditText = rootView.findViewById(R.id.adresseEditText);
        emailEditText = rootView.findViewById(R.id.emailEditText);
        dateNaissanceTextView = rootView.findViewById(R.id.dateNaissanceTextView);
        typeSanguinSpinner = rootView.findViewById(R.id.typeSanguinSpinner);
        selectDateButton = rootView.findViewById(R.id.selectDateButton);
        saveButton = rootView.findViewById(R.id.saveButton);
        dossierImageView = rootView.findViewById(R.id.dossierImageView);
        checkBoxHomme = rootView.findViewById(R.id.checkBoxHomme);
        checkBoxFemme = rootView.findViewById(R.id.checkBoxFemme);
        dbHelper = new DossierDatabaseHelper(getActivity());

        // Initialiser le Spinner avec les types sanguins
        String[] typesSanguins = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, typesSanguins);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSanguinSpinner.setAdapter(adapter);

        // Ouvrir un calendrier pour sélectionner la date
        selectDateButton.setOnClickListener(v -> openDatePicker());

        // Sélectionner une image
        Button selectImageButton = rootView.findViewById(R.id.selectImageButton);
        selectImageButton.setOnClickListener(v -> openImagePicker());

        // Gérer le choix unique entre Homme et Femme
        checkBoxHomme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) checkBoxFemme.setChecked(false);
        });

        checkBoxFemme.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) checkBoxHomme.setChecked(false);
        });

        // Enregistrer le dossier médical
        saveButton.setOnClickListener(v -> saveDossier());

        return rootView;
    }

    // Ouvrir un calendrier pour sélectionner la date de naissance
    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year, month, dayOfMonth) -> dateNaissanceTextView.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    // Ouvrir la galerie pour sélectionner une image
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1); // Code de demande de l'image
    }

    // Gérer l'image sélectionnée
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            dossierImageView.setImageURI(selectedImageUri);  // Affiche l'image dans l'ImageView
        }
    }

    // Sauvegarder un dossier médical dans la base de données
    private void saveDossier() {
        String nom = nomEditText.getText().toString();
        String prenom = prenomEditText.getText().toString();
        String telephone = telephoneEditText.getText().toString();
        String adresse = adresseEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String dateNaissance = dateNaissanceTextView.getText().toString();
        String typeSanguin = typeSanguinSpinner.getSelectedItem().toString();
        String genre = checkBoxHomme.isChecked() ? "Homme" : checkBoxFemme.isChecked() ? "Femme" : "";

        // Créer un objet Dossier
        Dossier dossier = new Dossier();
        dossier.setNom(nom);
        dossier.setPrenom(prenom);
        dossier.setNumeroTelephone(telephone);
        dossier.setAdresse(adresse);
        dossier.setEmail(email);
        dossier.setDateNaissance(dateNaissance);
        dossier.setTypeSanguin(typeSanguin);
        dossier.setGenre(genre); // Stocker le genre sélectionné

        // Insérer le dossier dans la base de données
        dbHelper.addDossier(dossier);

        // Retourner à la liste des dossiers après l'enregistrement
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new DossierListFragment())
                .commit();
    }
}
