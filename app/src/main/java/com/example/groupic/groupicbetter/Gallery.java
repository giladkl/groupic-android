package com.example.groupic.groupicbetter;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.groupic.groupicbetter.resources.Photo;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Gallery extends AppCompatActivity {

    DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    List<String> imageUrls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Bundle extras = getIntent().getExtras();
        Integer event_id = extras.getInt("event_id");
        final JSONArray response = Photo.getPhotos(this, event_id);

        imageUrls = get_thumbnails(response);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();


        final GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new ImageAdapter());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(Gallery.this,ShowImage.class);
                intent.putStringArrayListExtra("urls", (ArrayList<String>) getImageUrls(response));
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

    }

    private List<String> get_thumbnails(JSONArray response)
    {
        imageUrls = new ArrayList<String>();

        for (int i=0;i<response.length();i++)
        {
            try {
                JSONObject jo = (JSONObject) response.get(i);
                imageUrls.add(jo.getString("thumbnail_url"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return imageUrls;
    }

    private List<String> getImageUrls(JSONArray response)
    {
        imageUrls = new ArrayList<String>();

        for (int i=0;i<response.length();i++)
        {
            try {
                JSONObject jo = (JSONObject) response.get(i);
                imageUrls.add(jo.getString("image_url"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return imageUrls;
    }

    public class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageView imageView;
            if (convertView == null) {
                imageView = (ImageView) getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
            } else {
                imageView = (ImageView) convertView;
            }

            imageLoader.displayImage(imageUrls.get(position), imageView, options);

            return imageView;
        }

    }
}


