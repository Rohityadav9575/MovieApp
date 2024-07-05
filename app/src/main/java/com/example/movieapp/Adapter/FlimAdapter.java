package com.example.movieapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.movieapp.Activities.DetailActivity;
import com.example.movieapp.Activities.MainActivity;
import com.example.movieapp.Activities.TMDbApi;
import com.example.movieapp.Model.FilmDetails;
import com.example.movieapp.Model.FlimList;
import com.example.movieapp.Model.ImageFetch;
import com.example.movieapp.Model.Poster;
import com.example.movieapp.Model.Result;
import com.example.movieapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlimAdapter extends RecyclerView.Adapter<FlimAdapter.ViewHolder> {
    private List<Result> items;
    private Context context;

    private List<Result> flimList;

    public FlimAdapter(List<Result> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.viewholder_film, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Result movie = items.get(position);
        holder.title.setText(movie.getTitle());

        RequestOptions requestOptions = new RequestOptions()
                .transform(new CenterCrop(), new RoundedCorners(30));

        String apiKey = "c72ae3a243a3fbc3ad44bf91dd5d6843";
        String imagesUrl = "https://api.themoviedb.org/3/movie/" + movie.getId() + "/images?api_key=" + apiKey;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TMDbApi tmDbApi = retrofit.create(TMDbApi.class);

        tmDbApi.getMovieImages(movie.getId(), apiKey).enqueue(new Callback<ImageFetch>() {
            @Override
            public void onResponse(Call<ImageFetch> call, Response<ImageFetch> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ImageFetch imageFetch = response.body();
                    List<Poster> posters = imageFetch.getPosters();
                    if (posters != null && !posters.isEmpty()) {
                        String filePath = posters.get(0).getFilePath(); // Example: Get the first backdrop image path
                        String imageUrl = buildImageUrl(filePath);

                        // Use Glide to load the image into ImageView
                        RequestOptions requestOptions = new RequestOptions()
                                .transform(new CenterCrop(), new RoundedCorners(30));

                        Glide.with(context)
                                .load(imageUrl)
                                .apply(requestOptions)
                                .into(holder.pic);
                    } else {
                        Log.e("MainActivity", "No backdrop images found for movie " + movie.getId());
                    }
                } else {
                    Log.e("MainActivity", "Failed to fetch movie images for movie " + movie.getId());
                }
            }

            @Override
            public void onFailure(Call<ImageFetch> call, Throwable t) {
                Log.e("MainActivity", "Error fetching movie images for movie " + movie.getId(), t);
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", movie.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    private String buildImageUrl(String filePath) {
        String baseImageUrl = "https://image.tmdb.org/t/p/w300"; // Adjust size as per your requirement
        return baseImageUrl + filePath;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.film_title);
            pic = itemView.findViewById(R.id.flim_image);
        }
    }
}
