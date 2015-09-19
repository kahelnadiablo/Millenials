package com.digitalmedia.millenials;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.digitalmedia.millenials.activities.Discover;
import com.digitalmedia.millenials.model.Song;
import com.digitalmedia.millenials.presenter.SongPresenter;
import com.digitalmedia.millenials.utils.Utilities;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.img_animated) ImageView animImageView;

    @Bind(R.id.img_map) ImageView img_map;
    @Bind(R.id.img_music) ImageView img_music;
    @Bind(R.id.img_search) ImageView img_search;
    @Bind(R.id.img_play) ImageView img_play;
    @Bind(R.id.img_link) ImageView img_link;


    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                if (result.getPostId() != null) {
                    Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Problem posting story", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getBaseContext(), "Error sharing", Toast.LENGTH_SHORT).show();
            }
        });

        animImageView.setBackgroundResource(R.drawable.anim_image);
        animImageView.post(new Runnable() {
            @Override
            public void run() {
                AnimationDrawable frameAnimation =
                        (AnimationDrawable) animImageView.getBackground();
                frameAnimation.start();
            }
        });

        //downloadManager();
        //StartDownload();
    }


    @OnClick(R.id.img_animated)
    public void getURL(){
        Random r = new Random();
        String url = "";

        switch (r.nextInt(5)+1){
            case 1:
                url = "http://10.0.2.106/services/smells_like.json";
                break;
            case 2:
                url = "http://10.0.2.106/services/smells_like.json";
                break;
            case 3:
                url = "http://10.0.2.106/services/smells_like.json";
                break;
            case 4:
                url = "http://10.0.2.106/services/smells_like.json";
                break;
            case 5:
                url = "http://10.0.2.106/services/smells_like.json";
                break;
            default:
                url = "http://10.0.2.106/services/smells_like.json";
                break;
        }

        if(Utilities.isOnline(getBaseContext())){
            SongPresenter songPresenter = new SongPresenter(getBaseContext(),this, url);
            songPresenter.getSong();
        }else{
            SharedPreferences saved_link = getSharedPreferences("saved_link", MODE_PRIVATE);
            Log.e("Test", "saved_link" + saved_link.getAll().size());
            saved_link.edit().putString("saved_link" + saved_link.getAll().size(), url+ (r.nextInt(11-1)+1) ).apply();
        }
    }


    //@OnClick(R.id.img_logo)
    public void facebookShare(){
        SharedPreferences saved_link = getSharedPreferences("saved_link", MODE_PRIVATE);
        for(int i=0; i<saved_link.getAll().size();i++){
            Log.e("The links", saved_link.getString("saved_link" + i, ""));
        }


        /*if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("http://collide.com"))
                    .setContentDescription("I unlocked the secret")
                    .setContentTitle("I unlocked the secret")
                    .setImageUrl(Uri.parse("http://piq.codeus.net/static/media/userpics/piq_95284_400x400.png"))
                    .build();

            shareDialog.show(this, content);
        }*/
    }

    @OnClick(R.id.img_map)
    public void mapClicked(){
        //todo show map activity
    }

    @OnClick(R.id.img_music)
    public void musicClicked(){
        //todo show music activity
    }

    @OnClick(R.id.img_search)
    public void searchClicked(){
        //todo show search activity
    }

    @OnClick(R.id.img_play)
    public void playClicked(){
        //todo show play activity
    }

    @OnClick(R.id.img_link)
    public void linkClicked(){
        //todo show link activity
    }

    public void DisplaySongInformation(Song song){
        Intent intent = new Intent(this, Discover.class);
        intent.putExtra("artist", song.getArtist());
        intent.putExtra("link", song.getLink());
        intent.putExtra("note", song.getNote());
        intent.putExtra("title", song.getTitle());
        intent.putExtra("lyrics", song.getLyrics());
        intent.putExtra("image_link", song.getImageLink());

        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
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
        }

        return super.onOptionsItemSelected(item);
    }


}
