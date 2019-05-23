package com.ilies.projet_3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieActivity extends Activity {

    private int movieId;

    private TextView mTitle;
    private TextView mOriginalTitle;
    private TextView mOverview;
    private ImageView mImage;
    private Button mButton;
    private Retrofit mRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent intent = getIntent();
        movieId = intent.getIntExtra("MOVIE_ID", 550);

        mOriginalTitle = (TextView) findViewById(R.id.originalTitle2);
        mTitle = (TextView) findViewById(R.id.movieName);
        mOverview = (TextView) findViewById(R.id.overview2);
        mImage = (ImageView) findViewById(R.id.image);
        mButton = (Button) findViewById(R.id.back);

        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonPlaceHolderApi jsonPlaceHolderApi = mRetrofit.create(JsonPlaceHolderApi.class);
        Call<MovieDetails> call = jsonPlaceHolderApi.getMovieDetails(movieId,"85b3c81f1c8c29053141a9028b24c46c");

        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                MovieDetails movieDetails = response.body();
                mOriginalTitle.setText(movieDetails.getOriginal_title());
                mTitle.setText(movieDetails.getTitle());
                mOverview.setText(movieDetails.getOverview());

                new DownloadImageTask(mImage).execute(movieDetails.getPoster_path());
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
                Log.d("Main_3", t.getMessage());
            }
        });

        mButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            Log.d("Main_3", "DownloadImageTask");
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            Log.d("Main_3", "doInBackground");
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            Log.d("Main_3", "onPostExecute");
            bmImage.setImageBitmap(result);
        }
    }
}