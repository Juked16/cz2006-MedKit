package com.example.medkit2006.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medkit2006.R;
import com.example.medkit2006.boundary.FacilityDetailUI;
import com.example.medkit2006.boundary.SearchUI;
import com.example.medkit2006.entity.MedicalFacility;

import java.util.ArrayList;


/**
 * RecyclerView.Adapter
 * RecyclerView.ViewHolder
 */

public class MedicalFacilityAdapter extends RecyclerView.Adapter<MedicalFacilityAdapter.MFViewHolder>{
    LayoutInflater inflater;
    ArrayList<MedicalFacility> medicalFacilityList;

    public MedicalFacilityAdapter(Context context, ArrayList<MedicalFacility> medicalFacilityList) {
        this.inflater = LayoutInflater.from(context);
        this.medicalFacilityList = medicalFacilityList;
    }


    @NonNull
    @Override
    public MFViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.tile_medical_facility,parent, false);
        return new MFViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MFViewHolder holder, int position) {
        MedicalFacility medicalFacility = medicalFacilityList.get(position);
        Log.d("position, facility",position +", "+medicalFacility.getName());
        holder.txtMFName.setText(medicalFacility.getName());
        holder.txtMFType.setText(medicalFacility.getType());
        holder.txtMFAddress.setText(medicalFacility.getAddress());
        holder.txtMFContact.setText(medicalFacility.getContact());
        holder.txtMFRating.setText(String.valueOf(medicalFacility.getAveRating()));
        CardView cv = holder.itemView.findViewById(R.id.cardView);
        cv.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), FacilityDetailUI.class);
            intent.putExtra(SearchUI.EXTRA_MESSAGE, holder.txtMFName.getText());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return medicalFacilityList.size();
    }

    public static class MFViewHolder extends RecyclerView.ViewHolder{
        TextView txtMFName,txtMFContact,txtMFType,txtMFAddress,txtMFRating;

        public MFViewHolder(View itemView){
            super(itemView);
            txtMFName = itemView.findViewById(R.id.cardViewMFName);
            txtMFType = itemView.findViewById(R.id.cardViewMFType);
            txtMFAddress = itemView.findViewById(R.id.cardViewMFAdress);
            txtMFContact = itemView.findViewById(R.id.cardViewMFContact);
            txtMFRating = itemView.findViewById(R.id.cardViewRating);

        }
    }
}
