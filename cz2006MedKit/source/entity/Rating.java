package entity;

public class Rating {

	private User user;
	private float rating;

	public User getUser() {
		return this.user;
	}

	/**
	 * 
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	public float getRating() {
		return this.rating;
	}

	/**
	 * 
	 * @param rating
	 */
	public void setRating(float rating) {
		this.rating = rating;
	}

}