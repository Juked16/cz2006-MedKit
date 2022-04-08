package com.example.medkit2006.control;

import android.util.Log;

import com.BoardiesITSolutions.AndroidMySQLConnector.MySQLRow;
import com.example.medkit2006.DB;
import com.example.medkit2006.entity.MedicalFacility;
import com.example.medkit2006.entity.User;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MedicalFacilityMgr {
	
	/**
	 * Variable of list of all medical facility in database
	 */

	public void getAllFacilityList(@NotNull Consumer<ArrayList<MedicalFacility>> callback, Consumer<Exception> error){
		String query = "select * from medical_facilities".toUpperCase();
		DB.instance.executeQuery(query, resultSet -> {
			MedicalFacility tmp_facil = new MedicalFacility();
			ArrayList<MedicalFacility> facility_list = new ArrayList<MedicalFacility>();
			MySQLRow row;
			while((row = resultSet.getNextRow())!=null) {
				try {
					tmp_facil.setName(row.getString("name"));    //name is nullable
					tmp_facil.setType(row.getString("type"));
					tmp_facil.setAddress(row.getString("address"));
					tmp_facil.setContact(row.getString("contact"));
					//tmp_facil.setLongitude(row.getFloat("longitude"));
					//tmp_facil.setLatitude(row.getFloat("latitude"));
					//tmp_facil.setDescription(row.getString("description"));
					facility_list.add(tmp_facil);
				} catch (Exception e) {
					error.accept(e);
					return;
				}
			}
			callback.accept(facility_list);
		}, error);
	}

	public void getFacilityList(String input, String filter, int Mode, Consumer<MedicalFacility> callback, Consumer<Exception> error) {
		// TODO - implement MedicalFacilityMgr.getFacilityList

	}

	public void getFacilityDetails(String facility_name, Consumer<MedicalFacility> callback, Consumer<Exception> error) {
		String query = ("select * from medical_facilities where name = '"+facility_name+"'").toUpperCase();
		Log.d("Issued query", query);
		DB.instance.executeQuery(query, resultSet -> {
			MedicalFacility tmp_facil = new MedicalFacility();
			ArrayList<MedicalFacility> facility_list = new ArrayList<MedicalFacility>();
			MySQLRow row = resultSet.getNextRow();
			try {
				tmp_facil.setName(row.getString("name"));    //name is nullable
				tmp_facil.setType(row.getString("type"));
				tmp_facil.setAddress(row.getString("address"));
				tmp_facil.setContact(row.getString("contact"));
				tmp_facil.setLongitude(row.getFloat("longitude"));
				tmp_facil.setLatitude(row.getFloat("latitude"));
				tmp_facil.setDescription(row.getString("description"));
				facility_list.add(tmp_facil);
			} catch (Exception e) {
				error.accept(e);
				return;
			}
			callback.accept(tmp_facil);
		}, error);
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