package com.richydave.prostortimetracker.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.richydave.prostortimetracker.api.DeleteIconClickListener;
import com.richydave.prostortimetracker.model.Service;
import com.richydave.prostortimetracker.model.Wage;
import com.richydave.prostortimetracker.data.DataBaseHelper;
import com.richydave.prostortimetracker.R;

import java.util.List;
import java.util.Locale;

public class WageDetailAdapter extends RecyclerView.Adapter<WageDetailAdapter.WageDetailsViewHolder> {

    private static final String FORMAT_PATTERN = "%.2f Hours - (%.2f %s)";
    //instance variable declaration
    private List<Wage> wageDetails;
    private DeleteIconClickListener deleteIconClickListener;

    //constructor
    public WageDetailAdapter(List<Wage> wageDetails) {
        this.wageDetails = wageDetails;
    }

    @NonNull
    @Override
    public WageDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wage_items, parent, false);
        return new WageDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WageDetailsViewHolder holder, int position) {
        Wage wageDetail = wageDetails.get(position);
        double wageHours = wageDetail.getHours();
        double wage = wageDetail.getWage();
        String saveTime = wageDetail.getSaveTime();
        String currencyCode = holder.serviceDetails.getCurrencyCode();

        holder.wagePerHour.setText(String.format(Locale.US, FORMAT_PATTERN, wageHours, wage, currencyCode));
        holder.savedDateTime.setText(saveTime);
        holder.deleteIcon.setOnClickListener((view) ->
                //set on delete icon clicked listener
                //deleteItem(position,wageDetails,this,holder.dataBaseHelper));
                deleteIconClickListener.setOnDeleteIconClick(position,wageDetails,this,holder.dataBaseHelper));
    }

    @Override
    public int getItemCount() {
        return wageDetails.size();
    }

    public void deleteItem(int position, List<Wage> wageDetails, WageDetailAdapter adapter,
                           DataBaseHelper dataBaseHelper) {
        Wage removedDetails = wageDetails.remove(position);
        //delete the removed item from the database
        dataBaseHelper.deleteWageDetails(removedDetails);
        //notify the adapter
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, wageDetails.size());
    }

    //create WageDetailsViewHolder class
    public class WageDetailsViewHolder extends RecyclerView.ViewHolder {
        //instance variable declaration
        TextView wagePerHour;
        TextView savedDateTime;
        ImageView deleteIcon;
        private Service serviceDetails;
        private DataBaseHelper dataBaseHelper;


        //constructor
        private WageDetailsViewHolder(View view) {
            super(view);
            //reference all required widget from the wage item layout
            wagePerHour = view.findViewById(R.id.wage_hour_price);
            savedDateTime = view.findViewById(R.id.wage_save_time);
            deleteIcon = view.findViewById(R.id.close_icon);
            //obtain service details from the data base
            dataBaseHelper = new DataBaseHelper(view.getContext());
            serviceDetails = new Service();
            serviceDetails = dataBaseHelper.getService();
        }

    }

}
