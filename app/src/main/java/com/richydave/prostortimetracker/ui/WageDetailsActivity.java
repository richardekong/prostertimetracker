package com.richydave.prostortimetracker.ui;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.richydave.prostortimetracker.model.Wage;
import com.richydave.prostortimetracker.data.DataBaseHelper;
import com.richydave.prostortimetracker.R;

import java.util.List;
import java.util.Locale;


public class WageDetailsActivity extends AppCompatActivity {
    private DataBaseHelper dataBaseHelper;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.wage_list);
        getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.wage_details_activity_title);
        //obtain all record of wage details from the database
        dataBaseHelper = new DataBaseHelper(this);
        List<Wage> wageDetails = dataBaseHelper.getListOfWageDetails();
        setColorFromDatabase();
        //trouble shoot list
        for (Wage wage : wageDetails)
            Log.d("wage details", String.format("%d %.2f %.2f \n%s", wage.getWageId(),
                    wage.getWage(), wage.getHours(), wage.getSaveTime()));
        //create the recycle view
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //instantiate recycler adapter
        WageDetailAdapter adapter = new WageDetailAdapter(wageDetails);
        recyclerView.setAdapter(adapter);
    }

    public void setColorFromDatabase() {
        if (!(dataBaseHelper.isColorRecordEmpty())) {
            String hex = dataBaseHelper.getColor();
            hex = String.format(Locale.US, "#%s", hex);
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor(hex));
            try {
                getSupportActionBar().setBackgroundDrawable(colorDrawable);
            } catch (NullPointerException e) {
                Log.e("NullPointerException", e.getMessage());
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
