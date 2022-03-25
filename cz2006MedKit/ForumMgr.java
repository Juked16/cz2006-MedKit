package control;
import boundary.ForumUI;
import entity.*;

public class ForumMgr {

	/**
	 * Add post to postlist if logged in and display error message otherwise
	 * @param post
	 */
	public boolean addPost(Post post) {
		// TODO - implement ForumMgr.addPost
		ForumUI forumInstance = new ForumUI();
		AccountMgr accountmgr = new AccountMgr();
		if (accountmgr.isAccountVerified(post.getUser()) == 0) {
			forumInstance.displayErrorMessage();
			return false;
		}
		Postlist postlist = new Postlist();
		postlist.addPost(post);
		return true;
	}

	/**
	 * Add comment to post if logged in and display error message otherwise
	 * @param post
	 * @param comment
	 */
	public boolean addComment(Post post, Text comment) {
		// TODO - implement ForumMgr.addComment
		ForumUI forumInstance = new ForumUI();
		AccountMgr accountmgr = new AccountMgr();
		if (accountmgr.isAccountVerified(comment.getUser()) == 0) {
			forumInstance.displayErrorMessage();
			return false;
		}
		post.addComment(comment);
		return true;
	}

}