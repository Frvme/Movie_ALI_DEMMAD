package com.movie_ali_demmad.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.movie_ali_demmad.R;
import com.movie_ali_demmad.adapter.MovieAdapter;
import com.movie_ali_demmad.api.GetMovieDataService;
import com.movie_ali_demmad.api.RetrofitInstance;
import com.movie_ali_demmad.model.Movie;
import com.movie_ali_demmad.model.MoviePageResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String API_KEY = "b16b569ecf551c7a299489f104379675";

    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;


    private Call<MoviePageResult> call;

    private int totalPages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_main);
        movieAdapter = new MovieAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);

        loadPage(1);

    }

    private void loadPage(final int page) {
        GetMovieDataService movieDataService = RetrofitInstance.getRetrofitInstance().create(GetMovieDataService.class);

                call = movieDataService.getTopRatedMovies(page, API_KEY);

        call.enqueue(new Callback<MoviePageResult>() {
            @Override
            public void onResponse(Call<MoviePageResult> call, Response<MoviePageResult> response) {
                Toast.makeText(MainActivity.this, "Succes", Toast.LENGTH_SHORT).show();
                if(page == 1) {
                    movieList = response.body().getMovieResult();
                    totalPages = response.body().getTotalPages();

                    recyclerView.setAdapter(movieAdapter);
                } else {
                    List<Movie> movies = response.body().getMovieResult();
                    for(Movie movie : movies){
                        movieList.add(movie);
                        movieAdapter.notifyItemInserted(movieList.size() - 1);
                    }
                }

            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur", Toast.LENGTH_SHORT).show();
            }
        });
    }

}








