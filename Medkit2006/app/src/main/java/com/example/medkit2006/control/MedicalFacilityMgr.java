package com.example.medkit2006.control;

import com.example.medkit2006.entity.MedicalFacility;
import com.example.medkit2006.entity.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MedicalFacilityMgr {
	
	/**
	 * Variable of list of all medical facility in database
	 */
	private MedicalFacility[] medicalFacilityList;
	/** TODO: a function to return 30 facility for display
	 * @return certain number of medical facilities, the certain number is defined in SearchActivity.NUMBER_OF_DISPLAY
	 */

	/** TODO: modifie the function to return the list in a certain order
	 * Get list of Medical facilities matching the input and filter (if any)
	 * @param input Query of matching medical facilities
	 * @param filter List of filters - changed to single filter -- filters are realized by dropdown list, and can only be selected once
	 * @param Mode Denote the pattern of return result: 0/Alphabet, 1/Distance, 2/Rating, -1/Arbitrary
	 * @return null if no available result
	 */
	public MedicalFacility[] getFacilityList(String input, String filter, int Mode) {
		// TODO - implement MedicalFacilityMgr.getFacilityList
		Pattern p = Pattern.compile(input, Pattern.CASE_INSENSITIVE);
		for(MedicalFacility i : medicalFacilityList) {
			String name = i.getName();
			Matcher m = p.matcher(name);
			if(m.find()) {
	//			SearchUI searchInstance = new SearchUI();
	//			searchInstance.displayFacilityDetail(i);
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
				//SearchUI searchInstance = new SearchUI();
				//searchInstance.displayFacilityDetail(i);
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