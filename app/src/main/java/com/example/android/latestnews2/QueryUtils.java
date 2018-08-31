package com.example.android.latestnews2;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gikonyo hannah on 6.06.2018.
 */
public class QueryUtils {
    private static final String KEY_RESPONSE = "response";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_TITLE = "webTitle";
    private static final String KEY_SECTION_NAME = "sectionName";
    private static final String KEY_WEB_PUBLICATION_DATE = "webPublicationDate";
    private static final String KEY_WEB_URL = "webUrl";
    private static final String KEY_IMAGE="webImage";
    private static final int READ_TIMEOUT = 1000;
    private static final int CONNECT_TIMEOUT = 1500;
    private static final int HTTP_RESPONSE_OK = 200;
    private static final String GET_REQUEST = "GET";
    private  static final String TAG_TAGS="tags";
    private static final String UNICODE_FORMAT = "Utf-8";

    private QueryUtils() {}
    public static List<NewsUpdate> fetchNewsData(String requestUrl) {

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
        }

        return extractResponseFromJSON(jsonResponse);
    }

    private static List<NewsUpdate> extractResponseFromJSON(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<NewsUpdate> articles = new ArrayList<> ();

        try {
            JSONObject root = new JSONObject(newsJSON);

            JSONObject response = root.getJSONObject(KEY_RESPONSE);

            JSONArray results = response.getJSONArray(KEY_RESULTS);

            for(int i = 0; i < results.length(); i++) {

                JSONObject newsItem = results.getJSONObject(i);
                String title = newsItem.getString(KEY_TITLE);
                String section = newsItem.getString(KEY_SECTION_NAME);
                String dateTime = newsItem.getString(KEY_WEB_PUBLICATION_DATE);
                String url = newsItem.getString(KEY_WEB_URL);
                String image=newsItem.getString (KEY_IMAGE);
                JSONArray tags = newsItem.getJSONArray(TAG_TAGS);

                String author = "";

                int authors = tags.length();

                if (authors == 0) {
                    author = "";
                } else {
                    for (int j = 0; j < authors; j++) {
                        if (j > 0) {
                            author += ",";
                        }

                        JSONObject tag = tags.getJSONObject(j);

                        author += tag.getString(KEY_TITLE);
                    }
                }

                articles.add(new NewsUpdate (title, author, section, dateTime, url,image));
            }
        } catch (JSONException e) {
        }

        return articles;
    }



    private static URL createUrl(String stringUrl) {

        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT /* milliseconds */);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT /* milliseconds */);
            urlConnection.setRequestMethod(GET_REQUEST);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HTTP_RESPONSE_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
            }
        } catch (IOException e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName(UNICODE_FORMAT));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}

