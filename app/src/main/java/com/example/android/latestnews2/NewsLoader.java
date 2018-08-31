package com.example.android.latestnews2;
import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;

/**
 * Created by Gikonyo hannah on 6.06.2018.
 */

public class NewsLoader extends AsyncTaskLoader<List<NewsUpdate>> {

    private String mUrl;

    private List<NewsUpdate> mData;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {

        if (mData != null) {
            deliverResult(mData);
        }
        else {
            forceLoad();
        }
    }
    @Override
    public List<NewsUpdate> loadInBackground() {

        if (mUrl == null) {
            return null;
        }
        return QueryUtils.fetchNewsData(mUrl);
    }

    @Override
    public void deliverResult(List<NewsUpdate> data) {
        mData = data;
        super.deliverResult(data);
    }
}
