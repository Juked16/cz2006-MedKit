package com.example.medkit2006.control;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.BoardiesITSolutions.AndroidMySQLConnector.Exceptions.SQLColumnNotFoundException;
import com.BoardiesITSolutions.AndroidMySQLConnector.MySQLRow;
import com.example.medkit2006.data.DB;
import com.example.medkit2006.entity.Message;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Consumer;

public class ChatMgr extends AppCompatActivity {

    public static int chatId;

    public void init() {
        DB.instance.executeQuery("select max(id) as id from chat", resultSet -> {
            MySQLRow row = resultSet.getNextRow();
            if (row != null) {
                try {
                    chatId = row.getInt("id");
                } catch (SQLColumnNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Start a chat with someone
     *
     * @param receiver Receiver of message
     * @param sender   Sender of message
     * @param callback Called when no error
     * @param error    Called when error
     */
    public void startPrivateMessage(String sender, String receiver, Runnable callback, Consumer<Exception> error) {
        DB.instance.executeQuery("select c1.id from chat c1 inner join chat c2 on c1.id=c2.id where c1.username = '"
                +sender + "' and c2.username ='" + receiver+"'", resultSet -> {
            MySQLRow row= resultSet.getNextRow();
            if (row == null) {
                try {
                    int id = ++chatId;
                    DB.instance.execute("insert into chat values (" + id + ",'" + sender + "')", null, Throwable::printStackTrace);
                    DB.instance.execute("insert into chat values (" + id + ",'" + receiver + "')", callback, error);
                } catch (Exception e) {
                    error.accept(e);
                    return;
                }
            }
            else Toast.makeText(getApplicationContext(),"Chat with " + receiver + " already Exist",Toast.LENGTH_LONG).show();

        }, error);

    }

    public void sendMessage(int chatId, String sender, String content, Runnable callback, Consumer<Exception> error){
        DB.instance.execute("insert into text (username, content, timestamp, chatId) values('" + sender + "','" + content + "','" + Instant.now().toString() + "'," + chatId + ")", callback, error);
    }

    /**
     * Get chatIds user is member of
     * @param username Username
     * @param callback Called when no error
     * @param error    Called when error
     */
    public void getChats(String username, Consumer<ArrayList<Integer>> callback, Consumer<Exception> error) {
        DB.instance.executeQuery("select id from chat where username = '" + username + "'", resultSet -> {
            ArrayList<Integer> chatIds = new ArrayList<>();
            MySQLRow row;
            try {
                while ((row = resultSet.getNextRow()) != null) {
                    chatIds.add(row.getInt("id"));
                }
            } catch (SQLColumnNotFoundException e) {
                error.accept(e);
                return;
            }
            callback.accept(chatIds);
        }, error);
    }

    /**
     * Get messages in specified chat
     *
     * @param callback Called when no error
     * @param error    Called when error
     */
    public void getMessages(int chatId, @NotNull Consumer<ArrayList<Message>> callback, Consumer<Exception> error) {
        DB.instance.executeQuery("select * from text where chatId = " + chatId, resultSet -> {
            ArrayList<Message> messages = new ArrayList<>();
            MySQLRow row;
            while ((row = resultSet.getNextRow()) != null) {
                try {
                    messages.add(new Message(row.getString("username"), row.getString("content"), new SimpleDateFormat("yyyy-MM-dd H:m:s", Locale.US).parse(row.getString("timestamp"))));
                } catch (Exception e) {
                    error.accept(e);
                    return;
                }
                callback.accept(messages);
            }
        }, error);
    }

    /**
     * Get usernames in specified chat
     *
     * @param chatId   ChatId
     * @param callback Called when no error
     * @param error    Called when error
     */
    public void getMembers(int chatId, @NotNull Consumer<ArrayList<String>> callback, Consumer<Exception> error) {
        DB.instance.executeQuery("select username from chat where id = " + chatId, resultSet -> {
            ArrayList<String> usernames = new ArrayList<>();
            MySQLRow row;
            while ((row = resultSet.getNextRow()) != null) {
                try {
                    usernames.add(row.getString("username"));
                } catch (Exception e) {
                    error.accept(e);
                    return;
                }
                callback.accept(usernames);
            }
        }, error);
    }

    //TODO: add member
    //TODO: remove member
}