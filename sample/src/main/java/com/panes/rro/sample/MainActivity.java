package com.panes.rro.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.panes.rro.RRO;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static String API_URL = "https://api.github.com";

    TextView tv = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv = (TextView) findViewById(R.id.tv);
        Log.i("RRO", "v2 = " + tv.toString());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // 不通过RxJava直接得到回调
            tv.setText("");
            Snackbar.make(findViewById(R.id.fab), "RRO requesting ...", Snackbar.LENGTH_LONG).setAction("delete", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("RRO", "fab delete");
                }
            }).show();
            request();
            Log.d("RRO", "executed");
        } else if (id == R.id.nav_gallery) {
            // 观察者得到回调
            tv.setText("");
            Snackbar.make(findViewById(R.id.fab), "Rx requesting...", Snackbar.LENGTH_LONG).show();
            rxRequest();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeTxt(HashMap<String, String> hashMap) {
        StringBuilder sb;
        for (String key : hashMap.keySet()) {
            sb = new StringBuilder(key + hashMap.get(key));
            System.out.println(key + hashMap.get(key));
            Log.d("RRO", sb.toString());
            tv.setText(tv.getText() + "\n" + sb.toString());
        }
    }

    public void request() {
        GitHub github = RRO.getApiService(GitHub.class, API_URL);

        Call<HashMap<String, String>> call = github.index();
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                changeTxt(response.body());
                Log.d("RRO", "onresponse");
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Log.d("RRO", "onFail: " + t.getMessage());
            }
        });
    }


    public interface GitHub {
        @GET("/")
        Call<HashMap<String, String>> index();
    }

    public interface RxGitHub {
        @GET("/")
        Observable<HashMap<String, String>> index();
    }

    private void rxRequest() {
        RRO.setApiUrl(API_URL);
        RxGitHub apiService = RRO.getApiService(RxGitHub.class);
        apiService.index().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HashMap<String, String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HashMap<String, String> stringStringHashMap) {
                        changeTxt(stringStringHashMap);
                    }
                });
    }
}
