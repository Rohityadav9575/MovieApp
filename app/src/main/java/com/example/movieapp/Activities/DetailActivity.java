package com.example.movieapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.movieapp.Model.FilmDetails;
import com.example.movieapp.Model.VideoKey;
import com.example.movieapp.Model.VideoModel;
import com.example.movieapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private TextView title, MovieRatTxt, MovieDescTxt, MovieTime,popularity,relaesedate;
    private ImageButton back, favorite;
    private Button trailer;
    private NestedScrollView nestedScrollView;
    private ImageView moviesimage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        // Initialize views
        initViews();

        // Get movie ID from intent
        int idfilm = getIntent().getIntExtra("id", 1);

        // Make API call to fetch movie details
        fetchMovieDetails(idfilm);
        fetchTrailer(idfilm);


        // Apply window insets listener for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchTrailer(int idfilm) {
        TMDbApi tmDbApi = RetrofitClientInstance.getRetrofitInstance().create(TMDbApi.class);
        Call<VideoModel> call = tmDbApi.getMovieTrailerKey(idfilm, "c72ae3a243a3fbc3ad44bf91dd5d6843");
        call.enqueue(new Callback<VideoModel>() {
            @Override
            public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    VideoModel videoModel = response.body();
                    List<VideoKey> videoKeys = videoModel.getResults();
                    if (!videoKeys.isEmpty()) {
                        String key = videoKeys.get(0).getKey();
                        trailer.setOnClickListener(v -> {
                            String url = "https://www.youtube.com/watch?v=" + key;
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                        });
                    } else {
                        Log.e("DetailActivity", "No video keys available");
                    }
                } else {
                    Log.e("DetailActivity", "Response unsuccessful or video model is null");
                }
            }

            @Override
            public void onFailure(Call<VideoModel> call, Throwable t) {
                Log.e("DetailActivity", "Error fetching movie details", t);
            }
        });
    }
    private void fetchMovieDetails(int idfilm) {
        TMDbApi tmDbApi = RetrofitClientInstance.getRetrofitInstance().create(TMDbApi.class);
        Call<FilmDetails> call = tmDbApi.getMovieDetail(idfilm, "c72ae3a243a3fbc3ad44bf91dd5d6843");


        nestedScrollView.setVisibility(View.GONE);

        call.enqueue(new Callback<FilmDetails>() {
            @Override
            public void onResponse(Call<FilmDetails> call, Response<FilmDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FilmDetails filmDetails = response.body();
                    String filePath = filmDetails.getPosterPath(); // Example: Get the first backdrop image path
                    String imageUrl = buildImageUrl(filePath);
                    RequestOptions requestOptions = new RequestOptions()
                            .transform(new CenterCrop(), new RoundedCorners(30));

                    Glide.with(getApplicationContext())
                            .load(imageUrl)
                            .apply(requestOptions)
                            .into(moviesimage);


                    nestedScrollView.setVisibility(View.VISIBLE);

                    // Bind data to views
                    bindMovieDetails(filmDetails);
                } else {
                    Log.e("DetailActivity", "Failed to fetch movie details");

                }
            }

            @Override
            public void onFailure(Call<FilmDetails> call, Throwable t) {
                Log.e("DetailActivity", "Error fetching movie details", t);

            }
        });
    }



        private void bindMovieDetails(FilmDetails filmDetails) {
        // Set text data

        title.setText(filmDetails.getTitle());
        MovieRatTxt.setText(String.valueOf(filmDetails.getVoteAverage()));
        MovieDescTxt.setText(filmDetails.getOverview());
        MovieTime.setText(String.format("%d min", filmDetails.getRuntime()));
        popularity.setText("Popularity  "+String.valueOf(filmDetails.getPopularity()));
        relaesedate.setText("Release Date  "+filmDetails.getReleaseDate());


    }
        private String buildImageUrl(String filePath) {
            String baseImageUrl = "https://image.tmdb.org/t/p/w780"; // Adjust size as per your requirement
            return baseImageUrl + filePath;
        }


    private void initViews() {
        title = findViewById(R.id.textView_moviename);
        moviesimage = findViewById(R.id.imageViewPic);
        nestedScrollView = findViewById(R.id.ScrollViewdetail);
        MovieRatTxt = findViewById(R.id.textViewstar);
        MovieDescTxt = findViewById(R.id.textView_summary_description);
        MovieTime = findViewById(R.id.textViewtime);
        popularity = findViewById(R.id.popularity_txt);
        relaesedate=findViewById(R.id.releasedate_txt);
        back = findViewById(R.id.imageBnt_back);
        favorite = findViewById(R.id.imagebtn_fav);
        trailer=findViewById(R.id.trailer_button);

        back.setOnClickListener(v -> finish());
    }
}