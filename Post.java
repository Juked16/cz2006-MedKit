package entity;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

//***I changed Post to be a subclass of Text here***//

public class Post extends Text {
	
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
        super(user, content);
		throw new UnsupportedOperationException();
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
	 * Get list of all comments of post
	 * @return list of all comments
	 */
    public ArrayList<Text> getComment() {
		// TODO - implement Post.getComment
        return this.comments;
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

