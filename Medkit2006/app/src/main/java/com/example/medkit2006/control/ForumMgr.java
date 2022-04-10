package com.example.medkit2006.control;

import android.util.Log;

import com.BoardiesITSolutions.AndroidMySQLConnector.MySQLRow;
import com.example.medkit2006.data.DB;
import com.example.medkit2006.entity.Post;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ForumMgr {
	public void getPostAbstract(String query, Consumer<ArrayList<Post>> callback, Consumer<Exception> error){
		DB.instance.executeQuery(query, resultSet -> {
			ArrayList<Post> post_list = new ArrayList<Post>();
			try {
				MySQLRow row;
				while ((row = resultSet.getNextRow()) != null) {
					Post tmp_post = new Post(row.getInt("_ID"),
							row.getString("TITLE"),
							row.getString("USERNAME"),
							row.getString("DATETIME"),
							row.getString("MEDICAL_FACILITY"));
					post_list.add(tmp_post);
					Log.d("MF_Search Result", tmp_post.getTitle());
				}
				Log.d("DB_search", String.valueOf(post_list.size()) + "medical facility records retrieved");
			} catch (Exception e) {
				error.accept(e);
				return;
			}
			callback.accept(post_list);
		}, error);
	}
	public void getAllPostAbstract(Consumer<ArrayList<Post>> callback, Consumer<Exception> error) {
		String query = "select * from POST order by _id".toUpperCase();
		getPostAbstract(query, callback::accept, error::accept);
	}
	public void getMyPostAbstract(String username, Consumer<ArrayList<Post>> callback, Consumer<Exception> error){
		String query = "select * from POST where USERNAME = '" + username +"' and STATUS = 1 order by _id";
		getPostAbstract(query, callback::accept, error::accept);
	}
	public void getMyDraftAbstract(String username, Consumer<ArrayList<Post>> callback, Consumer<Exception> error){
		String query = "select * from POST where USERNAME = '" + username +"' and STATUS = 0 order by _id";
		getPostAbstract(query, callback::accept, error::accept);

	}
	public void getPostDetail(int post_id, Consumer<Post> callback, Consumer<Exception> error) {
		String query = "select * from POST where _ID = " + post_id;
		DB.instance.executeQuery(query, resultSet -> {
			Post tmp_post = null;
			try {
				MySQLRow row;
				while ((row = resultSet.getNextRow()) != null) {
					int like, report;
					try{ like = row.getInt("LIKES");}catch(Exception e){ like = 0; }
					try{ report = row.getInt("REPORTS");}catch(Exception e){ report = 0; }
					 tmp_post = new Post(
							row.getInt("_ID"),
							row.getString("TITLE"),
							row.getString("POST"),
							row.getString("COMMENTS"),
							row.getString("DATETIME"),
							row.getString("USERNAME"),
							row.getString("MEDICAL_FACILITY"),
							row.getString("TAGS"),
							like, report);
					Log.d("MF_Search Result", tmp_post.getTitle());
				}
				Log.d("Forum_mgr get detail of", tmp_post.getTitle());
			} catch (Exception e) {
				error.accept(e);
				return;
			}
			callback.accept(tmp_post);
		}, error);
	}
	public void addPost(String title, String content, String username, String medical_facility, String tags, int status, int rating, @NotNull Runnable callback, Consumer<Exception> error) {
		 DB.instance.execute("insert into post (title, post, comments, username, medical_facility, likes, tags, status, report) values (\""
				 + title + "\",\""
				 + content + "\",\""
				 + "" + "\",\""
				 + username + "\",\""
				 + medical_facility + "\",\""
				 + 0 + "\",\""
				 + tags + "\",\""
				 + status + "\",\""
				 + 0 + "\")", callback, error);
		 if(status == 1 ){
		 DB.instance.execute("insert into rating (username, medical_facility, rating) values (\""
				 + username + "\",\""
				 + medical_facility + "\",\""
				 + rating + "\")", callback, error);}
	 }
	public void updatePost(int ID, String title, String content, String newDate, String username, String medical_facility, String tags, int status, int rating, @NotNull Runnable callback, Consumer<Exception> error) {
		DB.instance.execute("update post set"
				+ " TITLE = '" + title + "',"
				+ " POST = '" + content + "',"
				+ " DATETIME = '" + newDate + "',"
				+ " MEDICAL_FACILITY = '" + medical_facility + "',"
				+ " TAGS = '" + tags + "',"
				+ " STATUS = " + status +" WHERE _ID = " + ID , callback, error);
		if(status == 1){
		DB.instance.execute("insert into rating (username, medical_facility, rating) values (\""
				+ username + "\",\""
				+ medical_facility + "\",\""
				+ rating + "\")", callback, error);}
	}
	public void deletePost(int post_id){
		String query = "delete from post where _ID = "+post_id;
		DB.instance.execute(query, ()->{}, e -> {
			Log.d("ForumMgr delete post failure", e.getMessage());
		});
	}

}