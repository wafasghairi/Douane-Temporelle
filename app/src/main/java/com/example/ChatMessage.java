package com.example;


// Classe ChatMessage: Modèle de données (POJO) mta3 message wa7id fel chat
// T stocki el contenu mta3 el message, shkoun ba3tho (User wala Bot), w wa9t el ba3then

public class ChatMessage {

    // text mta3 el message (el text eli yodhhor fel bulle)
    private final String texte;

    // Boolean bech nfar9ou bin: true (message mta3 l'utilisateur) w false (message mta3 el Douanier IA)
    private final boolean isUser;

    // L'heure mta3 ba3then el message (format HH:mm)
    private final String heure;

    // Constructeur bech na3mlou initialisation  lel message b les 3 valeurs mta3ou
    public ChatMessage(String texte, boolean isUser, String heure) {
        this.texte = texte;
        this.isUser = isUser;
        this.heure = heure;
    }

    // Getter bech nraj3ou el text mta3 el message
    public String getTexte() {
        return texte;
    }

    // Getter bech nverifiw ken el message mta3 l'utilisateur walla la
    public boolean isUser() {
        return isUser;
    }

    // Getter bech nraj3ou l'heure mta3 el message
    public String getHeure() {
        return heure;
    }
}
