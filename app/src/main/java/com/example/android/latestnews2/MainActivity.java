package com.example.android.latestnews2;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<NewsUpdate>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int NEWSARTICLE_LOADER_ID =1;

    private static final String NEWS_REQUEST_URL= "http://content.guardianapis.com/search";

    private NewsAdapter mAdapter;

    private TextView mEmptyTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ListView newsListView = (ListView) findViewById(R.id.listNews);

        mAdapter = new NewsAdapter (this, new ArrayList<NewsUpdate> ());

        newsListView.setAdapter(mAdapter);

        mEmptyTextView = (TextView) findViewById(R.id.empty_textView);
        newsListView.setEmptyView(mEmptyTextView);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        prefs.registerOnSharedPreferenceChangeListener(this);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                NewsUpdate currentArticle = mAdapter.getItem(position);

                Uri articleUri = Uri.parse(currentArticle.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                startActivity(websiteIntent);

            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWSARTICLE_LOADER_ID, null, this);
        }
        else {

            View loadingIndicator = findViewById(R.id.Indicator);

            loadingIndicator.setVisibility(View.GONE);

            mEmptyTextView.setText(R.string.no_connection);}}

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.settings_news_subject_key))) {

            mAdapter.clear();

            mEmptyTextView.setVisibility(View.GONE);

            View loadingIndicator = findViewById(R.id.Indicator);

            loadingIndicator.setVisibility(View.VISIBLE);

            getLoaderManager().restartLoader(NEWSARTICLE_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<NewsUpdate>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String subject = sharedPrefs.getString(
                getString(R.string.settings_news_subject_key),
                getString(R.string.settings_news_subject_default));
        Uri baseUri = Uri.parse(NEWS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(getString(R.string.q),subject );
        uriBuilder.appendQueryParameter(getString(R.string.api_k), getString(R.string.key_itself));
        uriBuilder.appendQueryParameter(getString(R.string.show_tags), getString(R.string.contributor));
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsUpdate>> loader, List<NewsUpdate> newsArticles) {

        mEmptyTextView.setText(R.string.no_news_found);

        View loadingIndicator = findViewById(R.id.Indicator);

        loadingIndicator.setVisibility(View.GONE);

        mAdapter.clear();

        if (newsArticles != null && !newsArticles.isEmpty()) {
            mAdapter.addAll(newsArticles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsUpdate>> loader) {

        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.settings) {

            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
