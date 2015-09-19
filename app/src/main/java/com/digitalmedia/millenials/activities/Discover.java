package com.digitalmedia.millenials.activities;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalmedia.millenials.R;
import com.digitalmedia.millenials.presenter.SongPresenter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Digital Media on 9/19/2015.
 */
public class Discover extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.txt_title) TextView title;

    @Bind(R.id.txt_song) TextView song_name;
    @Bind(R.id.txt_note) TextView note;
    @Bind(R.id.img_album) CircleImageView album_art;
    @Bind(R.id.btn_download) Button download;

    @Bind(R.id.btn_share) Button share;
    @Bind(R.id.btn_play) Button play;
    @Bind(R.id.txt_add_to_playlist) TextView playlist;
    @Bind(R.id.ll_action) LinearLayout ll_action;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.icn_back);
        title.setText("MY MUSIC");

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

        song_name.setText(getIntent().getStringExtra("title").toUpperCase());
        Picasso.with(getBaseContext()).load(getIntent().getStringExtra("image_link")).placeholder(R.drawable.speaker_icn1).into(album_art);
        note.setText(getIntent().getStringExtra("note"));

        playlist.setText(Html.fromHtml("or <u>Add to Playlist</u>"));

        downloadManager();

    }

    @OnClick(R.id.btn_download)
    public void Download(){
        download.setText("Downloading...");
        StartDownload(getIntent().getStringExtra("title"), getIntent().getStringExtra("link"));
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }


    private long enqueue;
    private DownloadManager dm;
    BroadcastReceiver receiver;
    String media_uri;

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
                            ll_action.setVisibility(View.VISIBLE);
                            download.setText("Download");
                            download.setVisibility(View.GONE);
                            String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                            media_uri = uriString;
                            unregisterReceiver(receiver);
                        }
                    }
                }
            }
        };

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void StartDownload(String title,String url) {

        File mydownload = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/Millenials");

        if (!mydownload.exists()){
            mydownload.mkdir();
        }

            dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle("Millenials")
                    .setDestinationInExternalPublicDir("/Millenials", title.concat(".mp3"));
            enqueue = dm.enqueue(request);

    }

    @OnClick(R.id.btn_share)
    public void facebookShare(){
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("http://collide.com"))
                    .setContentDescription(getIntent().getStringExtra("note"))
                    .setContentTitle("I unlocked the song " + getIntent().getStringExtra("title"))
                    .setImageUrl(Uri.parse(getIntent().getStringExtra("image_link")))
                    .build();

            shareDialog.show(this, content);
        }
    }

    @OnClick(R.id.btn_play)
    public void playAudio(){
        if(!media_uri.isEmpty()){
            MediaPlayer mpintro = MediaPlayer.create(this, Uri.parse(media_uri));
            mpintro.setLooping(true);
            mpintro.start();
        }
    }
}
