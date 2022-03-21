package entity;
import java.util.ArrayList; 
import java.util.Arrays;

public class MedicalFacility {

	/**
	 * Variable of type of medical facility
	 */
	private String type;
	
	/**
	 * Variable of name of medical facility
	 */
	private String name;
	/**
	 * Variable of address of medical facility
	 */
	private String address;
	/**
	 * Variable of contact number of medical facility
	 */
	private String contact;
	/**
	 * Variable of ratings given by patients
	 */
	private ArrayList<Rating> ratings;
	/**
	 * Variable of services provided by the medical facility
	 */
	private ArrayList<Service> services;
	
	/**
	 * Variable of list of users who book marked the medical facility
	 */
	private ArrayList<User> notifyList;

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
	public ArrayList<Rating> getRatings() {
		return  this.ratings;
	}

	/**
	 * Add a new rating to list of all ratings
	 * @param rating Medical facility rating
	 */
	public void addRating(Rating rating) {
		// TODO - implement MedicalFacility.addRating
		ratings.add(rating); //add a new rating to the array list
		
		throw new UnsupportedOperationException();
	}

	/**
	 * Get average rating of all ratings of the medical facility
	 * @return Average rating of all ratings
	 */
	public float getAverageRating() {
		// TODO - implement MedicalFacility.getAverageRating
		int size = ratings.size(); //sum of all ratings
		float sum=0;
		for(Rating i: ratings) {
			sum += i.getRating();
		}
		return sum/size;
		
	}

	/**
	 * Get list of all services provided by the medical facility
	 * @return list of all services provided
	 */
	public ArrayList<Service> getServices() {
		return this.services;
	}

	/**
	 * Add services provided by the medical facility
	 * @param services Services provided by medical facility
	 */
	public void addServices(Service service) {
		services.add(service);
	}
	
	public ArrayList<User> getNotifyList() {
		return this.notifyList;
	}

}