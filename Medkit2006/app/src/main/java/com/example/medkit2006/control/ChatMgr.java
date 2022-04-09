package com.example.medkit2006.control;

import com.BoardiesITSolutions.AndroidMySQLConnector.MySQLRow;
import com.example.medkit2006.MainActivity;
import com.example.medkit2006.data.DB;
import com.example.medkit2006.entity.Message;
import com.example.medkit2006.entity.User;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ChatMgr {

	/**
	 * Start a message with someone in forum
	 * @param receiver Receiver of message
	 * @param sender Sender of message
	 * @param content Content of message
	 * @param callback Called when no error
	 * @param error Called when error
	 */
	public void startPrivateMessage(String sender, String receiver, String content, @NotNull Runnable callback, Consumer<Exception> error) {
		// TODO - implement ChatMgr.startPrivateMessage
		DB.instance.execute("insert into chat (sender, receiver, content) values(\"" + sender + "\",\"" + receiver + "\",\"" + content + "\")", callback, error);
	}

	/**
	 * Get messages that are sent or received by current user
	 * @param sender Sender of message
	 * @param receiver Receiver of message
	 * @param callback Called when no error
	 * @param error Called when error
	 */
	public void getChatSender(String sender, String receiver, @NotNull Consumer<Message> callback, Consumer<Exception> error) {
		DB.instance.executeQuery("select * from chat where sender = \"" + sender + "\"", resultSet -> {
			Message message = new Message(sender);
			while (resultSet.getNextRow() != null) {
				MySQLRow row = resultSet.getNextRow();
				try {
					message.setReciever(row.getString("receiver"));
					message.setContent(row.getString("content"));
				} catch (Exception e) {
					error.accept(e);
					return;
				}
				if (message.getReciever().equals(receiver)) {
					callback.accept(message);
				}
			}
		}, error);
	}

	/**
	 * Get users that received messages from current user
	 * @param sender Sender of message
	 * @param callback Called when no error
	 * @param error Called when error
	 */
	public void getReceiver(String sender, @NotNull Consumer<User> callback, Consumer<Exception> error) {
		AccountMgr mgr = MainActivity.accountMgr;
		DB.instance.executeQuery("select * from chat where sender = \"" + sender + "\"", resultSet -> {
			MySQLRow row = resultSet.getNextRow();
			String receiver = null;
			try {
				receiver = row.getString("receiver");
			} catch (Exception e) {
				error.accept(e);
				return;
			}
			mgr.getUserDetails(receiver, user -> {
				callback.accept(user);
			}, error);
		}, error);
	}

	/**
	 * Get users that received messages from current user
	 * @param receiver Sender of message
	 * @param callback Called when no error
	 * @param error Called when error
	 */
	public void getSender(String receiver, @NotNull Consumer<User> callback, Consumer<Exception> error) {
		AccountMgr mgr = MainActivity.accountMgr;
		DB.instance.executeQuery("select * from chat where receiver = \"" + receiver + "\"", resultSet -> {
			MySQLRow row = resultSet.getNextRow();
			String sender = null;
			try {
				sender = row.getString("sender");
			} catch (Exception e) {
				error.accept(e);
				return;
			}
			mgr.getUserDetails(sender, user -> {
				callback.accept(user);
			}, error);
		}, error);
	}
}