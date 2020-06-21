package com.example.bookreview;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class XmlParser {

    private String title = "title";
    private String rating = "rating";
    private String image = "image";
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean parsingComplete = true;
    private String urlString = null;

    public XmlParser(String url) {
        this.urlString = url;
    }



    public String getTitle() {
        return title;
    }

    public String getRating() {
        return rating;
    }

    public String getImageUrl() {
        return image;
    }

    public void parseXmlAndStoreIt( XmlPullParser myParser) {
        int event;
        String text = null;

        List<String> listTitles = new ArrayList<>();
        List<String> listRatings = new ArrayList<>();
        List<String> listImage = new ArrayList<>();
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();

                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        Log.d("text", "textnya: " + text);
                        break;
                    case XmlPullParser.END_TAG:
                        if(name.equals("title")) {
                            listTitles.add(text);
                            title = listTitles.get(0);
                            Log.d("title", "title = " + listTitles.get(0));
                        }

                        if(name.equals("average_rating")) {
                            listRatings.add(text);
                            rating = listRatings.get(0);
                            Log.d("rating", "rating= " + rating);
                        }

                        if(name.equals("image_url")) {
                            listImage.add(text);
                            image = listImage.get(0);
                            Log.d("imageurl", "image: " + image);

                        }
                        break;
                }
                event = myParser.next();
                Log.d("event", "event = " + event);
            }
            parsingComplete=false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchXml() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                    connect.setReadTimeout(10000);
                    connect.setConnectTimeout(  15000);
                    connect.setRequestMethod("GET");
                    connect.setDoInput(true);
                    connect.connect();

                    InputStream stream = connect.getInputStream();
                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myParser = xmlFactoryObject.newPullParser();
                    myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myParser.setInput(stream, null);
                    parseXmlAndStoreIt(myParser);
                    stream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}


