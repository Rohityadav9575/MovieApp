package com.example.movieapp.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.movieapp.Adapter.ActorList;
import com.example.movieapp.Adapter.GenresAdapter;
import com.example.movieapp.Model.FilmDetails;

import com.example.movieapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private ProgressBar progressBarfilm, progressBaractor;
    private TextView title, MovieRatTxt, MovieDescTxt, MovieCastInfo, MovieTime;
    private RecyclerView recyclerViewactorlist, recyclerViewcatogorylist;
    private ImageButton back, favorite;
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

            // Apply window insets listener for system bars
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        private void fetchMovieDetails(int idfilm) {
            TMDbApi tmDbApi = RetrofitClientInstance.getRetrofitInstance().create(TMDbApi.class);
            Call<FilmDetails> call = tmDbApi.getMovieDetail(idfilm, "9127d2fe3bff62b14d19c160fa9e7c11");

            progressBarfilm.setVisibility(View.VISIBLE);
            progressBaractor.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.GONE);

            call.enqueue(new Callback<FilmDetails>() {
                @Override
                public void onResponse(Call<FilmDetails> call, Response<FilmDetails> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        FilmDetails filmDetails = response.body();

                        progressBarfilm.setVisibility(View.GONE);
                        progressBaractor.setVisibility(View.GONE);
                        nestedScrollView.setVisibility(View.VISIBLE);

                        // Bind data to views
                        bindMovieDetails(filmDetails);
                    } else {
                        Log.e("DetailActivity", "Failed to fetch movie details");
                        progressBarfilm.setVisibility(View.GONE);
                        progressBaractor.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<FilmDetails> call, Throwable t) {
                    Log.e("DetailActivity", "Error fetching movie details", t);
                    progressBarfilm.setVisibility(View.GONE);
                    progressBaractor.setVisibility(View.GONE);
                }
            });
        }

        private void bindMovieDetails(FilmDetails filmDetails) {
            // Load image using Glide
            Glide.with(this).load(filmDetails.getPosterPath()).into(moviesimage);

            // Set text data
            title.setText(filmDetails.getTitle());
            MovieRatTxt.setText(filmDetails.getImdbId());
            MovieDescTxt.setText(filmDetails.getOverview());
           // MovieCastInfo.setText(filmDetails.getActors());

            // Bind adapter to RecyclerViews if data is available



        }

    private void initViews() {
        title = findViewById(R.id.textView_moviename);
        progressBarfilm = findViewById(R.id.progressBar6);
        progressBaractor = findViewById(R.id.progressBar5);
        moviesimage = findViewById(R.id.imageViewPic);
        nestedScrollView = findViewById(R.id.ScrollViewdetail);
        MovieRatTxt = findViewById(R.id.textViewstar);
        MovieDescTxt = findViewById(R.id.textView_summary_description);
        MovieTime = findViewById(R.id.textViewtime);
        MovieCastInfo = findViewById(R.id.textView_cast);
        back = findViewById(R.id.imageBnt_back);
        favorite = findViewById(R.id.imagebtn_fav);
        recyclerViewactorlist = findViewById(R.id.recyclerView);
        recyclerViewcatogorylist = findViewById(R.id.recyclerView2);

        recyclerViewactorlist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewcatogorylist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        back.setOnClickListener(v -> finish());
    }
}
