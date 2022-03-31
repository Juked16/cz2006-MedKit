package control;
import entity.*;

public class BookmarkMgr {

	/**
	 * 
	 * @param medicalfacility
	 */
	public boolean addBookmark(MedicalFacility medicalfacility, String notes) {
		// TODO - implement BookmarkMgr.addBookmark
		if(medicalfacility.name
		Bookmark b=new BookMark(medicalfacility,notes);
		return true;
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param medicalfacility
	 */
	public boolean removeBookmark(MedicalFacility medicalfacility) {
		// TODO - implement BookmarkMgr.removeBookmark
		throw new UnsupportedOperationException();
	}

}
