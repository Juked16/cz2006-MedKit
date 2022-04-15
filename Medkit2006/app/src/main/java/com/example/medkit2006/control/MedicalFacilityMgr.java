package com.example.medkit2006.control;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.BoardiesITSolutions.AndroidMySQLConnector.MySQLRow;
import com.example.medkit2006.DB;
import com.example.medkit2006.entity.MedicalFacility;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.function.Consumer;

public class MedicalFacilityMgr {

	public final String[] filters_type = {"Type Unselected", "hospital", "nursing home","dental","hospice" };
	public final String[] filters_rating = {"Rating Unselected", "Rating>3.5", "Rating>4.0", "Rating>4.5"};
	public String[] all_facility_names;

	public void getAllFacilityName(@NotNull Consumer<String[]> callback, Consumer<Exception> error){
		String query = "select name from medical_facilities";
		DB.instance.executeQuery(query, resultSet -> {
			ArrayList<String> names = new ArrayList<>();
			try{
				MySQLRow row;
				while((row = resultSet.getNextRow()) != null){
					names.add(row.getString("name"));
				}
			}catch(Exception e){
				error.accept(e);
			}
			String[] namesArray = new String[names.size()];
			for (int i = 0; i < names.size(); i++) {
				namesArray[i] = names.get(i);
			}
			callback.accept(namesArray);
		}, error);
	}

	public void getAllFacilityAbstract(@NotNull Consumer<ArrayList<MedicalFacility>> callback, Consumer<Exception> error){
		String query = "select *, avg(fill_na_rate) as rate from(select m.*, coalesce(r.rating, 0) as fill_na_rate from medical_facilities m left join rating r on m.name = r.medical_facility)t group by t.name";
		getFacilityAbstract(query, callback, error);
	}

	public void getFacilityAbstract(String name, int[] filter_pos, String order, Consumer<ArrayList<MedicalFacility>> callback, Consumer<Exception> error) {
		String join_tab = "(select *, avg(fill_na_rate) as rate from(select m.*, coalesce(r.rating, 0) as fill_na_rate from medical_facilities m left join rating r on m.name = r.medical_facility)t1 group by t1.name)t";
		String query = "select * from " + join_tab + " where t.name like '%" + DB.escape(name.toUpperCase()) + "%'";
		switch (filter_pos[0]) {
			case 0:
				break;
			case 1:
				query+=" and t.type = 'hospital'";
				break;
			case 2:
				query+=" and t.type = 'nursing home'";
				break;
			case 3:
				query+=" and t.type = 'dental'";
				break;
			case 4:
				query+=" and t.type = 'hospice'";
				break;
		}
		switch(filter_pos[1]){
			case 0: break;
			case 1:
				query+=" and t.rate>3.5";
				break;
			case 2:
				query+=" and t.rate>4.0";
				break;
			case 3:
				query+=" and t.rate>4.5";
				break;
		}
		switch(order){
			case "Alphabet":
				query+=" order by t.name";
				break;
			case "Type":
				query+=" order by t.type";
				break;
			case "Rating":
				query+=" order by t.rate DESC";
				break;
		}
		Log.d("@string/MF_mgr_tag"+"Search keywords",name+", type: "+filters_type[filter_pos[0]]+", rating: "+filters_rating[filter_pos[1]]+", "+order);
		getFacilityAbstract(query, callback, error);
	}

	public void getFacilityAbstract(String query, Consumer<ArrayList<MedicalFacility>> callback, Consumer<Exception> error) {
		Log.d("@string/MF_mgr_tag"+"getFacilityAbstract Executing query", query);
		DB.instance.executeQuery(query, resultSet -> {
			ArrayList<MedicalFacility> facility_list = new ArrayList<>();
			try {
				MySQLRow row;
				while ((row = resultSet.getNextRow()) != null) {
					MedicalFacility tmp_facil = new MedicalFacility();
					tmp_facil.setName(row.getString("name"));    //name is nullable
					tmp_facil.setType(row.getString("type"));
					tmp_facil.setAddress(row.getString("address"));
					tmp_facil.setContact(row.getString("contact"));
					try{tmp_facil.setAveRating(row.getFloat("rate"));}catch(Exception e){
						tmp_facil.setAveRating(0.0F); }
					facility_list.add(tmp_facil);
					Log.d("@string/MF_mgr_tag"+"getFacilityAbstract Result", tmp_facil.getName());
				}
				Log.d("@string/MF_mgr_tag"+"getFacilityAbstract returned entries", facility_list.size() + "medical facility records retrieved");
			} catch (Exception e) {
				error.accept(e);
				return;
			}
			callback.accept(facility_list);
		}, error);
	}
	public void getFacilityDetails(String facility_name, Consumer<MedicalFacility> callback, Consumer<Exception> error) {
		String query = ("select * from medical_facilities where name = '" + DB.escape(facility_name) + "'").toUpperCase();
		Log.d("@string/MF_mgr_tag"+"getFacilityDetails executing query", query);
		DB.instance.executeQuery(query, resultSet -> {
			MedicalFacility tmp_facil = new MedicalFacility();
			MySQLRow row = resultSet.getNextRow();
			try {
				tmp_facil.setName(row.getString("name"));
				tmp_facil.setType(row.getString("type"));
				tmp_facil.setAddress(row.getString("address"));
				tmp_facil.setContact(row.getString("contact"));
				try{tmp_facil.setLatitude(row.getFloat("latitude"));} catch(Exception e){
					tmp_facil.setLatitude(0.0F);}
				try{tmp_facil.setLongitude(row.getFloat("longitude"));}catch(Exception e){
					tmp_facil.setLongitude(0.0F);}
				try{
					tmp_facil.setDescription(row.getString("description"));
					if(tmp_facil.getDescription().length()<10)
						tmp_facil.setDescription("This is a great facility!"); }catch(Exception e){
					tmp_facil.setDescription("This is a great facility!");}
				Log.d("@string/MF_mgr_tag"+"getFacilityDetail Result",tmp_facil.getName());
			} catch (Exception e) {
				Log.d("@string/MF_mgr_tag"+"getFacilityDetail Error",e.getMessage());
				error.accept(e);
				return;
			}
			callback.accept(tmp_facil);
		}, error);
	}

	public void getImage(String facility_name, Consumer<Bitmap> callback, Consumer<Exception> error) {
		new Thread(() -> {
			try {
				URL urlConnection = new URL("http://159.138.106.155/"
						+ facility_name.replace(" ", "").replace("/","") + ".png");
				HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				callback.accept(BitmapFactory.decodeStream(input));
			} catch (Exception e) {
				error.accept(e);
			}
		}).start();
	}
}