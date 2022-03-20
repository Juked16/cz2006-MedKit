package entity;

public class MedicalFacility {

	private String type;
	private String name;
	private String address;
	private String contact;
	private Rating[] ratings;
	private Service[] services;

	/**
	 * 
	 * @param name
	 */
	public MedicalFacility(String name) {
		// TODO - implement MedicalFacility.MedicalFacility
		throw new UnsupportedOperationException();
	}

	public String getName() {
		return this.name;
	}

	public String getAddress() {
		return this.address;
	}

	/**
	 * 
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return this.contact;
	}

	/**
	 * 
	 * @param contact
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	public Rating[] getRatings() {
		return this.ratings;
	}

	/**
	 * 
	 * @param rating
	 */
	public void addRating(Rating rating) {
		// TODO - implement MedicalFacility.addRating
		throw new UnsupportedOperationException();
	}

	public float getAverageRating() {
		// TODO - implement MedicalFacility.getAverageRating
		throw new UnsupportedOperationException();
	}

	public Service[] getServices() {
		return this.services;
	}

	/**
	 * 
	 * @param services
	 */
	public void setServices(Service[] services) {
		this.services = services;
	}

}