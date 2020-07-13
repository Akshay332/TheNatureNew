package com.nature.thenature;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

public class ViewStorageImage extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageView, ivStart;
    private Button download, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_storage_image);
        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        ivStart = findViewById(R.id.iv_toolbar_start);
        ivStart.setImageResource(R.drawable.ic_left_arrow);
        ivStart.setVisibility(View.VISIBLE);
    }
}