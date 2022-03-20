package entity;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

public class Post {

	private User user;
	private Date timestamp;
	private String content;
	private Text[] comments;

	/**
	 * 
	 * @param user
	 * @param content
	 */
	public Post(User user, String content) {
		// TODO - implement Post.Post
		throw new UnsupportedOperationException();
	}

	public User getUser() {
		return this.user;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

	public String getContent() {
		return this.content;
	}

	/**
	 * 
	 * @param content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	public Text[] getComment() {
		// TODO - implement Post.getComment
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param comment
	 */
	public void addComment(Text comment) {
		// TODO - implement Post.addComment
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param comment
	 */
	public void deleteComment(Text comment) {
		// TODO - implement Post.deleteComment
		throw new UnsupportedOperationException();
	}

}