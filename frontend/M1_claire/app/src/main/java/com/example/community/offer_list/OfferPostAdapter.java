package com.example.community.offer_list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.community.R;
import com.example.community.classes.OfferPostObj;
import com.example.community.classes.ReqPostObj;

import java.io.InputStream;
import java.util.ArrayList;

public class OfferPostAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<OfferPostObj> offerPosts;
    private LayoutInflater inflater;

    public OfferPostAdapter(Context applicationContext, ArrayList<OfferPostObj> offerPostList) {
        this.context = applicationContext;
        this.offerPosts = offerPostList;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return this.offerPosts.size();
    }

    @Override
    public Object getItem(int i) {
        return this.offerPosts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.fragment_offer_post, null);
        TextView itemName = (TextView) view.findViewById(R.id.item_name);
        TextView bestBefore = (TextView) view.findViewById(R.id.best_before);
        TextView distance = (TextView) view.findViewById(R.id.distance);
        //item description will not be listed in preview, only when user expands the post

        OfferPostObj post = this.offerPosts.get(i);

        itemName.setText(post.itemName);
        //TODO: fix once we have calculated distance
        //distance.setText(post.distanceKm);
        bestBefore.setText(String.valueOf(post.bestBefore));

        ImageView itemImage = (ImageView) view.findViewById(R.id.item_image);
        return view;
    }
}
        //TODO: verify correct URI
        // show The Image in a ImageView
        /*new DownloadImageTask((ImageView) view.findViewById(R.id.item_image))
                .execute("http://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png");
        itemImage.setImageURI(Uri.parse(post.image));
    }

    @Override
    public void onClick(View view) {
        Intent showImgIntent = new Intent(OfferPostAdapter.this, IndexActivity.class);
        startActivity(showImgIntent);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
     */
