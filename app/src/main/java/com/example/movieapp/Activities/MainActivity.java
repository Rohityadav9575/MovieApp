package com.example.movieapp.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.Adapter.CategoryAdapter;
import com.example.movieapp.Adapter.FlimAdapter;
import com.example.movieapp.Adapter.SliderAdapter;
import com.example.movieapp.Model.FilmList;
import com.example.movieapp.Model.GenersModel;
import com.example.movieapp.Model.SliderModel;
import com.example.movieapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
private List<SliderModel> sliderModelList;
SliderAdapter myAdapter;
private ViewPager2 viewPager2;
private Handler sliderhandler=new Handler();
    private RecyclerView.Adapter adapterBestMovies,adapterUpcomingMovies,adapterCategory;
    private RecyclerView recyclerViewBestMovies,recyclerViewUpcomingMovies,recyclerViewCategory;
    private ProgressBar progressBar,progressBar2,progressBar3;
    private RequestQueue requestQueue;
    private StringRequest stringRequest1,stringRequest2,stringRequest3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        sliderModelList=new ArrayList<>();
        initView();
        banner();
        sendRequestBestMovies();
        sendRequestCategory();
        sendRequestUpcomingMovies();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void sendRequestBestMovies() {
        requestQueue= Volley.newRequestQueue(this);
        progressBar.setVisibility(View.VISIBLE);
        stringRequest1=new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Gson gson=new Gson();
                progressBar.setVisibility(View.GONE);
                FilmList items=gson.fromJson(s, FilmList.class);
                adapterBestMovies=new FlimAdapter(items,getApplicationContext());
                recyclerViewBestMovies.setAdapter(adapterBestMovies);



            }
        } , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar.setVisibility(View.VISIBLE);
                Log.e("error",volleyError.getMessage());

            }
        });
        requestQueue.add(stringRequest1);
    }
    private void sendRequestCategory() {
        requestQueue= Volley.newRequestQueue(this);
        progressBar2.setVisibility(View.VISIBLE);
        stringRequest2=new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/genres", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Gson gson=new Gson();
                progressBar2.setVisibility(View.GONE);
                ArrayList<GenersModel> genersModelArrayList =gson.fromJson(s,new TypeToken<ArrayList<GenersModel>>(){}.getType());
                adapterCategory=new CategoryAdapter(genersModelArrayList,getApplicationContext());
                recyclerViewCategory.setAdapter(adapterCategory);



            }
        } , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {

                progressBar2.setVisibility(View.VISIBLE);
                Log.e("error",volleyError.getMessage());

            }
        });
        requestQueue.add(stringRequest2);
    }
    private void sendRequestUpcomingMovies() {
        requestQueue= Volley.newRequestQueue(this);
        progressBar3.setVisibility(View.VISIBLE);
        stringRequest3=new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=3", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Gson gson=new Gson();
                progressBar3.setVisibility(View.GONE);
                FilmList items=gson.fromJson(s, FilmList.class);
                adapterUpcomingMovies=new FlimAdapter(items,getApplicationContext());
                recyclerViewUpcomingMovies.setAdapter(adapterUpcomingMovies);



            }
        } , new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressBar3.setVisibility(View.VISIBLE);
                Log.e("error",volleyError.getMessage());

            }
        });
        requestQueue.add(stringRequest3);
    }

    private void banner() {

        sliderModelList.add(new SliderModel(R.drawable.wide1));
        sliderModelList.add(new SliderModel(R.drawable.wide));
        sliderModelList.add(new SliderModel(R.drawable.wide3));

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
        viewPager2=findViewById(R.id.viewPager2);
        recyclerViewBestMovies=findViewById(R.id.recyclerViewBestMovies);
        recyclerViewUpcomingMovies=findViewById(R.id.recyclerViewUpcomingMovies);
        recyclerViewCategory=findViewById(R.id.recyclerViewCategory);
        myAdapter=new SliderAdapter(sliderModelList,this,viewPager2);
        viewPager2.setAdapter(myAdapter);
        recyclerViewBestMovies.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewUpcomingMovies.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        recyclerViewBestMovies.setAdapter(adapterBestMovies);
        recyclerViewUpcomingMovies.setAdapter(adapterUpcomingMovies);
        recyclerViewCategory.setAdapter(adapterCategory);

        progressBar=findViewById(R.id.progressBar);
        progressBar2=findViewById(R.id.progressBar2);
        progressBar3=findViewById(R.id.progressBar3);
        

    }
}