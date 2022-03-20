package entity;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

public class Text {

	private User user;
	private String content;
	private Date timestamp;

	public User getUser() {
		return this.user;
	}

	public String getContent() {
		return this.content;
	}

	public String setContent() {
		// TODO - implement Text.setContent
		throw new UnsupportedOperationException();
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

}