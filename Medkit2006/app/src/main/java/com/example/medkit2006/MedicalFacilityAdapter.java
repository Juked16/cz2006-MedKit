package com.example.medkit2006;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medkit2006.boundary.FacilDetailUI;
import com.example.medkit2006.boundary.SearchUI;
import com.example.medkit2006.entity.MedicalFacility;

import java.util.ArrayList;
import java.util.List;


/**
 * RecyclerView.Adapter
 * RecyclerView.ViewHolder
 */

public class MedicalFacilityAdapter extends RecyclerView.Adapter<MedicalFacilityAdapter.MFViewHolder>{
    LayoutInflater inflater;
    List<MedicalFacility> medicalFacilityList;

    public MedicalFacilityAdapter(Context context, ArrayList<MedicalFacility> medicalFacilityList) {
        this.inflater = LayoutInflater.from(context);
        this.medicalFacilityList = medicalFacilityList;
    }


    @Override
    public MFViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.medical_facility_layout,parent, false);
        return new MFViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MFViewHolder holder, int position) {
        MedicalFacility medicalFacility = medicalFacilityList.get(position);
        holder.txtMFName.setText(medicalFacility.getName());
        holder.txtMFType.setText(medicalFacility.getType());
        //holder.txtMFAddress.setText(medicalFacility.getAddress());
        holder.txtMFContact.setText(medicalFacility.getContact());
        //holder.txtMFRating.setText(String.valueOf(medicalFacility.getAverageRating()));
        CardView cv = (CardView) holder.itemView.findViewById(R.id.cardView);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(inflater.getContext(),
                        "click success bind", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), FacilDetailUI.class);
                intent.putExtra(SearchUI.EXTRA_MESSAGE, medicalFacility.getName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicalFacilityList.size();
    }

    public class MFViewHolder extends RecyclerView.ViewHolder{
        TextView txtMFName,txtMFContact,txtMFType,txtMFAddress,txtMFRating;

        public MFViewHolder(View itemView){
            super(itemView);
            txtMFName = itemView.findViewById(R.id.cardViewMFName);
            txtMFType = itemView.findViewById(R.id.cardViewMFType);
            //txtMFAddress = itemView.findViewById(R.id.cardview);
            txtMFContact = itemView.findViewById(R.id.cardViewMFContact);

        }
    }
}
