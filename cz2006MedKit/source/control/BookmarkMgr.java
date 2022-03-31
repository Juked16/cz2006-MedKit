package control;
import entity.*;

public class BookmarkMgr {

	/**
	 * 
	 * @param medicalfacility
	 */
	public boolean addBookmark(MedicalFacility medicalfacility, String notes) {
		// TODO - implement BookmarkMgr.addBookmark
		for(MedicalFacility i : medicalFacilityList){
			if(medicalFacility.getName().equalsIgnoreCase(i.getName())) {
		Bookmark b=new BookMark(medicalfacility,notes);
		return true;}
			else return false;
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param medicalfacility
	 */
	public boolean removeBookmark(MedicalFacility medicalfacility) {
		// TODO - implement BookmarkMgr.removeBookmark
		for(MedicalFacility i : medicalFacilityList){
			if(b.medicalFacility.getName().equalsIgnoreCase(i.getName())) {
				b=NULL;
				return true;}return false;
		throw new UnsupportedOperationException();
	}

}
