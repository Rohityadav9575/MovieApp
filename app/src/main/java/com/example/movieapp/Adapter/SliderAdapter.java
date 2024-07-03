package com.example.movieapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.movieapp.Model.FilmDetails;
import com.example.movieapp.Model.SliderModel;
import com.example.movieapp.R;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private List<SliderModel> sliderModelList;
    private Context context;
    private ViewPager2 viewPager2;

    public SliderAdapter(List<SliderModel> sliderModelList, Context context, ViewPager2 viewPager2) {
        this.sliderModelList = sliderModelList;
        this.context = context;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_item, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        SliderModel sliderModel = sliderModelList.get(position);
        holder.setImage(sliderModel.getImage());

        // Preload next image when reaching the second last item
        if (position == sliderModelList.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderModelList.size();
    }

    // ViewHolder class for each item in the RecyclerView
    public class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView sliderImage;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            sliderImage = itemView.findViewById(R.id.slider_image);
        }

        // Method to set image using Glide library
        void setImage(String imageUrl) {
            RequestOptions options = new RequestOptions()
                    .transform(new CenterCrop(), new RoundedCorners(16));

            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w500" + imageUrl) // Base URL for TMDb images
                    .apply(options)
                    .into(sliderImage);
        }
    }

    // Runnable to handle the circular scrolling of images
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderModelList.addAll(sliderModelList); // Duplicate the list for circular scrolling effect
            notifyDataSetChanged(); // Notify adapter that data has changed
        }
    };
}
