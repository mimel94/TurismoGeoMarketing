package com.mimel.turismogeomarketing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mimel.turismogeomarketing.modelos.Places;
import com.squareup.picasso.Picasso;

public class SIteActivity extends AppCompatActivity {

    CardView cv;
    ImageView img;
    TextView txtNombre, txtTipo, txtDescripcion, txtPago, txtCiudad;
    Button btnShare;
RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site);

        img= findViewById(R.id.imgSiteFinal);
        cv=findViewById(R.id.cvFinalSite);
        txtNombre= findViewById(R.id.txtNombreSiteFinal);
        btnShare= findViewById(R.id.shareButton);

       txtTipo= findViewById(R.id.txtTipoSiteFinal);
       txtCiudad= findViewById(R.id.txtLugarSiteFinal);

        txtDescripcion= findViewById(R.id.txtDescriptionSiteFinal);
        txtPago=findViewById(R.id.txtpaySiteFinal);
//       ratingBar.findViewById(R.id.starsSiteFinal);

        cv.setPreventCornerOverlap(false);

        final Places objeto = (Places) getIntent().getSerializableExtra(("objeto"));

        Toast.makeText(this, objeto.getName(), Toast.LENGTH_SHORT).show();

        Picasso.get()
                .load(objeto.getProfilePhotoUrl())
                .fit()
                .centerCrop()
                .into(img);


        txtNombre.append(objeto.getName());
        txtPago.append(objeto.getPaymentMethod());

        txtCiudad.append(objeto.getCity());
        txtTipo.append(objeto.getType());
        //ratingBar.setRating((float) objeto.getScore());
        //System.out.println("Dscripcion: "+objeto.getDescription());
       txtDescripcion.append("hola");


        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, objeto.getName()+ " Es un lugar muy hermoso, mira su ubicación https://maps.google.com/?q="+objeto.getLatitud()+","+objeto.getLongitud());
                startActivity(shareIntent);
            }
        });
    }
}
