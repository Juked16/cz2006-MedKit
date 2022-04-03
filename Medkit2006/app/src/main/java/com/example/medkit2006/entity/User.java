package com.example.medkit2006.entity;

import java.util.Date;

public class User {

	private final String username;
	private String email;
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
		this.username = username;
		//what about the other parameters? what about comparison?
	}

	public String getUsername() {
		return username;
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
