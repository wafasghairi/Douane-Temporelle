package com.example;

// Déclarina classe publique esmha Keys
public final class Keys {

    // Ma 3rafnech chnya matricule card étudiant danc 7atina mta3 identity card
    // Matricule mta3 Wafa Sghairi (1964) w Emna Ben Ali (3499)
    public static final String MATRICULE = "1964";

    private Keys() {}

    //fonction bech ngénériw clé mta3 ism l'voyageur
    public static String cleNom(String matricule) {
        return matricule + "_NOM_VOYAGEUR";
    }

    //fonction bech ngénériw clé mta3 année de naissance ta3 l'voyageur
    public static String cleAnneeNaissance(String matricule) {
        return matricule + "_ANNEE_NAISSANCE";
    }

    //fonction bech ngénériw clé mta3 année destination ta3 l'voyageur
    public static String cleAnneeDestination(String matricule) {
        return matricule + "_ANNEE_DESTINATION";
    }

    //fonction bech ngénériw clé mta3 motif de voyage
    public static String cleMotif(String matricule) {
        return matricule + "_MOTIF_VOYAGE";
    }

    //fonction bech ngénériw clé mta3 verdict final (accepté wala refusé)
    public static String cleVerdict(String matricule) {
        return matricule + "_VERDICT";
    }

    //fonction bech ngénériw clé mta3 l'âge temporel calculé
    public static String cleAgeTemporel(String matricule) {
        return matricule + "_AGE_TEMPOREL";
    }
}
