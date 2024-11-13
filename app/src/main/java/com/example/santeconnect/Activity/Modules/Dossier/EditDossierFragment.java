package com.example.santeconnect.Activity.Modules.Dossier;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.santeconnect.Activity.Entities.Dossier;
import com.example.santeconnect.R;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class EditDossierFragment extends Fragment {

    private EditText nomEditText, prenomEditText, telephoneEditText, adresseEditText, emailEditText;
    private TextView dateNaissanceTextView;
    private Spinner typeSanguinSpinner;
    private Button selectDateButton, saveButton, selectImageButton;
    private ImageView dossierImageView;
    private DossierDatabaseHelper dbHelper;
    private Dossier dossierToEdit; // Dossier à éditer
    private Bitmap selectedImageBitmap; // Pour stocker l'image sélectionnée

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_dossier, container, false);

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
        selectImageButton = rootView.findViewById(R.id.selectImageButton);
        dossierImageView = rootView.findViewById(R.id.dossierImageView);
        dbHelper = new DossierDatabaseHelper(getActivity());

        // Initialiser le Spinner avec les types sanguins
        String[] typesSanguins = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, typesSanguins);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSanguinSpinner.setAdapter(adapter);

        // Récupérer le dossier à modifier (via les arguments du fragment)
        if (getArguments() != null) {
            long dossierId = getArguments().getLong("dossierId");
            dossierToEdit = dbHelper.getDossierById((int) dossierId);
            if (dossierToEdit != null) {
                populateFieldsWithDossierData();
            }
        }

        // Ouvrir un calendrier pour sélectionner la date
        selectDateButton.setOnClickListener(v -> openDatePicker());

        // Sélectionner une image
        selectImageButton.setOnClickListener(v -> openImagePicker());

        // Enregistrer les modifications du dossier médical
        saveButton.setOnClickListener(v -> saveDossier());

        return rootView;
    }

    // Ouvrir un calendrier pour sélectionner la date de naissance
    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireActivity(),
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                // Convertir l'URI en Bitmap
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                dossierImageView.setImageBitmap(selectedImageBitmap);  // Affiche l'image dans l'ImageView
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Sauvegarder les modifications du dossier médical
    private void saveDossier() {
        if (dossierToEdit == null) {
            return;
        }

        String nom = nomEditText.getText().toString();
        String prenom = prenomEditText.getText().toString();
        String telephone = telephoneEditText.getText().toString();
        String adresse = adresseEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String dateNaissance = dateNaissanceTextView.getText().toString();
        String typeSanguin = typeSanguinSpinner.getSelectedItem().toString();

        // Convertir l'image sélectionnée en tableau de bytes
        byte[] imageBytes = null;
        if (selectedImageBitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            imageBytes = baos.toByteArray();
        }

        // Mettre à jour les données du dossier à modifier
        dossierToEdit.setNom(nom);
        dossierToEdit.setPrenom(prenom);
        dossierToEdit.setNumeroTelephone(telephone);
        dossierToEdit.setAdresse(adresse);
        dossierToEdit.setEmail(email);
        dossierToEdit.setDateNaissance(dateNaissance);
        dossierToEdit.setTypeSanguin(typeSanguin);
        dossierToEdit.setImg(imageBytes); // Mettre l'image convertie dans le dossier

        // Insérer les modifications dans la base de données
        dbHelper.updateDossier(dossierToEdit);

        // Retourner à la liste des dossiers après l'enregistrement
        requireFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new DossierListFragment())
                .commit();
    }

    // Remplir les champs avec les données du dossier existant
    private void populateFieldsWithDossierData() {
        if (dossierToEdit == null) {
            return;
        }

        nomEditText.setText(dossierToEdit.getNom());
        prenomEditText.setText(dossierToEdit.getPrenom());
        telephoneEditText.setText(dossierToEdit.getNumeroTelephone());
        adresseEditText.setText(dossierToEdit.getAdresse());
        emailEditText.setText(dossierToEdit.getEmail());
        dateNaissanceTextView.setText(dossierToEdit.getDateNaissance());
        typeSanguinSpinner.setSelection(((ArrayAdapter<String>) typeSanguinSpinner.getAdapter()).getPosition(dossierToEdit.getTypeSanguin()));

        // Si une image existe, l'afficher
        if (dossierToEdit.getImg() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(dossierToEdit.getImg(), 0, dossierToEdit.getImg().length);
            dossierImageView.setImageBitmap(bitmap);
        }
    }
}
