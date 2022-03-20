package entity;

public class Rating {

	/**
	 * Variable of User who rated a medical facility
	 */
	private User user;
	
	/**
	 * Rating given for a Medical Facility
	 */
	private float rating;
	
	/**
	 * Constructor for rating
	 * 
	 * @param user User account 
	 * @param rating Rating of medical facility
	 */
	public Rating(User user, float rating) {
		this.user = user;
		this.rating = rating;
	}
	
	/**
	 * Get User information
	 * @return User information
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * Set User information
	 * @param user User information
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Get Rating of Medical Facility
	 * @return Medical Facility rating
	 */
	public float getRating() {
		return this.rating;
	}

	/**
	 * Set rating of a medical facility
	 * @param rating of a medical facility
	 */
	public void setRating(float rating) {
		this.rating = rating;
	}

}