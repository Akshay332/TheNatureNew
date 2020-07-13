package com.nature.thenature;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.nature.thenature.Adapter.StaggeredRecylerAdapter;
import com.nature.thenature.Model.wallpapers;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class DashBoardActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private FirebaseAuth mAuth;
    private TextView mTitle;
    private FloatingActionButton mFloatingActionButton;
    private FirebaseAuth.AuthStateListener mAthListener;
    private RecyclerView staggeredRv;
    private StaggeredRecylerAdapter adapter;
    private StaggeredGridLayoutManager manager;
    private AdView mAdView;
    private MeowBottomNavigation meowBottomNavigation;
    private final static int ID_DASHBOARD = 1;
    private final static int ID_HEART = 2;
    private final static int ID_CAMERA = 3;
    private final static int ID_INFO = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        meowBottomNavigation = findViewById(R.id.bottom_navigation_bar);
        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_dashboard_black_24dp));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_heart_red));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_camera_enhance_24));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_info_24dp));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
        meowBottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Fragment selected_fragment = null;
                switch (model.getId()) {
                    case ID_DASHBOARD:
                        selected_fragment = new DashboardFragment();
                        mTitle.setText(R.string.explore);
                        mFloatingActionButton.setVisibility(View.VISIBLE);
                        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).commit();

                        break;
                    case ID_HEART:
                        selected_fragment = new Fav_Fragment();
                        mTitle.setText(R.string.favourites);
                        mFloatingActionButton.setVisibility(View.GONE);
                        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Fav_Fragment()).commit();

                        break;
                    case ID_CAMERA:
                        selected_fragment = new CameraFragment();
                        mTitle.setText(R.string.favourites);
                        mFloatingActionButton.setVisibility(View.GONE);
                        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).commit();

                        break;
                    case ID_INFO:
                        selected_fragment = new InfoFragment();
                        mTitle.setText(R.string.info);
                        mFloatingActionButton.setVisibility(View.GONE);
                       // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).commit();

                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected_fragment).commit();
                return Unit.INSTANCE;
            }

        });
        //init recylerview and set layout manager
        staggeredRv = findViewById(R.id.recyclerView);
        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredRv.setLayoutManager(manager);
        //end

        //set Query when data is changed it will notify
        FirebaseRecyclerOptions<wallpapers> options = new FirebaseRecyclerOptions.Builder<wallpapers>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("wallpapers"), wallpapers.class)
                .build();//end


        //set adapter in recyle view
        adapter = new StaggeredRecylerAdapter(options);
        staggeredRv.setAdapter(adapter);
        //end

        //Initialize adds
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //initialize the variable here
        InitVariables();
        //get Title of dashboard
        setToolbarTitle();

        //initailize and assignc variable of bottom navigation
        // initvariablesBottomnavigation();

        // //Get Firebase with instance
         getFirebaseInstances();
        mAthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null){
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(DashBoardActivity.this, SignUpPhoneActivity.class));
                    finish();
                }
            }
        };
        //on click listenre on floating action button
        floatingactionbutonClicklisner();
    }


    private void floatingactionbutonClicklisner() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashBoardActivity.this, SearchViewActivity.class));
                finish();
            }
        });
    }

    private void setToolbarTitle() {
        mTitle.setText(R.string.explore);

    }

    private void InitVariables() {
        mTitle = findViewById(R.id.txtv_title_toolbar);
        mFloatingActionButton = findViewById(R.id.floatingactionbutonSerach);
    }

    private void getFirebaseInstances() {
        mAuth = FirebaseAuth.getInstance();
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        //mAuth.addAuthStateListener(mAthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
//        if(mAthListener!=null){
//            mAuth.removeAuthStateListener(mAthListener);
//        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
