package com.example.medkit2006.control;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.medkit2006.R;
import com.example.medkit2006.entity.User;

public class AccountMgr extends AppCompatActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);
	}
}

class AccountMgrOG {

	/**
	 * 
	 * @param password
	 * @param confirmPassword
	 */
	public boolean validateConfirmPassword(String password, String confirmPassword) {
		// TODO - implement AccountMgr.validateConfirmPassword
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param email
	 */
	public boolean validEmail(String email) {
		// TODO - implement AccountMgr.validEmail
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param email
	 */
	public boolean emailExist(String email) {
		// TODO - implement AccountMgr.emailExist
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param email
	 * @param verificationCode
	 */
	public void sendVerificationCode(String email, String verificationCode) {
		// TODO - implement AccountMgr.sendVerificationCode
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param verificationCode
	 */
	public boolean validateVerificationCode(String verificationCode) {
		// TODO - implement AccountMgr.validateVerificationCode
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param user
	 */
	public boolean isAccountVerified(User user) {
		// TODO - implement AccountMgr.isAccountVerified
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param username
	 * @param password
	 */
	public boolean validateAccount(String username, String password) {
		// TODO - implement AccountMgr.validateAccount
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param username
	 */
	public User getUserDetails(String username) {
		// TODO - implement AccountMgr.getUserDetails
		throw new UnsupportedOperationException();
	}

}