package com.example.flixster;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    // movie to display
    Movie movie;

    // view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageButton tvBackdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //without ViewBinding (view boilerplate):
        //setContentView(R.layout.activity_movie_details);

        // ViewBinding:
        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        tvTitle = binding.tvTitle;
        tvOverview = binding.tvOverview;
        rbVoteAverage = binding.rbVoteAverage;
        tvBackdrop = view.findViewById(R.id.tvBackdrop);

        // unwrap movie passed in from intent from movieAdapter, using simple name as key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        // logging to confirm deserialization
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set title and overview from movie
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvOverview.setMovementMethod(new ScrollingMovementMethod()); // text scrolls if too long to fit

        // convert vote average (0-10) to scale of 0-5
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);

        // for rounded corners on images
        int radius = 20; // corner radius, higher value = more rounded
        int margin = 5; // crop margin, set to 0 for corners with no crop

        // add rounded corners to displaying the movie backdrops, needed to adjust for displaying
        // landscape (backdrop) picture in portrait orientation with CenterInside
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // backdrop for movie
            Glide.with(this)
                    .load(movie.getBackdropPath()) // actual image
                    .placeholder(R.drawable.flicks_backdrop_placeholder) // loads when the image hasn't loaded yet
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(tvBackdrop); // place image loads
        }
        else {
            // backdrop for movie
            Glide.with(this)
                    .load(movie.getBackdropPath()) // actual image
                    .placeholder(R.drawable.flicks_backdrop_placeholder) // loads when the image hasn't loaded yet
                    .centerCrop() // scale image to fill the entire ImageView
                    .transform(new CenterInside(), new RoundedCornersTransformation(radius, margin))
                    .into(tvBackdrop); // place image loads
        }
    }

    // when movie backdrop or play button is clicked, this method launches
    // creates new intent to display the trailer from YouTube in a new activity/page
    public void onTrailerClick(View view) {
        Log.d("TRAILER", "clicked");

        String key = movie.getKey();
        if (!key.equals("")) {
            Intent intent = new Intent(this, MovieTrailerActivity.class);
            // storing the key used to retrieve the YouTube trailer with "moviekey"
            intent.putExtra("moviekey", key);
            Log.d("hello from details", key);
            // show activity
            startActivity(intent);
        }

    }

}