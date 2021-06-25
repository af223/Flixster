package com.example.flixster;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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
        //setContentView(R.layout.activity_movie_details);

        ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        // assigning the fields using boilerplates
        /*tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);*/
        tvTitle = binding.tvTitle;
        tvOverview = binding.tvOverview;
        rbVoteAverage = binding.rbVoteAverage;
        tvBackdrop = binding.tvBackdrop;

        // unwrap movie passed in from intent, using simple name as key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        // logging to confirm deserialization
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set title and overview from movie
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // convert vote average (0-10) to scale of 0-5
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage / 2.0f);

        // for rounded corners on images
        int radius = 20; // corner radius, higher value = more rounded
        int margin = 5; // crop margin, set to 0 for corners with no crop

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

    public void onTrailerClick(View view) {
        Log.d("TRAILER", "clicked");

        String key = movie.getKey();
        if (!key.equals("")) {
            Intent intent = new Intent(this, MovieTrailerActivity.class);
            // serialize movie using parceler, use its short name as key
            intent.putExtra("moviekey", key);
            Log.d("hello from details", key);
            // show activity
            startActivity(intent);
        }

    }

}