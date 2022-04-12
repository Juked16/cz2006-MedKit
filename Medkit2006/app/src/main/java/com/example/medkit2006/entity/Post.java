package com.example.medkit2006.entity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by Student on 4/29/2017.
 */

public class Post {

    private int _id;
    private String title;
    private String content;
    private ArrayList<String> comments = new ArrayList<>();
    private String date;
    private String username;
    private String facility;
    private String tags;
    private int likeNum;
    private int reportNum;

    //Used to initialize database tile
    public Post(){}
    public Post(int id, String fquestion, String fusername, String fdate, String ffacility)
    {
        title = fquestion;
        username = fusername;
        date = fdate;
        _id = id;
        facility = ffacility;
    }
    public Post(int id, String ftitle, String fcontent, String fdate, String fusername, String ffacility, String ftags, int numLikes, int numReports)
    {
        _id = id;
        title = ftitle;
        content = fcontent;
        date = fdate;
        username = fusername;
        facility = ffacility;
        tags = ftags;
        likeNum = numLikes;
        reportNum = numReports;
    }



    public void setDate(String a) { date = a; }

    public String getDate() { return date; }

    public void setLikeNum(int a) { likeNum = a; }

    public String getContent() { return content; }

    public int getLikeNum() { return likeNum; }

    public void setReportNum(int a) { reportNum = a; }

    public int getReportNum()
    {
        return reportNum;
    }

    public void setComments(@NotNull ArrayList<String> a)
    {
        comments = a;
    }

    public String getFacility() { return facility; }

    public ArrayList<String> getComments()
    {
        return comments;
    }

    public String getTitle()
    {
        return title;
    }

    public String getUsername()
    {
        return username;
    }

    public int getID()
    {
        return _id;
    }

    public String getTags() { return tags; }
}
