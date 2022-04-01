package com.example.medkit2006.boundary;
import com.example.medkit2006.entity.*;

public class BookmarkUI extends SearchUI {

	/**
	 * 
	 * @param bookmark
	 */
	public void displayBookmarks(Bookmark[] bookmark) {
		// TODO - implement BookmarkUI.displayBookmarks
		for(Bookmark b : bookmark){
				SearchUI searchInstance = new SearchUI();
				searchInstance.displayFacilityDetail(b.getFacility());
			        System.out.println(b.getNotes());
		throw new UnsupportedOperationException();
		}
	}

}
