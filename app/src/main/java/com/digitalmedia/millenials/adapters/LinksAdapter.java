package com.digitalmedia.millenials.adapters;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
import com.digitalmedia.millenials.model.Collection;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mark on 8/20/2015.
 */
public class LinksAdapter extends RecyclerView.Adapter<LinksAdapter.ViewHolder> {

    Context context;
    ArrayList<String> links;

    @Override
    public int getItemCount() {
        return links.size();
    }

    public LinksAdapter(Context context, ArrayList<String> links){
        this.context = context;
        this.links = links;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_links, parent, false);
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

        holder.title.setText(links.get(position));

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txt_title) TextView title;

        public ViewHolder(View view, final Context context) {
            super(view);
            ButterKnife.bind(this, view);
         }


    }
}
