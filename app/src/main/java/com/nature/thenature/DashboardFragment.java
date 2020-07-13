package com.nature.thenature;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.nature.thenature.Adapter.StaggeredRecylerAdapter;
import com.nature.thenature.Model.wallpapers;


public class DashboardFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAthListener;
    private RecyclerView staggeredRv;
    private StaggeredRecylerAdapter adapter;
    private StaggeredGridLayoutManager manager;
    private TextView oopsTag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initViews(view);
        mAuth = FirebaseAuth.getInstance();
        mAthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null){
                    // user auth state is changed - user is null
                    // launch login activity
                    getFragmentManager().beginTransaction().replace(R.id.dashboard_fragment,new SignUpFragment()).commit();
                }
            }
        };
        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredRv.setLayoutManager(manager);
        //set Query when data is changed it will notify
        FirebaseRecyclerOptions<wallpapers> options = new FirebaseRecyclerOptions.Builder<wallpapers>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("wallpapers"), wallpapers.class)
                .build();
        //end

        //set adapter in recyle view
        adapter = new StaggeredRecylerAdapter(options);
        staggeredRv.setAdapter(adapter);
        //end

        return view;
    }



    private void initViews(View view) {
        staggeredRv = view.findViewById(R.id.recyclerView);
        oopsTag = view.findViewById(R.id.txtv_error);
    }

    @Override
    public void onStart() {
        super.onStart();
        oopsTag.setVisibility(View.GONE);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}