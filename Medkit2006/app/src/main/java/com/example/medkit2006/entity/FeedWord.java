package com.example.medkit2006.entity;

/**
 * Created by Student on 4/29/2017.
 */

public class FeedWord {
    private String question;
    private String username;
    private String date;

    public FeedWord(String fquestion, String fusername, String fdate)
    {
        question = fquestion;
        username = fusername;
        date = fdate;
    }

    public String getQuestion()
    {
        return question;
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
