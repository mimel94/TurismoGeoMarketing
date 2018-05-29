package com.mimel.turismogeomarketing;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mimel.turismogeomarketing.modelos.UserData;

public class EditProfile extends AppCompatActivity {
    Button updateData;
    FirebaseDatabase database;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference refUser;
    EditText name;
    EditText city;
    EditText description;
    EditText email;
    UserData user = new UserData();
    private ImageButton uploadPhoto;
    private StorageReference myStorage;
    private static final int GALERY_INTENT = 1;
    private ProgressDialog myProgressDialog;
    Uri descargarFoto;
    StorageReference httpsReference;

    @Override
    public void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            //mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        updateData = (Button)findViewById(R.id.btnSave);

        city = (EditText) findViewById(R.id.txtCity);
        description = (EditText) findViewById(R.id.txtDescription);
        database = FirebaseDatabase.getInstance();
        refUser = database.getReference(FirebaseReferences.USER);
        myStorage = FirebaseStorage.getInstance().getReference();
        uploadPhoto = (ImageButton)findViewById(R.id.btn_photo);
        myProgressDialog = new ProgressDialog(this);
        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, GALERY_INTENT);
            }
        });
        refUser.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserData.class);
                if(!(user.getCity().isEmpty() || user.getCity() == null)){
                    city.setText(user.getCity());
                }
                if((!user.getDescription().isEmpty() || user.getDescription() == null)){
                    description.setText(user.getDescription());
                }

                if(!(user.getProfilePhotoUrl().isEmpty())){
                    httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(user.getProfilePhotoUrl());
                    Glide.with(EditProfile.this)
                            .using(new FirebaseImageLoader())
                            .load(httpsReference)
                            .into(uploadPhoto);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.print("Error no hay datos en la base de datos! :P"+databaseError.toString());
            }
        });

        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setNombre(mAuth.getCurrentUser().getDisplayName());
                user.setCity(city.getText().toString().trim());
                user.setDescription(description.getText().toString().trim());
                if(!descargarFoto.toString().isEmpty()){
                    user.setProfilePhotoUrl(descargarFoto.toString());
                }
                user.setProfilePhotoUrl(descargarFoto.toString());
                Toast.makeText(EditProfile.this, ""+mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();

                refUser.child(mAuth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()){
                            System.out.println("ERRORR:"+task.getException());
                        }
                    }
                });
                startActivity(new Intent(EditProfile.this, PrincipalUser.class));
                finish();

            }
        });


        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALERY_INTENT && resultCode == RESULT_OK){
            myProgressDialog.setTitle("subiendo...");
            myProgressDialog.setMessage("Subiendo foto de perfil");
            myProgressDialog.setCancelable(false);
            myProgressDialog.show();

            Uri urlPhoto = data.getData();

            StorageReference filePath = myStorage.child("fotos/"+mAuth.getCurrentUser().getUid()+"/profile/"+urlPhoto.
                    getLastPathSegment()+"profile");

            filePath.putFile(urlPhoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    myProgressDialog.dismiss();

                    descargarFoto= taskSnapshot.getDownloadUrl();
                    Glide.with(EditProfile.this).load(descargarFoto).fitCenter()
                            .centerCrop().into(uploadPhoto);

                    Toast.makeText(EditProfile.this, "Se subio correctamente la foto", Toast.LENGTH_SHORT).show();


                }
            });
        }
    }
}
