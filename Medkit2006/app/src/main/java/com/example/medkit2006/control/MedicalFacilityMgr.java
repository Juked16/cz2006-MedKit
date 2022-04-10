package com.example.medkit2006.control;

import android.util.Log;

import com.BoardiesITSolutions.AndroidMySQLConnector.MySQLRow;
import com.example.medkit2006.data.DB;
import com.example.medkit2006.entity.MedicalFacility;
import com.example.medkit2006.entity.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

public class MedicalFacilityMgr {
	
	/**
	 * Variable of list of all medical facility in database
	 */
	public final String[] filters_type = {"Type Unselected", "hospital", "nursing home"};
	public final String[] filters_rating = {"Rating Unselected", "Rating>3.5", "Rating>4.0", "Rating>4.5"};

	public void getAllFacilityName(@NotNull Consumer<String[]> callback, Consumer<Exception> error){
		String query = "select name from medical_facilities";
		DB.instance.executeQuery(query, resultSet -> {
			String[] names = new String[150];
			try{
				MySQLRow row;
				int i = 0;
				while((row = resultSet.getNextRow()) != null && i<150){
					names[i] = row.getString("name");
					i++;
				}
			}catch(Exception e){
				error.accept(e);
			}
			callback.accept(names);
		}, error);
	}

	public void getAllFacilityAbstract(@NotNull Consumer<ArrayList<MedicalFacility>> callback, Consumer<Exception> error){
		String query = "select * from medical_facilities";
		getFacilityList(query, callback::accept, error::accept);
	}

	public void getFacilityList(String name, int[] filter_pos, String order, Consumer<ArrayList<MedicalFacility>> callback, Consumer<Exception> error) {
		// TODO - implement MedicalFacilityMgr.getFacilityList
		//String query = "select *, avg(r.rating) as aveRating from medical_facilities m, Rating r where m.name like '%".toUpperCase()+name+"%'".toUpperCase();
		String query = "select * from medical_facilities m where m.name like '%"+name+"%'";
		switch(filter_pos[0]){
			case 0: break;
			case 1:
				query+=" and m.type = 'hospital' group by m.name";
				break;
			case 2:
				query+=" and m.type = 'nursing home' group by m.name";
				break;
		}
		switch(filter_pos[1]){
			case 0: break;
			case 1:
				query+=" having avg(r.rating)>3.5";
				break;
			case 2:
				query+=" having avg(r.rating)>4.0";
				break;
			case 3:
				query+=" having avg(r.rating)>4.5";
				break;
		}
		switch(order){
			case "Alphabet":
				query+=" order by m.name";
				break;
			case "Type":
				query+=" order by m.type";
				break;
			case "Rating":
				query+=" order by avg(r.rating)";
		}
		Log.d("MF_mgr Received Search keywords",name+", type: "+filters_type[filter_pos[0]]+", rating: "+filters_rating[filter_pos[1]]+", "+order);
		getFacilityList(query, callback::accept, error::accept);
	}
	public void getFacilityList(String query, Consumer<ArrayList<MedicalFacility>> callback, Consumer<Exception> error) {
		Log.d("MF_mgr Executing query", query);
		DB.instance.executeQuery(query, resultSet -> {
			ArrayList<MedicalFacility> facility_list = new ArrayList<MedicalFacility>();
			try {
				MySQLRow row;
				while ((row = resultSet.getNextRow()) != null) {
					MedicalFacility tmp_facil = new MedicalFacility();
					tmp_facil.setName(row.getString("name"));    //name is nullable
					tmp_facil.setType(row.getString("type"));
					tmp_facil.setAddress(row.getString("address"));
					tmp_facil.setContact(row.getString("contact"));
					//tmp_facil.setLongitude(row.getFloat("longitude"));
					//tmp_facil.setLatitude(row.getFloat("latitude"));
					//tmp_facil.setDescription(row.getString("description"));
					facility_list.add(tmp_facil);
					Log.d("MF_Search Result", tmp_facil.getName());
				}
				Log.d("DB_search", String.valueOf(facility_list.size()) + "medical facility records retrieved");
			} catch (Exception e) {
				error.accept(e);
				return;
			}
			callback.accept(facility_list);
		}, error);
	}
	public void getFacilityDetails(String facility_name, Consumer<MedicalFacility> callback, Consumer<Exception> error) {
		String query = ("select * from medical_facilities where name = '"+facility_name+"'").toUpperCase();
		Log.d("MF_mgr geting details", query);
		DB.instance.executeQuery(query, resultSet -> {
			MedicalFacility tmp_facil = new MedicalFacility();
			MySQLRow row = resultSet.getNextRow();
			if(row == null)
				Log.d("MF_mgr retrieving detail error","0 column retrieved");
			try {
				tmp_facil.setName(row.getString("name"));    //name is nullable
				tmp_facil.setType(row.getString("type"));
				tmp_facil.setAddress(row.getString("address"));
				tmp_facil.setContact(row.getString("contact"));
				//tmp_facil.setLongitude(row.getFloat("longitude"));
				//tmp_facil.setLatitude(row.getFloat("latitude"));
				//tmp_facil.setDescription(row.getString("description"));
				Log.d("MF_mgr Retrieved Detail",tmp_facil.getName());
			} catch (Exception e) {
				Log.d("MF_mgr retrieving detail error",e.getMessage());
				error.accept(e);
				return;
			}
			callback.accept(tmp_facil);
		}, error);
	}

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