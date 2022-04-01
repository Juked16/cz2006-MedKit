package com.example.medkit2006.entity;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.security.PublicKey;
import java.sql.Timestamp;

//***I changed Text to be a superclass of Post here***//

public class Text {

	/**
	 * Variable of user that created text
	 */
    protected User user;
    /**
	 * Variable of content of text
	 */
	protected String content;
    /**
	 * Variable of time that text was created
	 */
	protected Date timestamp;

	/**
	 * Constructor for Text
	 * @param user User that created text
	 * @param content Content of text
	 */
	public Text(User user, String content) {
		this.user = user;
        this.timestamp = new Date();
        this.content = content;
	}
	
	/**
	 * Get user that created text
	 * @return user of text
	 */
    public User getUser() {
		return this.user;
	}

	/**
	 * Get content of text
	 * @return content of text
	 */
    public String getContent() {
		return this.content;
	}

	/**
	 * Set content of text
	 * @param content Content of text
	 */
    public void setContent(String content) {
		// TODO - implement Text.setContent
		this.content = content;
	}

	/**
	 * Get time that text was created
	 * @return timestamp of text
	 */
    public Date getTimestamp() {
		return this.timestamp;
	}

}