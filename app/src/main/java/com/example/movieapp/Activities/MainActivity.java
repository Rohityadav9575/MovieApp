package com.example.movieapp.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Adapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.movieapp.Adapter.SliderAdapter;
import com.example.movieapp.Model.SliderModel;
import com.example.movieapp.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
private List<SliderModel> sliderModelList;
SliderAdapter myAdapter;
private ViewPager2 viewPager2;
private Handler sliderhandler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        sliderModelList=new ArrayList<>();
        addToList();
        initView();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addToList() {
        sliderModelList.add(new SliderModel(R.drawable.wide1));
        sliderModelList.add(new SliderModel(R.drawable.wide));
        sliderModelList.add(new SliderModel(R.drawable.wide3));
    }

    private void initView() {
        viewPager2=findViewById(R.id.viewPager2);
        myAdapter=new SliderAdapter(sliderModelList,this,viewPager2);
        viewPager2.setAdapter(myAdapter);

    }
}