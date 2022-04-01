package com.example.medkit2006.control;

import com.example.medkit2006.entity.Text;
import com.example.medkit2006.entity.User;

import java.util.ArrayList;

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


	/** TEMP TO IGNORE ISLOGIN ERROR TO BUILD

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