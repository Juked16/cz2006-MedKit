package com.example.medkit2006.control;

import android.text.format.DateUtils;
import android.util.Log;

import com.BoardiesITSolutions.AndroidMySQLConnector.MySQLRow;
import com.example.medkit2006.DB;
import com.example.medkit2006.entity.Post;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public class ForumMgr {
	public void getPostAbstract(String query, Consumer<ArrayList<Post>> callback, Consumer<Exception> error){
		DB.instance.executeQuery(query, resultSet -> {
			ArrayList<Post> post_list = new ArrayList<>();
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
				Log.d("DB_search", post_list.size() + "medical facility records retrieved");
			} catch (Exception e) {
				error.accept(e);
				return;
			}
			callback.accept(post_list);
		}, error);
	}
	public void getAllPostAbstract(Consumer<ArrayList<Post>> callback, Consumer<Exception> error) {
		String query = "select * from POST order by _id".toUpperCase();
		getPostAbstract(query, callback, error);
	}
	public void getMyPostAbstract(String username, Consumer<ArrayList<Post>> callback, Consumer<Exception> error){
		String query = "select * from POST where USERNAME = '" + username +"' and STATUS = 1 order by _id";
		getPostAbstract(query, callback, error);
	}
	public void getMyDraftAbstract(String username, Consumer<ArrayList<Post>> callback, Consumer<Exception> error){
		String query = "select * from POST where USERNAME = '" + username +"' and STATUS = 0 order by _id";
		getPostAbstract(query, callback, error);

	}
	public void getPostDetail(int post_id, Consumer<Post> callback, Consumer<Exception> error) {
		String query = "select * from POST where _ID = " + post_id;
		DB.instance.executeQuery(query, resultSet -> {
			try {
				MySQLRow row;
				while ((row = resultSet.getNextRow()) != null) {
					int ID = row.getInt("_ID");
					int like = row.getInt("LIKES"), report = row.getInt("REPORT");
					Post tmp_post = new Post(
							row.getInt("_ID"),
							row.getString("TITLE"),
							row.getString("POST"),
							row.getString("DATETIME"),
							row.getString("USERNAME"),
							row.getString("MEDICAL_FACILITY"),
							row.getString("TAGS"),
							like, report);
					Log.d("MF_Search Result", tmp_post.getTitle());
					DB.instance.executeQuery("select * from text where postId = " + ID, commentResult -> {
						ArrayList<String> comments = new ArrayList<>();
						MySQLRow r;
						while((r = commentResult.getNextRow()) != null){
							try {
								comments.add(r.getString("username") + ": " + r.getString("content") + "\n" + DateUtils.getRelativeTimeSpanString(Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd H:m:s", Locale.US).parse(r.getString("timestamp"))).getTime()));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						tmp_post.setComments(comments);
						callback.accept(tmp_post);
						Log.d("Forum_mgr get detail of", tmp_post.getTitle());
					}, error);
				}
			} catch (Exception e) {
				error.accept(e);
			}
		}, error);
	}
	public void addPost(String title, String content, String username, String medical_facility, String tags, int status, int rating, @NotNull Runnable callback, Consumer<Exception> error) {
		 DB.instance.execute("insert into post (title, post, username, medical_facility, likes, tags, status, report) values (\""
				 + title + "\",\""
				 + content + "\",\""
				 + username + "\",\""
				 + medical_facility + "\",\""
				 + 0 + "\",\""
				 + tags + "\",\""
				 + status + "\",\""
				 + 0 + "\")", () -> {
			 if (status == 1) {
				 DB.instance.executeQuery("select max(_ID) as ID from post", resultSet -> {
					 try {
						 int id = resultSet.getNextRow().getInt("ID");
						 DB.instance.execute("insert into rating (username, medical_facility, rating, postId) values (\""
								 + username + "\",\""
								 + medical_facility + "\",\""
								 + rating + "\"," + id + ")", callback, error);
					 } catch (Exception e) {
						 error.accept(e);
					 }
				 });
			 }
		 }, error);
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
		DB.instance.execute("delete from text where postId = " + post_id, null, Throwable::printStackTrace);
		DB.instance.execute("delete from rating where postId = " + post_id, null, Throwable::printStackTrace);
		String query = "delete from post where _ID = "+post_id;
		DB.instance.execute(query, null, e -> Log.d("ForumMgr delete post failure", e.getMessage()));
	}

	public void addComment(int postId, String sender, String content, Runnable callback, Consumer<Exception> error){
		DB.instance.execute("insert into text (username, content, postId) values('" + sender + "','" + content + "'," + postId + ")", callback, error);
	}
}