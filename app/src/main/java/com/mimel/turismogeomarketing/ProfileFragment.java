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


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView nameTextView;
    private TextView emailTextView;
    private TextView uidTextView;
    private Button closeSession;
    private ImageView profilePhoto;
    private TextView uriFoto;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_profile, container, false);

        nameTextView = (TextView) rootview.findViewById(R.id.nameTextView);
        //emailTextView = (TextView) rootview.findViewById(R.id.emailTextView);
        //uidTextView = (TextView) rootview.findViewById(R.id.uidTextView);
        //closeSession = (Button) rootview.findViewById(R.id.close_session_btn);
        profilePhoto = (ImageView) rootview.findViewById(R.id.profile_img);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String name = user.getDisplayName();
            String email = user.getEmail();
            final Uri photoUrl = user.getPhotoUrl();
            Glide.with(getActivity())
                    .load(photoUrl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profilePhoto);

            String uid = user.getUid();


            nameTextView.setText(name);
            //emailTextView.setText(email);
            //uidTextView.setText(uid);
        } else {
            goLoginScreen();
        }

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


