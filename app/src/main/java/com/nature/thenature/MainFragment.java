package com.nature.thenature;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nature.thenature.Adapter.StaggeredRecylerAdapter;
import com.nature.thenature.Model.wallpapers;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class  MainFragment extends Fragment {

    private FirebaseAuth mAuth;
    private TextView mTitle;
    private FloatingActionButton mFloatingActionButton;
    private AdView mAdView;
    private MeowBottomNavigation meowBottomNavigation;
    private final static int ID_DASHBOARD = 1;
    private final static int ID_HEART = 2;
    private final static int ID_CAMERA = 3;
    private final static int ID_INFO = 4;
    private ConstraintLayout layout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mAuth = FirebaseAuth.getInstance();
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        meowBottomNavigation = view.findViewById(R.id.bottom_navigation_bar);
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_dashboard_black_24dp));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_heart_red));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_camera_enhance_24));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_info_24dp));
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
        meowBottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Fragment selected_fragment = null;
                switch (model.getId()) {
                    case ID_DASHBOARD:
                        selected_fragment = new DashboardFragment();
                        mTitle.setText(R.string.explore);
                        mFloatingActionButton.setVisibility(View.VISIBLE);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).commit();

                        break;
                    case ID_HEART:
                        selected_fragment = new Fav_Fragment();
                        //startActivity(new Intent(getActivity(),FavActivity.class));
                        mTitle.setText(R.string.favourites);
                        mFloatingActionButton.setVisibility(View.GONE);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new Fav_Fragment()).commit();

                        break;
                    case ID_CAMERA:
                        selected_fragment = new CameraFragment();
                        mTitle.setText(R.string.photo_backup);
                        mFloatingActionButton.setVisibility(View.GONE);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).commit();

                        break;
                    case ID_INFO:
                        selected_fragment = new InfoFragment();
                        mTitle.setText(R.string.info);
                        mFloatingActionButton.setVisibility(View.GONE);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).commit();

                        break;

                }
               // getFragmentManager().beginTransaction().add(R.id.fragment_container, selected_fragment).commit();

                return Unit.INSTANCE;
            }

        });



        //Initialize adds
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //initialize the variable here
        InitVariables(view);
        //get Title of dashboard
        setToolbarTitle();
        //on click listenre on floating action button
        floatingactionbutonClicklisner();
        return  view;
    }

    private void floatingactionbutonClicklisner() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchViewActivity.class));

            }
        });
    }

    private void setToolbarTitle() {
        mTitle.setText(R.string.explore);
    }

    private void InitVariables(View view) {
        mTitle = view.findViewById(R.id.txtv_title_toolbar);
        mFloatingActionButton = view.findViewById(R.id.floatingactionbutonSerach);
        layout = view.findViewById(R.id.mainFrag);
    }




}