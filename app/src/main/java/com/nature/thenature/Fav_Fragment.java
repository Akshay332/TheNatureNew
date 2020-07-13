package com.nature.thenature;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nature.thenature.Adapter.StaggeredRecyclerAdapterLikesImages;
import com.nature.thenature.Model.Likes;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class Fav_Fragment extends Fragment {

    private FirebaseAuth mAuth;
    private FloatingActionButton mFloatingActionButtonUploaad, mFloatingActionButtonPicImage;
    private Uri filepathImg;
    private StorageReference mStorageRef;
    private FirebaseStorage mStorage;
    private RecyclerView staggeredRv;
    private StaggeredRecyclerAdapterLikesImages adapter;
    private StaggeredGridLayoutManager manager;
    private TextView mfavTag;
    private String uid;
    private AdView mAdView;
    private Spinner mspinner;
    private List<String> list;
    private ArrayAdapter<String> dataAdapter;
    private String spinner_items;
    private ProgressBar progressBar;
    private static final int STORAGE_PERMISSION_CODE = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fav_, container, false);
        mAuth = FirebaseAuth.getInstance();
        //get current user
        final FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();

        initViews(view);
        spinnerITems();

        initAdds();



        //set Query when data is changed it will notify
        FirebaseRecyclerOptions<Likes> option = new FirebaseRecyclerOptions.Builder<Likes>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Likes").child(uid), Likes.class)
                .build();//end

        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredRv.setLayoutManager(manager);
        //set adapter in recyle view
        adapter = new StaggeredRecyclerAdapterLikesImages(option);
        staggeredRv.setAdapter(adapter);
        //end
        //choose image
        mFloatingActionButtonPicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
            }
        });
        //upload Image
        mFloatingActionButtonUploaad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mspinner.getSelectedItem().equals("Choose Category")) {
                    Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), "Please select one category from the list.", Snackbar.LENGTH_LONG).show();
                } else {
                    UploadImages();
                }

            }
        });
        return view;
    }

    private void checkPermission(String writeExternalStorage, int storagePermissionCode) {
        if (ContextCompat.checkSelfPermission(getContext(), writeExternalStorage)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] { writeExternalStorage },
                    storagePermissionCode);
        }
        else {
            chooseImage();
        }
    }


    private void initAdds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void spinnerITems() {
        list = new ArrayList<>();
        list.add(0, "Choose Category");
        list.add("Animals");
        list.add("Food");
        list.add("Movies");
        list.add("Nature");
        list.add("Celebrities");
        list.add("Army");
        list.add("Brands");
        list.add("Bikes & Motorcycle");
        list.add("Cars");
        list.add("Cartoon");
        list.add("Landscapes");
        list.add("Planes");
        list.add("Sports");
        list.add("Models");
        list.add("Others");

        dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspinner.setAdapter(dataAdapter);
        mspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (parent.getItemAtPosition(position).equals("Choose Category")) {
                    //do nothing
                } else {
                    spinner_items = parent.getItemAtPosition(position).toString();
                    Log.e("Spinner_Items", spinner_items);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepathImg = data.getData();
            mFloatingActionButtonUploaad.setVisibility(View.VISIBLE);
            mspinner.setVisibility(View.VISIBLE);
            mspinner.performClick();


        }
    }

    private void UploadImages() {
        // Using timestamp to save image
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String str = timestamp.toString();
        String image_suffix = str.trim();
        String image_extn = ".jpg";

        String imagename = image_suffix + image_extn;

        if (filepathImg != null) {
            progressBar.setVisibility(View.VISIBLE);


            final StorageReference reference = mStorageRef.child("Wallpaper").child(imagename);
            reference.putFile(filepathImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference imageDatabase = FirebaseDatabase.getInstance().getReference().child("wallpapers");
                            DatabaseReference new_ref = imageDatabase.push();
                            String key = new_ref.getKey();

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("imageUrl", String.valueOf(uri));
                            hashMap.put("image_key", key);
                            hashMap.put("SearchWallpaper", spinner_items);
                            new_ref.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    mFloatingActionButtonUploaad.setVisibility(View.GONE);
                                }
                            });
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Failed to upload!", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(getContext(), "Please add image before uploading!", Toast.LENGTH_SHORT).show();
        }
    }




    private void initViews(View view) {
        mspinner = view.findViewById(R.id.spinner);
        staggeredRv = view.findViewById(R.id.recyclerView);
        mAdView = view.findViewById(R.id.adView);
        progressBar = view.findViewById(R.id.loading);
        mFloatingActionButtonPicImage = view.findViewById(R.id.main_activity_floatingbtnAdd);
        mFloatingActionButtonUploaad = view.findViewById(R.id.main_activity_floatingbtn);
        mfavTag = view.findViewById(R.id.txtv_noFav);
        mFloatingActionButtonUploaad.setVisibility(View.GONE);
        mspinner.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
//        mFloatingActionButtonUploaad.setVisibility(View.GONE);
//        mspinner.setVisibility(View.GONE);
        adapter.startListening();
        final DatabaseReference like_ref = FirebaseDatabase.getInstance().getReference().child("Likes").child(uid);
        like_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {

                    mfavTag.setVisibility(View.VISIBLE);

                } else {

                    mfavTag.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.startListening();
    }


}