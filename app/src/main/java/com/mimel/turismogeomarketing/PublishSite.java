package com.mimel.turismogeomarketing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mimel.turismogeomarketing.modelos.Places;

public class PublishSite extends AppCompatActivity {

    Button next ;
    EditText name;
    DatabaseReference refSite;
    EditText city;
    EditText description;
    EditText type;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseDatabase database;
    ImageButton photoSiteButon;
    private static final int GALERY_INTENT = 1;
    private ProgressDialog myProgressDialog;
    StorageReference myStorage;
    Uri descargarFoto;
    Places place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_site);
        place = new Places();
        next = (Button)findViewById(R.id.btnNext);
        mAuth = FirebaseAuth.getInstance();
        name = (EditText)findViewById(R.id.txtName);
        city = (EditText)findViewById(R.id.txtCity);
        description = (EditText)findViewById(R.id.txtDescription);
        type = (EditText)findViewById(R.id.txtType);
        database = FirebaseDatabase.getInstance();
        refSite = database.getReference(FirebaseReferences.PLACES);

        myStorage = FirebaseStorage.getInstance().getReference();
        myProgressDialog = new ProgressDialog(this);
        photoSiteButon = (ImageButton)findViewById(R.id.btn_img_site);

        photoSiteButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, GALERY_INTENT);
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(name.toString().isEmpty() || description.toString().isEmpty() || city.toString().isEmpty()
                        || type.toString().isEmpty() || place.getProfilePhotoUrl() == null){
                    Toast.makeText(getApplicationContext(),"Complete todos los campos",Toast.LENGTH_SHORT).show();
                }else{
                    place.setName(name.getText().toString());
                    place.setCity(city.getText().toString());
                    place.setDescription(description.getText().toString());
                    place.setType(type.getText().toString());

                    Intent i = new Intent(getApplicationContext(), MapSite.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("place",place);
                    i.putExtras(bundle);
                    startActivity(i);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALERY_INTENT && resultCode == RESULT_OK){
            myProgressDialog.setTitle("subiendo...");
            myProgressDialog.setMessage("Subiendo foto del lugar");
            myProgressDialog.setCancelable(false);
            myProgressDialog.show();

            Uri urlPhoto = data.getData();
            //ojo la img se sube al servidor y primero deberia mostrarse, luego si se sube !!arreglarlo!!
            StorageReference filePath = myStorage.child("fotos/"+mAuth.getCurrentUser().getUid()+"/site/"+urlPhoto.
                    getLastPathSegment());

            filePath.putFile(urlPhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    myProgressDialog.dismiss();

                    descargarFoto= taskSnapshot.getDownloadUrl();
                    Glide.with(PublishSite.this).load(descargarFoto).fitCenter()
                            .centerCrop().into(photoSiteButon);

                    Toast.makeText(PublishSite.this, "Se subio correctamente la foto", Toast.LENGTH_SHORT).show();
                    place.setProfilePhotoUrl(descargarFoto.toString());

                }
            });
        }
    }
}
