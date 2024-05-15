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
import com.example.movieapp.Model.SliderModel;
import com.example.movieapp.R;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private List<SliderModel> sliderModelList;
    private Context context;
    private ViewPager2 viewPager2;


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    public SliderAdapter(List<SliderModel> sliderModelList, Context context, ViewPager2 viewPager2) {
        this.sliderModelList = sliderModelList;
        this.context = context;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       LayoutInflater layoutInflater = LayoutInflater.from(context);
       View view=layoutInflater.inflate(R.layout.slider_item,parent,false);
       return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
      holder.setImage(sliderModelList.get(position));
      if(position==sliderModelList.size()-2){
          viewPager2.post(runnable);

      }
      

    }



    @Override
    public int getItemCount() {
        return sliderModelList.size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder{
        public ImageView sliderimage;
        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            sliderimage=itemView.findViewById(R.id.slider_image);
        }
        void setImage(SliderModel sliderModel){
            RequestOptions options=new RequestOptions();
            options.transform(new CenterCrop(),new RoundedCorners(60));
            Glide.with(context).load(sliderModel.getImage()).apply(options).into(sliderimage);
        }
    }

}
