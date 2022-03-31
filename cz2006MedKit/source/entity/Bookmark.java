package entity;

public class Bookmark  {

	private MedicalFacility facility;
	private String notes;

	/**
	 * 
	 * @param facility
	 */
	public Bookmark(MedicalFacility facility) {
		// TODO - implement Bookmark.Bookmark
		this.facility=facility;
		throw new UnsupportedOperationException();
	}

	public String getNotes() {
		return this.notes;
	}

	/**
	 * 
	 * @param notes
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	public MedicalFacility getFacility() {
		return this.facility;
	}

}
