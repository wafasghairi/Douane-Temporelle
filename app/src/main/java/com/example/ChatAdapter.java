package com.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;


// Classe ChatAdapter: BaseAdapter ma3moul bech yaffichi les bulles de conversation fel ListView
// Gère 2 types de vues (User w Bot) et gérer el sens mta3 el ktiba RTL/LTR automatiquement.

public class ChatAdapter extends BaseAdapter {

    // Type 0: Bulle mta3 l'utilisateur (Ymin)
    private static final int TYPE_USER = 0;
    // Type 1: Bulle mta3 el Douanier IA (Ysar)
    private static final int TYPE_BOT = 1;

    private final Context context;
    private final List<ChatMessage> messages;
    private final LayoutInflater inflater;

    // Constructeur mta3 el adapter bech na5dhou el context w list mta3 les messages
    public ChatAdapter(Context context, List<ChatMessage> messages) {
        this.context = context;
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return messages != null ? messages.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        if (messages == null || position < 0 || position >= messages.size()) {
            return null;
        }
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2; // Zouz types mta3 layouts différents (User w Bot)
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = (ChatMessage) getItem(position);
        // Nreturniw TYPE_USER ken el message mta3 l'utilisateur, sinon TYPE_BOT
        return (message != null && message.isUser()) ? TYPE_USER : TYPE_BOT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage message = (ChatMessage) getItem(position);
        int type = getItemViewType(position);
        ViewHolder holder;
        View resultView;

        // Optimisation b ViewHolder pattern bech el ListView tkoun rapide w ma tlagich
        if (convertView == null) {
            holder = new ViewHolder();
            if (type == TYPE_USER) {
                // Inflate el layout XML mta3 l'utilisateur (item_message_user)
                resultView = inflater.inflate(R.layout.item_message_user, parent, false);
                holder.tvMessageWs = resultView.findViewById(R.id.tv_message_user_ws);
                holder.tvHeureWs = resultView.findViewById(R.id.tv_heure_user_ws);
            } else {
                // Inflate el layout XML mta3 el Douanier IA (item_message_bot)
                resultView = inflater.inflate(R.layout.item_message_bot, parent, false);
                holder.tvMessageWs = resultView.findViewById(R.id.tv_message_bot_ws);
                holder.tvHeureWs = resultView.findViewById(R.id.tv_heure_bot_ws);
            }
            resultView.setTag(holder);
        } else {
            resultView = convertView;
            holder = (ViewHolder) resultView.getTag();
        }

        if (message != null) {
            // Naffichiw el text mta3 el message et gerer el sens RTL/LTR lel loghat lkol (Arabe, Derja, Français, Anglais)
            if (holder.tvMessageWs != null) {
                holder.tvMessageWs.setText(message.getTexte());
                holder.tvMessageWs.setTextDirection(View.TEXT_DIRECTION_FIRST_STRONG);
            }

            // Naffichiw l'heure mta3 el message fel coin mta3 el bulle
            if (holder.tvHeureWs != null) {
                holder.tvHeureWs.setText(message.getHeure());
            }

            // Accessibility description for screen readers
            String sender = message.isUser() ? "You" : "AI Douanier";
            resultView.setContentDescription(sender + ", message: " + message.getTexte() + ", at " + message.getHeure());
        }

        return resultView;
    }

    // Classe interne ViewHolder bech nstockiw fiha les références mta3 les TextViews w nevitiw findViewById zeyd
    private static class ViewHolder {
        TextView tvMessageWs;
        TextView tvHeureWs;
    }
}
