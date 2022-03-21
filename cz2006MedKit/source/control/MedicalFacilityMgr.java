package control;
import entity.*;
import boundary.SearchUI;
import java.util.ArrayList; 
import java.util.Arrays;
import java.util.regex.*;

public class MedicalFacilityMgr {
	
	/**
	 * Variable of list of all medical facility in database
	 */
	private MedicalFacility[] medicalFacilityList;
	
	
	/**
	 * Get list of Medical facilities matching the input and filter (if any)
	 * @param input Query of matching medical facilities
	 * @param filter List of filters
	 */
	public MedicalFacility[] getFacilityList(String input, String[] filter) {
		// TODO - implement MedicalFacilityMgr.getFacilityList
		Pattern p = Pattern.compile(input, Pattern.CASE_INSENSITIVE);
		for(MedicalFacility i : medicalFacilityList) {
			String name = i.getName();
			Matcher m = p.matcher(name);
			if(m.find()) {
				SearchUI searchInstance = new SearchUI();
				searchInstance.displayFacilityDetail(i);
			}
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * Get details of a medical facility
	 * @param medicalFacility name of a medical facility
	 */
	public MedicalFacility getFacilityDetails(String medicalFacility) {
		// TODO - implement MedicalFacilityMgr.getFacilityDetails
		for(MedicalFacility i : medicalFacilityList){
			if(medicalFacility.equalsIgnoreCase(i.getName())) {
				SearchUI searchInstance = new SearchUI();
				searchInstance.displayFacilityDetail(i);
				break;
			}
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * Add user to a list of users who book marked the medical facility
	 * @param user User account
	 * @param medicalFacility Medical Facility
	 */
	public void addNotifyList(User user, MedicalFacility medicalFacility) {
		// TODO - implement MedicalFacilityMgr.addNotifyList
		medicalFacility.getNotifyList().add(user);
		throw new UnsupportedOperationException();
	}

	/**
	 * Remove user from list of users who book marked the medical facility
	 * @param user User account
	 * @param medicalFacility Medical Facility
	 */
	public void removeNotifyList(User user, MedicalFacility medicalFacility) {
		// TODO - implement MedicalFacilityMgr.removeNotifyList
		medicalFacility.getNotifyList().remove(user);
		throw new UnsupportedOperationException();
	}

}