package com.example.medkit2006.control;

import com.example.medkit2006.data.DB;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ChatMgr {

	/**
	 * Start a message with someone in forum
	 * @param receiver Receiver of message
	 * @param sender Sender of message
	 * @param message Content of message
	 * @param callback Called when no error
	 * @param error Called when error
	 */
	public void startPrivateMessage(String sender, String receiver, String message, @NotNull Runnable callback, Consumer<Exception> error) {
		// TODO - implement ChatMgr.startPrivateMessage
		DB.instance.execute("insert into chat (sender, receiver, message) values(\"" + sender + "\",\"" + receiver + "\",\"" + message + "\")", callback, error);
	}


	/** TEMP TO GET RID OF ERRORS FOR BUILDING
	public boolean sendMessage(User sender, User receiver, String message) {
		// TODO - implement ChatMgr.sendMessage
		ChatUI chatInstance = new ChatUI();
		for (User i : UserList) {
			if (receiver == i) {
				Text chat = new Text(sender, message);
				message.add(chat);
				return true;
			}
		}
		chatInstance.displayErrorMessage();
		return false;
		
	}
	*/


}