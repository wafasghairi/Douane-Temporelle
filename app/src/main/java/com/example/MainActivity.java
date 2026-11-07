package com.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

// Déclarina classe publique esmha MainActivity accessible mel autres classes
// w héritina men Activity bech tekhdem comme une activité Android
public class MainActivity extends Activity {

    //3malna déclaration lel les composant graphique
    private EditText etNomWs;
    private EditText etAnneeNaissanceWs;
    private EditText etAnneeDestinationWs;
    private Spinner spinnerMotifWs;
    private Button btnInspecterWs;
    private Button btnChatDouanierWs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//3malna liaison bin les variables déclarées fel Java w les éléments graphiques correspondants fel fichier XML
// en utilisant findViewById() w l'ID mta3 kol élément
        etNomWs = findViewById(R.id.et_nom_ws);
        etAnneeNaissanceWs = findViewById(R.id.et_annee_naissance_ws);
        etAnneeDestinationWs = findViewById(R.id.et_annee_destination_ws);
        spinnerMotifWs = findViewById(R.id.spinner_motif_ws);
        btnInspecterWs = findViewById(R.id.btn_inspecter_ws);
        btnChatDouanierWs = findViewById(R.id.btn_chat_douanier_ws);

//7attina écouteur d'événement 3al bouton btnInspecterWs bech ki l'utilisateur yenzel 3lih
// te5dem méthode onClick() eli t3ayet lel fonction traiterInspectionWs() bech tebda vérification des informations
        btnInspecterWs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                traiterInspectionWs();
            }
        });

        // 7attina écouteur d'événement 3al bouton chat bech y7el ChatActivity
        btnChatDouanierWs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentChat = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intentChat);
            }
        });
    }
//3malna fonction traiterInspectionWs() bech net2akdou men validité des données
// w ta9ra les informations saisies par l'utilisateur
// tna7i les espaces supplémentaires w stockinahom fi variables de type String
    private void traiterInspectionWs() {
        String nom = etNomWs.getText().toString().trim();
        String anneeNaissanceStr = etAnneeNaissanceWs.getText().toString().trim();
        String anneeDestinationStr = etAnneeDestinationWs.getText().toString().trim();
        String motif = spinnerMotifWs.getSelectedItem().toString();
        // Correction bug validation: vérifier en plus que le nom contient au moins 2 caractères
        if (nom.isEmpty() || nom.length() < 2) {
            etNomWs.setError(getString(R.string.erreur_nom_vide));
            etNomWs.requestFocus();
            Toast.makeText(MainActivity.this, R.string.erreur_nom_vide, Toast.LENGTH_SHORT).show();
            return;
        }
//vérifina ken champ année de naissance fera8 w ken fera8 n'affichiw message d'erreur
// nraja3ou le curseur lel champ bech l'utilisateur ysala7ha w nwa9fou l'exécution avec return
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
// 3malna Intent bech nemchiw mel MainActivity lel VerificationActivity
// w b3athna les informations mta3 voyageur avec putExtra() ba3d bdina l'activité jdida avec startActivity().
        Intent intent = new Intent(MainActivity.this, VerificationActivity.class);
        intent.putExtra(Keys.cleNom(Keys.MATRICULE), nom);
        intent.putExtra(Keys.cleAnneeNaissance(Keys.MATRICULE), anneeNaissance);
        intent.putExtra(Keys.cleAnneeDestination(Keys.MATRICULE), anneeDestination);
        intent.putExtra(Keys.cleMotif(Keys.MATRICULE), motif);
        startActivity(intent);
    }
}
