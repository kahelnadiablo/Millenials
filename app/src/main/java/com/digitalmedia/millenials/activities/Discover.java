package com.digitalmedia.millenials.activities;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.digitalmedia.millenials.R;
import com.digitalmedia.millenials.presenter.SongPresenter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Digital Media on 9/19/2015.
 */
public class Discover extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;

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

    }
}
