package com.example.dossiermedical;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DossierAdapter extends RecyclerView.Adapter<DossierAdapter.ViewHolder> {

    private List<Dossier> dossiers;
    private Context context;
    private DossierDatabaseHelper dbHelper;
    ImageButton viewButton, editButton, deleteButton;

    public DossierAdapter(List<Dossier> dossiers, Context context) {
        this.dossiers = dossiers;
        this.context = context;
        this.dbHelper = new DossierDatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dossier, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dossier dossier = dossiers.get(position);

        // Affichage des informations du dossier
        holder.nomTextView.setText(dossier.getNom());
        holder.prenomTextView.setText(dossier.getPrenom());
        holder.dateNaissanceTextView.setText(dossier.getDateNaissance());

        // Afficher l'image depuis byte[]
        if (dossier.getImg() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(dossier.getImg(), 0, dossier.getImg().length);
            holder.dossierImageView.setImageBitmap(bitmap);
        } else {
            holder.dossierImageView.setImageResource(R.drawable.default_image); // Image par défaut
        }

        // Configuration des boutons view, edit et delete
        holder.viewButton.setOnClickListener(v -> {
            // Créer un fragment pour afficher le dossier
            ViewDossierFragment viewFragment = new ViewDossierFragment();
            Bundle args = new Bundle();
            args.putInt("dossierId", (int) dossier.getId());  // Passer l'ID du dossier
            viewFragment.setArguments(args);

            // Remplacer le fragment actuel par le fragment de vue
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, viewFragment)
                    .addToBackStack(null)
                    .commit();
        });

        holder.editButton.setOnClickListener(v -> {
            // Créer un fragment pour éditer le dossier
            EditDossierFragment editFragment = new EditDossierFragment();
            Bundle args = new Bundle();
            args.putInt("dossierId", (int) dossier.getId());  // Passer l'ID du dossier
            editFragment.setArguments(args);

            // Remplacer le fragment actuel par le fragment d'édition
            ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, editFragment)
                    .addToBackStack(null)
                    .commit();
        });

        holder.deleteButton.setOnClickListener(v -> {
            // Demander confirmation avant suppression
            new AlertDialog.Builder(context)
                    .setMessage("Êtes-vous sûr de vouloir supprimer ce dossier ?")
                    .setPositiveButton("Oui", (dialog, which) -> {
                        // Supprimer le dossier de la base de données
                        dbHelper.deleteDossier((int) dossier.getId());  // Suppression du dossier
                        dossiers.remove(position);  // Retirer de la liste
                        notifyItemRemoved(position);  // Actualiser l'adaptateur
                        notifyItemRangeChanged(position, dossiers.size());  // Mettre à jour la vue
                    })
                    .setNegativeButton("Non", null)
                    .show();
        });



    // Configuration des boutons view, edit et delete
//        holder.viewButton.setOnClickListener(v -> {
//            Intent intent = new Intent(context, ViewDossierFragment.class);
//            intent.putExtra("dossierId", dossier.getId());
//            context.startActivity(intent);
//        });

//        holder.editButton.setOnClickListener(v -> {
//            Intent intent = new Intent(context, EditDossierFragment.class);
//            intent.putExtra("dossierId", dossier.getId());
//            context.startActivity(intent);
//        });

//        holder.deleteButton.setOnClickListener(v -> {
//            // Assurez-vous que dossier.getId() est bien converti en int si nécessaire
//            dbHelper.deleteDossier((int) dossier.getId());  // Conversion en int ici
//            dossiers.remove(position);
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, dossiers.size());
//        });
    }

    @Override
    public int getItemCount() {
        return dossiers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomTextView, prenomTextView, dateNaissanceTextView;
        ImageView dossierImageView;
        ImageButton viewButton, editButton, deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            dossierImageView = itemView.findViewById(R.id.dossierImageView);
            nomTextView = itemView.findViewById(R.id.nomTextView);
            prenomTextView = itemView.findViewById(R.id.prenomTextView);
            dateNaissanceTextView = itemView.findViewById(R.id.dateNaissanceTextView);
            viewButton = itemView.findViewById(R.id.viewButton);  // Vérifiez ce bouton
            editButton = itemView.findViewById(R.id.editButton);  // Vérifiez ce bouton
            deleteButton = itemView.findViewById(R.id.deleteButton);  // Vérifiez ce bouton
        }
    }

    // Méthode pour mettre à jour la liste des dossiers et rafraîchir la vue
    public void updateDossiers(List<Dossier> newDossiers) {
        this.dossiers.clear();
        this.dossiers.addAll(newDossiers);
        notifyDataSetChanged();
    }
}
