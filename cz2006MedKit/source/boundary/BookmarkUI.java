package boundary;
import entity.*;

public class BookmarkUI extends SearchUI {

	/**
	 * 
	 * @param bookmark
	 */
	public void displayBookmarks(Bookmark[] bookmark) {
		// TODO - implement BookmarkUI.displayBookmarks
		for(bookmark b : Bookmark){
				SearchUI searchInstance = new SearchUI();
				searchInstance.displayFacilityDetail(b.getFacility());
			        System.out.println(b.getNotes());
		throw new UnsupportedOperationException();
	}

}
