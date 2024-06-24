package com.example.movieapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;

import java.util.List;

public class ActorList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<String> actor_image;
    Context context;

    public ActorList(List<String> actor_image) {
        this.actor_image = actor_image;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.viewholder_actor, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Glide.with(context).load(actor_image.get(position)).into(((ViewHolder) holder).actorImage);


    }

    @Override
    public int getItemCount() {
        return actor_image.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView actorImage;

        public ViewHolder(View itemView) {
            super(itemView);
            actorImage = itemView.findViewById(R.id.actor_image);
        }
    }
}
