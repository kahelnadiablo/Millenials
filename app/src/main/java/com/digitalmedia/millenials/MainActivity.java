package com.digitalmedia.millenials;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.digitalmedia.millenials.model.Song;
import com.digitalmedia.millenials.presenter.SongPresenter;
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

        SongPresenter songPresenter = new SongPresenter(getBaseContext(),this);
        songPresenter.getSong();


        //downloadManager();
        //StartDownload();
    }


    @OnClick(R.id.img_logo)
    public void facebookShare(){
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("http://collide.com"))
                    .setContentDescription("I unlocked the secret")
                    .setContentTitle("I unlocked the secret")
                    .setImageUrl(Uri.parse("http://piq.codeus.net/static/media/userpics/piq_95284_400x400.png"))
                    .build();

            shareDialog.show(this, content);
        }
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
        Log.e("Test", song.getArtist());
        Log.e("Test", song.getLink());
        Log.e("Test", song.getNote());
        Log.e("Test", song.getTitle());
        Log.e("Test", song.getLyrics());
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


    private long enqueue;
    private DownloadManager dm;
    BroadcastReceiver receiver;

    private void downloadManager(){
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                            /*ImageView view = (ImageView) findViewById(R.id.imageView1);*/
                            String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            /*view.setImageURI(Uri.parse(uriString));*/
                            unregisterReceiver(receiver);
                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void StartDownload() {

        File mydownload = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/Millenials");

        if (!mydownload.exists()){
            mydownload.mkdir();
        }

        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://www.vogella.de/img/lars/LarsVogelArticle7.png"));
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Millenials")
                .setDescription("Test Description")
                .setDestinationInExternalPublicDir("/Millenials", "test.jpg");
        enqueue = dm.enqueue(request);
    }

}
