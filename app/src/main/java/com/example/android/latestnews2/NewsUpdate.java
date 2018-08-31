package com.example.android.latestnews2;

/**
 * Created by Gikonyo hannah on 6.06.2018.
 */


public class NewsUpdate {

    private String mTitle;

    private String mAuthor;

    private String mSection;

    private String mDate;

    private String mUrl;

    private  String mImage;

    public NewsUpdate(String title, String author, String section, String date, String url, String image) {

        mTitle = title;
        mAuthor = author;
        mSection = section;
        mDate = date;
        mUrl = url;
        mImage=image;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() { return mAuthor; }

    public String getSection() {
        return mSection;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {return mUrl;}
    public String getImage(){return mImage;}


    }

