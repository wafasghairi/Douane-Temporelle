package com.example;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

// Adapter ChatAdapter: Afficheur mta3 les bulles de conversation (Fonctionnalité AI Générée b Gemini, el System Prompt fih  README.md)
public class ChatAdapter extends BaseAdapter {

    // Zouz types mta3 bulles: 0 mta3 l'utilisateur w 1 mta3 el Bot IA
    private static final int TYPE_USER = 0;
    private static final int TYPE_BOT = 1;

    private final Context context;
    private final List<ChatMessage> messages;
    private final LayoutInflater inflater;

    // Constructeur mta3 el adapter
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
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2; // Zouz types mta3 layouts (user w bot)
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        return message.isUser() ? TYPE_USER : TYPE_BOT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage message = messages.get(position);
        int type = getItemViewType(position);
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            if (type == TYPE_USER) {
                // Infliwi layout mta3 bulle utilisateur
                convertView = inflater.inflate(R.layout.item_message_user, parent, false);
                holder.tvMessageWs = convertView.findViewById(R.id.tv_message_user_ws);
                holder.tvHeureWs = convertView.findViewById(R.id.tv_heure_user_ws);
            } else {
                // Infliwi layout mta3 bulle mta3 el Douanier IA (AI Generated)
                convertView = inflater.inflate(R.layout.item_message_bot, parent, false);
                holder.tvMessageWs = convertView.findViewById(R.id.tv_message_bot_ws);
                holder.tvHeureWs = convertView.findViewById(R.id.tv_heure_bot_ws);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (holder.tvMessageWs != null) {
            holder.tvMessageWs.setText(message.getTexte());
            // Alignement automatique mta3 el ktaba RTL/LTR (Tunisien/Arabe ou Français/Anglais)
            holder.tvMessageWs.setTextDirection(View.TEXT_DIRECTION_FIRST_STRONG);
        }

        if (holder.tvHeureWs != null) {
            holder.tvHeureWs.setText(message.getHeure());
        }

        return convertView;
    }

    // ViewHolder pattern bech n'optimisiw el performance mta3 el ListView
    private static class ViewHolder {
        TextView tvMessageWs;
        TextView tvHeureWs;
    }
}
