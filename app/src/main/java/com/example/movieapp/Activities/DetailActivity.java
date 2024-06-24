package com.example.movieapp.Activities;

import android.os.Bundle;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.movieapp.Adapter.ActorList;
import com.example.movieapp.Adapter.GenresAdapter;
import com.example.movieapp.Model.FilmDetails;
import com.example.movieapp.R;
import com.google.gson.Gson;

public class DetailActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ProgressBar progressBarfilm,progressBaractor;
    private TextView title,MovieRatTxt,MovieDescTxt,MovieCastInfo,MovieTime;
   private  RecyclerView.Adapter actorlist,catogorylist;
    private RecyclerView recyclerViewactorlist,recyclerViewcatogorylist;
    private int idfilm;
    private ImageButton back,favorite;
    private NestedScrollView nestedScrollView;
    private ImageView moviesimage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        idfilm=getIntent().getIntExtra("id",1);
        initViews();
        sendrequest();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void sendrequest() {
        mRequestQueue = Volley.newRequestQueue(this);
        progressBarfilm.setVisibility(View.VISIBLE);
        progressBaractor.setVisibility(View.VISIBLE);
       nestedScrollView.setVisibility(View.GONE);
        mStringRequest=new StringRequest(Request.Method.GET, "https://https://moviesapi.ir/api/v1/movies/" + idfilm, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson=new Gson();
                progressBarfilm.setVisibility(View.GONE);
                progressBaractor.setVisibility(View.GONE);
                nestedScrollView.setVisibility(View.VISIBLE);
                FilmDetails filmDetails=gson.fromJson(response, FilmDetails.class);
                Glide.with(DetailActivity.this).load(filmDetails.getPoster()).into(moviesimage);
                title.setText(filmDetails.getTitle());
                MovieRatTxt.setText(filmDetails.getImdbId());
                MovieDescTxt.setText(filmDetails.getPlot());
                MovieCastInfo.setText(filmDetails.getActors());
                if(filmDetails.getImages()!=null){
                    actorlist=new ActorList(filmDetails.getImages());
                    recyclerViewactorlist.setAdapter(actorlist);
                }
                if(filmDetails.getGenres()!=null){
                    catogorylist=new GenresAdapter(filmDetails.getGenres());
                    recyclerViewcatogorylist.setAdapter(catogorylist);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBarfilm.setVisibility(View.GONE);
                progressBaractor.setVisibility(View.GONE);



            }
        });
        mRequestQueue.add(mStringRequest);

    }

    private void initViews() {
        title=findViewById(R.id.textView_moviename);
        progressBarfilm=findViewById(R.id.progressBar6);
        progressBaractor=findViewById(R.id.progressBar5);

        moviesimage=findViewById(R.id.imageViewPic);
        nestedScrollView=findViewById(R.id.ScrollViewdetail);
        MovieRatTxt=findViewById(R.id.textViewstar);
        MovieDescTxt=findViewById(R.id.textView_summary_description);
        MovieTime=findViewById(R.id.textViewtime);
        MovieCastInfo=findViewById(R.id.textView_cast);
        back=findViewById(R.id.imageBnt_back);
        favorite=findViewById(R.id.imagebtn_fav);
        MovieCastInfo=findViewById(R.id.textView_actor);
        recyclerViewactorlist=findViewById(R.id.recyclerView);
        recyclerViewcatogorylist=findViewById(R.id.recyclerView2);
        recyclerViewactorlist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewcatogorylist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        back.setOnClickListener(v -> finish());




    }
}