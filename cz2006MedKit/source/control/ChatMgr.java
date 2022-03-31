package control;

import java.util.ArrayList;

import boundary.ChatUI;
import entity.*;

public class ChatMgr {

	/**
	 * Variable of list of all users in database
	 */
	private User[] UserList;

	/**
	 * Variable of list of all messages in database
	 */
	private ArrayList<Text> message;

	/**
	 * Start a message with someone in forum
	 * @param user
	 */
	public void startPrivateMessage(User sender, User receiver, String message) {
		// TODO - implement ChatMgr.startPrivateMessage
		ChatUI chatInstance = new ChatUI();
		AccountMgr accountmgr = new AccountMgr();
		if (accountmgr.isLogin(sender)) {
			Text chat = new Text(sender, message);
			message.add(chat);
			chatInstance.displayConversationStarted();
		}

	}

	/**
	 * Send message if the user exists and display error message otherwise
	 * @param user
	 * @param message
	 */
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

}