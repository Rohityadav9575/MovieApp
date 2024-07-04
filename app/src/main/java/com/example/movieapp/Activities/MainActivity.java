package com.example.movieapp.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.movieapp.Adapter.CategoryAdapter;
import com.example.movieapp.Adapter.FlimAdapter;
import com.example.movieapp.Adapter.GenresAdapter;
import com.example.movieapp.Adapter.SliderAdapter;
import com.example.movieapp.Model.FilmDetails;
import com.example.movieapp.Model.FlimList;
import com.example.movieapp.Model.ImageFetch;
import com.example.movieapp.Model.Poster;
import com.example.movieapp.Model.Result;

import com.example.movieapp.Model.SliderModel;
import com.example.movieapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private List<SliderModel> sliderModelList;
    private ViewPager2 viewPager2;
    private RecyclerView recyclerViewBestMovies, recyclerViewUpcomingMovies, recyclerViewCategory;
    private ProgressBar progressBar, progressBar2, progressBar3;
    private GenresAdapter genresAdapter;
    private TMDbApi tmDbApi;
    private Handler sliderhandler=new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sliderModelList = new ArrayList<>();
        initView();
        setupRetrofit();
        banner();
        fetchBestMovies();
        genresAdapter = new GenresAdapter(new ArrayList<>()); // Properly initialize genresAdapter
        recyclerViewCategory.setAdapter(genresAdapter); // Set the adapter to recyclerViewCategory
        fetchCategories();
        fetchUpcomingMovies();
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        tmDbApi = retrofit.create(TMDbApi.class);
    }

    private void fetchBestMovies() {
        progressBar.setVisibility(View.VISIBLE);
        tmDbApi.getBestMovies("c72ae3a243a3fbc3ad44bf91dd5d6843", 1).enqueue(new Callback<FlimList>() {
            @Override
            public void onResponse(@NonNull Call<FlimList> call, @NonNull Response<FlimList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FlimList result = response.body();
                    List<Result> movies = result.getResults();
                    FlimAdapter adapter = new FlimAdapter(movies, MainActivity.this);
                    recyclerViewBestMovies.setAdapter(adapter);
                } else {
                    Log.e("MainActivity", "Failed to get best movies");
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<FlimList> call, @NonNull Throwable t) {
                Log.e("MainActivity", "Error fetching popular movies", t);
                progressBar.setVisibility(View.GONE);
            }
        });
    }
 private void fetchCategories() {
     progressBar2.setVisibility(View.VISIBLE);
     tmDbApi.getCategories("c72ae3a243a3fbc3ad44bf91dd5d6843").enqueue(new Callback<GenresResponse>() {
         @Override
         public void onResponse(@NonNull Call<GenresResponse> call, @NonNull Response<GenresResponse> response) {
             if (response.isSuccessful() && response.body() != null) {
                 genresAdapter.updateGenres(response.body().getGenres());
             } else {
                 Log.e("MainActivity", "Failed to get genres");
             }
             progressBar2.setVisibility(View.GONE);
         }

            @Override
            public void onFailure(@NonNull Call<GenresResponse> call, @NonNull Throwable t) {
                Log.e("MainActivity", "Error fetching genres", t);
                progressBar2.setVisibility(View.GONE);
            }
        });
    }

    private void fetchUpcomingMovies() {
        progressBar3.setVisibility(View.VISIBLE);
        tmDbApi.getUpcomingMovies("c72ae3a243a3fbc3ad44bf91dd5d6843", 3).enqueue(new Callback<FlimList>() {
            @Override
            public void onResponse(@NonNull Call<FlimList> call, @NonNull Response<FlimList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FlimList result = response.body();
                    List<Result> movies = result.getResults();
                    FlimAdapter adapter = new FlimAdapter(movies, MainActivity.this);
                    recyclerViewUpcomingMovies.setAdapter(adapter);
                } else {
                    Log.e("MainActivity", "Failed to get upcoming movies");
                }
                progressBar3.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<FlimList> call, @NonNull Throwable t) {
                Log.e("MainActivity", "Error fetching upcoming movies", t);
                progressBar3.setVisibility(View.GONE);
            }
        });
    }


    private void banner() {
        sliderModelList.add(new SliderModel(R.drawable.wide1));
        sliderModelList.add(new SliderModel(R.drawable.wide));
        sliderModelList.add(new SliderModel(R.drawable.wide3));
        sliderModelList.add(new SliderModel(R.drawable.slider1));
        sliderModelList.add(new SliderModel(R.drawable.slider2));
        sliderModelList.add(new SliderModel(R.drawable.slider3));

        viewPager2.setAdapter(new SliderAdapter(sliderModelList,getApplicationContext(),viewPager2));
        viewPager2.setClipToPadding(true);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer=new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);

            }


        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderhandler.removeCallbacks(sliderRunnable);
            }
        });
    }
    private  Runnable sliderRunnable=new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderhandler.removeCallbacks(sliderRunnable);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sliderhandler.postDelayed(sliderRunnable,2000);
    }

    private void initView() {
        viewPager2 = findViewById(R.id.viewPager2);
        recyclerViewBestMovies = findViewById(R.id.recyclerViewBestMovies);
        recyclerViewUpcomingMovies = findViewById(R.id.recyclerViewUpcomingMovies);
        recyclerViewCategory = findViewById(R.id.recyclerViewCategory);
        recyclerViewBestMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewUpcomingMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        progressBar = findViewById(R.id.progressBar);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar3 = findViewById(R.id.progressBar3);
    }
}
