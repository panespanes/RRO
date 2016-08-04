package com.panes.rro;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by panes on 2016/8/4.
 */
public class RROService {
    public static final String API_URL = "https://api.github.com";
    public String ret = null;
    public interface IOnResult{
        void onReceive(String msg);
    }
    public void get(final IOnResult onResult) {
        // Create a very simple REST adapter which points the GitHub API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our GitHub API interface.
        GitHub github = retrofit.create(GitHub.class);

        // Create a call instance for looking up Retrofit contributors.
        Call<List<Contributor>> call = github.contributors("square", "retrofit");

        // Fetch and print a list of the contributors to the library.
//        List<Contributor> contributors = null;
//        try {
//            contributors = call.execute().body();
        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                List<Contributor> contributors = response.body();
                for (Contributor contributor : contributors) {
                    System.out.println(contributor.login + " (" + contributor.contributions + ")");

                    Log.d("RRO", contributor.login + "(" + contributor.contributions + ")");
                    onResult.onReceive(contributor.login);
                }

                Log.d("RRO", "onresponse");
            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {
                Log.d("RRO", "onFail");
            }
        });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for (Contributor contributor : contributors) {
//            System.out.println(contributor.login + " (" + contributor.contributions + ")");
//            Log.d("RRO", contributor.login + "("+contributor.contributions+")");
//        }
    }


    public static class Contributor {
        public  String login;
        public  int contributions;

        public Contributor(String login, int contributions) {
            this.login = login;
            this.contributions = contributions;
        }
    }

    public interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        Call<List<Contributor>> contributors(
                @Path("owner") String owner,
                @Path("repo") String repo);

    }

}