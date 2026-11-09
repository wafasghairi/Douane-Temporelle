package com.example;

// Classe ChatMessage: Modèle de données mta3 les messages mta3 el Chatbot (Hadhi fonctionnalité AI Générée b Gemini w el System Prompt maktoub  README.md)
public class ChatMessage {

    // Contenu mta3 el message texte
    private final String texte;
    // Boolean bech n'différenciw bin el utilisateur (true) w el Chatbot AI (false)
    private final boolean isUser;
    // Horodatage mta3 el message (heure:minute)
    private final String heure;

    // Constructeur mta3 el message
    public ChatMessage(String texte, boolean isUser, String heure) {
        this.texte = texte;
        this.isUser = isUser;
        this.heure = heure;
    }

    public String getTexte() {
        return texte;
    }

    public boolean isUser() {
        return isUser;
    }

    public String getHeure() {
        return heure;
    }
}
