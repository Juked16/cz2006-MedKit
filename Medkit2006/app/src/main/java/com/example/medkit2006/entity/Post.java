package com.example.medkit2006.entity;

/**
 * Created by Student on 4/29/2017.
 */

public class Post {
    private final String title;
    private final String username;
    private final String date;

    //Used to initialize database tile
    public Post(String fquestion, String fusername, String fdate)
    {
        title = fquestion;
        username = fusername;
        date = fdate;
    }

    public String getQuestion()
    {
        return title;
    }

    public String getUsername()
    {
        return username;
    }

    public String getDate()
    {
        return date;
    }
}
