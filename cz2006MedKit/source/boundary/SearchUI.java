package boundary;

import entity.MedicalFacility;

public class SearchUI {

	/**
	 * Display details of a list of medical facilities
	 * @param MedicalFacility List of Medical Facilities
	 */
	public void displayFacilityList(MedicalFacility[] MedicalFacility) {
		// TODO - implement SearchUI.displayFacilityList
		
		for(MedicalFacility i : MedicalFacility) {
			System.out.println(i.getName());
			System.out.println(i.getAddress());
			System.out.println(i.getContact());
			System.out.println(i.getAverageRating());
		}
		throw new UnsupportedOperationException();
	}

	/**
	 * Display details of a Medical Facility
	 * @param medicalFacility A Medical Facility
	 */
	public void displayFacilityDetail(MedicalFacility medicalFacility) {
		// TODO - implement SearchUI.displayFacilityDetail
		System.out.println(medicalFacility.getName());
		System.out.println(medicalFacility.getAddress());
		System.out.println(medicalFacility.getContact());
		System.out.println(medicalFacility.getAverageRating());
		
		throw new UnsupportedOperationException();
	}

}