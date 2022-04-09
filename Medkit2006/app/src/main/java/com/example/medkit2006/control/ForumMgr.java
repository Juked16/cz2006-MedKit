package com.example.medkit2006.control;

import android.util.Log;

import com.BoardiesITSolutions.AndroidMySQLConnector.MySQLRow;
import com.example.medkit2006.data.DB;
import com.example.medkit2006.entity.MedicalFacility;
import com.example.medkit2006.entity.Post;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ForumMgr {

	public void getAllPost(Consumer<ArrayList<Post>> callback, Consumer<Exception> error) {
		String query = "select * from POST".toUpperCase();
		DB.instance.executeQuery(query, resultSet -> {
			ArrayList<Post> post_list = new ArrayList<Post>();
			try {
				MySQLRow row;
				while ((row = resultSet.getNextRow()) != null) {
					Post tmp_post = new Post(row.getString("title"), row.getString("username"), row.getString("date"));
					post_list.add(tmp_post);
					Log.d("MF_Search Result", tmp_post.getQuestion());
				}
				Log.d("DB_search", String.valueOf(post_list.size()) + "medical facility records retrieved");
			} catch (Exception e) {
				error.accept(e);
				return;
			}
			callback.accept(post_list);
		}, error);
	}

	 public void addPost(String title, String content, String username, String medical_facility, String tags, int status, int rating, @NotNull Runnable callback, Consumer<Exception> error) {
		 DB.instance.execute("insert into post (title, post, comments, username, medical_facility, likes, tags, status, report) values (\""
				 + title + "\",\""
				 + content + "\",\""
				 + "" + "\",\""
				 + username + "\",\""
				 + medical_facility + "\",\""
				 + "" + "\",\""
				 + tags + "\",\""
				 + status + "\",\""
				 + "" + "\")", callback, error);
		 DB.instance.execute("insert into rating (username, medical_facility, rating) values (\""
				 + username + "\",\""
				 + medical_facility + "\",\""
				 + rating + "\")", callback, error);
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