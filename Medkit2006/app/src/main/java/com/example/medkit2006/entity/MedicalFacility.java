package com.example.medkit2006.entity;
import java.util.ArrayList;

public class MedicalFacility {
	private String type;
	private String name;
	private String address;
	private String contact;
	private float latitude;
	private float longitude;
	private String description;
	private float aveRating;
	private ArrayList<Service> services;
	private ArrayList<User> notifyList;

	/**
	 * Empty Constructor for medical facility
	 * 	 */
	public MedicalFacility() {
	}

	/**
	 * Constructor for medical facility
	 * @param name Name of medical Facility
	 */
	public MedicalFacility(String name) {
		this.name = name;
	}
	
	/**
	 * Set name of medical facility name
	 * @param name medical facility name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get name of medical facility
	 * @return medical facility name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get address of medical facility
	 * @return medical facility address
	 */
	public String getAddress() {
		return this.address;
	}

	/**
	 * Set Address of medical facility
	 * @param address Medical facility address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Get contact number of medical facility
	 * @return contact number
	 */
	public String getContact() {
		return this.contact;
	}

	/**
	 * Set contact number of medical facility
	 * @param contact Medical facility contact number
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * Get list of all ratings of the medical facility 
	 * @return list of all ratings
	 */
	public void setAveRating(float rating) { this.aveRating = rating; }
	public float getAveRating() { return this.aveRating; }
	public void addServices(Service service) {services.add(service); }
	public ArrayList<Service> getServices() {
		return this.services;
	}
	public void setType(String type){ this.type = type; }
	public String getType(){return this.type;}
	public void addNotifyUser(User user){ notifyList.add(user); }
	public ArrayList<User> getNotifyList() { return this.notifyList; }
	public void setLatitude(float lat){this.latitude = lat; }
	public float getLatitude(){return this.latitude; }
	public void setLongitude(float longitude){this.longitude = longitude; }
	public float getLongitude(){return this.longitude; }
	public void setDescription(String des){this.description = des; }
	public String getDescription(){return this.description; }
}