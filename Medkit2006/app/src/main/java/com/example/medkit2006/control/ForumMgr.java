package com.example.medkit2006.control;
import com.example.medkit2006.entity.Post;

import java.util.ArrayList;

public class ForumMgr {

	/**
	 * Add post to postlist if logged in and display error message otherwise
	 * @param post
	 */
	private ArrayList<Post> postList;


	/** TEMP TO GET RID OF ERRORS FOR BUILDING

	 public boolean addPost(Post post) {
		// TODO - implement ForumMgr.addPost
		ForumUI forumInstance = new ForumUI();
		AccountMgr accountmgr = new AccountMgr();
		if (accountmgr.isLogin(post.getUser()) == 0) {
			forumInstance.displayErrorMessage();
			return false;
		}
		postList.add(post);
		return true;
	}

	/**
	 * Add comment to post if logged in and display error message otherwise
	 * @param post
	 * @param comment
	 */

/** TEMP TO GET RID OF ERRORS FOR BUILDING

 public boolean addComment(Post post, Text comment) {
		// TODO - implement ForumMgr.addComment
		ForumUI forumInstance = new ForumUI();
		AccountMgr accountmgr = new AccountMgr();
		if (accountmgr.isLogin(comment.getUser()) == 0) {
			forumInstance.displayErrorMessage();
			return false;
		}
		post.addComment(comment);
		return true;
	}
 */

}