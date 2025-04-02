package com.example.jokesphone;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import androidx.recyclerview.widget.RecyclerView;

import com.example.jokesphone.modelos.Dialplan;

import java.util.List;

// Esta clase conecta la lista de bromas con la vista
public class DialplanAdapter extends RecyclerView.Adapter<DialplanAdapter.ViewHolder> {

    //Guardamos la lista de elementos q vamos a mostrar
    private List<Dialplan> dialplanList;

    public DialplanAdapter(List<Dialplan> dialplanList) {
        this.dialplanList = dialplanList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dialplan, parent, false);
        return new ViewHolder(view);
    }

    // Mostramos los datos en la vista
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Dialplan dialplan = dialplanList.get(position);
        holder.textTitle.setText(dialplan.titulo);
        holder.textDescription.setText(dialplan.desc);
        Glide.with(holder.imageDialplan.getContext())
                .load(dialplan.image_url)
                .into(holder.imageDialplan);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetalleActivity.class);
            intent.putExtra("id", dialplan._id);
            intent.putExtra("titulo", dialplan.titulo);
            holder.itemView.getContext().startActivity(intent);
        });


    }

    // Devuelve el tamaño de la lista
    @Override
    public int getItemCount() {
        return dialplanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textDescription;
        ImageView imageDialplan;

        public ViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDescription);
            imageDialplan = itemView.findViewById(R.id.imageDialplan);
        }
    }
}
