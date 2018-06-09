package com.mimel.turismogeomarketing.adapters;




import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mimel.turismogeomarketing.R;
import com.mimel.turismogeomarketing.SIteActivity;
import com.mimel.turismogeomarketing.modelos.Places;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class SitioAdapter extends RecyclerView.Adapter<SitioAdapter.SitioViewHolder> {

    List<Places> lugares;

    public SitioAdapter( List<Places> lugares){

        this.lugares=lugares ;
    }

    @Override
    public SitioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_site, parent, false);
        SitioViewHolder holder = new SitioViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(SitioViewHolder holder, int position) {

        final Places lugar = lugares.get(position);

        holder.nombre.setText(lugar.getName());
        holder.descripcion.setText(lugar.getDescription());
        holder.metodoPago.setText(lugar.getPaymentMethod());
        holder.lugar.setText(lugar.getCity());
        holder.calificacion.setRating((float) lugar.getScore());
        Picasso.get()
                .load(lugar.getProfilePhotoUrl())
                .resize(350, 300)
                .centerCrop()
                .into(holder.img);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
               bundle.putSerializable("objeto",  lugar);

                v.getContext().startActivity(new Intent(v.getContext(),SIteActivity.class).putExtras(bundle));
            }
        });

    }

    @Override
    public int getItemCount() {
        return lugares.size();
    }

    public static class SitioViewHolder extends RecyclerView.ViewHolder {


        TextView nombre, lugar, metodoPago, descripcion;
        ImageView img;
        RatingBar calificacion;
        public View view;

        public SitioViewHolder(View itemView) {
            super(itemView);

            this.view=itemView;

            nombre = itemView.findViewById(R.id.txtNombreSite);
            lugar= itemView.findViewById(R.id.txtLugarSite);
            metodoPago = itemView.findViewById(R.id.txtPayMethodSite);
            img = itemView.findViewById(R.id.imgSite);
            descripcion=itemView.findViewById(R.id.txtDescriptionSite);
            calificacion= itemView.findViewById(R.id.starsSite);

        }



    }


}
