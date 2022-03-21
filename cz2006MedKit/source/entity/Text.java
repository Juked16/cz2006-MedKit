package entity;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

public class Text {

	/**
	 * Variable of user that created text
	 */
    private User user;
    /**
	 * Variable of content of text
	 */
	private String content;
    /**
	 * Variable of time that text was created
	 */
	private Date timestamp;

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