package entity;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

public class User {

	private String username;
	private String email;
	private String passwordHash;
	private boolean verified;
	private String firstName;
	private String lastName;
	private String gender;
	private String bloodType;
	private Date dateOfBirth;

	/**
	 * 
	 * @param username
	 */
	public User(String username) {
		// TODO - implement User.User
		throw new UnsupportedOperationException();
	}

	public String getEmail() {
		return this.email;
	}

	/**
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @param password
	 */
	public boolean verifyPassword(String password) {
		// TODO - implement User.verifyPassword
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param passwordHash
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public boolean getVerified() {
		return this.verified;
	}

	/**
	 * 
	 * @param verified
	 */
	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * 
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	/**
	 * 
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return this.gender;
	}

	/**
	 * 
	 * @param gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBloodType() {
		return this.bloodType;
	}

	/**
	 * 
	 * @param bloodType
	 */
	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	/**
	 * 
	 * @param dateOfBirth
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

}