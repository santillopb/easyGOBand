package com.androidstudio.easyGOBand;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class AdaptadorTitulares extends ArrayAdapter<Provider> {

    public AdaptadorTitulares(Context context, ArrayList<Provider> datos) {
        super(context, R.layout.listitem_titular, datos);
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View item = convertView;
        final ViewHolder holder;

        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.listitem_titular, null);

            holder = new ViewHolder();
            holder.titulo = (TextView) item.findViewById(R.id.LblTitulo);
            holder.subtitulo = (TextView) item.findViewById(R.id.LblSubTitulo);

            item.setTag(holder);
        } else {
            holder = (ViewHolder) item.getTag();
        }

        holder.titulo.setText(getItem(position).getId() + " - " + getItem(position).getName());
        List<Session> sessions = getItem(position).getSessions();
        String cad = "";
        for (Session session: sessions) {
            cad += session.getId() + " - " + session.getName() + "\n";
        }
        holder.subtitulo.setText(cad);


        return (item);
    }
}