package com.mimel.turismogeomarketing;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.mimel.turismogeomarketing.modelos.UserData;
import com.google.firebase.storage.StorageReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    FirebaseDatabase database =FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    //private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference refUser =database.getReference(FirebaseReferences.USER);
    UserData user = new UserData();

    private StorageReference myStorage;
    StorageReference httpsReference;

    private TextView nameTxt;
    private TextView cityTxt;
    private TextView descriptionTxt;
    private ImageView profilePhoto;
    private ImageButton editButon;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        nameTxt = (TextView) rootview.findViewById(R.id.nameTextView);
        cityTxt = (TextView) rootview.findViewById(R.id.city_txt);
        descriptionTxt = (TextView) rootview.findViewById(R.id.description_txt);
        profilePhoto = (ImageView) rootview.findViewById(R.id.profile_img);
        editButon = (ImageButton) rootview.findViewById(R.id.editBtn);
        editButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditProfile.class));
            }
        });


        refUser.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserData.class);
                nameTxt.setText(mAuth.getCurrentUser().getDisplayName());

                if(user.getCity().isEmpty()){
                    cityTxt.setText("Complete su ciudad");
                }else {
                    cityTxt.setText(user.getCity());
                }
                if(user.getDescription().isEmpty()){
                    descriptionTxt.setText("Añada una descripcion");
                }else {
                    descriptionTxt.setText(user.getDescription());
                }
                if(!(user.getProfilePhotoUrl().isEmpty())){
                    httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl(user.getProfilePhotoUrl());
                    Glide.with(getActivity())
                            .using(new FirebaseImageLoader())
                            .load(httpsReference)
                            .into(profilePhoto);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.print("Error no hay datos en la base de datos! :P"+databaseError.toString());
            }
        });
        mAuth.getCurrentUser();


      /*  closeSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(getView());
            }
        });
*/
        return rootview;
    }
    private void goLoginScreen() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }
}


