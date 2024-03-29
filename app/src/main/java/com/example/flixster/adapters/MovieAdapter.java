package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @NotNull
    @Override
    // inflate a layout from XML and return it inside a ViewHolder
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // populate data into ViewHolder. Take data at that position into view at holder
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder" + position);
        // get the movie at the position
        Movie movie = movies.get(position);

        // bind movie data into ViewHolder
        holder.bind(movie);
    }

    // return total count of items in list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            // adds this as itemView's OnClickListener
            itemView.setOnClickListener(this);
        }

        // bind movie data into ViewHolder
        public void bind(Movie movie) {
            String imageUrl;
            int placeholder;

            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            // if phone in landscape then backdrop image, else poster image
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
                placeholder = R.drawable.flicks_backdrop_placeholder;
            }
            else {
                imageUrl = movie.getPosterPath();
                placeholder = R.drawable.flicks_movie_placeholder;
            }

            // for rounded corners on images
            int radius = 20; // corner radius, higher value = more rounded
            int margin = 5; // crop margin, set to 0 for corners with no crop

            Glide.with(context)
                    .load(imageUrl) // actual image
                    .placeholder(placeholder) // loads when the image hasn't loaded yet
                    .centerCrop() // scale image to fill the entire ImageView
                    .transform(new CenterInside(), new RoundedCornersTransformation(radius, margin)) // rounded corners
                    .into(ivPoster); // place image loads

        }

        // When the ViewHolder is clicked, create intent to go to MovieDetailsActivity page
        @Override
        public void onClick(View v) {
            // get item position
            int position = getAdapterPosition();
            // ensure is valid position
            if (position != RecyclerView.NO_POSITION) {
                // get movie at that position
                Movie movie = movies.get(position);
                // create intent for new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize movie using parceler, use its short name as key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show activity
                context.startActivity(intent);
            }
        }
    }
}
