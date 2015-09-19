package com.digitalmedia.millenials.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.digitalmedia.millenials.R;
import com.digitalmedia.millenials.adapters.CollectionsAdapter;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Digital Media on 9/19/2015.
 */
public class Collections  extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.txt_title) TextView title;

    @Bind(R.id.recycler_collections) RecyclerView collections;

    CollectionsAdapter adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Roboto-RobotoRegular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.icn_back);
        title.setText("MY COLLECTIONS");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        collections.setLayoutManager(linearLayoutManager);

        ArrayList<String> songs = new ArrayList<String>();
        SharedPreferences collections_shared = getSharedPreferences("collections", MODE_PRIVATE);

        for(int i=0; i<collections_shared.getAll().size();i++){
            songs.add(collections_shared.getString("collections" + i, ""));
        }

        adapter = new CollectionsAdapter(getBaseContext(),songs, getIntent().getBooleanExtra("play_last_download", false));
        collections.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.img_discover)
    public void searchClicked(){
        //todo show search activity
        Intent intent = new Intent(this, Discover.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        this.finish();

    }
}
