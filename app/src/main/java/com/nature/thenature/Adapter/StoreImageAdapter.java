package com.nature.thenature.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.nature.thenature.Model.wallpapers;
import com.nature.thenature.R;
import com.nature.thenature.ViewImageActivity;
import com.nature.thenature.ViewStorageImage;

public class StoreImageAdapter extends FirebaseRecyclerAdapter<wallpapers,StoreImageAdapter.ImageViewHolder> {

    Context mContext;

    public StoreImageAdapter(@NonNull FirebaseRecyclerOptions<wallpapers> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ImageViewHolder holder, int position, @NonNull wallpapers model) {
        final String image_url = model.getImageUrl();
        final String image_key = model.getImage_key();

        holder.getImage(image_url);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(mContext, ViewStorageImage.class);
                intent.putExtra("image_key",image_key);
                intent.putExtra("imageUrl",image_url);
                mContext.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView mImage;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.iv_wallpaper);
        }
        public void getImage(String image_url) {
            Glide.with(mContext)
                    .load(image_url)
                    .placeholder(R.color.colorPinkLight)
                    .into(mImage);
        }
    }
}
