package com.digitalmedia.millenials.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.digitalmedia.millenials.R;
import com.digitalmedia.millenials.activities.Collections;
import com.digitalmedia.millenials.model.Collection;
import com.digitalmedia.millenials.model.Song;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mark on 8/20/2015.
 */
public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.ViewHolder> {

    Context context;
    ArrayList<String> songs;
    ImageLoader mImageLoader;
    boolean play_last;

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public CollectionsAdapter(Context context, ArrayList<String> songs, boolean play_last){
        this.context = context;
        this.songs = songs;
        this.play_last = play_last;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collections, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, context);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        //holder.album_art


        Collection collection = new Collection();
        Gson gson = new Gson();
        collection = gson.fromJson(songs.get(position).substring(1,songs.get(position).length()-1), Collection.class);

        holder.artist.setText(collection.getArtist());
        holder.title.setText(collection.getTitle());
        Picasso.with(context).load(collection.getImage_link()).into(holder.album_art);

        final MediaPlayer mpintro = MediaPlayer.create(context, Uri.parse(collection.getPath()));
        holder.play.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mpintro.setLooping(true);
                    mpintro.start();
                }else {
                    mpintro.pause();
                }

            }
        });

        if(play_last&&position==songs.size()-1){
            holder.play.setChecked(true);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.img_album_art) ImageView album_art;
        @Bind(R.id.txt_title) TextView title;
        @Bind(R.id.txt_artist) TextView artist;
        @Bind(R.id.btn_play) CheckBox play;
        @Bind(R.id.btn_add) Button add;
        MediaPlayer mpintro;

        public ViewHolder(View view, final Context context) {
            super(view);
            ButterKnife.bind(this, view);
         }


    }
}
