package com.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

// Déclarina classe publique esmha MainActivity accessible mel autres classes
public class MainActivity extends Activity {

    // 3malna déclaration lel les composant graphique w Option 2 SharedPreferences
    private EditText etNomWs;
    private EditText etAnneeNaissanceWs;
    private EditText etAnneeDestinationWs;
    private Spinner spinnerMotifWs;
    private Button btnInspecterWs;
    private Button btnHistoriqueWs;
    private Button btnChatDouanierWs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 3malna liaison bin les variables Java w les éléments XML
        etNomWs = findViewById(R.id.et_nom_ws);
        etAnneeNaissanceWs = findViewById(R.id.et_annee_naissance_ws);
        etAnneeDestinationWs = findViewById(R.id.et_annee_destination_ws);
        spinnerMotifWs = findViewById(R.id.spinner_motif_ws);
        btnInspecterWs = findViewById(R.id.btn_inspecter_ws);
        btnHistoriqueWs = findViewById(R.id.btn_historique_ws);
        btnChatDouanierWs = findViewById(R.id.btn_chat_douanier_ws);

        // Ecouteur d'événement 3al bouton btnInspecterWs
        btnInspecterWs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                traiterInspectionWs();
            }
        });

        // Option 2: Ecouteur d'événement 3al bouton Historique des Sauts (SharedPreferences)
        btnHistoriqueWs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afficherHistoriqueSharedPreferencesWs();
            }
        });

        // Ecouteur d'événement 3al bouton chat bech y7el ChatActivity
        btnChatDouanierWs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentChat = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intentChat);
            }
        });
    }

    // Fonction Option 2: Sauvegarde ta3 l'inspection fel SharedPreferences
    private void enregistrerInSharedPreferencesWs(String nom, int anneeNais, int anneeDest) {
        SharedPreferences prefs = getSharedPreferences("PREFS_SAUTS_TEMPORELS", Context.MODE_PRIVATE);
        String ancienHistorique = prefs.getString("DERNIERS_VOYAGEURS", "");
        ancienHistorique = ancienHistorique.replace("Aucun saut enregistré.", "").trim();
        if (!ancienHistorique.isEmpty()) {
            ancienHistorique = "\n" + ancienHistorique;
        }
        String nouvelEntree = "• " + nom + " (" + anneeNais + " ➔ " + anneeDest + ")";
        prefs.edit().putString("DERNIERS_VOYAGEURS", nouvelEntree + ancienHistorique).apply();
    }

    // Fonction Option 2: Affichage mta3 l'historique enregistré fil SharedPreferences
    private void afficherHistoriqueSharedPreferencesWs() {
        SharedPreferences prefs = getSharedPreferences("PREFS_SAUTS_TEMPORELS", Context.MODE_PRIVATE);
        String historique = prefs.getString("DERNIERS_VOYAGEURS", "Aucun saut enregistré.");
        historique = historique.replace("Aucun saut enregistré.", "").trim();
        if (historique.isEmpty()) {
            historique = "Aucun saut enregistré.";
        }

        new AlertDialog.Builder(this)
                .setTitle("Historique des Sauts Temporels")
                .setMessage(historique)
                .setPositiveButton("Fermer", null)
                .show();
    }

    private void traiterInspectionWs() {
        String nom = etNomWs.getText().toString().trim();
        String anneeNaissanceStr = etAnneeNaissanceWs.getText().toString().trim();
        String anneeDestinationStr = etAnneeDestinationWs.getText().toString().trim();
        String motif = spinnerMotifWs.getSelectedItem().toString();

        if (nom.isEmpty() || nom.length() < 2) {
            etNomWs.setError(getString(R.string.erreur_nom_vide));
            etNomWs.requestFocus();
            Toast.makeText(MainActivity.this, R.string.erreur_nom_vide, Toast.LENGTH_SHORT).show();
            return;
        }

        if (anneeNaissanceStr.isEmpty()) {
            etAnneeNaissanceWs.setError(getString(R.string.erreur_annee_naissance_vide));
            etAnneeNaissanceWs.requestFocus();
            Toast.makeText(MainActivity.this, R.string.erreur_annee_naissance_vide, Toast.LENGTH_SHORT).show();
            return;
        }

        if (anneeDestinationStr.isEmpty()) {
            etAnneeDestinationWs.setError(getString(R.string.erreur_annee_destination_vide));
            etAnneeDestinationWs.requestFocus();
            Toast.makeText(MainActivity.this, R.string.erreur_annee_destination_vide, Toast.LENGTH_SHORT).show();
            return;
        }

        int anneeNaissance;
        int anneeDestination;

        try {
            anneeNaissance = Integer.parseInt(anneeNaissanceStr);
            anneeDestination = Integer.parseInt(anneeDestinationStr);
        } catch (NumberFormatException e) {
            Toast.makeText(MainActivity.this, R.string.erreur_annee_invalide, Toast.LENGTH_SHORT).show();
            return;
        }

        if (anneeDestination < anneeNaissance) {
            etAnneeDestinationWs.setError(getString(R.string.erreur_annee_destination_anterieure));
            etAnneeDestinationWs.requestFocus();
            Toast.makeText(MainActivity.this, R.string.erreur_annee_destination_anterieure, Toast.LENGTH_LONG).show();
            return;
        }

        // Option 2: Enregistrer el voyageur fil SharedPreferences
        enregistrerInSharedPreferencesWs(nom, anneeNaissance, anneeDestination);

        Intent intent = new Intent(MainActivity.this, VerificationActivity.class);
        intent.putExtra(Keys.cleNom(Keys.MATRICULE), nom);
        intent.putExtra(Keys.cleAnneeNaissance(Keys.MATRICULE), anneeNaissance);
        intent.putExtra(Keys.cleAnneeDestination(Keys.MATRICULE), anneeDestination);
        intent.putExtra(Keys.cleMotif(Keys.MATRICULE), motif);
        startActivity(intent);
    }
}
