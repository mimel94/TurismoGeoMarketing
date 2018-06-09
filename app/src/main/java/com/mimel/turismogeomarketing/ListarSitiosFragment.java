package com.mimel.turismogeomarketing;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mimel.turismogeomarketing.adapters.SitioAdapter;
import com.mimel.turismogeomarketing.modelos.Places;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListarSitiosFragment extends Fragment {


    RecyclerView rv;
     View rootview;
     List<Places> lugares;

     SitioAdapter sitioAdapter;

    private FirebaseDatabase database;
    private DatabaseReference sitioRef;
    private FirebaseAuth mAuth;

    public ListarSitiosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview= inflater.inflate(R.layout.fragment_listar_sitios, container, false);

        rv = rootview.findViewById(R.id.recyclerViewSites);


        sitioAdapter = new SitioAdapter(lugares);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(sitioAdapter);


        return rootview;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        database = FirebaseDatabase.getInstance();
        sitioRef = database.getReference("places");

        lugares= new ArrayList<>();

        sitioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lugares.removeAll(lugares);

                for (DataSnapshot personas:dataSnapshot.getChildren())
                {

                    for (DataSnapshot publicaciones: personas.getChildren()) {

                        Places lugarPublicado= publicaciones.getValue(Places.class);


                        lugares.add(lugarPublicado);



                    }

                }


                sitioAdapter.notifyDataSetChanged();
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
