package com.example.medkit2006;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medkit2006.entity.MedicalFacility;

import java.util.List;


/**
 * RecyclerView.Adapter
 * RecyclerView.ViewHolder
 */

public class MedicalFacilityAdapter extends RecyclerView.Adapter<MedicalFacilityAdapter.MFViewHolder>{
    private Context mContext;
    private List<MedicalFacility> medicalFacilityList;

    public MedicalFacilityAdapter(Context mContext, List<MedicalFacility> medicalFacilityList) {
        this.mContext = mContext;
        this.medicalFacilityList = medicalFacilityList;
    }


    @Override
    public MFViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.medical_facility_layout,null);
        return new MFViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MFViewHolder holder, int position) {
        MedicalFacility medicalFacility = medicalFacilityList.get(position);
        holder.txtMFName.setText(medicalFacility.getName());
        holder.txtMFType.setText(medicalFacility.getType());
        holder.txtMFAddress.setText(medicalFacility.getAddress());
        holder.txtMFContact.setText(medicalFacility.getContact());
        holder.txtMFRating.setText(String.valueOf(medicalFacility.getAverageRating()));
    }

    @Override
    public int getItemCount() {
        return medicalFacilityList.size();
    }

    class MFViewHolder extends RecyclerView.ViewHolder{
        TextView txtMFName,txtMFContact,txtMFType,txtMFAddress,txtMFRating;

        public MFViewHolder(View itemView){
            super(itemView);
            txtMFName = itemView.findViewById(R.id.txtMedicalFacilityName);
            txtMFType = itemView.findViewById(R.id.txtMedicalFacilityType);
            txtMFAddress = itemView.findViewById(R.id.txtMedicalFacilityAddress);
            txtMFContact = itemView.findViewById(R.id.txtMedicalFacilityContact);
        }
    }

}
