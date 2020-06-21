package com.example.bookreview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class ScanActivity extends AppCompatActivity {

    public TextView textView, rating;
    //private static String urlString = "https://www.goodreads.com/book/isbn/0441172717?key=hgTduibWyDqLAgu1v6wnRQ";
    private XmlParser obj;
    public String urlImage;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        textView = findViewById(R.id.title);
        rating = findViewById(R.id.ratingText);

        Intent i = getIntent();
        String isbn = i.getStringExtra("isbn");

        String urlString = "https://www.goodreads.com/book/isbn/" + isbn + "?key=hgTduibWyDqLAgu1v6wnRQ";

        img = findViewById(R.id.imageView);

        obj = new XmlParser(urlString);
        obj.fetchXml();

        while (obj.parsingComplete) {
            textView.setText(obj.getTitle());
            rating.setText("Average Rating: " + obj.getRating() + " out of 5");
            urlImage = obj.getImageUrl();
            Log.d("urlimage", "urlimage: " + urlImage);
            new DownloadFiles().execute(urlImage);

        }

    }


    private class DownloadFiles extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... url) {
            String imageUrl = url[0];
            Bitmap bitmap = null;

            try {
                InputStream input = new java.net.URL(imageUrl).openStream();
                bitmap = BitmapFactory.decodeStream(input);

            }catch (Exception e) {
                Log.d("bitmap", "failed log bitmap");
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            img.setImageBitmap(result);
        }


    }

}

