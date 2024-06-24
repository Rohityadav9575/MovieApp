package com.example.movieapp.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
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
import com.example.movieapp.Model.FilmList;
import com.example.movieapp.R;

public class FlimAdapter extends RecyclerView.Adapter<FlimAdapter.ViewHolder> {
   FilmList items;
   Context context;

    public FlimAdapter(FilmList items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.viewholder_film,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int actualposition=holder.getAbsoluteAdapterPosition();
    holder.title.setText(items.getData().get(actualposition).getTitle());
        RequestOptions requestOptions=new RequestOptions();
        requestOptions.transform(new CenterCrop(),new RoundedCorners(30));
        Glide.with(context).load(items.getData().get(actualposition).getPoster()).apply(requestOptions).into(holder.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(context, DetailActivity.class);
               intent.putExtra("id",items.getData().get(actualposition).getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.getData().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.film_title);
            pic=itemView.findViewById(R.id.flim_image);
        }
    }

}
