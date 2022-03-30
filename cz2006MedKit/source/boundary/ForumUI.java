package boundary;

import entity.*;

public class ForumUI {

	/**
	 * Display details of a list of posts
	 * @param post
	 */
	public void displayPosts(Post[] post) {
		// TODO - implement ForumUI.displayPosts
		for(Post i : post) {
			System.out.println(i.getUser());
			System.out.println(i.getTimestamp());
			System.out.println(i.getContent());
			System.out.println(i.getComment());
		}
	}

	/**
	 * Display successful message of sending a post or a comment
	 */
	public void displaySuccessMessage() {
		// TODO - implement ForumUI.displaySuccessMessage
		System.out.println("Content was succesfully posted.");
	}

	/**
	 * Display error message of not able to send a post or a comment
	 */
	public void displayErrorMessage() {
		// TODO - implement ForumUI.displayErrorMessage
		System.out.println("Error! Content was not succesfully posted. Need to log in first.");
	}

	/**
	 * Display the comments of a post
	 * @param post
	 * @param comment ?
	 */
	public void displayComment(Post post) {
		// TODO - implement ForumUI.displayComment
		System.out.println(post.getComment());
	}

}