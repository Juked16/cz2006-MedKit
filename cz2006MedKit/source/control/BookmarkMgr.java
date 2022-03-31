package control;
import java.util.ArrayList;

import entity.*;

public class BookmarkMgr {

	/**
	 * 
	 * @param medicalfacility
	 */
	private MedicalFacility[] medicalFacilityList;
	private ArrayList<Bookmark> bookmarkList;

	public boolean addBookmark(MedicalFacility medicalfacility, String notes) {
		// TODO - implement BookmarkMgr.addBookmark
		for(MedicalFacility i : medicalFacilityList){
			if(medicalfacility.getName().equalsIgnoreCase(i.getName())) {
		Bookmark b=new Bookmark(medicalfacility,notes);
		return true;
		}
		}
		return false;
	}

	/**
	 * 
	 * @param medicalfacility
	 */
	public boolean removeBookmark(MedicalFacility medicalfacility) {
		// TODO - implement BookmarkMgr.removeBookmark
		for(Bookmark b : bookmarkList){
			if(b.getFacility().getName().equalsIgnoreCase(medicalfacility.getName())) {
				bookmarkList.remove(b);
				return true;}
			}
		return false;
	}

}
