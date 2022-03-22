package entity;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

public class Post {
	
	/**
	 * Variable of user that posted post
	 */
    private User user;
    /**
	 * Variable of time that post was posted
	 */
	private Date timestamp;
    /**
	 * Variable of content of post
	 */
	private String content;
    /**
	 * Variable of all comments of post
	 */
	private ArrayList<Text> comments;

	/**
	 * Constructor for post
	 * @param user User that posted post
	 * @param content Content of post
	 */
	public Post(User user, String content) {
		// TODO - implement Post.Post
        this.user = user;
        this.timestamp = new Date();
        this.content = content;
		throw new UnsupportedOperationException();
	}

	/**
	 * Get user that posted post
	 * @return user of post
	 */
    public User getUser() {
		return this.user;
	}

	/**
	 * Get time that post was posted
	 * @return timestamp of post
	 */
    public Date getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Get content of post
	 * @return content of post
	 */
    public String getContent() {
		return this.content;
	}

	/**
	 * Set content of post
	 * @param content Content of post
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Get list of all comments of post
	 * @return list of all comments
	 */
    public ArrayList<Text> getComment() {
		// TODO - implement Post.getComment
        return this.comments;
	}

	/**
	 * Add a new comment to list of all comments
	 * @param comment New comment to the post
	 */
	public void addComment(Text comment) {
		// TODO - implement Post.addComment
        comments.add(comment);
		throw new UnsupportedOperationException();
	}

	/**
	 * Delete a comment to the post
	 * @param comment Comment need to be deleted
	 */
	public void deleteComment(Text comment) {
		// TODO - implement Post.deleteComment
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i) == comment) {
                comments.remove(i);
                break;
            }
        }
		throw new UnsupportedOperationException();
	}

}

