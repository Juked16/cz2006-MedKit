package entity;

public class Bookmark extends MedicalFacility {

	private MedicalFacility facility;
	private String notes;

	/**
	 * 
	 * @param facility
	 */
	public Bookmark(MedicalFacility facility) {
		// TODO - implement Bookmark.Bookmark
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
		this.notes = new String(notes);
	}

	public MedicalFacility getFacility() {
		return this.facility;
	}

}
