package com.example.uppersky_movil.instagram_api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by UPPERSKY-MOVIL on 01/09/2015.
 */
public class InstagramAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<InstagramImage> images;
    private LayoutInflater inflater;

    private static class ViewHolder {
        ImageView imageView;
    }

    public InstagramAdapter(Context context, ArrayList<InstagramImage> images) {
        this.context = context;
        this.images = images;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return this.images.size();
    }

    @Override
    public Object getItem(int position) {
        return this.images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.content_image_instagram,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.imageViewInstagram);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (viewHolder.imageView != null) {
            new ImageDownloaderTask(viewHolder.imageView).execute(images
                    .get(position).getStandard_resolution().getUrl());
        }
        return convertView;
    }

    private static Bitmap downloadBitmap(String url) {
        final AndroidHttpClient client = AndroidHttpClient
                .newInstance("Android");
        final HttpGet getRequest = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getRequest);
            final int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("ImageDownloader", "Error " + statusCode
                        + " while retrieving bitmap from " + url);
                return null;
            }

            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = null;
                try {
                    inputStream = entity.getContent();
                    final Bitmap bitmap = BitmapFactory
                            .decodeStream(inputStream);
                    return bitmap;
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    entity.consumeContent();
                }
            }
        } catch (Exception e) {
            // Could provide a more explicit error message for IOException or
            // IllegalStateException
            getRequest.abort();
            Log.w("ImageDownloader", "Error while retrieving bitmap from "
                    + url);
        } finally {
            if (client != null) {
                client.close();
            }
        }
        return null;
    }

    private class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private static final String TAG = "Asyncs ";
        private final WeakReference imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference(imageView);
        }

        @Override
        // Actual download method, run in the task thread
        protected Bitmap doInBackground(String... params) {
            // params comes from the execute() call: params[0] is the url.
            return downloadBitmap(params[0]);
        }

        @Override
        // Once the image is downloaded, associates it to the imageView
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = (ImageView) imageViewReference.get();
                if (imageView != null) {

                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Log.d(TAG, "ELSE");
                       /* imageView
                                .setImageDrawable(imageView.getContext()
                                        .getResources()
                                        .getDrawable(R.));*/
                    }
                }

            }
        }

    }
}