package com.ilies.projet_3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView mGreetingText;
    private Button mPlayButton;
    private AutoCompleteTextView mAutoComplete;
    private Retrofit mRetrofit;
    ArrayList<String> movieList = new ArrayList<String>();
    private MainActivity instance;
    private int mIdMovie = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Main_3", "Starting main JAVA");
        instance = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mGreetingText = (TextView) findViewById(R.id.activity_main_greeting_txt);
        mPlayButton = (Button) findViewById(R.id.activity_main_play_btn);
        mAutoComplete= (AutoCompleteTextView) findViewById(R.id.editText);

        mAutoComplete.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                Log.d("Main_3", "Text changed : " + s);

                JsonPlaceHolderApi jsonPlaceHolderApi = mRetrofit.create(JsonPlaceHolderApi.class);
                Call<MovieSearch> call = jsonPlaceHolderApi.getMovies("85b3c81f1c8c29053141a9028b24c46c", s.toString(), "1");
                Log.d("Main_3", call.request().url().toString());

                call.enqueue(new Callback<MovieSearch>() {
                    @Override
                    public void onResponse(Call<MovieSearch> call, Response<MovieSearch> response) {
                        Log.d("Main_3", "Entering callback");
                        if (!response.isSuccessful()) {
                            Log.d("Main_3", "Code: " + response.code());
                            return;
                        }

                        MovieSearch post = response.body();
                        movieList.clear();

                        for(MovieData data : post.getMovieList()){
                            movieList.add(data.getTitle());
                        }

                        if(post.getMovieList().size() > 0)
                            mIdMovie = post.getMovieList().get(0).getId();

                        ArrayAdapter adapter = new ArrayAdapter(instance,android.R.layout.simple_list_item_1,movieList);
                        mAutoComplete.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<MovieSearch> call, Throwable t) {
                        Log.d("Main_3", t.getMessage());
                    }
                });

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        mPlayButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                if(mIdMovie == 0)
                    return;
                Log.d("Main_3", "Let's Go !");

                Intent intent = new Intent(MainActivity.this, MovieActivity.class);
                intent.putExtra("MOVIE_ID", mIdMovie);
                startActivity(intent);
            }
        });
    }
}
